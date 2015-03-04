package com.test.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Group.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-2
 */

@Entity
// 名字不能为 group, 因为该字段在内部被使用
@Table(name="t_group")
public class Group {
    private Long id;
    private String groupName;
    private Set<User> users;

    @Id
    @GeneratedValue
    @Column(name="group_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(cascade=CascadeType.ALL ,fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
    @Column(name="group_name",unique=true)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
