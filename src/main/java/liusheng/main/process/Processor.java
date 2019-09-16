package liusheng.main.process;

import java.util.List;

public interface Processor {

    // 可能返回多个数据
    List<Object>  process(Object input) throws Throwable;

}
