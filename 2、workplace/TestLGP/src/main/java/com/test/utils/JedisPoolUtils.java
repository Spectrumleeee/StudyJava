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
package com.test.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * JedisPoolUtils.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 20, 2015
 */
public class JedisPoolUtils {
    private static JedisPool pool;

    private static void createJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(2);
        config.setMaxWaitMillis(2000);
        config.setMinEvictableIdleTimeMillis(3000);
        config.setTimeBetweenEvictionRunsMillis(60000);
        config.setMaxIdle(10);
        pool = new JedisPool(config, "127.0.0.1", 9999, 5000);
    }

    private static void createJedisPool0() {
        ResourceBundle bundle = ResourceBundle.getBundle("jedisPool");
        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[jedisPool.properties] is not found!");
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(bundle
                .getString("redis.pool.maxActive")));
        config.setMaxIdle(Integer.valueOf(bundle
                .getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle
                .getString("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(bundle
                .getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle
                .getString("redis.pool.testOnReturn")));
        pool = new JedisPool(config, bundle.getString("redis.ip"),
                Integer.valueOf(bundle.getString("redis.port")),
                Protocol.DEFAULT_TIMEOUT, bundle.getString("redis.password"),
                Protocol.DEFAULT_DATABASE);
    }

    private static synchronized void poolInit(boolean auto) {
        if (auto && pool == null)
            createJedisPool0();
        else if(pool == null)
            createJedisPool();
    }
    
    public synchronized static void close(){
        if(pool != null)
            pool.close();
    }

    public static Jedis getJedis() {

        if (pool == null)
            poolInit(false);
        return pool.getResource();
    }

    public static void returnRes(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = JedisPoolUtils.getJedis();
        Jedis jedis1 = JedisPoolUtils.getJedis();
        JedisPoolUtils.returnRes(jedis);
        Jedis jedis2 = JedisPoolUtils.getJedis();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jedis.set("hello", "world");
        jedis.set("date", sdf.format(new Date()));
        System.out.println(jedis.get("date"));
        
        JedisPoolUtils.returnRes(jedis1);
        JedisPoolUtils.returnRes(jedis2);
        
        JedisPoolUtils.close();
        
        while(true){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
