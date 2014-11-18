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

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.exceptions.JedisClusterMaxRedirectionsException;

/**
 * TestJedis.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Nov 12, 2014
 */
public class TestJedis extends Jedis{
    
    public TestJedis(Set<HostAndPort> jedisClusterNodes) {
        super(jedisClusterNodes);
    }

    public static void main(String[] args) {
        
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("172.29.88.117", 7000));
        jedisClusterNodes.add(new HostAndPort("172.29.88.117", 7001));
        TestJedis jc = new TestJedis(jedisClusterNodes);
        SetGetShell(jc);
    }
    
    public static void SetGetShell(TestJedis jc){
        String value = "haliluya";
        Scanner scan = new Scanner(System.in);
        try {
            while (true) {
                System.out.print(">> ");
                String inStr = scan.nextLine().trim();
                String[] params = inStr.split(" ");

                if (params.length == 1 && !params[0].equals("")) {
                    value = jc.get(params[0]);
                    System.out.println("## get key " + params[0] + " : "
                            + value);
                } else if (params.length == 2) {
                    jc.set(params[0], params[1]);
                    System.out.println("## set key " + params[0] + " : "
                            + params[1]);
                } else {
                    System.out.println("## input error~~! ");
                }
            }
        }catch(JedisClusterMaxRedirectionsException e){ 
            e.printStackTrace();
            System.out.println("## Too many Cluster redirections, check your network, exit~~! ");
            return;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("## input error exit~~! ");
            return;
        }finally {
            jc.close();
            scan.close();
        }
    }
}

