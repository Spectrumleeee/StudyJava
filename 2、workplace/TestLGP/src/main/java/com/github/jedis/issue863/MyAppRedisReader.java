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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * MyAppRedisReader.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 30, 2015
 */
public class MyAppRedisReader implements Runnable {

    private List<String> keysToRead;
    public static JedisPool jedisPool;

    public MyAppRedisReader(String name, List<String> keysToRead) {
        this.keysToRead = keysToRead;
    }

    @Override
    public void run() {
        List<Object> list = new ArrayList<>();
        try (Jedis jedis = JedisConnectionManager.getInstance().getJedis()) {
            long pollStartTime = System.currentTimeMillis();
            if (keysToRead != null) {
                for (Iterator<String> iterator = keysToRead.iterator(); iterator
                        .hasNext();) {
                    Set<String> set = new HashSet<>();

                    String key = iterator.next();
                    set = jedis.zrangeByScore(key, "-inf", "+inf");
                    list.add(set.size());

                }
                System.out.println("Redis Keys polling Toal Time :--"
                        + (pollStartTime - System.currentTimeMillis()));

            }

        } catch (JedisConnectionException e) {
            System.out.println(e + "....... : fail to get connection...");
        } finally {
            // jedisPool.returnResource(jedis);
            // System.out.println("--D--"+poolSize--);
        }

    }

}
