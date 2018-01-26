package tech.luckylau.demo.easythread;

/**
 * @author luckylau
 * @date 2018/1/25/025 19:55
 */
public class Task implements Runnable {

    @Override
    public void run() {
        System.out.println("i am working");
    }

}
