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

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.test.entity.Account;

/**
 * AccountDAO.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Feb 11, 2015
 */
public class AccountDAO extends HibernateDaoSupport{

    public void queryAll() {
        Session session = this.getSessionFactory().openSession();
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
        Session session = this.getSessionFactory().openSession();
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
        Session session = this.getSessionFactory().openSession();
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
        Session session = this.getSessionFactory().openSession();
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
    
    // 根据Id修改对象
    public void updateById(int id, String mobile){
        Session session = this.getSession();
        try {
            session.beginTransaction();
            Account at = (Account) session.load(Account.class, id);
            at.setMobile(mobile);
            session.update(at);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            // 通过openSession()打开的session与上下文无关，不受事务控制，需要手动关闭
            releaseSession(session);
        }
    }
    
    // 根据Id修改对象,加悲观锁，应该死锁，但是测试结果，表没有被锁住，？？？ 数据库引擎不支持？隔离级别不对？
    public void updateByIdLock(int id, String mobile){
        Session session = this.getSession();
        try {
            session.beginTransaction();
            Account at = (Account) session.load(Account.class, id, LockOptions.UPGRADE);
            at.setMobile(mobile);
            session.update(at);
            
            updateById(id, mobile+"_A");
            
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            // 通过openSession()打开的session与上下文无关，不受事务控制，需要手动关闭
            releaseSession(session);
        }
    }
    
    // 根据Id查询对象，同时加上悲观锁 LockMode.UPGRADE
    public void queryById(int id){
        Session session = this.getSession();
        try{
            session.beginTransaction();
            Account account = (Account) session.load(Account.class, id, LockOptions.UPGRADE);
            System.out.println("Pessimistic Query Account: "+account.getEmail()+" "+account.getMobile());
            session.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }finally{
            releaseSession(session);
        }
    }
}
