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
import com.test.service.CommonService;
import com.test.service.TestService;

/**
 * Connect to mysql using hibernate
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Feb 11, 2015
 */
public class HibernateApp {

	private static ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"application.xml");

	public static void main(String[] args) {
//	    test_mobileInfo();
//	    test_account();
	    test_user_group();
	}

	public static void test_flight(){
		CommonService cs = (CommonService) ctx.getBean("commonService");
		cs.insertFlight("dongfang flight", "53");
	}
	
	public static void test_cat(){
		CommonService cs = (CommonService) ctx.getBean("commonService");
		cs.insertCat();
		cs.insertCat();
	}
	
	public static void test_person() {
		CommonService cs = (CommonService) ctx.getBean("commonService");
		cs.insertPerson(27, "li", "li");
		cs.insertPerson(27, "li", "jun");
		cs.insertPerson(26, "li", "yang");
		cs.insertPerson(26, "li", "huiyi");
	}
	
	public static void test_person_1() {
		TestService ts = (TestService) ctx.getBean("testService");
		ts.insertPerson(28, "li", "yunpeng");
	}

	public static void test_account() {
		AccountService as = (AccountService) ctx.getBean("accountService");
		as.queryAll();
//		as.saveAccount("liguangpu");
//		as.saveAccount("liyunpeng");
//		as.saveAccount("chenbiren");
		as.saveAccount("zhangsanfeng");
//	    as.updateMobile("liguangpu", "13725638497");
//	    as.updateMobile("liyunpeng", "15765854958");
//	    as.updateMobile("chenbiren", "18856359785");
//	    as.updatePassword("zhangsanfeng", "55555");
	    as.updateMobile("zhangsanfeng", "18333458569");
//	    as.deleteAccount("zhangsanfeng");
	}
	
	public static void test_mobileInfo(){
	    CommonService cs = (CommonService) ctx.getBean("commonService");
	    cs.insertMobileInfo("18333458569");
	    cs.deleteMobileInfo("18333458569");
	}
	
	public static void test_user_group(){
	    CommonService cs = (CommonService) ctx.getBean("commonService");
	    cs.insertGroup("class_1");
	    cs.insertGroup("class_2");
	    cs.queryGroupUser("class_2");
//	    cs.queryUserGroupUsers("class_1 Wang Wu");
//	    cs.deleteGroup("class_1");
//	    cs.insertUser("Zhang San");
//	    cs.insertUser("Li Si");
//	    cs.insertUser("Wang Wu");
	}
}
