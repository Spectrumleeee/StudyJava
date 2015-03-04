package com.test.entity;

/**
 * Person.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-2-15
 */
public class Person {
	private long id;
	private int age;
	private String firstname;
	private String lastname;
	
	public Person(int age, String firstname, String lastname){
		this.setAge(age);
		this.setFirstname(firstname);
		this.setLastname(lastname);
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
