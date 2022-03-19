package tech.luckylau.concurrent.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.luckylau.concurrent.core.thread.ThreadStateInfo;
import tech.luckylau.concurrent.core.utils.ThreadUtil;

/**
 * @author luckylau
 * @date 2018/1/4/004 12:25
 */
public class ThreadStateJob extends AbstractJob {

    private final static Logger logger = LoggerFactory.getLogger(ThreadStateJob.class);

    private static final String DEFAULT_THREAD_GROUP_NAME_PREFIX = "Easy-thread-Group-";

    public ThreadStateJob(int interval) {
        super.interval = interval;
    }

    @Override
    protected void execute() {
        ThreadGroup root = ThreadUtil.getRootThreadGroup();
        int groupCapacity = root.activeGroupCount() * 2;
        ThreadGroup[] groupList = new ThreadGroup[groupCapacity];
        int groupNum = root.enumerate(groupList, true);
        for (int i = 0; i < groupNum; i++) {
            ThreadGroup threadGroup = groupList[i];
            if (threadGroup.getName().contains(DEFAULT_THREAD_GROUP_NAME_PREFIX)) {
                ThreadStateInfo stateInfo = ThreadUtil.stateThreadGroup(threadGroup);
                logger.info("ThreadGroup:{}, New:{},  Runnable:{}, Blocked:{}, Waiting:{}, TimedWaiting:{}, Terminated:{}",
                        threadGroup.getName(), stateInfo.newCount, stateInfo.runnableCount, stateInfo.blockedCount,
                        stateInfo.waitingCount, stateInfo.terminatedCount, stateInfo.terminatedCount);
            }
        }
        super.sleep();

    }
}
