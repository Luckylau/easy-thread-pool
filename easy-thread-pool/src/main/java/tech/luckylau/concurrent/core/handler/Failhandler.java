package tech.luckylau.concurrent.core.handler;

/**
 * 队列满时候，任务的处理
 *
 * @author luckylau
 * @date 2017/12/21/021 19:40
 */
public interface Failhandler<T> {

    void execute(T task);

}
