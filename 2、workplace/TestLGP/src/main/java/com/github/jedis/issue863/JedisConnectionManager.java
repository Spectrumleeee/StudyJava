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
package com.github.jedis.issue863;

import com.test.jedis.TestCase;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisConnectionManager.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 30, 2015
 */
public class JedisConnectionManager extends TestCase{
    private static JedisConnectionManager instance = null;
    private static JedisPool pool;

    private JedisConnectionManager() {
        connect();
    }

    public final static JedisConnectionManager getInstance() {
        if (instance == null) {
            synchronized (JedisConnectionManager.class) {
                if (instance == null) {
                    instance = new JedisConnectionManager();
                }
            }
        }
        return instance;
    }

    private void connect() {

        // Create and set a JedisPoolConfig
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxWaitMillis(8000);// 10000
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setNumTestsPerEvictionRun(50);
        poolConfig.setMinEvictableIdleTimeMillis(60000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        poolConfig.setMaxTotal(2000);
        poolConfig.setMaxIdle(150);
        poolConfig.setMinIdle(100);
        pool = new JedisPool(poolConfig, bundle.getString("redis1.ip"), 9999, 0);
    }

    public void release() {
        pool.destroy();
    }

    public Jedis getJedis() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement se = stacktrace[2];// maybe this number needs to be
                                             // corrected
        // System.err.println("Called from "+se.getClassName()+"."+se.getMethodName());
        Jedis jedis = null;
        int cnt = 1;
        do {
            try {
                cnt++;
                if (cnt >= 1000) {
                    System.out.println("Not getting Jedis even after " + cnt
                            + " Attempts.");
                    return null;
                }
                jedis = pool.getResource();
            } catch (Exception e) {
                System.err.println("(" + cnt
                        + ") Got exception during getting jedis connection. "
                        + e.getMessage() + " Called from " + se.getClassName()
                        + "." + se.getMethodName());
                if (cnt > 3) {
                    e.printStackTrace();
                }
            }
        } while (jedis == null);
        if (cnt > 2) {
            System.out.println("Got Jedis after " + cnt + " Attempts.");
        }
        return jedis;
    }

    public void returnJedis(Jedis jedis) {
        pool.returnResource(jedis);
    }

    /* (non-Javadoc)
     * @see com.test.jedis.TestCase#specifiedMethod()
     */
    @Override
    public void specifiedMethod() {
        // TODO Auto-generated method stub
        
    }

}
