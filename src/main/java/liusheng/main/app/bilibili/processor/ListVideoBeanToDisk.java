package liusheng.main.app.bilibili.processor;

import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.AbstractVideoBean;
import liusheng.main.app.bilibili.entity.NewVideoBean;
import liusheng.main.app.bilibili.entity.OldVideoBean;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ListVideoBeanToDisk extends AbstractLinkedListableProcessor<Object, ListVideoBeanToDisk> {

    static class ClosableFixedThreadPoolExecutor extends ScheduledThreadPoolExecutor {
        public ClosableFixedThreadPoolExecutor() {
            this(Runtime.getRuntime().availableProcessors());
            setKeepAliveTime(30, TimeUnit.SECONDS);
            allowCoreThreadTimeOut(true);
        }

        public ClosableFixedThreadPoolExecutor(int corePoolSize) {
            super(corePoolSize);
        }
    }

    private Logger logger = Logger.getLogger(ListVideoBeanToDisk.class);
    private ExecutorService executorService = new ClosableFixedThreadPoolExecutor();
    private ClosableFixedThreadPoolExecutor stepExecutorService = new ClosableFixedThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2);
    // 因为是克隆添加，所以防止添加多次
    private final DownloadSpeedListner listener = new DownloadSpeedListner();

    static class DownloadSpeedListner implements RetryDownloader.DownloaderListener {

        private AtomicLong size = new AtomicLong();

        @Override
        public void listen(int length) {
            size.getAndAdd(length);
        }

        long getSize() {
            return size.get();
        }
    }

    class ShutDownPhaser extends Phaser {
        public ShutDownPhaser() {
            super();
        }

        public ShutDownPhaser(int parties) {
            super(parties);
        }

        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            executorService.shutdown();
            stepExecutorService.shutdown();
            return super.onAdvance(phase, registeredParties);
        }
    }

    final ShutDownPhaser shutDownPhaser = new ShutDownPhaser();
    final AtomicInteger set = new AtomicInteger();

    @Override
    protected void doProcess(Object object, List<Object> returnData) throws Throwable {

        List<DownloadSpeedListner> listners = Arrays.asList(listener);
        AbstractVideoBean abstractVideoBean = (AbstractVideoBean) object;
        if(set.compareAndSet(0,1)) {
            int size = abstractVideoBean.getSize();
            shutDownPhaser.bulkRegister(size);
            stepExecutorService.scheduleAtFixedRate(new Runnable() {
                private long pre = -1;

                @Override
                public void run() {
                    long curr = listener.getSize();
                    if (pre != -1) logger.debug((curr - pre) / 1024);
                    pre = curr;
                }
            }, 2 , 1, TimeUnit.SECONDS);
        }
        // 没有数据
        executorService.execute(() -> {


            try {
                if (abstractVideoBean instanceof NewVideoBean) {
                    new HandleNewVideoBean(listners, stepExecutorService, 3).process((NewVideoBean) abstractVideoBean);
                } else if (abstractVideoBean instanceof OldVideoBean) {
                    new HandleOldVideoBean(listners, 3).process((OldVideoBean) abstractVideoBean);

                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            } finally {
                shutDownPhaser.arriveAndDeregister();
            }

        });

    }
}
