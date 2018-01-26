package tech.luckylau.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author luckylau
 * @date 2017/12/12/012 11:12
 */


@ImportResource(value = {"classpath*:META-INF/*/applicationContext.xml"})
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(Application.class,args);
    }
}
