package liusheng.main.app.bilibili;

import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.listener.DownloadSpeedListener;
import liusheng.main.app.bilibili.processor.ListVideoBeanToDisk;
import liusheng.main.app.bilibili.processor.av.PagesBeanToVideoBean;
import liusheng.main.app.bilibili.processor.av.UrlToPagesBean;
import liusheng.main.pipeline.DefaultPipeline;

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

    public boolean isStart() {
        return start.get();
    }

    public BlockingQueue<String> getWorks() {
        return works;
    }

    public static synchronized AvDownloader getInstance() {
        return donwloader;
    }

    public void start(RetryDownloader.DownloaderController controller, DownloadSpeedListener... listeners) {
        if (start.compareAndSet(false, true)) {
            new Thread(() -> {
                DefaultPipeline defaultPipeline = new DefaultPipeline();
                defaultPipeline.addLast(new UrlToPagesBean());
                defaultPipeline.addLast(new PagesBeanToVideoBean(dir));
                defaultPipeline.addLast(new ListVideoBeanToDisk(controller, listeners));

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String url = works.take();
                        defaultPipeline.processor(url);
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
