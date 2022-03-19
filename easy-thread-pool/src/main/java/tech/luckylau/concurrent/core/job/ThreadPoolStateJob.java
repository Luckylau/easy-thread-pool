package tech.luckylau.concurrent.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author luckylau
 * @date 2018/1/4/004 11:30
 */
public class ThreadPoolStateJob extends AbstractJob {
    private final static Logger logger = LoggerFactory.getLogger(ThreadPoolStateJob.class);

    private final Map<String, ExecutorService> multiThreadPool;

    public ThreadPoolStateJob(Map<String, ExecutorService> multiThreadPool, int interval) {
        this.multiThreadPool = multiThreadPool;
        super.interval = interval;
    }

    @Override
    protected void execute() {
        Set<Map.Entry<String, ExecutorService>> poolSet = multiThreadPool.entrySet();
        for (Map.Entry<String, ExecutorService> entry : poolSet) {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) entry.getValue();
            logger.info("ThreadPool:{}, ActiveThread:{}, TotalTask:{}, CompletedTask:{}, Queue:{}",
                    entry.getKey(), pool.getActiveCount(), pool.getTaskCount(), pool.getCompletedTaskCount(), pool.getQueue().size());
        }

        super.sleep();

    }
}
