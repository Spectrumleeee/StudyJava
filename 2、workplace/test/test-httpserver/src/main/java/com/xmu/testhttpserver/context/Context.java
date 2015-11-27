/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-27
 *
 */
package com.xmu.testhttpserver.context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xmu.testhttpserver.core.impl.HttpHandler;
import com.xmu.testhttpserver.utils.XmlUtils;

public class Context {
    private static Map<String, HttpHandler> contextMap = new HashMap<String, HttpHandler>();
    public static String contextPath = "";

    public static void load() {
        try {
            String confDirStr = System.getProperty("conf.dir", "src"
                    + File.separator + "main" + File.separator + "resources");
            Document doc = XmlUtils.load(confDirStr + File.separator
                    + "context.xml");
            Element root = doc.getDocumentElement();

            contextPath = XmlUtils.getAttribute(root, "context");
            Element[] handlers = XmlUtils.getChildrenByName(root, "handler");
            for (Element ele : handlers) {
                String handle_class = XmlUtils.getChildText(ele,
                        "handler-class");
                String url_pattern = XmlUtils.getChildText(ele, "url-pattern");

                Class<?> cls = Class.forName(handle_class);
                Object newInstance = cls.newInstance();
                if (newInstance instanceof HttpHandler) {
                    contextMap.put(contextPath + url_pattern,
                            (HttpHandler) newInstance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param key
     * @return
     */
    public static HttpHandler getHandler(String key) {
        return contextMap.get(key);
    }

}
