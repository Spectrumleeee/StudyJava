package com.test.ssh;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.service.AccountService;
import com.test.service.CommonService;

/**
 * TestHibernateApp.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-5
 */
public class TestHibernateApp {
    private static ApplicationContext ctx;
    
    @Before
    public void setUp(){
        ctx = new ClassPathXmlApplicationContext("application.xml");
    }
    
    @Test
    public void test_account_query(){
        AccountService as = (AccountService) ctx.getBean("accountService");
        //使用debug模式运行，将停在断点处，接着运行HibernateApp中的更新操作，看看锁住期间能否更新
        as.queryByIdPessimisticLock(2);
    }
    
    @Ignore
    public void test_account_update(){
        AccountService as = (AccountService) ctx.getBean("accountService");
        as.updateMobileById(2, "13333458569");
    }
    
    @Ignore
    public void testOptimisticLock(){
        CommonService cs = (CommonService) ctx.getBean("commonService");
        cs.testOptimisticLockB(1l, "USA1");
    }
    
    
    
}
