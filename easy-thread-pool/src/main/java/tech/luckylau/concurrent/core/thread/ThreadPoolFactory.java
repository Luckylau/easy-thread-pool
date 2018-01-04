package tech.luckylau.concurrent.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liujun
 * @date 2017/12/29/029 18:26
 */
public class ThreadPoolFactory implements ThreadFactory {

    private static final AtomicInteger threadNumber = new AtomicInteger(1);
    private static final String DEFAULT_THREAD_POOL_NAME_PRIFIX = "Easy-thread-pool";
    private ThreadGroup threadGroup;
    private String threadPoolName;

    public ThreadPoolFactory(String threadPoolName){
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s != null)? s.getThreadGroup():Thread.currentThread().getThreadGroup();
        this.threadPoolName = threadPoolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r, DEFAULT_THREAD_POOL_NAME_PRIFIX + "-" + threadPoolName + threadNumber.getAndIncrement(), 0);
        if(t.isDaemon()) {
            t.setDaemon(false);
        }
        if(t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
