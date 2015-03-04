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

import org.hibernate.Query;
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

    public void queryAll() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            // List<?> rs =
            // session.createSQLQuery("select * from account").addEntity(Account.class).list();
            List<?> rs = session.createQuery("from Account").list();
            for (Object at : rs) {
                Account account = (Account) at;
                System.out.println(account.getEmail() + " -- "
                        + account.getMobile() + " -- " + account.getPassword());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            // 通过openSession()打开的session与上下文无关，不受事务控制，需要手动关闭
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
            session.getTransaction().rollback();
        } finally {
            // 通过openSession()打开的session与上下文无关，不受事务控制，需要手动关闭
            if (session != null)
                session.close();
        }
    }

    public void delete(String username) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            String hql = "delete Account as at where at.email = ?";
            Query deleteQuery = session.createQuery(hql);
            deleteQuery.setParameter(0, username+"@tp-link.net");
            deleteQuery.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            // 通过openSession()打开的session与上下文无关，不受事务控制，需要手动关闭
            if (session != null)
                session.close();
        }
    }
    
    public void update(String username, String columnName, String columnValue) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            String hql = "update Account at set at." + columnName
                    + " = ? where at.email = ? ";
            Query updateQuery = session.createQuery(hql);
            updateQuery.setParameter(0, columnValue);
            updateQuery.setParameter(1, username + "@tp-link.net");
            updateQuery.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            // 通过openSession()打开的session与上下文无关，不受事务控制，需要手动关闭
            if (session != null)
                session.close();
        }
    }
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
