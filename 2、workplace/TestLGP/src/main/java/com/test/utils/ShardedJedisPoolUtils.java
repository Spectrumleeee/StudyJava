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

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * ShardedJedisPoolUtils.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 21, 2015
 */
public class ShardedJedisPoolUtils {

    private static ShardedJedisPool pool;
    private static ResourceBundle bundle = ResourceBundle.getBundle("shardedJedisPool");
    
    private static List<JedisShardInfo> getJedisShardInfoList(){
        
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();

        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[shardedJedisPool.properties] is not found!");
        }

        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(
                bundle.getString("redis1.ip"), Integer.parseInt(bundle
                        .getString("redis1.port")));
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(
                bundle.getString("redis2.ip"), Integer.parseInt(bundle
                        .getString("redis2.port")));
        list.add(jedisShardInfo1);
        list.add(jedisShardInfo2);
        
        return list;
    }
    
    private static void createShardedJedisPool() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(bundle.getString("redis.pool.maxActive")));
        config.setMaxIdle(Integer.valueOf(bundle
                .getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle
                .getString("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(bundle
                .getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle
                .getString("redis.pool.testOnReturn")));
        
        pool = new ShardedJedisPool(config, getJedisShardInfoList());
    }
    
    private static synchronized void initPool(){
        if(pool == null)
            createShardedJedisPool();
    }
    
    public static ShardedJedis getShardedJedis(){
        if(pool == null)
            initPool();
        return pool.getResource();
    }
    
    public static void returnRes(ShardedJedis shardedJedis){
        pool.returnResource(shardedJedis);
    }
    
    public static void main(String[] args) {
        ShardedJedis shardedJedis = ShardedJedisPoolUtils.getShardedJedis();
        ShardedJedis shardedJedis1 = ShardedJedisPoolUtils.getShardedJedis();
        
        for(int i=0; i<10; i++){
            shardedJedis.set("shardedJedis-"+i, "test ShardedJedis "+i);
            i++;
            shardedJedis1.set("shardedJedis-"+i, "test ShardedJedis "+i);
        }
        
        System.out.println(shardedJedis.get("shardedJedis-3"));
        
        ShardedJedisPoolUtils.returnRes(shardedJedis);
        ShardedJedisPoolUtils.returnRes(shardedJedis1);
    }
}
