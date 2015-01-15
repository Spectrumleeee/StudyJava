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
package com.xmu.cs.lgp.redis.cluster.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xmu.cs.lgp.jedis.factory.JedisClusterFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * JedisData.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Dec 15, 2014
 */
public class JedisTools {

    private static final int NODE_NUMBER = 1024;
    private int[] flag = new int[NODE_NUMBER];
    private Object[][] data;
    private Object lock;
    private JedisCluster jedisCluster;
    private String[] nodeNames;
    private Map<String, String> result;
    private Map<String, Jedis> ipPort2Jedis;
    private Map<String, String> ipPort2nodeId;
    private Map<String, ArrayList<Integer>> ipPort2nodeSlots;

    public Jedis[] jedisArray = null;
    public JedisPool[] jedisPool = null;

    public JedisTools(Object lock) {
        this.lock = lock;
        ipPort2Jedis = new HashMap<String, Jedis>();
    }

    public Object[][] getMemoryInfo() {
        synchronized (lock) {
            if (jedisArray == null)
                jedisArray = getJedis("MEMROY");

            data = new Object[nodeNames.length][6];
            int index = 0;
            for (; index < jedisArray.length; index++) {
                if (flag[index] == 1) {
                    try {
                        String info = jedisArray[index].info("memory");
                        Map<String, String> keyValues = parseInfo(info);
                        String mx = jedisArray[index].configGet("maxmemory")
                                .get(1);
                        keyValues.put("maxmemory", mx);
                        Integer mm_human = Integer.parseInt(mx) / (1024 * 1024);
                        keyValues.put("maxmemory_human", mm_human.toString()
                                + "M");
                        int um = Integer.parseInt(keyValues.get("used_memory"));
                        int mm = Integer.parseInt(keyValues.get("maxmemory"));
                        keyValues.put("percent",
                                String.format("%f", (float) 100 * um / mm));
                        data[index][0] = nodeNames[index];
                        data[index][1] = "OK";
                        data[index][2] = keyValues.get("used_memory") + " / "
                                + keyValues.get("used_memory_human");
                        data[index][3] = keyValues.get("used_memory_peak")
                                + " / "
                                + keyValues.get("used_memory_peak_human");
                        data[index][4] = keyValues.get("maxmemory") + " / "
                                + keyValues.get("maxmemory_human");
                        data[index][5] = new Float(keyValues.get("percent"));
                    } catch (Exception e) {
                        data[index][1] = "FAIL";
                        flag[index] = 2;
                    }
                } else {
                    data[index][0] = nodeNames[index];
                    data[index][1] = "FAIL";
                }

            }
        }
        return data;
    }

    /*
     * Get slots info of each redis node
     */
    public Object[][] getSlotsInfo() {
        synchronized (lock) {
            if (jedisArray == null)
                jedisArray = getJedis("SLOT");

            for (int index = 0; index < jedisArray.length; index++) {
                if (flag[index] == 1) {
                    try {
                        String info = jedisArray[index].clusterNodes();

                        initNodeInfo(info);

                        data = new Object[ipPort2nodeSlots.size()][2];
                        int count = 0;
                        for (String item : ipPort2nodeSlots.keySet()) {
                            data[count][0] = item;
                            data[count++][1] = ipPort2nodeSlots.get(item)
                                    .size();
                        }
                        break;
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        return data;
    }

    /*
     * Migrate Slots From source to target
     */
    public Object[][] migrateSlots(String source, String target, int slot_nums) {

        if (jedisArray == null)
            jedisArray = getJedis("SLOT");

        // Initiate first
        for (String item : ipPort2Jedis.keySet()) {
            try {
                String info = ipPort2Jedis.get(item).clusterNodes();
                initNodeInfo(info);
                break;
            } catch (Exception e) {
                continue;
            }
        }

        List<Integer> nodeSlots = ipPort2nodeSlots.get(source);

        if (nodeSlots.size() < slot_nums) {
            System.err
                    .println("[INFO] source node does not have such many slots");
        }
        // Move slot one by one
        for (int i = 1; i <= slot_nums; i++) {
            try {
                moveSlot(source, target, nodeSlots.get(0), 0, 15000);
                nodeSlots.remove(0);
                String output = String.format(
                        "[INFO] moved %-5d slots[%-5d] from %-20s to %-20s", i,
                        nodeSlots.get(0), source, target);
                System.out.println(output);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        return data;
    }

    public void moveSlot(String source_ipport, String target_ipport, int slot,
            int db, int timeout) {
        Jedis source = ipPort2Jedis.get(source_ipport);
        Jedis target = ipPort2Jedis.get(target_ipport);

        if (!source.clusterSetSlotMigrating(slot,
                ipPort2nodeId.get(source_ipport)).equals("OK"))
            System.err.println("command clusterSetSlotMigrating error!");
        if (!target.clusterSetSlotImporting(slot,
                ipPort2nodeId.get(target_ipport)).equals("OK"))
            System.err.println("command clusterSetSlotImporting error!");
        // Migrate all the keys of slot one by one
        while (true) {
            List<String> keys = source.clusterGetKeysInSlot(slot, 123);
            if (keys.size() == 0) {
                for (String item : ipPort2Jedis.keySet()) {
                    ipPort2Jedis.get(item).clusterSetSlotNode(slot,
                            ipPort2nodeId.get(item));
                }
                break;
            }
            String rst;
            for (String key : keys) {
                String[] ipPort = target_ipport.split(":");
                rst = source.migrate(ipPort[0], Integer.parseInt(ipPort[1]),
                        key, db, timeout);
                if (rst == null || !rst.equals("OK")) {
                    System.out.println("migrate failed!");
                }
            }
        }
    }

    /*
     * get the Jedis connection to each cluster node
     */
    public Jedis[] getJedis(String msg) {
        result = new HashMap<String, String>();
        if (jedisCluster == null)
            jedisCluster = JedisClusterFactory.getJedisCluster();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        int node_nums = clusterNodes.size();
        jedisArray = new Jedis[node_nums];
        jedisPool = new JedisPool[node_nums];
        for (int i = 0; i < node_nums; i++)
            flag[i] = 1;
        int index = 0;
        nodeNames = new String[node_nums];
        Map<String, Boolean> clusterNodesStatus = null;
        for (String key : clusterNodes.keySet()) {
            nodeNames[index] = key;
            try {
                jedisPool[index] = clusterNodes.get(key);
                if (clusterNodesStatus == null) {
                    jedisArray[index] = jedisPool[index].getResource();
                    ipPort2Jedis.put(key, jedisArray[index]);
                    clusterNodesStatus = getClusterNodes(jedisArray[index]
                            .clusterNodes());
                    if (clusterNodesStatus.size() != node_nums) {
                        jedisArray[index].close();
                        jedisCluster = JedisClusterFactory.getJedisCluster();
                        return getJedis("Node Delete Or Add In Redis Cluster!");
                    }
                } else if (clusterNodesStatus.get(key)) {
                    jedisArray[index] = jedisPool[index].getResource();
                    ipPort2Jedis.put(key, jedisArray[index]);
                } else {
                    flag[index] = 0;
                }
            } catch (JedisException e) {
                flag[index] = 0;
                jedisPool[index].returnBrokenResource(jedisArray[index]);
            } finally {
                try {
                    jedisPool[index].returnResource(jedisArray[index]);
                } catch (Exception e) {
                }
            }
            index++;
        }
        clusterNodes.clear();
        return jedisArray;
    }

    /*
     * parse the `cluster info[memory]`
     */
    public Map<String, String> parseInfo(Object from) {
        String[] eachlines = ((String) from).split("\r\n");
        String[] keyValue = null;
        for (int i = 1; i < eachlines.length; i++) {
            keyValue = eachlines[i].split(":");
            result.put(keyValue[0], keyValue[1]);
        }
        return result;
    }

    /*
     * get the status[OK/FAIL/PFAIL] of each cluster nodes
     */
    public Map<String, Boolean> getClusterNodes(String info) {
        Map<String, Boolean> rst = new HashMap<String, Boolean>();
        String[] clusterNodesStatus = info.split("\n");
        for (String item : clusterNodesStatus) {
            if (item.contains("fail"))
                rst.put(item.split(" ")[1], false);
            else
                rst.put(item.split(" ")[1], true);
        }
        return rst;
    }

    /*
     * initiate the node information from the reply of `cluster nodes` 1、ip-port
     * to arraylist of node slot'; 2、 ip-port to node id;
     */
    public void initNodeInfo(String info) {
        ipPort2nodeSlots = new HashMap<String, ArrayList<Integer>>();
        ipPort2nodeId = new HashMap<String, String>();
        String[] clusterNodesStatus = info.split("\n");
        for (String item : clusterNodesStatus) {
            String[] eachNodeStatus = item.split(" ");
            ipPort2nodeId.put(eachNodeStatus[1], eachNodeStatus[0]);

            if (!item.contains("master") || item.contains("fail"))
                continue;
            if (eachNodeStatus.length <= 8) {
                ipPort2nodeSlots.put(eachNodeStatus[1],
                        new ArrayList<Integer>());
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
            ipPort2nodeSlots.put(eachNodeStatus[1], slots);
        }
    }
    
    public static void main(String[] args){
        Map<String, String> ipPort2nodeId = new HashMap<String, String>();
        ipPort2nodeId.put("abc", "123");
        ipPort2nodeId.put("def", "456");
        ipPort2nodeId.clear();
        ipPort2nodeId.put("123", "abc");
        ipPort2nodeId.put("456", "def");
    }
}
