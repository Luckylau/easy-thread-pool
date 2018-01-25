package tech.luckylau.concurrent.exception;

/**
 * @author luckylau
 * @date 2018/1/25/025 16:42
 */
public class EasyThreadExpection extends RuntimeException {

    public EasyThreadExpection(String message) {
        super(message);
    }

    public EasyThreadExpection(String message, Throwable cause) {
        super(message, cause);
    }
}
