package tech.luckylau.concurrent.core.utils;

import tech.luckylau.concurrent.core.thread.ThreadStateInfo;

/**
 * @author luckylau
 * @date 2018/1/4/004 13:55
 */
public class ThreadUtil {

    public static ThreadGroup getRootThreadGroup(){
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (null != threadGroup.getParent()) {
            threadGroup = threadGroup.getParent();
        }
        return threadGroup;
    }

    public static ThreadStateInfo stateThreadGroup(ThreadGroup threadGroup){
        if( null == threadGroup){
            throw new IllegalArgumentException("threadGroup is null");
        }

        int threadCapacity = threadGroup.activeCount() * 2;
        Thread[] threadlist = new Thread[threadCapacity];
        int threadNum = threadGroup.enumerate(threadlist);

        ThreadStateInfo threadStateInfo = new ThreadStateInfo();
        for(int j = 0; j < threadNum; j++) {
            Thread thread = threadlist[j];
            switch (thread.getState()) {
                case NEW:
                    threadStateInfo.newCount += 1;
                    break;
                case RUNNABLE:
                    threadStateInfo.runnableCount += 1;
                    break;
                case BLOCKED:
                    threadStateInfo.blockedCount += 1;
                    break;
                case WAITING:
                    threadStateInfo.waitingCount += 1;
                    break;
                case TIMED_WAITING:
                    threadStateInfo.timedWaitingCount += 1;
                    break;
                case TERMINATED:
                    threadStateInfo.terminatedCount += 1;
                    break;
                default:
                    // nothing
                    break;
            }
        }
        return threadStateInfo;
    }


}
