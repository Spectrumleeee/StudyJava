/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.test;

import java.util.Map;

import com.xmu.cs.lgp.jedis.factory.JedisClusterFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;

/**
 * TestMyJedisClient.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Nov 28, 2014
 */
public class TestMyJedisClient {

    public static void main(String[] args) {
        JedisCluster jc = JedisClusterFactory.getJedisCluster();
        JedisTestCase jtc = new JedisTestCase(jc);
        /*
         * jtc.test_AvaiableCommand(); 
         * jtc.test_JedisPool(); 
         * jtc.test_list();
         */
        jtc.test_sds(0);
    }
}

class JedisTestCase {

    private JedisCluster jc;

    public JedisTestCase(JedisCluster jc) {
        this.jc = jc;
    }

    public void test_sds(long nums) { // sds : simple dynamic string

        for (int i = 0; i < nums; i++) {
            jc.set("lgp-" + i, "ligp-" + i);
            System.out.println(jc.get("lgp-" + i));
        }
        System.out.println(jc.get("lgp-1") + " : " + jc.strlen("lgp-1"));
    }

    public void test_JedisPool() {
        // JedisPool jp = new JedisPool(new JedisPoolConfig(), "172.29.88.117",
        // 7000);
        // Jedis jc = jp.getResource();
        Map<String, JedisPool> jcn = jc.getClusterNodes();
        Jedis[] jcc = new Jedis[20];
        int i = 0;
        for (String key : jcn.keySet()) {
            System.out.println(key);
            System.out.println(jcn.get(key));
            JedisPool jp = jcn.get(key); // Not Support Cluster
            jcc[i++] = jp.getResource();
        }
        System.out.println(jcc[3].get("hello")); // Error MOVED 866
    }

    public void test_list() {
        jc.lpush("lgp", "pku", "xmu", "nku");
        jc.lpush("lhy", "xmu", "zju", "thu");

        System.out.println(jc.lpop("lhy") + " >> " + jc.lpop("lhy"));

        SortingParams sp = new SortingParams();
        jc.sort("lgp", sp.alpha()); // doesn't work ,why ??

        System.out.println(jc.lpop("lgp") + " >> " + jc.lpop("lgp"));

        System.out.println(jc.lrange("lgp", 0, -1));
        System.out.println(jc.lrange("lhy", 0, -1));

        jc.del("lgp");
        jc.del("lhy");
    }
    
    public void test_AvaiableCommand() {
//      jc.slaveof("172.29.88.117", 7000);    // Not Support
//      jc.info();                            // Not Support
//      jc.dbSize();                          // Not Support
//      jc.select(1);                         // Not Support
//      jc.bgsave();                          // Not Support
//      jc.save();                            // Not Support
  }

}
