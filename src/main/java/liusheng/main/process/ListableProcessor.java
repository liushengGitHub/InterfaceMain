package liusheng.main.process;

import liusheng.main.listener.ProcessListener;

import java.util.List;

public interface ListableProcessor<B extends ListableProcessor<B>>  extends Processor{

    B addListener(ProcessListener listener);

    List<ProcessListener> listeners();

    List<Object>  process(Object input) throws Throwable;
}
