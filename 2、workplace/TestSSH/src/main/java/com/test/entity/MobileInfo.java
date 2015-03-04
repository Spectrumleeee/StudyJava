package com.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MobileInfo.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-2
 */

@Entity
@Table(name="mobile_info")
public class MobileInfo {

    private Long id;
    private String mobile;
    private String country;
    private String province;
    
    public MobileInfo(String mobile){
        this.mobile = mobile;
        this.country = "CN";
        this.province = "JiangXi";
    }
    @Id
    @GeneratedValue
    @Column(name="mobile_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(unique=true)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
