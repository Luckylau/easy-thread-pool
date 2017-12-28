package tech.luckylau.concurrent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.luckylau.concurrent.core.config.ThreadPoolConfig;
import tech.luckylau.concurrent.core.handler.Failhandler;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author luckylau
 * @date 2017/12/22/022 14:25
 */
public class EasyThreadPoolImpl implements EasyThreadPool {
    private final static Logger logger = LoggerFactory.getLogger(EasyThreadPoolImpl.class);

    private ThreadPoolConfig threadPoolConfig;

    private static final String DEFAULT_THREAD_POOL = "default";

    public EasyThreadPoolImpl(ThreadPoolConfig threadPoolConfig) {
        this.threadPoolConfig = threadPoolConfig;
    }

    @Override
    public void init(){

    }

    @Override
    public void close(){

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
