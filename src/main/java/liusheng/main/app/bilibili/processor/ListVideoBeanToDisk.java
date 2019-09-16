package liusheng.main.app.bilibili.processor;

import liusheng.main.app.bilibili.ThreadSafe;
import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.AbstractVideoBean;
import liusheng.main.app.bilibili.executor.FailTask;
import liusheng.main.app.bilibili.listener.DownloadSpeedListener;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@ThreadSafe
public class ListVideoBeanToDisk extends AbstractLinkedListableProcessor<Object, ListVideoBeanToDisk> {

    public ListVideoBeanToDisk(RetryDownloader.DownloaderListener listener) {
        this.listener = listener;
    }

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

    private final Logger logger = Logger.getLogger(ListVideoBeanToDisk.class);
    private ClosableFixedThreadPoolExecutor stepExecutorService = new ClosableFixedThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2);
    // 因为是克隆添加，所以防止添加多次
    private final RetryDownloader.DownloaderListener listener ;
    private final Semaphore semaphore = new Semaphore(2);

    @Override
    protected void doProcess(Object object, List<Object> returnData) throws Throwable {

        List<RetryDownloader.DownloaderListener> listeners = Arrays.asList(listener);
        AbstractVideoBean abstractVideoBean = (AbstractVideoBean) object;


        // 没有数据
        this.pipeline().getExecutorService().execute(new FailTask(abstractVideoBean, listeners, semaphore, stepExecutorService));

    }
}
