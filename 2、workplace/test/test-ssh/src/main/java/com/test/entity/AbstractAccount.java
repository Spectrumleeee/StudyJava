package com.test.entity;

import java.sql.Timestamp;


/**
 * AbstractAccount entity provides the base persistence definition of the Account entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAccount  implements java.io.Serializable {

    // Fields    

    private static final long serialVersionUID = 4128450639815508545L;
    private Integer accountId;
     private String email;
     private String mobile;
     private String password;
     private String status;
     private Timestamp registerDate;
     private Timestamp lockEndDate;
     private Long version;


    // Constructors

    /** default constructor */
    public AbstractAccount() {
    }

	/** minimal constructor */
    public AbstractAccount(String password, String status, Timestamp registerDate, Long version) {
        this.password = password;
        this.status = status;
        this.registerDate = registerDate;
        this.version = version;
    }
    
    /** full constructor */
    public AbstractAccount(String email, String mobile, String password, String status, Timestamp registerDate, Timestamp lockEndDate, Long version) {
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.status = status;
        this.registerDate = registerDate;
        this.lockEndDate = lockEndDate;
        this.version = version;
    }

   
    // Property accessors

    public Integer getAccountId() {
        return this.accountId;
    }
    
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getRegisterDate() {
        return this.registerDate;
    }
    
    public void setRegisterDate(Timestamp registerDate) {
        this.registerDate = registerDate;
    }

    public Timestamp getLockEndDate() {
        return this.lockEndDate;
    }
    
    public void setLockEndDate(Timestamp lockEndDate) {
        this.lockEndDate = lockEndDate;
    }

    public Long getVersion() {
        return this.version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
   








}