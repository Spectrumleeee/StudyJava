package com.test.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.test.entity.Cat;
import com.test.entity.Flight;
import com.test.entity.Group;
import com.test.entity.MobileInfo;
import com.test.entity.User;

/**
 * CommonDAO.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-2-26
 */
public class CommonDAO extends HibernateDaoSupport {

    public void saveCat(Cat cat) {
        Session session = this.getSession();
        try {
            session.beginTransaction();
            session.save(cat);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            // spring框架会自动为我们关闭通过getSession获取的session,直接调用session.close()会出错
            releaseSession(session);
        }
    }

    public void saveFlight(Flight flight) {
        Session session = this.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(flight);
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

    public void saveMobileInfo(MobileInfo mobileInfo) {
        Session session = this.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(mobileInfo);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void deleteMobileInfo(String mobile) {
        Session session = this.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            String hql = "delete MobileInfo as mi where mi.mobile = ?";
            Query deleteQuery = session.createQuery(hql);
            deleteQuery.setParameter(0, mobile);
            deleteQuery.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void saveUser(User user) {
        Session session = this.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /*
     * 保存One端一条记录，Many端也级联save所有相关记录
     */
    public void saveGroup0(Group group) {
        Session session = this.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(group);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /*
     * 使用 HibernateTemplate 完成CURD操作 保存One端一条记录，Many端也级联save所有相关记录
     */
    public void saveGroup(Group group) {
        try {
            // HibernateTemplate默认自动提交事务的
            getHibernateTemplate().save(group);
        } catch (RuntimeException e) {
            // TODO logger
        }
    }

    // 只会删除Group中记录，不会级联删除User中关联记录
    public void deleteGroup(String groupName) {
        Session session = this.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            // 下面的语句可以用 this.getHibernateTemplate().delete()实现
            String hql = "delete Group as gp where gp.groupName = ?";
            Query deleteQuery = session.createQuery(hql);
            deleteQuery.setParameter(0, groupName);
            deleteQuery.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /*
     * 删除One端一条记录，Many端所有关联记录级联update或者delete
     */
    public void deleteGroupCascade(String groupName) {
        Session session = this.getSession();
        try {
            session.beginTransaction();
            // 1、如果根据ID获取Group对象，delete后，从表执行delete删除所有级联记录
            // Group group = (Group) session.get(Group.class, 1L);
            Group group = queryGroupUsers(groupName);

            // 2、如果直接new一个Group对象，那么调用delete后，从表中执行update set null而不删除级联记录
            // Group group = new Group();
            // group.setId(1L);
            session.delete(group);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            releaseSession(session);
        }
    }

    /*
     * hql查询One端，查出Group, 从Group.getUsers()获取所有属于该Group的User
     */
    @SuppressWarnings("unchecked")
    public Group queryGroupUsers(String groupName) {
        Session session = this.getSession();
        Group group = null;
        try {
            session.beginTransaction();
            // 1、使用Query进行查找
            String hql = "from Group as gp where gp.groupName = ?";
            List<Group> groups = session.createQuery(hql)
                    .setParameter(0, groupName).list();
            // 2、使用HibernateTemplate.find(hql)进行查找，但不支持惰性加载LAZY
            // String hql = "from Group as gp where gp.groupName = ?";
            // List<Group> groups = this.getHibernateTemplate().find(hql,
            // new Object[] { groupName });
            // 3、使用HibernateTemplate.findByExample,默认left join有重复数据问题
            // Group example = new Group();
            // example.setGroupName(groupName);
            // example.setId(8l);
            // List<Group> groups =
            // this.getHibernateTemplate().findByExample(example);

            for (Group gp : groups) {
                for (User user : gp.getUsers()) {
                    System.out.println(user.getUserName());
                    // user.setUserName(user.getUserName() + " md1");
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            releaseSession(session);
        }
        return group;
    }

    /*
     * 查询User, 从User中获取Group,再从Group中获取所有User
     */
    public User queryUserGroupUsers(String userName) {
        Session session = this.getSession();
        User user = null;
        try {
            session.beginTransaction();
            String hql = "from User as usr where usr.userName = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, userName);
            List<?> users = query.list();
            // 由于 userName 是unique的，所以最多只有一个
            user = (User) users.get(0);
            Group group = user.getGroup();
            for (User usr : group.getUsers())
                System.out.println(usr.getUserName());
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().commit();
        } finally {
            releaseSession(session);
        }
        return user;
    }

    // 测试乐观锁，同一个应用，两个事务中同时修改数据
    public void testOptimisticLockA(long id, String country) {
        Session s1 = this.getSessionFactory().openSession();
        Session s2 = this.getSessionFactory().openSession();

        s1.beginTransaction();
        MobileInfo mi1 = (MobileInfo) s1.load(MobileInfo.class, id);
        mi1.setCountry(country + "_A");
        s1.update(mi1);
        System.out.println(mi1.getVersion());

        s2.beginTransaction();
        MobileInfo mi2 = (MobileInfo) s2.load(MobileInfo.class, id);
        mi2.setCountry(country + "_B");
        s2.update(mi2);
        System.out.println(mi2.getVersion());

        s1.getTransaction().commit();
        s2.getTransaction().commit();
        s1.close();
        s2.close();
    }

    // 测试乐观锁，需要两个main同时运行，一个中断
    public void testOptimisticLockB(long id, String country) {
        Session session = this.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            MobileInfo mi1 = (MobileInfo) session.load(MobileInfo.class, id);
            mi1.setCountry(country);
            session.update(mi1);
            System.out.println(mi1.getVersion());
            // 此处设置断点，debug运行至此处，然后运行另外一个程序修改该条记录
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
