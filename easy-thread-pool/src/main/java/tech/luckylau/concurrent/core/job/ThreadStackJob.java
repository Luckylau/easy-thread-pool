package tech.luckylau.concurrent.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author luckylau
 * @date 2018/1/4/004 11:48
 */
public class ThreadStackJob extends AbstractJob {

    private final static Logger logger = LoggerFactory.getLogger(ThreadStackJob.class);

    private final static String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final String DEFAULT_THREAD_POOL_NAME_PREFIX = "Easy-thread-pool";

    private final static int BUFFER_SIZE = 4096;

    public ThreadStackJob(int interval) {
        super.interval = interval;
    }


    @Override
    protected void execute() {
        Map<Thread, StackTraceElement[]> stackMap = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> entry : stackMap.entrySet()) {
            Thread thread = entry.getKey();
            if (thread != null && thread.getName().contains(DEFAULT_THREAD_POOL_NAME_PREFIX)) {
                StringBuilder buffer = new StringBuilder(BUFFER_SIZE).append("name:")
                        .append(thread.getName())
                        .append(", id:").append(thread.getId())
                        .append(", status:").append(thread.getState().toString())
                        .append(", priority:").append(thread.getPriority())
                        .append(LINE_SEPARATOR);

                StackTraceElement[] stackList = entry.getValue();
                for (StackTraceElement ste : stackList) {
                    buffer.append(ste.toString()).append(LINE_SEPARATOR);
                }
                logger.info(buffer.toString());
            }
        }
        super.sleep();

    }
}
