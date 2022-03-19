package tech.luckylau.concurrent;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tech.luckylau.concurrent.core.thread.ThreadPoolConfig;
import tech.luckylau.concurrent.core.thread.ThreadPoolInfo;
import tech.luckylau.concurrent.core.utils.DomUtil;
import tech.luckylau.concurrent.core.utils.NodeParser;
import tech.luckylau.concurrent.service.EasyThreadPool;
import tech.luckylau.concurrent.service.EasyThreadPoolImpl;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.Assert.state;

/**
 * @author luckylau
 * @date 2017/12/22/022 15:41
 */
public class EasyThreadPoolBeanFactory implements FactoryBean<EasyThreadPool>, InitializingBean {

    private ThreadPoolConfig threadPoolConfig;

    private final List<ThreadPoolInfo> threadPoolInfos = new ArrayList<>();

    /**
     * 配置文件的位置
     */
    private String configLocation;

    private EasyThreadPool easyThreadPool;


    @Override
    public void afterPropertiesSet() throws Exception {
        state((configLocation != null), " 'configLocation' can not be null");
        configLocation = "/" + configLocation;
        threadPoolConfig = new ThreadPoolConfig();
        easyThreadPool = buildEasyThreadPool();
    }

    private EasyThreadPool buildEasyThreadPool() {
        Document document = DomUtil.createDocument(configLocation);

        Element root = document.getDocumentElement();
        NodeParser rootParser = new NodeParser(root);
        List<Node> nodeList = rootParser.getChildNodes();
        for (Node node : nodeList) {
            NodeParser nodeParser = new NodeParser(node);
            if ("pool".equals(node.getNodeName())) {
                ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo();
                threadPoolInfo.setName(nodeParser.getAttributeValue("name"));
                threadPoolInfo.setCoreSize(Integer.parseInt(nodeParser.getChildNodeValue("corePoolSize")));
                threadPoolInfo.setMaxSize(Integer.parseInt(nodeParser.getChildNodeValue("maxPoolSize")));
                threadPoolInfo.setThreadKeepAliveTime(Long.parseLong(nodeParser.getChildNodeValue("keepAliveTime")));
                threadPoolInfo.setQueueSize(Integer.parseInt(nodeParser.getChildNodeValue("workQueueSize")));
                threadPoolInfo.setQueuePriority(Boolean.parseBoolean(nodeParser.getChildNodeValue("queuePriority")));
                threadPoolInfos.add(threadPoolInfo);
            } else if ("threadstate".equals(node.getNodeName())) {
                threadPoolConfig.setThreadStateSwitch("on".equalsIgnoreCase(nodeParser.getAttributeValue("switch")));
                threadPoolConfig.setThreadStateInterval(Integer.parseInt(nodeParser.getAttributeValue("interval")));
            } else if ("threadpoolstate".equals(node.getNodeName())) {
                threadPoolConfig.setThreadPoolStateSwitch("on".equalsIgnoreCase(nodeParser.getAttributeValue("switch")));
                threadPoolConfig.setThreadPoolStateInterval(Integer.parseInt(nodeParser.getAttributeValue("interval")));
            } else if ("threadstack".equals(node.getNodeName())) {
                threadPoolConfig.setThreadStackSwitch("on".equalsIgnoreCase(nodeParser.getAttributeValue("switch")));
                threadPoolConfig.setThreadStackInterval(Integer.parseInt(nodeParser.getAttributeValue("interval")));
            }
        }
        threadPoolConfig.setThreadPoolInfo(threadPoolInfos);

        EasyThreadPool easyThreadPool = new EasyThreadPoolImpl(threadPoolConfig);
        easyThreadPool.init();
        return easyThreadPool;
    }

    @Override
    public EasyThreadPool getObject() throws Exception {
        if (this.easyThreadPool == null) {
            afterPropertiesSet();
        }
        return this.easyThreadPool;
    }

    @Override
    public Class<?> getObjectType() {
        return this.easyThreadPool == null ? EasyThreadPool.class : this.easyThreadPool.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }
}
