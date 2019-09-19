package liusheng.main.app.bilibili;

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
import liusheng.main.app.bilibili.processor.ListVideoBeanToDisk;
import liusheng.main.app.bilibili.processor.av.PagesBeanToVideoBean;
import liusheng.main.app.bilibili.processor.av.UrlToPagesBean;
import liusheng.main.pipeline.DefaultPipeline;
import liusheng.main.pipeline.Pipeline;
import org.apache.log4j.Logger;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class DefaultPipelineSelector implements PipelineSelector {
    private final String dir;
    private final RetryDownloader.DownloaderController controller;
    private final RetryDownloader.DownloaderListener[] listeners;
    private final List<PipelineAdapter> pipelineAdapters;
    private final Queue<FailTask> downloadTasks = new ConcurrentLinkedQueue<>();
    private final ClosableFixedThreadPoolExecutor stepExecutorService;
    private final Logger logger = Logger.getLogger(DefaultPipelineSelector.class);

    public DefaultPipelineSelector(String dir, RetryDownloader.DownloaderController controller, RetryDownloader.DownloaderListener[] listeners, Queue<FailTask> imageTask) {
        this.dir = dir;
        this.controller = controller;
        this.listeners = listeners;
        // 线程池
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
                logger.info("队列的数量是 :" + downloadTasks.size() + "=" + System.identityHashCode(imageTask));
                runnable = downloadTasks.poll();
                if (Objects.nonNull(runnable) && runnable.getCount() < FailTask.COUNT) {
                    // 自增计数一下
                    runnable.count();
                    // 直接运行任务
                    runnable.run();
                }
                // 重试次数达到了上限 ,提示下载失败
                if (runnable.getCount() >= FailTask.COUNT) {

                }
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
                if (Objects.nonNull(runnable) && runnable.getCount() < FailTask.COUNT) {
                    // 自增计数一下
                    runnable.count();
                    // 直接运行任务
                    runnable.run();
                }
                // 重试次数达到了上限 ,提示下载失败
                if (runnable.getCount() >= FailTask.COUNT) {

                }
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

        pipelineAdapters = Collections.unmodifiableList(pipelines);
    }

    @Override
    public Pipeline select(String url) {
        for (PipelineAdapter adapter :
                pipelineAdapters) {
            if (adapter.isSupport(url)) return adapter.pipeline();
        }
        return null;
    }
}
