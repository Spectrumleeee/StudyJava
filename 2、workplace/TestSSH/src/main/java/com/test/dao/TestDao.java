package com.test.dao;

import com.test.entity.Admin;
import com.test.entity.Person;

/**
 * TestDaoInterface.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-2-28
 */
public interface TestDao {
	public void savePerson(Person person);
	public void saveAdmin(Admin admin);
}
