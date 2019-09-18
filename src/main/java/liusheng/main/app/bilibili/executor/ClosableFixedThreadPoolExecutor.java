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
    private final Queue<FailTask> queue;
    private final Logger logger = Logger.getLogger(ClosableFixedThreadPoolExecutor.class);

    public ClosableFixedThreadPoolExecutor(Queue<FailTask> queue, int corePoolSize) {
        super(corePoolSize);
        this.queue = queue;
        setKeepAliveTime(30, TimeUnit.SECONDS);
        allowCoreThreadTimeOut(true);

        if (Objects.isNull(queue)) return;
        // 启动一个定时任务,每次执行任务后疫苗就向队列中取一个失败的任务执行
        this.scheduleAtFixedRate(() -> {
            FailTask runnable = null;
            try {
                runnable = queue.poll();
                if (Objects.nonNull(runnable) && runnable.getCount() < FailTask.COUNT) {
                    // 自增计数一下
                    runnable.count();
                    // 直接运行任务
                    runnable.run();
                }
            } catch (Throwable throwable) {
                if (Objects.nonNull(runnable)) {
                    if (!queue.offer(runnable)) {
                        logger.info("队列已满");
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public ClosableFixedThreadPoolExecutor(Queue<FailTask> queue) {
        this(queue, Runtime.getRuntime().availableProcessors());
    }

    public ClosableFixedThreadPoolExecutor() {
        this(null);
    }
}