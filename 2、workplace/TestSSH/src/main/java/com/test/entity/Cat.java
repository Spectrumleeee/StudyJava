package com.test.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cat.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-2-26
 */

@Entity
@Table(name = "cats")
public class Cat {

	private Long id;
	private String name;
	private BigDecimal weight;

	@Id
	@GeneratedValue
	@Column(name="cat_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
