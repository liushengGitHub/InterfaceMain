package liusheng.main.pipeline;

import liusheng.main.process.AbstractLinkedProcessor;
import liusheng.main.process.Processor;

import java.util.concurrent.ExecutorService;

public interface Pipeline<T extends  Pipeline<T>> {
    T addLast(AbstractLinkedProcessor processor);
    T addLast(String name,AbstractLinkedProcessor processor);
    T addFirst(AbstractLinkedProcessor processor);
    T addFirst(String name,AbstractLinkedProcessor processor);
    Object processor(Object object) throws Throwable;
    ExecutorService getExecutorService();
}
