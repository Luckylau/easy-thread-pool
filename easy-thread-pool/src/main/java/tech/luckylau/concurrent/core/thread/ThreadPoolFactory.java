package tech.luckylau.concurrent.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luckylau
 * @date 2017/12/29/029 18:26
 */
public class ThreadPoolFactory implements ThreadFactory {

    private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
    private static final String DEFAULT_THREAD_POOL_NAME_PREFIX = "Easy-thread-Pool-";
    private static final String DEFAULT_THREAD_GROUP_NAME_PREFIX = "Easy-thread-Group-";
    private ThreadGroup threadGroup;
    private String threadPoolName;

    public ThreadPoolFactory(String threadPoolName) {
        this.threadPoolName = threadPoolName;
        threadGroup = new ThreadGroup(DEFAULT_THREAD_GROUP_NAME_PREFIX + threadPoolName);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r, DEFAULT_THREAD_POOL_NAME_PREFIX + threadPoolName + "-" + THREAD_NUMBER.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
