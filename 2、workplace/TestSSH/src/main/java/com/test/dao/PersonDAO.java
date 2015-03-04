package com.test.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.test.entity.Person;

/**
 * PersonDAO.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-2-15
 */
public class PersonDAO {

    private SessionFactory sessionFactory;

    public void save(Person person) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(person);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            // 通过openSession()打开的session与上下文无关，不受事务控制，需要手动关闭
            session.close();
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
