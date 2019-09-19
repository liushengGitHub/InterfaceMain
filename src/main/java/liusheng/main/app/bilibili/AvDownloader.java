package liusheng.main.app.bilibili;

import liusheng.main.app.adapter.PipelineSelector;
import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.executor.ClosableFixedThreadPoolExecutor;
import liusheng.main.app.bilibili.executor.FailListExecutorService;
import liusheng.main.app.bilibili.listener.DownloadSpeedListener;
import liusheng.main.pipeline.Pipeline;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 单例模式
 */
public class AvDownloader {
    private static AvDownloader donwloader = new AvDownloader();
    private final BlockingQueue<String> works = new LinkedBlockingQueue<>(1000);
    private String dir = "e:\\hello";
    private final AtomicBoolean start = new AtomicBoolean(false);
    private PipelineSelector selector;

    public boolean isStart() {
        return start.get();
    }

    public BlockingQueue<String> getWorks() {
        return works;
    }

    public static AvDownloader getInstance() {
        return donwloader;
    }

    public void start(RetryDownloader.DownloaderController controller, FailListExecutorService failListExecutorService, DownloadSpeedListener... listeners) {
        if (start.compareAndSet(false, true)) {
            selector = new DefaultPipelineSelector(dir, controller, listeners,failListExecutorService.failTaskQueue());
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String url = works.take();
                        Pipeline pipeline = selector.select(url);
                        if (pipeline != null) {
                            pipeline.processor(url);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        } else {
            throw new RuntimeException("已经启动的");
        }
    }
}
