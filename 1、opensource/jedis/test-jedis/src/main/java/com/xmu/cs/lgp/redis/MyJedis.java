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
package com.xmu.cs.lgp.redis;

import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * Jedis.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Nov 12, 2014
 */
public class MyJedis {
    private JedisCluster jc = null;

    public MyJedis(Set<HostAndPort> jedisClusterNodes){

        // Jedis Cluster will attempt to discover cluster nodes automatically
        jc = new JedisCluster(jedisClusterNodes);
    }
    
    public JedisCluster getJedis(){
        return jc;
    }
    
    public String set(String key, String value){
        return jc.set(key, value);
    }
    
    public String get(String key){
        return jc.get(key);
    }
    
    public void close(){
        jc.close();
    }
}
