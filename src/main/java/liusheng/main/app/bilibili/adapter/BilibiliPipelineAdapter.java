package liusheng.main.app.bilibili.adapter;

import liusheng.main.app.adapter.PipelineAdapter;
import liusheng.main.app.bilibili.parser.PageInfoParser;
import liusheng.main.pipeline.Pipeline;

public class BilibiliPipelineAdapter implements PipelineAdapter {
    private final Pipeline<?> pipeline;

    public BilibiliPipelineAdapter(Pipeline<?> pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public boolean isSupport(String url) {
        return PageInfoParser.CHECK.matcher(url).matches();
    }

    @Override
    public Pipeline<?> pipeline() {
        return pipeline;
    }
}
