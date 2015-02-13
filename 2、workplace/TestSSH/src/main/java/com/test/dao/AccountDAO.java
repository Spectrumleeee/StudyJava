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
package com.test.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.test.entity.Account;

/**
 * AccountDAO.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Feb 11, 2015
 */
public class AccountDAO {

	private SessionFactory sessionFactory;

	public void findNameById() {
		Session session = sessionFactory.openSession();
		String sql = "select * from account";
		try {
			SQLQuery q = session.createSQLQuery(sql).addEntity(Account.class);
			List<?> rs = q.list();
			for (Object at : rs) {
				System.out.println(((Account) at).getEmail());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
	}

	public void save(Account account) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(account);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// session如果不关闭，能否重用？
			if (session != null)
				session.close();
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
