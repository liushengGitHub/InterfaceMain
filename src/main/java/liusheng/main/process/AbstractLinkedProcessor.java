package liusheng.main.process;

import liusheng.main.pipeline.Pipeline;

public abstract class AbstractLinkedProcessor implements LinkedProcessor{

    private AbstractLinkedProcessor next;

    private Pipeline pipeline;
    public Pipeline pipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }
    @Override
    public AbstractLinkedProcessor nextProcessor() {
        return  next;
    }

    public final void setNext(AbstractLinkedProcessor next) {
        this.next = next;
    }


}
