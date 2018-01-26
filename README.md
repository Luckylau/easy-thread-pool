# Easy-Thread-Pool
### Java异步调用和线程池框架

#### Easy-Thread-Pool项目

集成Spring ,按照demo即可使用，支持线程池的状态，堆栈的收集等。

使用说明：

1.引用依赖

```xml
        <dependency>
            <groupId>tech.luckylau.concurrent</groupId>
            <artifactId>easy-thread-pool</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

2.将conf中的配置文件log4j.xml 和thread-pool.xml放入resources中即可

3.在applicationContext.xml中配置bean

```xml
    <bean id ="easyThreadPool" class="tech.luckylau.concurrent.EasyThreadPoolBeanFactory">
        <property name="configLocation" value="thread-pool.xml"/>
    </bean>
```

4.在需要的地方使用即可

```java
Resource(name = "easyThreadPool")
private EasyThreadPool easyThreadPool;
```

