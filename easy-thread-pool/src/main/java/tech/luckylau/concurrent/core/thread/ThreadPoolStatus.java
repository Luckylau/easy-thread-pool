package tech.luckylau.concurrent.core.thread;

/**
 * @author luckylau
 * @date 2017/12/29/029 17:36
 */
public class ThreadPoolStatus {

    public final static int UNINITIALIZED = 0;

    public final static int INITIALISATION_SUCCESSFULLY = 1;

    public final static int INITIALIZATION_FAILED = 2;

    public final static int DESTROYED = 3;
}
