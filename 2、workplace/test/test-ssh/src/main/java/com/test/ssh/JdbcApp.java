package com.test.ssh;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Connect to mysql using jdbc directly
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 11, 2015
 */
public class JdbcApp 
{
    public static void main( String[] args ) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        DataSource ds = (DataSource)ctx.getBean("dataSource");
        
        Connection conn = ds.getConnection();
        Statement sm = conn.createStatement();
        String sql = "select * from account";
        
        ResultSet rs = sm.executeQuery(sql);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        while(rs.next()){
            System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getObject(6));
        }
        System.out.println("--------------------------------------------------------");
    }
}
