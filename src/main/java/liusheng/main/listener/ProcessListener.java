package liusheng.main.listener;

public interface ProcessListener {
    default void startDo(Object source,Object target){};
    default  void endDo(Object source,Object target) {};
    default void errorDo(Object source,Object target, Throwable throwable) {};
    default void  finallyDo(Object source,Object target) {};
}
