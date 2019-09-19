package liusheng.main.app.adapter;

import liusheng.main.pipeline.Pipeline;

public interface PipelineAdapter {
    boolean isSupport(String url);
    Pipeline<?> pipeline();
}
