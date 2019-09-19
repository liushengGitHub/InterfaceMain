package liusheng.main.app.bilibili.executor;

import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;

public class FailListExecutorService extends ThreadPoolExecutor {
    private final Logger logger = Logger.getLogger(FailListExecutorService.class);
    public final Queue<FailTask> queue;

    public Queue<FailTask> failTaskQueue() {
        return queue;
    }

    public FailListExecutorService(int fixedSize, Queue<FailTask> queue) {
        super(fixedSize, fixedSize, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1024));
        this.queue = queue;
    }

    public FailListExecutorService() {
        this(Runtime.getRuntime().availableProcessors() * 2, new ConcurrentLinkedQueue());
    }
    public FailListExecutorService(Queue<FailTask> queue) {
        this(Runtime.getRuntime().availableProcessors() * 2,queue );
    }
    @Override
    protected void beforeExecute(Thread t, Runnable r) {

    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {

        // 加入失败的任务
        if (!Objects.isNull(t) && r instanceof FailTask) {

            logger.info(t);

            queue.add((FailTask) r);

        }
    }
}
