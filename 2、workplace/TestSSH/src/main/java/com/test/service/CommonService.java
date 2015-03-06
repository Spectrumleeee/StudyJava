package com.test.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.test.dao.CommonDAO;
import com.test.dao.PersonDAO;
import com.test.entity.Cat;
import com.test.entity.Flight;
import com.test.entity.Group;
import com.test.entity.MobileInfo;
import com.test.entity.Person;
import com.test.entity.User;

/**
 * CommonService.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-2-15
 */
public class CommonService {
	private PersonDAO personDao;
	private CommonDAO commonDao;
	
	public void insertPerson(int age, String firstname, String lastname){
		Person person = new Person(age, firstname, lastname);
		personDao.save(person);
	}
	
	public void insertCat(){
		Cat cat = new Cat();
		cat.setWeight(new BigDecimal("123456"));
		commonDao.saveCat(cat);
	}
	
	public void insertFlight(String name, String num){
		Flight flight = new Flight(name, num);
		commonDao.saveFlight(flight);
	}
	
	public void insertMobileInfo(String mobile){
	    MobileInfo mobileInfo = new MobileInfo(mobile);
	    commonDao.saveMobileInfo(mobileInfo);
	}
	
	public void deleteMobileInfo(String mobile){
	    commonDao.deleteMobileInfo(mobile);
	}
	
	public void insertUser(String username){
	    User user = new User();
	    user.setUserName(username);
	    commonDao.saveUser(user);
	}
	
	public void insertGroup(String groupName){
	    Set<User> users = new HashSet<User>();
	    User user1 = new User();
	    user1.setUserName(groupName + " Zhang San");
	    User user2 = new User();
	    user2.setUserName(groupName + " Li Si");
	    User user3 = new User();
	    user3.setUserName(groupName + " Wang Wu");
	    users.add(user1);
	    users.add(user2);
	    users.add(user3);
	    
	    Group group = new Group();
	    group.setUsers(users);
	    group.setGroupName(groupName);
	    commonDao.saveGroup(group);
	}
	
	public void deleteGroup(String groupName){
//	     commonDao.deleteGroup(groupName);
	    commonDao.deleteGroupCascade(groupName);
	}
	
	public void testOptimisticLockA(long id, String country){
	    commonDao.testOptimisticLockA(id, country);
	}
	
    public void testOptimisticLockB(long id, String country) {
        commonDao.testOptimisticLockB(id, country);
    }
    
	public void queryGroupUser(String groupName){
	    commonDao.queryGroupUsers(groupName);
	}
	
	public User queryUserGroupUsers(String userName){
	    return commonDao.queryUserGroupUsers(userName);
	}
	
	public void setPersonDao(PersonDAO personDao){
		this.personDao = personDao;
	}
	
	public void setCommonDao(CommonDAO commonDao){
		this.commonDao = commonDao;
	}
}
