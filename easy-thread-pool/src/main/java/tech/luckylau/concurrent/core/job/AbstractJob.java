package tech.luckylau.concurrent.core.job;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author luckylau
 * @date 2018/1/4/004 11:20
 */
public abstract class AbstractJob implements Runnable {
    private final AtomicBoolean run = new AtomicBoolean(false);

    protected int interval = 60;

    public void init() {
        run.set(true);
    }

    public void close() {
        run.set(false);
    }


    @Override
    public void run() {
        while (run.get()) {
            execute();
        }
    }

    protected abstract void execute();

    protected void sleep() {
        try {
            Thread.sleep(interval * 1000);
        } catch (InterruptedException e) {
        }
    }

}
