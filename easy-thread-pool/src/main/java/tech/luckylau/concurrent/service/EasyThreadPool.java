package tech.luckylau.concurrent.service;

import tech.luckylau.concurrent.core.handler.Failhandler;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author luckylau
 * @date 2017/12/22/022 20:22
 */
public interface EasyThreadPool {

    public void init();

    public void close();
    /**
     * 提交一个不需要返回值的异步任务给默认的线程池执行。
     * @param task
     * @return
     */
    public Future<?> submit(Runnable task);

    /**
     * 提交一个不需要返回值的异步任务给指定的线程池执行。
     * @param task
     * @param threadPoolName
     * @return
     */
    public Future<?> submit(Runnable task, String threadPoolName);

    /**
     * 提交一个不需要返回值的异步任务给指定的线程池执行,并指定无法提交时的处理方式。
     * @param task
     * @param threadPoolName
     * @param failHandler
     * @return
     */
    public Future<?> submit(Runnable task, String threadPoolName, Failhandler<Runnable> failHandler);

    /**
     * 提交一个需要返回值的异步任务给默认的线程池执行。
     * @param task
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(Callable<T> task);

    /**
     * 提交一个需要返回值的异步任务给指定的线程池执行。
     * @param task
     * @param threadPoolName
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(Callable<T> task, String threadPoolName);

    /**
     * 提交一个需要返回值的异步任务给指定的线程池执行,并指定无法提交时的处理方式。
     * @param task
     * @param threadPoolName
     * @param failHandler
     * @return
     */
    public <T> Future<T> submit(Callable<T> task, String threadPoolName, Failhandler<Callable<T>> failHandler);

    /**
     * 在线程池"default"执行多个需要返回值的异步任务，并设置超时时间。
     * @param tasks
     * @param timeout
     * @param timeoutUnit
     * @param <T>
     * @return
     */
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeoutUnit);

    /**
     * 在指定的线程池执行多个需要返回值的异步任务，并设置超时时间。
     * @param tasks
     * @param timeout
     * @param timeoutUnit
     * @param threadpoolName
     * @param <T>
     * @return
     */
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit timeoutUnit, String threadpoolName);


}
