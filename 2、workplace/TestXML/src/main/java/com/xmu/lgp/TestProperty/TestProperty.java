package com.xmu.lgp.TestProperty;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * TestProperty.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-3-10
 */
public class TestProperty {

    InputStream in = null;
    OutputStream out = null;
    // 可以读取修改配置文件
    Properties p = null;
    // 只能读取配置文件
    ResourceBundle bundle = null;
    String profilePath = null;

    public TestProperty() {
        p = new Properties();
    }

    // 方法一：通过FileInputStream最终构造java.util.Properties
    public Properties inputStream(String name) throws IOException {
        // FileInputStream中参数是相对路径，相对于项目根目录
        profilePath = name;
        in = new BufferedInputStream(new FileInputStream(profilePath));
        p.load(in);
        return p;
    }

    // 方法二：通过ResourceBundle解析properties配置文件
    public ResourceBundle testResourceBundle(String name) {
        // 可以自动加载 classpath 目录下文件
        // 不需要后缀，如 dbConnection.properties文件，name="dbConnection"
        bundle = ResourceBundle.getBundle(name);

        for (String key : bundle.keySet())
            System.out.println(key + " : " + bundle.getString(key));
        return bundle;
    }

    // 方法三：通过FileInputStream构建ResourceBundle
    public ResourceBundle testPropertyResourceBundle(String name) throws IOException{
        in = new BufferedInputStream(new FileInputStream(name));
        bundle = new PropertyResourceBundle(in);
        
        for (String key : bundle.keySet())
            System.out.println(key + " : " + bundle.getString(key));
        return bundle;
    }
    
    // 方法四：通过class的getResourceAsStream方法获取InputStream
    public Properties testClassGetResourceAsStream(String name) throws IOException{
        in = TestProperty.class.getResourceAsStream(name);
        p = new Properties();
        p.load(in);
        
        return p;
    }
    
    // 方法五：通过class的classloader的getResourceAsStream方法获取InputStream
    public Properties testClassLoaderGetResourceAsStream(String name) throws IOException{
        in = TestProperty.class.getClassLoader().getResourceAsStream(name);
        p = new Properties();
        p.load(in);
        
        return p;
    }
    
    // 方法六：通过ClassLoader的getSystemResourceAsStream方法获取InputStream
    public Properties testClassLoaderGetSystemResourceAsStream(String name) throws IOException{
        in = ClassLoader.getSystemResourceAsStream(name);
        p = new Properties();
        p.load(in);
        
        return p;
    }
    
    // 读取配置文件信息
    public void readKeyValue() {
        if (p == null)
            return;
        // 读取配置文件
        for (String key : p.stringPropertyNames())
            System.out.println(key + "\t= " + p.getProperty(key));
    }

    /*
     * 添加或更新key/value信息到配置文件 如果该主键存在，就更新该主键的值 如果该主键不存在，就添加主键的键值对 最终保存到文件中
     */
    public void writeProperties(String key, String value) throws IOException {
        if (p == null || profilePath == null)
            return;
        out = new FileOutputStream(profilePath);
        // 设置key/value,最终调用put方法,如果key已经存在则更新，如果不存在就添加
        p.setProperty(key, value);
        // 设置key/value键值对
        p.put(key + "_put", value + "_put");
        // 保存到文件中
        p.store(out, "Insert a property:" + key);
    }
}
