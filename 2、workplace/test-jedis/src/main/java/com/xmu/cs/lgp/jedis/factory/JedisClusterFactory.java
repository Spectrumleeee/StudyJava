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
package com.xmu.cs.lgp.jedis.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * JedisClusterFactory.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Nov 28, 2014
 */
public class JedisClusterFactory {

    public static JedisCluster getJedisCluster() {
        Set<HostAndPort> jedisClusterNodes = configFromFile();
        JedisCluster jc = new JedisCluster(jedisClusterNodes);
        return jc;
        
    }

    private static Set<HostAndPort> configFromFile() {
//        System.out.println("hellow worlod haliluya!!");
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        Properties props = new Properties();
        InputStream istream = null;
        istream = (InputStream) JedisClusterFactory.class.getClassLoader().getResourceAsStream("jedis.properties");
        
        try {
            props.load(istream);
            istream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(props.get("nodes"));
        int nodeNums = Integer.parseInt((String) props.get("nodes"));
        String[] ipPort;
        for(int i=1; i<=nodeNums; i++){
            ipPort = ((String)props.get("node"+i)).split(":");
            jedisClusterNodes.add(new HostAndPort(ipPort[0], Integer.parseInt(ipPort[1])));
        }

        return jedisClusterNodes;
    }
}
