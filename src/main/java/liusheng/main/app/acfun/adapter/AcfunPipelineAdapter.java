package liusheng.main.app.acfun.adapter;

import liusheng.main.app.adapter.PipelineAdapter;
import liusheng.main.pipeline.Pipeline;

public class AcfunPipelineAdapter implements PipelineAdapter {
    private final Pipeline<?> pipeline;

    public AcfunPipelineAdapter(Pipeline<?> pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public boolean isSupport(String url) {
        return true;
    }

    @Override
    public Pipeline<?> pipeline() {
        return pipeline;
    }
}
