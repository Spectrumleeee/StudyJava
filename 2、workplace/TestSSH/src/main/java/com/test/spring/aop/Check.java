package com.test.spring.aop;

import org.aspectj.lang.JoinPoint;

/**
 * Check.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-3
 */
public class Check {
    
    public void checkValidity(){
        System.out.println("------------------验证合法性----------------");
        System.out.println("-----------------合法权限用户----------------");
    }
    
    public void addLog(JoinPoint j){
        System.out.println("-------------------添加日志----------------"); 
        Object obj[] = j.getArgs();
        int i=0;
        for(Object o : obj)
            System.out.println("[INFO] args"+(i++)+":" + o);
        System.out.println("[INFO] ========checkSecurity======== "+j.getSignature().getName());
    }
    
}
