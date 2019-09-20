package liusheng.main.app.bilibili;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import liusheng.main.app.acfun.adapter.AcfunPipelineAdapter;
import liusheng.main.app.acfun.processor.M3u8BeanToDisk;
import liusheng.main.app.acfun.processor.UrlToM3u8Bean;
import liusheng.main.app.adapter.PipelineAdapter;
import liusheng.main.app.adapter.PipelineSelector;
import liusheng.main.app.bilibili.adapter.BilibiliPipelineAdapter;
import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.executor.ClosableFixedThreadPoolExecutor;
import liusheng.main.app.bilibili.executor.FailListExecutorService;
import liusheng.main.app.bilibili.executor.FailTask;
import liusheng.main.app.bilibili.listener.DownloadSpeedListener;
import liusheng.main.app.bilibili.processor.ListVideoBeanToDisk;
import liusheng.main.app.bilibili.processor.av.PagesBeanToVideoBean;
import liusheng.main.app.bilibili.processor.av.UrlToPagesBean;
import liusheng.main.pipeline.DefaultPipeline;
import liusheng.main.pipeline.Pipeline;
import org.apache.log4j.Logger;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 单例模式
 */
public class AvDownloader {
    private final Logger logger = Logger.getLogger(AvDownloader.class);
    private ClosableFixedThreadPoolExecutor stepExecutorService;
    private static AvDownloader donwloader = new AvDownloader();
    private final BlockingQueue<String> works = new LinkedBlockingQueue<>(1000);
    private String dir = "e:\\hello";
    private final AtomicBoolean start = new AtomicBoolean(false);
    private PipelineSelector selector;
    private final Queue<FailTask> downloadTasks = new ConcurrentLinkedQueue<>();

    public boolean isStart() {
        return start.get();
    }

    public BlockingQueue<String> getWorks() {
        return works;
    }

    public static AvDownloader getInstance() {
        return donwloader;
    }

    public void start(RetryDownloader.DownloaderController controller, FailListExecutorService imageFailExecutorService, DownloadSpeedListener... listeners) {
        if (start.compareAndSet(false, true)) {

            List<PipelineAdapter> pipelineAdapters = getPipelineAdpaters(imageFailExecutorService.failTaskQueue(), controller, listeners);
            //dir, controller, listeners,imageFailExecutorService.failTaskQueue()
            selector = new DefaultPipelineSelector(pipelineAdapters);
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

    private List<PipelineAdapter> getPipelineAdpaters(Queue<FailTask> imageTask, RetryDownloader.DownloaderController controller, DownloadSpeedListener[] listeners) {
        FailListExecutorService executorService = new FailListExecutorService(downloadTasks);

        stepExecutorService = new ClosableFixedThreadPoolExecutor();


        /**
         *
         * 取出失败的任务
         */

        // 启动一个定时任务,每次执行任务后疫苗就向队列中取一个失败的任务执行
        stepExecutorService.scheduleAtFixedRate(() -> {
            FailTask runnable = null;
            try {
                //logger.info("队列的数量是 :" + downloadTasks.size() + "=" + System.identityHashCode(imageTask));
                runnable = downloadTasks.poll();
                failAndAdd(runnable);
            } catch (Throwable throwable) {
                if (Objects.nonNull(runnable)) {
                    if (!downloadTasks.offer(runnable)) {
                        logger.info("队列已满");
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);


        stepExecutorService.scheduleAtFixedRate(() -> {
            FailTask runnable = null;
            try {
                runnable = imageTask.poll();

                // 重试次数达到了上限 ,提示下载失败
                failAndAdd(runnable);

            } catch (Throwable throwable) {
                if (Objects.nonNull(runnable)) {
                    if (!imageTask.offer(runnable)) {
                        logger.info("队列已满");
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
        // b站下载器
        DefaultPipeline bilibiliPipeline = new DefaultPipeline(executorService);
        bilibiliPipeline.addLast(new UrlToPagesBean());
        bilibiliPipeline.addLast(new PagesBeanToVideoBean(dir));
        bilibiliPipeline.addLast(new ListVideoBeanToDisk(controller, stepExecutorService, listeners));
        //a站下载器
        DefaultPipeline acfunPipeline = new DefaultPipeline(executorService);
        acfunPipeline.addLast(new UrlToM3u8Bean());
        acfunPipeline.addLast(new M3u8BeanToDisk(Paths.get(dir)));

        // 加入到列表中
        List<PipelineAdapter> pipelines = new LinkedList<>();
        pipelines.add(new BilibiliPipelineAdapter(bilibiliPipeline));
        pipelines.add(new AcfunPipelineAdapter(acfunPipeline));


        return pipelines;
    }

    private void failAndAdd(FailTask runnable) {
        if (Objects.nonNull(runnable)) {

            if (runnable.getCount() >= FailTask.COUNT) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "下载失败", ButtonType.FINISH);
                    alert.showAndWait();
                });
                return;
            }
            // 自增计数一下
            runnable.count();
            // 直接运行任务
            runnable.run();
        }
    }
}
