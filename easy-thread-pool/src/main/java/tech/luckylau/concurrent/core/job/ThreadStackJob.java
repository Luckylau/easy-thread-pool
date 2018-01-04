package tech.luckylau.concurrent.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author luckylau
 * @date 2018/1/4/004 11:48
 */
public class ThreadStackJob extends AbstractJob{

    private final static Logger logger = LoggerFactory.getLogger(ThreadStackJob.class);

    private final static  String lineSeparator = System.getProperty("line.separator");

    private static final String DEFAULT_THREAD_POOL_NAME_PRIFIX = "Easy-thread-pool";

    private final static int BUFFER_SIZE = 4096;

    public ThreadStackJob(int interval){
        super.interval = interval;
    }


    @Override
    protected void execute() {
        Map<Thread, StackTraceElement[]> stackMap = Thread.getAllStackTraces();
        for(Map.Entry<Thread, StackTraceElement[]> entry : stackMap.entrySet()){
            Thread thread = entry.getKey();
            if(thread != null && thread.getName().contains(DEFAULT_THREAD_POOL_NAME_PRIFIX)){
                StringBuffer buffer = new StringBuffer(BUFFER_SIZE).append("name:")
                        .append(thread.getName())
                        .append(", id:").append(thread.getId())
                        .append(", status:").append(thread.getState().toString())
                        .append(", priority:").append(thread.getPriority())
                        .append(lineSeparator);

                StackTraceElement[] stackList = entry.getValue();
                for(StackTraceElement ste : stackList){
                    buffer.append(ste.toString()).append(lineSeparator);
                }
                logger.info(buffer.toString());
            }
        }
        super.sleep();

    }
}
