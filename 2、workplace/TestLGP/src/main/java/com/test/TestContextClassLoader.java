/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-9-11
 *
 */
package com.test;

public class TestContextClassLoader {
    private TestShutdownHook tsh;
    
    public TestContextClassLoader(){
        tsh = new TestShutdownHook();
    }
    
    public void testContextClassLoader(){
        tsh.testContextClassLoader();
    }
    
    public static void main(String[] args){
        System.out.println(TestContextClassLoader.class.getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
        
        new Thread(new Runnable(){
            @Override
            public void run() {
                new TestContextClassLoader().testContextClassLoader();
            }
        }).start();
        
    }
}
