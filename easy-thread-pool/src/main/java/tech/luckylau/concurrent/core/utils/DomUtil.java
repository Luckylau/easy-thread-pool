package tech.luckylau.concurrent.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author luckylau
 * @date 2017/12/26/026 17:53
 */
public class DomUtil {

    private final static Logger logger = LoggerFactory.getLogger(DomUtil.class);

    public static Document createDocument(String classPathXmlFile){
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        Document document = null;

        try {
            DocumentBuilder dom = domFactory.newDocumentBuilder();
            document = dom.parse(DomUtil.class.getResourceAsStream(classPathXmlFile));
        } catch (Exception var4) {
            logger.error(String.format("create Document of xml file[%s] occurs error", classPathXmlFile), var4);
        }

        return document;
    }

}
