/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-9-21
 *
 */
package com.test.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class TestCassandra {

    private Cluster cluster;
    private Session session;
    
    public void connect(String nodes){
        cluster = Cluster.builder().addContactPoint(nodes).build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        session = cluster.connect("cloud");
        System.out.println("connected...");
    }
    
    public void createSchema(){
        session.execute("create keyspace if not exists simplex with replication = {'class':'SimpleStrategy', 'replication_factor':1};");
    }
    
    public void close(){
        cluster.close();
    }
    
    public static void main(String[] args) {
        TestCassandra tc = new TestCassandra();
        tc.connect("172.31.1.153");
//        tc.createSchema();
//        tc.close();
    }
}
