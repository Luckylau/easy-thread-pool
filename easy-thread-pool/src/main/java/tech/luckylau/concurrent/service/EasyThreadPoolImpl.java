package tech.luckylau.concurrent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.luckylau.concurrent.core.thread.ThreadPoolConfig;
import tech.luckylau.concurrent.core.thread.ThreadPoolFactory;
import tech.luckylau.concurrent.core.thread.ThreadPoolInfo;
import tech.luckylau.concurrent.core.thread.ThreadPoolStatus;
import tech.luckylau.concurrent.core.handler.Failhandler;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author luckylau
 * @date 2017/12/22/022 14:25
 */
public class EasyThreadPoolImpl implements EasyThreadPool {
    private final static Logger logger = LoggerFactory.getLogger(EasyThreadPoolImpl.class);

    private ThreadPoolConfig threadPoolConfig;

    private volatile int status = ThreadPoolStatus.UNINITIALIZED ;

    private static final long closeServiceWaitTime = 2 * 60 * 1000;

    private static Lock lock = new ReentrantLock();

    private static final String DEFAULT_THREAD_POOL = "default";

    private Map<String, ExecutorService> multiThreadPool = new HashMap<>();

    public EasyThreadPoolImpl(ThreadPoolConfig threadPoolConfig) {
        this.threadPoolConfig = threadPoolConfig;
    }

    @Override
    public void init(){
        lock.lock();
        if(status != ThreadPoolStatus.UNINITIALIZED){
            logger.warn("initialization failed, status was wrong, current status was {} (0:UNINITIALIZED, 1:INITIALITION_SUCCESSFUL, 2:INITIALITION_FAILED, 3:DESTROYED)", status);
            return;
        }

        try {
            initThreadPool();
            status = ThreadPoolStatus.INITIALIYION_SUCESSFUL;
        } catch (Exception e) {
            status = ThreadPoolStatus.INITIALIYION_FAILED;
            logger.error("initialization failed ", e.getMessage());
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run(){
                    close();
                }
            });
            lock.unlock();
        }


    }

    private void initThreadPool(){
        if(threadPoolConfig == null){
            throw new IllegalStateException("threadPoolConfig initialization failed, is null");
        }

        List<ThreadPoolInfo> threadPoolInfos= threadPoolConfig.getThreadPoolInfo();
        for(ThreadPoolInfo threadPoolInfo : threadPoolInfos){
            int queueSize = threadPoolInfo.getQueueSize();
            if(queueSize == -1){
                ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getCoreSize(),threadPoolInfo.getThreadKeepAliveTime(),TimeUnit.SECONDS, null, new ThreadPoolFactory(threadPoolInfo.getName()));
                multiThreadPool.put(threadPoolInfo.getName(), threadPool);
                logger.info("initialization thread pool '{}' sucess", threadPoolInfo.getName());
            }else{
                BlockingQueue<Runnable> workQueue;
                if(threadPoolInfo.isQueuePriority()){
                    workQueue = new PriorityBlockingQueue<Runnable>(threadPoolInfo.getQueueSize());
                }else {
                    workQueue = new ArrayBlockingQueue<Runnable>(threadPoolInfo.getQueueSize());
                }
                ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getMaxSize(), threadPoolInfo.getThreadKeepAliveTime(), TimeUnit.SECONDS, workQueue, new ThreadPoolFactory(threadPoolInfo.getName()));
                multiThreadPool.put(threadPoolInfo.getName(), threadPool);
                logger.info("initialization thread pool '{}' sucess", threadPoolInfo.getName());
            }
        }

    }


    @Override
    public void close(){
        if(ThreadPoolStatus.DESTROYED == status){
            return;
        }
        try{
            for(Map.Entry<String, ExecutorService> entry: multiThreadPool.entrySet()){
                entry.getValue().awaitTermination(closeServiceWaitTime, TimeUnit.MICROSECONDS);
                entry.getValue().shutdown();
                logger.info("shutdown the thread pool '{}'", entry.getKey());
            }
        }catch (InterruptedException e){
            logger.error("shut down threadPool error", e.getMessage());
        }




    }

    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }

    @Override
    public Future<?> submit(Runnable task, String threadPoolName) {
        return null;
    }

    @Override
    public Future<?> submit(Runnable task, String threadPoolName, Failhandler<Runnable> failHandler) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, String threadPoolName) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, String threadPoolName, Failhandler<Callable<T>> failHandler) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeoutUnit) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeoutUnit, String threadpoolName) {
        return null;
    }
}
