package tech.luckylau.concurrent.core.handler;

/**队列满时候,直接丢弃不处理
 * @author luckylau
 * @date 2017/12/22/022 15:10
 */
public class DiscardFailHandler<T> implements Failhandler<T> {

    @Override
    public void execute(T task) {
        //nothing
    }
}
