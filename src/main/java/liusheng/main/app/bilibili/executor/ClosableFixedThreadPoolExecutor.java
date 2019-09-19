package liusheng.main.app.bilibili.executor;

import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 专门执行失败的任务的线程池和临时调度的用的
public class ClosableFixedThreadPoolExecutor extends ScheduledThreadPoolExecutor {
    // 失败的任务队列
    private final Logger logger = Logger.getLogger(ClosableFixedThreadPoolExecutor.class);

    public ClosableFixedThreadPoolExecutor( int corePoolSize) {

        super(corePoolSize);
        setKeepAliveTime(30, TimeUnit.SECONDS);
        allowCoreThreadTimeOut(true);


    }

    public ClosableFixedThreadPoolExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }

}