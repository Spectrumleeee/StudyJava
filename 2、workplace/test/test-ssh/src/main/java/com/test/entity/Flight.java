package com.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Flight.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-2-26
 */

@Entity
@Table(name="TBL_FLIGHT",
		uniqueConstraints=
		@UniqueConstraint(name="flight_number", columnNames={"comp_prefix", "flight_number"}))
public class Flight{
	private Long id;
	private String companyPrefix;
	private String number;
	
	public Flight(String cp, String nm){
		this.companyPrefix = cp;
		this.number = nm;
	}
	
	@Column(name="comp_prefix")
	public String getCompanyPrefix(){
		return companyPrefix;
	}
	public void setCompanyPrefix(String cp){
		this.companyPrefix = cp;
	}
	
	@Column(name="flight_number")
	public String getNumber(){
		return number;
	}
	public void setNumber(String nm){
		this.number = nm;
	}

	@Id @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
