package liusheng.main.process;

import liusheng.main.listener.ProcessListener;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class AbstractLinkedListableProcessor<T,B extends AbstractLinkedListableProcessor<T, B>> extends AbstractLinkedProcessor
        implements ListableProcessor< AbstractLinkedListableProcessor<T,B>>, LinkedProcessor{

 /*   protected final static int BEGIN = 0;
    protected final static int EXECUTE = 1;
    protected final static int END = 2;
    protected final static int ERROR = 3;

    private volatile int state = BEGIN;*/


    private List<ProcessListener> listeners = new CopyOnWriteArrayList<>();
    private final Class<T> clazz;
    {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }


    // 只读

    public List<ProcessListener> listeners() {

        return listeners;

    }

    public B addListener(ProcessListener listener) {

        listeners.add(listener);

        return (B) this;
    }


    protected abstract void doProcess(T t, List<Object> returnData) throws Throwable;

    @Override
    public List<Object> process(Object input) throws Throwable {



        // 如歌类型不符合，直接下一个
        if ( !clazz.isInstance(input)) return Arrays.asList(input);

        try {
            listenersDoStart(input);


            List<Object> list = new LinkedList<>();

            // handle
            doProcess((T) input, list);

            // 调用下一个处理器 ,并何必所有结果
            List<Object> results = list.stream().flatMap(data -> Optional.ofNullable(nextProcessor())
                    .map(p -> {
                        try {
                            return p.process(data);
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }).orElse(Collections.emptyList()).stream()).collect(Collectors.toList());


            listenersDoEnd(input);

            return results.isEmpty() ?  list: results ;

        } catch (Throwable e) {


            try {
                listenersDoError(input, e);
            }catch (Throwable t){
                e.addSuppressed(t);
            }
            throw e;
        } finally {

            listenersDoFinally(input);
        }
    }

    private void listenersDoFinally(Object object) {

        listeners().forEach(listener -> listener.finallyDo(this, object));

    }

    private void listenersDoEnd(Object object) {

        listeners().forEach(listener -> listener.endDo(this, object));
    }

    private void listenersDoError(Object object, Throwable e) {

        listeners().forEach(listener -> listener.errorDo(this, object, e));
    }

    private void listenersDoStart(Object object) {

        listeners().forEach(listener -> listener.startDo(this, object));
    }
}
