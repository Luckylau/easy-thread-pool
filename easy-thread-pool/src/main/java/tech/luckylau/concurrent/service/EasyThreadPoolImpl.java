package tech.luckylau.concurrent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import tech.luckylau.concurrent.core.handler.Failhandler;
import tech.luckylau.concurrent.core.job.ThreadPoolStateJob;
import tech.luckylau.concurrent.core.job.ThreadStackJob;
import tech.luckylau.concurrent.core.job.ThreadStateJob;
import tech.luckylau.concurrent.core.thread.ThreadPoolConfig;
import tech.luckylau.concurrent.core.thread.ThreadPoolFactory;
import tech.luckylau.concurrent.core.thread.ThreadPoolInfo;
import tech.luckylau.concurrent.core.thread.ThreadPoolStatus;
import tech.luckylau.concurrent.exception.EasyThreadExpection;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author luckylau
 * @date 2017/12/22/022 14:25
 */
public class EasyThreadPoolImpl implements EasyThreadPool {
    private final static Logger logger = LoggerFactory.getLogger(EasyThreadPoolImpl.class);
    private static final long closeServiceWaitTime = 2 * 60 * 1000;
    private static final String DEFAULT_THREAD_POOL = "default";
    private static Lock lock = new ReentrantLock();
    private ThreadPoolConfig threadPoolConfig;
    private volatile int status = ThreadPoolStatus.UNINITIALIZED;
    private Map<String, ExecutorService> multiThreadPool = new HashMap<>();

    private ThreadStackJob threadStackJob;

    private ThreadPoolStateJob threadPoolStateJob;

    private ThreadStateJob threadStateJob;

    public EasyThreadPoolImpl(ThreadPoolConfig threadPoolConfig) {
        this.threadPoolConfig = threadPoolConfig;
    }

    @Override
    public void init() {
        lock.lock();
        if (status != ThreadPoolStatus.UNINITIALIZED) {
            logger.warn("initialization failed, status was wrong, current status was {} (0:UNINITIALIZED, 1:INITIALITION_SUCCESSFUL, 2:INITIALITION_FAILED, 3:DESTROYED)", status);
            return;
        }

        try {
            initThreadPool();
            startThreadPoolStateJob();
            startThreadStackJob();
            startThreadStateJob();
            status = ThreadPoolStatus.INITIALIYION_SUCESSFUL;
        } catch (Exception e) {
            status = ThreadPoolStatus.INITIALIYION_FAILED;
            logger.error("initialization failed ", e.getMessage());
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    close();
                }
            });
            lock.unlock();
        }


    }

    private void initThreadPool() {
        if (threadPoolConfig == null) {
            throw new IllegalStateException("threadPoolConfig initialization failed, is null");
        }

        List<ThreadPoolInfo> threadPoolInfos = threadPoolConfig.getThreadPoolInfo();
        for (ThreadPoolInfo threadPoolInfo : threadPoolInfos) {
            int queueSize = threadPoolInfo.getQueueSize();
            if (queueSize == -1) {
                ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getCoreSize(), threadPoolInfo.getThreadKeepAliveTime(), TimeUnit.SECONDS, null, new ThreadPoolFactory(threadPoolInfo.getName()));
                multiThreadPool.put(threadPoolInfo.getName(), threadPool);
                logger.info("initialization thread pool '{}' sucess", threadPoolInfo.getName());
            } else {
                BlockingQueue<Runnable> workQueue;
                if (threadPoolInfo.isQueuePriority()) {
                    workQueue = new PriorityBlockingQueue<Runnable>(threadPoolInfo.getQueueSize());
                } else {
                    workQueue = new ArrayBlockingQueue<Runnable>(threadPoolInfo.getQueueSize());
                }
                ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getMaxSize(), threadPoolInfo.getThreadKeepAliveTime(), TimeUnit.SECONDS, workQueue, new ThreadPoolFactory(threadPoolInfo.getName()));
                multiThreadPool.put(threadPoolInfo.getName(), threadPool);
                logger.info("initialization thread pool '{}' sucess", threadPoolInfo.getName());
            }
        }

    }

    private void startThreadPoolStateJob() {
        if (!threadPoolConfig.isThreadPoolStateSwitch()) {
            return;
        }

        threadPoolStateJob = new ThreadPoolStateJob(multiThreadPool, threadPoolConfig.getThreadPoolStateInterval());
        threadPoolStateJob.init();
        Thread jobThread = new Thread(threadPoolStateJob);
        jobThread.setName("easy-thread-pool-threadPoolState");
        jobThread.start();

        logger.info("start job 'easy-thread-pool-threadpoolstate' success");

    }

    private void startThreadStackJob() {
        if (!threadPoolConfig.isThreadStackSwitch()) {
            return;
        }

        threadStackJob = new ThreadStackJob(threadPoolConfig.getThreadStackInterval());
        threadStackJob.init();
        Thread jobThread = new Thread(threadStackJob);
        jobThread.setName("easy-thread-pool-threadstack");
        jobThread.start();

        logger.info("start job 'easy-thread-pool-threadstack' success");

    }

    private void startThreadStateJob() {
        if (!threadPoolConfig.isThreadStateSwitch()) {
            return;
        }

        threadStateJob = new ThreadStateJob(threadPoolConfig.getThreadStateInterval());
        threadStateJob.init();
        Thread jobThread = new Thread(threadStateJob);
        jobThread.setName("easy-thread-pool-threadstate");
        jobThread.start();

        logger.info("start job 'easy-thread-pool-threadstate' success");

    }

    @Override
    public void close() {
        if (ThreadPoolStatus.DESTROYED == status) {
            return;
        }
        try {
            for (Map.Entry<String, ExecutorService> entry : multiThreadPool.entrySet()) {
                entry.getValue().awaitTermination(closeServiceWaitTime, TimeUnit.MICROSECONDS);
                entry.getValue().shutdown();
                logger.info("shutdown the thread pool '{}'", entry.getKey());
            }
        } catch (InterruptedException e) {
            logger.error("shut down threadPool error", e.getMessage());
        }
    }

    private ExecutorService getExistsThreadPool(String threadpoolName) {
        if (StringUtils.isEmpty(threadpoolName)) {
            throw new EasyThreadExpection("threadPool name is empty");
        }
        ExecutorService threadPool = multiThreadPool.get(threadpoolName);
        return threadPool;
    }


    @Override
    public Future<?> submit(Runnable task) {
        return submit(task, DEFAULT_THREAD_POOL);
    }

    @Override
    public Future<?> submit(Runnable task, String threadPoolName) {
        if (task == null) {
            throw new EasyThreadExpection("task is null");
        }
        ExecutorService threadPool = getExistsThreadPool(threadPoolName);
        logger.debug("submit a task to thread pool {}", threadPoolName);

        return threadPool.submit(task);
    }

    @Override
    public Future<?> submit(Runnable task, Failhandler<Runnable> failHandler) {
        return submit(task, DEFAULT_THREAD_POOL, failHandler);
    }

    @Override
    public Future<?> submit(Runnable task, String threadPoolName, Failhandler<Runnable> failHandler) {

        try {
            return submit(task, threadPoolName);
        } catch (RejectedExecutionException e) {
            if (null != failHandler) {
                failHandler.execute(task);
            }
        }
        return null;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, DEFAULT_THREAD_POOL);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, String threadPoolName) {
        if (task == null) {
            throw new EasyThreadExpection("task is null");
        }
        ExecutorService threadPool = getExistsThreadPool(threadPoolName);
        logger.debug("submit a task to thread pool {}", threadPoolName);

        return threadPool.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, Failhandler<Callable<T>> failHandler) {
        return submit(task, DEFAULT_THREAD_POOL, failHandler);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, String threadPoolName, Failhandler<Callable<T>> failHandler) {
        try {
            return submit(task, threadPoolName);
        } catch (RejectedExecutionException e) {
            if (null != failHandler) {
                failHandler.execute(task);
            }
        }
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks) {
        return invokeAll(tasks, DEFAULT_THREAD_POOL);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeoutUnit) {

        return invokeAll(tasks, timeout, timeoutUnit, DEFAULT_THREAD_POOL);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, String threadpoolName) {
        if (tasks == null || tasks.isEmpty()) {
            throw new EasyThreadExpection("task list is null or empty");
        }

        ExecutorService threadPool = getExistsThreadPool(threadpoolName);

        try {
            return threadPool.invokeAll(tasks);
        } catch (InterruptedException e) {
            logger.error("invoke task list occurs error", e);
        }
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeoutUnit, String threadpoolName) {
        if (tasks == null || tasks.isEmpty()) {
            throw new EasyThreadExpection("task list is null or empty");
        }
        if (timeout <= 0) {
            throw new EasyThreadExpection("timeout less than or equals zero");
        }

        ExecutorService threadPool = getExistsThreadPool(threadpoolName);

        try {
            return threadPool.invokeAll(tasks, timeout, timeoutUnit);
        } catch (InterruptedException e) {
            logger.error("invoke task list occurs error", e);
        }

        return null;
    }
}
