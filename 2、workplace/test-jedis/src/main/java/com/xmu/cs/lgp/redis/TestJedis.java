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
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Nov 12, 2014
 */
public class TestJedis extends MyJedis {

    public TestJedis(Set<HostAndPort> jedisClusterNodes) {
        super(jedisClusterNodes);
    }

    public static void main(String[] args) {

        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("172.29.88.117", 7000));
        jedisClusterNodes.add(new HostAndPort("172.29.88.117", 7001));
//        TestJedis jc = new TestJedis(jedisClusterNodes);
//        SetGetShell(jc);
        
        long nums = Long.parseLong(args[0]);
        int con = Integer.parseInt(args[1]);
        int payload = args.length == 3 ? Integer.parseInt(args[2]) : 10 ;
        benchmark(jedisClusterNodes, nums, con, payload);
    }

    public static void SetGetShell(TestJedis jc) {
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
        } catch (JedisClusterMaxRedirectionsException e) {
            e.printStackTrace();
            System.out
                    .println("## Too many Cluster redirections, check your network, exit~~! ");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("## input error exit~~! ");
            return;
        } finally {
            jc.close();
            scan.close();
        }
    }

    public static void benchmark(Set<HostAndPort> jedisClusterNodes, long requestnums,
            int concurrent, int payload) {
        Thread th[] = new Thread[concurrent];

        for (int i = 0; i < concurrent; i++) {
            TestJedis jc = new TestJedis(jedisClusterNodes);
            StringBuilder payloaddata = new StringBuilder();

            for(int j=0; j < payload; j++){
                payloaddata.append((char)(Math.random()*26 + 65));
            }
            
            th[i] = new BenchmarkThread((new StringBuilder("BM-")).append(i)
                    .toString(), jc, requestnums / concurrent, payloaddata);
        }

        long start = System.currentTimeMillis();
        
        for (int i = 0; i < concurrent; i++)
            th[i].start();

        for (int i = 0; i < concurrent; i++)
            try {
                th[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        long spent = System.currentTimeMillis() - start;
        System.out.println((new StringBuilder("BenchMark stop ! \n"))
                .append((long) (1000 * requestnums) / spent)
                .append(" requests per sec").toString());
    }
}
