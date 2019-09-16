package liusheng.main.process;

public abstract class AbstractLinkedProcessor implements LinkedProcessor{

    private AbstractLinkedProcessor next;

    @Override
    public AbstractLinkedProcessor nextProcessor() {
        return  next;
    }

    public final void setNext(AbstractLinkedProcessor next) {
        this.next = next;
    }

}
