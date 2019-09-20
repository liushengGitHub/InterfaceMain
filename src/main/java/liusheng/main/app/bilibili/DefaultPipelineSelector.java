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
    private final List<PipelineAdapter> pipelineAdapters;
    private final Logger logger = Logger.getLogger(DefaultPipelineSelector.class);

    public DefaultPipelineSelector(List<PipelineAdapter> pipelineAdapters) {
        this.pipelineAdapters = Collections.unmodifiableList(pipelineAdapters);
        // 线程池
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
