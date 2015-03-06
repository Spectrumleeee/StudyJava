package com.test.ssh;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.spring.aop.Common;

/**
 * AopApp.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-3
 */
public class AopApp {
    private static ApplicationContext ctx = new ClassPathXmlApplicationContext(
            "application-aop.xml");
    
    public static void main(String[] args){
        System.out.println("++++--------------SpringAOP测试--------------++++");
        Common c = (Common)ctx.getBean("common");
        c.execute("liguangpu", "spectrumleeee");
        c.sayHello("lgp");
        System.out.println("++++-------------- AOP测试完成 --------------++++");
    }
}
