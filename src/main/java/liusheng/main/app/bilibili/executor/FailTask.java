package liusheng.main.app.bilibili.executor;

import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.AbstractVideoBean;
import liusheng.main.app.bilibili.entity.NewVideoBean;
import liusheng.main.app.bilibili.entity.OldVideoBean;
import liusheng.main.app.bilibili.processor.HandleNewVideoBean;
import liusheng.main.app.bilibili.processor.HandleOldVideoBean;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class FailTask implements Runnable {
    private final AbstractVideoBean abstractVideoBean;
    private final List<? extends RetryDownloader.DownloaderListener> listeners;
    private final Semaphore semaphore;
    private final ExecutorService stepExecutorService;

    public FailTask(AbstractVideoBean abstractVideoBean, List<? extends RetryDownloader.DownloaderListener> listeners, Semaphore semaphore, ExecutorService stepExecutorService) {
        this.abstractVideoBean = abstractVideoBean;
        this.listeners = listeners;
        this.semaphore = semaphore;
        this.stepExecutorService = stepExecutorService;
    }

    public AbstractVideoBean getAbstractVideoBean() {
        return abstractVideoBean;
    }

    @Override
    public void run() {
        try {

            // 限流,不然系统内存撑不住

            if (abstractVideoBean instanceof NewVideoBean) {
                new HandleNewVideoBean(listeners, stepExecutorService, 3, semaphore).process((NewVideoBean) abstractVideoBean);
            } else if (abstractVideoBean instanceof OldVideoBean) {
                new HandleOldVideoBean(listeners, 3, semaphore).process((OldVideoBean) abstractVideoBean);

            } else {
                throw new IllegalArgumentException();
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
