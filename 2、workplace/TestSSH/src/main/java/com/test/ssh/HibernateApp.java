/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.test.ssh;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.service.AccountService;

/**
 * Connect to mysql using hibernate
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 11, 2015
 */
public class HibernateApp {

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        AccountService as = (AccountService) ctx.getBean("accountService");
        
        as.saveAccount("liguangpu");
//        as.saveAccount("liyunpeng");
//        as.saveAccount("chenbiren");
//        as.saveAccount("chenbiren1");
//        as.saveAccount("chenbiren2");
//        as.saveAccount("chenbiren3");
//        as.saveAccount("chenbiren4");
//        as.saveAccount("chenbiren5");
//        as.saveAccount("chenbiren6");
//        as.saveAccount("chenbiren7");
    }
}
