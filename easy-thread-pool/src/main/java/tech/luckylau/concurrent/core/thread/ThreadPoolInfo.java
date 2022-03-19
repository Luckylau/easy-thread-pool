package tech.luckylau.concurrent.core.thread;

/**
 * @author luckylau
 * @date 2017/12/26/026 17:37
 */
public class ThreadPoolInfo {
    private String name;

    private int coreSize;

    private int maxSize;

    private long threadKeepAliveTime;

    private boolean queuePriority = false;

    /**
     * queueSize -1 为 SynchronousQueue
     */
    private int queueSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getThreadKeepAliveTime() {
        return threadKeepAliveTime;
    }

    public void setThreadKeepAliveTime(long threadKeepAliveTime) {
        this.threadKeepAliveTime = threadKeepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public boolean isQueuePriority() {
        return queuePriority;
    }

    public void setQueuePriority(boolean queuePriority) {
        this.queuePriority = queuePriority;
    }
}
