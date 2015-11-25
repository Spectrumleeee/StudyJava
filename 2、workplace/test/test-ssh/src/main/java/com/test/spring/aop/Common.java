package com.test.spring.aop;

/**
 * Common.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-3
 */
public class Common {
    public void execute(String username, String password){
        System.out.println("------------------普通类----------------");
    }
    
    public void sayHello(String username){
        System.out.println("------------------hello"+username+"----------------");
    }
}
