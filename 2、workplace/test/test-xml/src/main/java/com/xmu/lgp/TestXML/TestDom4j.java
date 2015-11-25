package com.xmu.lgp.TestXML;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * TestDom4j.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-3-9
 */
public class TestDom4j {

    public void readStringXml(String xml) {
        Document doc = null;
        try {

            // 读取并解析XML文档
            // SAXReader就是一个管道，用一个流的方式，把xml文件读出来
            //
            // SAXReader reader = new SAXReader(); //User.hbm.xml表示你要解析的xml文档
            // Document document = reader.read(new File("User.hbm.xml"));
            // 下面的是通过解析xml字符串的
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML

            Element rootElt = doc.getRootElement(); // 获取根节点
            System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

            Iterator<?> iter = rootElt.elementIterator("head"); // 获取根节点下的子节点head

            // 遍历head节点
            while (iter.hasNext()) {

                Element recordEle = (Element) iter.next();
                String title = recordEle.elementTextTrim("title"); // 拿到head节点下的子节点title值
                System.out.println("title:" + title);

                Iterator<?> iters = recordEle.elementIterator("script"); // 获取子节点head下的子节点script

                // 遍历Header节点下的Response节点
                while (iters.hasNext()) {

                    Element itemEle = (Element) iters.next();

                    String username = itemEle.elementTextTrim("username"); // 拿到head下的子节点script下的字节点username的值
                    String password = itemEle.elementTextTrim("password");

                    System.out.println("username:" + username);
                    System.out.println("password:" + password);
                }
            }
            Iterator<?> iterss = rootElt.elementIterator("body"); // /获取根节点下的子节点body
            // 遍历body节点
            while (iterss.hasNext()) {

                Element recordEless = (Element) iterss.next();
                String result = recordEless.elementTextTrim("result"); // 拿到body节点下的子节点result值
                System.out.println("result:" + result);

                Iterator<?> itersElIterator = recordEless
                        .elementIterator("form"); // 获取子节点body下的子节点form
                // 遍历Header节点下的Response节点
                while (itersElIterator.hasNext()) {

                    Element itemEle = (Element) itersElIterator.next();

                    String banlce = itemEle.elementTextTrim("banlce"); // 拿到body下的子节点form下的字节点banlce的值
                    String subID = itemEle.elementTextTrim("subID");

                    System.out.println("banlce:" + banlce);
                    System.out.println("subID:" + subID);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static Map<String, String> readStringXmlOut(String xml) {
        Map<String, String> map = new TreeMap<String, String>();
        Document doc = null;
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElt = doc.getRootElement();
            // 拿到根节点的名称
            System.out.println("根节点：" + rootElt.getName());

            // 获取根节点下的子节点head
            Iterator<?> iter = rootElt.elementIterator("head");
            // 遍历head节点
            while (iter.hasNext()) {

                Element recordEle = (Element) iter.next();
                // 拿到head节点下的子节点title值
                String title = recordEle.elementTextTrim("title");
                System.out.println("title:" + title);
                map.put("title", title);
                // 获取子节点head下的子节点script
                Iterator<?> iters = recordEle.elementIterator("script");
                // 遍历Header节点下的Response节点
                while (iters.hasNext()) {
                    Element itemEle = (Element) iters.next();
                    // 拿到head下的子节点script下的字节点username的值
                    String username = itemEle.elementTextTrim("username");
                    String password = itemEle.elementTextTrim("password");

                    System.out.println("username:" + username);
                    System.out.println("password:" + password);
                    map.put("username", username);
                    map.put("password", password);
                }
            }

            // 获取根节点下的子节点body
            Iterator<?> iterss = rootElt.elementIterator("body");
            // 遍历body节点
            while (iterss.hasNext()) {
                Element recordEless = (Element) iterss.next();
                // 拿到body节点下的子节点result值
                String result = recordEless.elementTextTrim("result");
                System.out.println("result:" + result);
                // 获取子节点body下的子节点form
                Iterator<?> itersElIterator = recordEless
                        .elementIterator("form");
                // 遍历Header节点下的Response节点
                while (itersElIterator.hasNext()) {
                    Element itemEle = (Element) itersElIterator.next();
                    // 拿到body下的子节点form下的字节点banlce的值
                    String banlce = itemEle.elementTextTrim("banlce");
                    String subID = itemEle.elementTextTrim("subID");

                    System.out.println("banlce:" + banlce);
                    System.out.println("subID:" + subID);
                    map.put("result", result);
                    map.put("banlce", banlce);
                    map.put("subID", subID);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    
    public static List<Map<String, String>> parseDBXML(String configFile) throws Exception {
        List<Map<String, String>> dbConnections = new ArrayList<Map<String, String>>();
        InputStream is = TestDom4j.class.getClassLoader().getResourceAsStream(configFile);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(is);
        Element connections = document.getRootElement();

        Iterator<?> rootIter = connections.elementIterator();
        while (rootIter.hasNext()) {
            Element connection = (Element) rootIter.next();
            Iterator<?> childIter = connection.elementIterator();
            Map<String, String> connectionInfo = new HashMap<String, String>();
            List<?> attributes = connection.attributes();
            for (int i = 0; i < attributes.size(); ++i) { // 添加节点属性
                connectionInfo.put(((Attribute) attributes.get(i)).getName(), ((Attribute) attributes.get(i)).getValue());
            }
            while (childIter.hasNext()) { // 添加子节点
                Element attr = (Element) childIter.next();
                connectionInfo.put(attr.getName().trim(), attr.getText().trim());
            }
            dbConnections.add(connectionInfo);
        }

        return dbConnections;
    }
}
