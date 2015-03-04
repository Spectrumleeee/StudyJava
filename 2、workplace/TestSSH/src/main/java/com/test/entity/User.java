package com.test.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 配置了双向一对多/多对一关系，因为Group中配置了OneToMany,这里又配置了ManyToOne
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. 
 * Author: liguangpu<liguangpu@tp-link.net>
 * Created: 2015-3-2
 */

@Entity
@Table(name = "t_user")
public class User {
    private Long id;
    private String userName;
    private Group group;

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    /*
     * @JoinTable(name = "GROUP_USER", joinColumns = {@JoinColumn(name =
     * "inter_user_id",referencedColumnName="user_id")}, inverseJoinColumns =
     * {@JoinColumn(name = "inter_group_id", referencedColumnName ="group_id")}
     * )
     */
    @JoinColumn(name = "group_id")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Column(name = "user_name", unique=true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
