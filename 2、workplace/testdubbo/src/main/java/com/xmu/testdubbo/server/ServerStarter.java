/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-17
 *
 */
package com.xmu.testdubbo.server;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerStarter {
    public static AbstractApplicationContext context;
    
    public static void main(String[] args) {
        try {
            start();
            while (true) {
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Get conf.dir property then load conf files to classpath
     */
    private static void start() throws Exception {
        System.out.println("The cloud-app service is starting.");
        
        String confDirStr = System.getProperty("conf.dir", "../conf");
        if(getOs().startsWith("Windows")){
            confDirStr = System.getProperty("conf.dir", "src/main/resources");
        }

        File confDir = new File(confDirStr);
        if (!confDir.exists()) {
            throw new RuntimeException("Conf directory "
                    + confDir.getAbsolutePath() + " does not exist.");
        }
        
        // load conf files
        ClassLoader loader = new URLClassLoader(
                new URL[] { confDir.toURI().toURL() });
        Thread.currentThread().setContextClassLoader(loader);
        
        // start spring context
        context = new ClassPathXmlApplicationContext("provider.xml");
        
        System.out.println("The cloud-app service started!");
    }
    
    private static String getOs(){
        return System.getProperties().getProperty("os.name");
    }
}
