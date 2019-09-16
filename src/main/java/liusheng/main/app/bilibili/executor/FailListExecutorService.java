package liusheng.main.app.bilibili.executor;

import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;

public class FailListExecutorService extends ThreadPoolExecutor {
    private final Logger logger = Logger.getLogger(FailListExecutorService.class);
    public final Queue<FailTask> queue = new ConcurrentLinkedQueue();

    public FailListExecutorService(int fixedSize) {
        super(fixedSize, fixedSize, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1024));
    }

    public FailListExecutorService() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {

    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (!Objects.isNull(null) && r instanceof FailTask) {

            logger.info(t);

            queue.add((FailTask) r);
        }
    }
}
