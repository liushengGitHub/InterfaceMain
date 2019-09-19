package liusheng.main.app.adapter;


import liusheng.main.pipeline.Pipeline;

public interface PipelineSelector {
    Pipeline select(String url);
}
