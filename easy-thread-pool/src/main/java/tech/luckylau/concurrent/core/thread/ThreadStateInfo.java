package tech.luckylau.concurrent.core.thread;

import java.io.Serializable;

/**
 * @author luckylau
 * @date 2018/1/4/004 13:59
 */
public class ThreadStateInfo {
    public int newCount;

    public int runnableCount;

    public int blockedCount;

    public int waitingCount;

    public int timedWaitingCount;

    public int terminatedCount;


}
