package liusheng.main.process;

import liusheng.main.pipeline.Pipeline;

import java.util.concurrent.ExecutorService;

public interface LinkedProcessor extends Processor {
    Processor nextProcessor();
    Pipeline<?> pipeline();
}
