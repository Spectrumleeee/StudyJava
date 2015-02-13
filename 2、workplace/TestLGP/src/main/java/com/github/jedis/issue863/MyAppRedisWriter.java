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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * MyAppRedisWriter.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 30, 2015
 */
public class MyAppRedisWriter implements Runnable {
    private int keyOffSet;
    private int keyCount;
    public static JedisPool jedisPool;

    public MyAppRedisWriter(String name, int keyOffSet, int keyCount) {
        this.keyOffSet = keyOffSet;
        this.keyCount = keyCount;

    }

    @Override
    public void run() {
//        String t1 = "7:spuser6:6:e5cc606d7f00000113159de7b49f89e9:6:Demand by Geo";
//        String t2 = ":CAM~~SW";
        String t1 = "T1-";
        String t2 = "-T2";
        try (Jedis jedis = JedisConnectionManager.getInstance().getJedis()){
        long pollStartTime = System.currentTimeMillis();
        for (int i = keyOffSet; i < keyCount; i++) {
                for (int j = 0; j < 5; j++) {
//                    jedis.zadd(t1 + i + t2, j,"CAM, APAC, Split Factor, 2014, Q1, Dec-14, -1E-9, -1E-9, 0, Demand by Geo6000, 5000-"+ j);
                    jedis.zadd(t1 + i + t2, j,"V-"+ j);
                    
                }   
        }
        System.out.println("Redis Keys Toal Polling Time :--"+ (pollStartTime - System.currentTimeMillis()));
        }catch (JedisConnectionException e) {
            System.out.println(e + " : fail to get connection...");
        } finally {
            //JedisConnectionManager.getInstance().returnJedis(jedis);
            // jedisPool.returnResource(jedis);
        }

    }

}