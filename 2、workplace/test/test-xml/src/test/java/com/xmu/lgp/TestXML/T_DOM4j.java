package com.xmu.lgp.TestXML;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.xmu.lgp.TestXML.TestDom4j;

/**
 * T_DOM4j.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-9
 */
public class T_DOM4j {
    // 下面是需要解析的xml字符串例子
    String xmlString = "<html>" + "<head>" + "<title>dom4j解析一个例子</title>"
            + "<script>" + "<username>yangrong</username>"
            + "<password>123456</password>" + "</script>" + "</head>"
            + "<body>" + "<result>0</result>" + "<form>"
            + "<banlce>1000</banlce>" + "<subID>36242519880716</subID>"
            + "</form>" + "</body>" + "</html>";
    
    @Ignore
    public void test_parseStringXml2Map(){
        Map<String, String> map = TestDom4j.readStringXmlOut(xmlString);
        Iterator<?> iters = map.keySet().iterator();
        while (iters.hasNext()) {
            String key = iters.next().toString(); // 拿到键
            String val = map.get(key).toString(); // 拿到值
            System.out.println(key + "=" + val);
        }
    }
    
    @Test
    public void test_parseDBXML2Map() throws Exception{
        List<Map<String, String>> map = TestDom4j.parseDBXML("bookstore.xml"); //dbConnection.xml
        for(Map<String, String> conn : map){
            for(String key : conn.keySet())
                System.out.println(key+"="+conn.get(key));
        }
    }
}
