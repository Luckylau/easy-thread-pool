package tech.luckylau.concurrent.core.utils;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * @author luckylau
 * @date 2017/12/26/026 17:59
 */
public class NodeParser {
    private Node node;

    private Map<String, String> attrMap;

    private List<Node> childNodes;

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public String getChildNodeValue(String nodeName){
        if(nodeName == null){
            return null;
        }
        for(Node node : childNodes){
            String name = node.getNodeName();
            if(Objects.equals(nodeName, name)){
                return node.getTextContent();
            }
        }
        return null;
    }

    public String getAttributeValue(String attrName){
        return attrMap.get(attrName);
    }


    public NodeParser(Node node){
        this.node = node;
        this.childNodes = initChildNodeList(node);
        this.attrMap = initAttrMap(node);
    }

    private List<Node> initChildNodeList(Node node){

        List<Node> childNodes = new ArrayList<Node>();
        NodeList nodeList = node.getChildNodes();

        for(int i = 0 ; i < nodeList.getLength(); i++){
            Node tmp = nodeList.item(i);
            childNodes.add(tmp);
        }

        return childNodes;
    }

    private Map<String, String> initAttrMap(Node node){

        Map<String, String> map = new HashMap<>();
        NamedNodeMap namedNodeMap = node.getAttributes();
        if(namedNodeMap != null){
            for(int i = 0 ; i < namedNodeMap.getLength();i++){
                Node attr = namedNodeMap.item(i);
                map.put(attr.getNodeName(), attr.getNodeValue());
            }
        }
        return map;

    }


}
