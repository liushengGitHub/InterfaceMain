package liusheng.main.app.bilibili.processor;

import liusheng.main.app.bilibili.ThreadSafe;
import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.AbstractVideoBean;
import liusheng.main.app.bilibili.entity.NewVideoBean;
import liusheng.main.app.bilibili.entity.OldVideoBean;
import liusheng.main.app.bilibili.executor.ClosableFixedThreadPoolExecutor;
import liusheng.main.app.bilibili.executor.FailListExecutorService;
import liusheng.main.app.bilibili.executor.FailTask;
import liusheng.main.pipeline.Pipeline;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@ThreadSafe
public class ListVideoBeanToDisk extends AbstractLinkedListableProcessor<Object, ListVideoBeanToDisk> {

    private final RetryDownloader.DownloaderController controller;
    private final List<RetryDownloader.DownloaderListener> listeners = new LinkedList<>();
    private final Logger logger = Logger.getLogger(ListVideoBeanToDisk.class);
    private ClosableFixedThreadPoolExecutor stepExecutorService;
    private HandleNewVideoBean handleNewVideoBean;
    private HandleOldVideoBean handleOldVideoBean;

    /**
     * 在构造该对象之后使用
     * @param pipeline
     */
    @Override
    public void setPipeline(Pipeline pipeline) {
        super.setPipeline(pipeline);
        ExecutorService executorService = pipeline().getExecutorService();
        if (executorService instanceof FailListExecutorService) {
            FailListExecutorService failListExecutorService = (FailListExecutorService) executorService;
            stepExecutorService = new ClosableFixedThreadPoolExecutor(failListExecutorService.failTaskQueue());
        } else {
            stepExecutorService = new ClosableFixedThreadPoolExecutor();
        }
        /**
         *
         * 因为者两个对象是线程安全的,所有可用提取出来
         */
        handleNewVideoBean = new HandleNewVideoBean(this.listeners, stepExecutorService, 3, semaphore, controller);
        handleOldVideoBean = new HandleOldVideoBean(this.listeners, 3, semaphore, controller);
    }
    public ListVideoBeanToDisk(RetryDownloader.DownloaderController controller, RetryDownloader.DownloaderListener... listeners) {
        this.controller = controller;
        this.listeners.addAll(Arrays.asList(listeners));
    }

    public ClosableFixedThreadPoolExecutor getStepExecutorService() {
        return stepExecutorService;
    }

    // 因为是克隆添加，所以防止添加多次

    private final Semaphore semaphore = new Semaphore(2);

    @Override
    protected void doProcess(Object object, List<Object> returnData) throws Throwable {
        AbstractVideoBean abstractVideoBean = (AbstractVideoBean) object;
        // 限流,不然系统内存撑不住

        if (abstractVideoBean instanceof NewVideoBean) {

            handleNewVideoBean.process((NewVideoBean) abstractVideoBean);
        } else if (abstractVideoBean instanceof OldVideoBean) {

            handleOldVideoBean.process((OldVideoBean) abstractVideoBean);

        } else {
            throw new IllegalArgumentException();
        }

    }
}
