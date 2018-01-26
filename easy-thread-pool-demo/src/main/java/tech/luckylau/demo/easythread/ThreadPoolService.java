package tech.luckylau.demo.easythread;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.luckylau.concurrent.core.handler.DiscardFailHandler;
import tech.luckylau.concurrent.core.handler.Failhandler;
import tech.luckylau.concurrent.service.EasyThreadPool;

import javax.annotation.Resource;

/**
 * @author luckylau
 * @date 2018/1/25/025 19:35
 */
@RestController
@RequestMapping("/demo")
public class ThreadPoolService {

    @Resource(name = "easyThreadPool")
    private EasyThreadPool easyThreadPool;

    @RequestMapping(value = "/start")
    public void testSubmitTask(){
        easyThreadPool.submit(new Task(), "default", new DiscardFailHandler<Runnable>());
    }

}

