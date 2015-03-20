package com.test.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * UserData.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-20
 */
public class UserData implements Serializable{

    private static final long serialVersionUID = -331886277946622427L;
    private String username;
    private String password;
    private Set<String> pets;
    private Map<String, String> favoriteMovies;
    private Date birthday;
    
    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Set<String> getPets() {
        return pets;
    }
    public void setPets(Set<String> pets) {
        this.pets = pets;
    }
    public Map<String, String> getFavoriteMovies() {
        return favoriteMovies;
    }
    public void setFavoriteMovies(Map<String, String> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }
    public Date getBrithday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
