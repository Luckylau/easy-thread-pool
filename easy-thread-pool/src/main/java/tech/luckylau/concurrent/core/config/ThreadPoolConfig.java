package tech.luckylau.concurrent.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理xml文件
 * @author luckylau
 * @date 2017/12/22/022 14:28
 */
public class ThreadPoolConfig {

    private Map<String, ThreadPoolInfo> threadPoolInfo;

    /**是否开启线程池状态收集
     * 时间间隔单位为秒
     * */
    private boolean threadPoolStateSwitch = false;
    private int threadPoolStateInterval = 60;

    /**是否开启线程状态收集
     * 时间间隔单位为秒
     * */
    private boolean threadStateSwitch = false;
    private int threadStateInterval = 60;

    /**是否开启线程堆栈收集
     * 时间间隔单位为秒
     * */
    private boolean threadStackSwitch = false;
    private int threadStackInterval = 60;

    public boolean isThreadStateSwitch() {
        return threadStateSwitch;
    }

    public void setThreadStateSwitch(boolean threadStateSwitch) {
        this.threadStateSwitch = threadStateSwitch;
    }

    public int getThreadStateInterval() {
        return threadStateInterval;
    }

    public void setThreadStateInterval(int threadStateInterval) {
        this.threadStateInterval = threadStateInterval;
    }

    public boolean isThreadStackSwitch() {
        return threadStackSwitch;
    }

    public void setThreadStackSwitch(boolean threadStackSwitch) {
        this.threadStackSwitch = threadStackSwitch;
    }

    public int getThreadStackInterval() {
        return threadStackInterval;
    }

    public void setThreadStackInterval(int threadStackInterval) {
        this.threadStackInterval = threadStackInterval;
    }

    public Map<String, ThreadPoolInfo> getThreadPoolInfo() {
        return threadPoolInfo;
    }

    public void setThreadPoolInfo(Map<String, ThreadPoolInfo> threadPoolInfo) {
        this.threadPoolInfo = threadPoolInfo;
    }

    public boolean isThreadPoolStateSwitch() {
        return threadPoolStateSwitch;
    }

    public void setThreadPoolStateSwitch(boolean threadPoolStateSwitch) {
        this.threadPoolStateSwitch = threadPoolStateSwitch;
    }

    public int getThreadPoolStateInterval() {
        return threadPoolStateInterval;
    }

    public void setThreadPoolStateInterval(int threadPoolStateInterval) {
        this.threadPoolStateInterval = threadPoolStateInterval;
    }
}
