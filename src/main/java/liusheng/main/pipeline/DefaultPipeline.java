package liusheng.main.pipeline;

import liusheng.main.app.bilibili.executor.FailListExecutorService;
import liusheng.main.process.AbstractLinkedListableProcessor;
import liusheng.main.process.AbstractLinkedProcessor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultPipeline implements Pipeline<DefaultPipeline> {

    // 执行链的线程
    private FailListExecutorService executorService = new FailListExecutorService();

    public ExecutorService getExecutorService() {
        return executorService;
    }

    static class HeadProcessor extends AbstractLinkedListableProcessor<Object, HeadProcessor> {

        @Override
        protected void doProcess(Object o, List<Object> returnData) throws Throwable {
            returnData.add(o);

        }
    }

    static class TailProcessor extends AbstractLinkedListableProcessor<Object, TailProcessor> {

        @Override
        protected void doProcess(Object o, List<Object> returnData) throws Throwable {
            returnData.add(o);
        }

    }

    private final Node head = new Node("HEAD", new HeadProcessor());

    private Node tail = new Node("TAIL", new TailProcessor());

    {
        head.next = tail;
        tail.last = head;

        head.processor.setPipeline(this);
        tail.processor.setPipeline(this);
    }

    static class Node {
        final String name;
        AbstractLinkedProcessor processor;
        Node next;
        Node last;

        Node(String name, AbstractLinkedProcessor processor) {
            this.name = name;
            this.processor = processor;
        }
    }


    @Override
    public synchronized DefaultPipeline addLast(AbstractLinkedProcessor processor) {
        return addLast(UUID.randomUUID().toString(), processor);
    }

    @Override
    public synchronized DefaultPipeline addLast(String name, AbstractLinkedProcessor processor) {


        Node last = tail.last;

        Node nextNode = new Node(name, processor);

        last.next = nextNode;
        nextNode.last = last;

        nextNode.next = tail;
        tail.last = nextNode;

        last.processor.setNext(processor);

        processor.setPipeline(this);
        return this;
    }

    @Override
    public synchronized DefaultPipeline addFirst(AbstractLinkedProcessor processor) {
        return addFirst(UUID.randomUUID().toString(), processor);
    }

    @Override
    public synchronized DefaultPipeline addFirst(String name, AbstractLinkedProcessor processor) {

        Node next = head.next;


        head.next = new Node(name, processor);

        if (next != null)
            head.next.processor.setNext(next.processor);

        head.next.next = next;
        processor.setPipeline(this);
        return this;
    }

    @Override
    public Object processor(Object object) throws Throwable {
        return Optional.ofNullable(head.next).map((t) -> {
            try {
                // 执行第一个处理器
                return t.processor.process(object);

            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }).orElse(null);
    }

}
