package com.test.dao.impl;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.test.dao.TestDao;
import com.test.entity.Admin;
import com.test.entity.Person;

/**
 * TestDao.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-2-28
 */
public class TestDaoImpl extends HibernateDaoSupport implements TestDao {
    public void savePerson(Person person) {
        // 默认为true,为false则所在方法必须处于事务控制中，因为此时获 取的session必须与当前所在的事务关联，所以必须处于事务中
        Session session = this.getSession(false);
        session.save(person);
        // 通过 getSession获取的session，spring框架会自动为我们关闭session,直接调用session.close()会出错
        releaseSession(session);
        // session.close();
    }

    public void saveAdmin(Admin admin1) {
        Session session = null;
        for (int m = 0; m < 5; m++) {
            Admin admin = new Admin();
            admin.setName("test");
            admin.setPassword("098");
            session = this.getSession();
            session.save(admin);
        }
    }
}
