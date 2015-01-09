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
package com.xmu.cs.lgp.jedis.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.ClusterCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import com.xmu.cs.lgp.jedis.factory.JedisClusterFactory;

/**
 * JedisClient.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Nov 28, 2014
 */
public class JedisClient {

    public static void main(String[] args) {
        // for (int i = 0; i < 30; i++) {
        // JedisCluster jc = JedisClusterFactory.getJedisCluster();
        // Map<String, JedisPool> jp = jc.getClusterNodes();
        // int index = 1;
        // for (String nodename : jp.keySet()) {
        // boolean jd = jp.get(nodename).isClosed();
        //
        // System.out.println(index + " " + nodename + " " + jd);
        //
        // index++;
        // }
        // jc.close();
        // }
        // for (int i = 0; i < 50; i++) {
        // Jedis jd = new Jedis("172.29.88.117", 7000);
        // System.out.println(jd.info("memory"));
        // System.out.println(jd.configGet("maxmemory"));
        // jd.close();
        // }

        // JedisCluster jc = JedisClusterFactory.getJedisCluster();
        // Map<String, JedisPool> jp = jc.getClusterNodes();
        // getNodeSlots(jedis.clusterNodes());
        // List<Object> lt = jedis.clusterSlots();
        // String str;
        // for (Object o : lt) {
        // str = o.toString();
        // String[] params = str.split(",");
        // System.out.println(o);
        // System.out.println(params[0].replace("[", "") + " --- "
        // + params[1].trim() + " : "
        // + params[3].trim().replace("]", ""));
        // }
        // jedis.migrate("172.29.88.117", 7004, key, 0, 1000);

        // while (true) {}

        // return 122 records once time.
        Jedis source = new Jedis("172.29.88.117", 7003);
        Jedis target = new Jedis("172.29.88.117", 7009);

        source.clusterSetSlotMigrating(1138,
                "b8e319ebc706e1f0c38ded40370b0be891a63bf4");
        target.clusterSetSlotImporting(1138,
                "faf3c82ebd36f6e04327d2ae9a0864b661259106");

        while (1 < 2) {
            List<String> keys = source.clusterGetKeysInSlot(1138, 123);
            for (String key : keys) {
                System.out.println(key);
                source.migrate("172.29.88.117", 7009, key, 0, 15000);
            }
            if(keys.size() == 0){
                source.clusterSetSlotNode(1138, "faf3c82ebd36f6e04327d2ae9a0864b661259106");
                break;
            }
            System.out.println("haliluya");
        }

        // System.out.println(keys.size());
    }

    /*
     * get the slots of each node
     */
    public static Map<String, ArrayList<Integer>> getNodeSlots(String info) {
        // System.out.println(info);
        Map<String, ArrayList<Integer>> rst = new HashMap<String, ArrayList<Integer>>();
        String[] clusterNodesStatus = info.split("\n");
        for (String item : clusterNodesStatus) {
            if (!item.contains("master") || item.contains("fail"))
                continue;
            String[] eachNodeStatus = item.split(" ");
            if (eachNodeStatus.length <= 8) {
                rst.put(eachNodeStatus[1], new ArrayList<Integer>());
                continue;
            }

            ArrayList<Integer> slots = new ArrayList<Integer>();
            int start, end;
            for (int index = 8; index < eachNodeStatus.length; index++) {
                if (!eachNodeStatus[index].contains("-"))
                    slots.add(Integer.parseInt(eachNodeStatus[index]));
                else {
                    start = Integer
                            .parseInt(eachNodeStatus[index].split("-")[0]);
                    end = Integer.parseInt(eachNodeStatus[index].split("-")[1]);
                    while (start <= end) {
                        slots.add(start);
                        start++;
                    }
                }
            }
            rst.put(eachNodeStatus[1], slots);
            // System.out.println(eachNodeStatus[1] + " " + slots.size());
        }
        return rst;
    }
}
