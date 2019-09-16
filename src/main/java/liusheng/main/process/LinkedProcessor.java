package liusheng.main.process;

public interface LinkedProcessor extends Processor {
    Processor nextProcessor();
}
