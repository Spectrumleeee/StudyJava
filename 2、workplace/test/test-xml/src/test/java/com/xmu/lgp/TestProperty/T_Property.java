package com.xmu.lgp.TestProperty;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * T_Property.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-10
 */
public class T_Property {
    
    private TestProperty tp;
    
    @Before
    public void setUp(){
        tp = new TestProperty();
    }
    
    @Test
    public void test_inputStream() throws IOException{
        // 使用FileInputStream加载配置文件
        tp.inputStream("src\\main\\resources\\dbConnection.properties");
        // 读取并输出配置文件信息到控制台
        tp.readKeyValue();
        tp.writeProperties("dbMaxConns", "100");
    }
    
    @Test 
    public void test_resourceBundle(){
        // 使用ResourceBundle加载配置文件,生成ResourceBundle
        tp.testResourceBundle("dbConnection");
    }
    
    @Test
    public void test_propertyResourceBundle() throws IOException{
        // 使用FileInputStream读取文件，生成ResourceBundle
        tp.testPropertyResourceBundle("src\\main\\resources\\dbConnection.properties");
    }
    
    @Test
    public void test_classGetResourceAsStream() throws IOException{
        // 不以’/'开头时默认是从此类所在的包下取资源，以’/'开头则是从 ClassPath根下获取
        tp.testClassGetResourceAsStream("/dbConnection.properties");
        tp.readKeyValue();
    }
    
    @Test
    public void test_classLoaderGetResourceAsStream() throws IOException{
        // 默认则是从ClassPath根下获取，path不能以’/'开头，最终是由   ClassLoader获取资源。
        tp.testClassLoaderGetResourceAsStream("dbConnection.properties");
        tp.readKeyValue();
    }
    
    @Test
    public void test_ClassLoaderGetSystemResourceAsStream() throws IOException{
        tp.testClassLoaderGetSystemResourceAsStream("dbConnection.properties");
        tp.readKeyValue();
    }
}
