package com.test.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.test.dao.AccountDAO;
import com.test.entity.Account;

/**
 * AccountService.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-2-13
 */
public class AccountService {
	private AccountDAO accountDao;
	
	public void saveAccount(String username){
		Account account = new Account();
		account.setEmail(username + "@tp-link.net");
		account.setPassword("123");
		account.setStatus("NORMAL");
		account.setRegisterDate(Timestamp.valueOf(new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new Date())));
		account.setVersion(0L);
		
		accountDao.save(account);
	}
	
	public void setAccountDao(AccountDAO accountDao){
		this.accountDao = accountDao;
	}
}
