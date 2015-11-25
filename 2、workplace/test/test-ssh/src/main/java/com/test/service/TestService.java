package com.test.service;

import com.test.dao.TestDao;
import com.test.entity.Person;

/**
 * TestService.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-2-28
 */
public class TestService {
	private TestDao testDao;
	
	public void insertPerson(int age, String firstname, String lastname){
		Person person = new Person(age, firstname, lastname);
		testDao.savePerson(person);
	}
	
	public void setTestDao(TestDao testDao){
		this.testDao = testDao;
	}
}
