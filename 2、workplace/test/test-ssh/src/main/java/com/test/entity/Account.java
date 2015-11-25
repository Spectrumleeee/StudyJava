package com.test.entity;

import java.sql.Timestamp;


/**
 * Account entity. @author MyEclipse Persistence Tools
 */
public class Account extends AbstractAccount implements java.io.Serializable {

    private static final long serialVersionUID = -1862124937735074138L;

    // Constructors

    /** default constructor */
    public Account() {
    }

	/** minimal constructor */
    public Account(String password, String status, Timestamp registerDate, Long version) {
        super(password, status, registerDate, version);        
    }
    
    /** full constructor */
    public Account(String email, String mobile, String password, String status, Timestamp registerDate, Timestamp lockEndDate, Long version) {
        super(email, mobile, password, status, registerDate, lockEndDate, version);        
    }
   
}
