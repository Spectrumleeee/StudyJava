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
    private Map<String, String> memoryInfo = null;
    private Map<String, Jedis> ipPort2Jedis = null;
    private Map<String, String> ipPort2nodeId = null;
    private Map<String, Boolean> clusterNodesStatus = null;
    private Map<String, ArrayList<Integer>> ipPort2nodeSlots = null;

    public Jedis[] jedisArray = null;
    public JedisPool[] jedisPool = null;

    public JedisTools(Object lock) {
        this.lock = lock;
        ipPort2Jedis = new HashMap<String, Jedis>();
    }

    public Object[][] getMemoryInfo() {
        
        Map<String, String> keyValues = null;
        String info,umh,ump,umph,mx;
        int um,mm,mmh;
        
        synchronized (lock) {
            if (jedisArray == null)
                jedisArray = getJedis("MEMROY");

            data = new Object[nodeNames.length][6];
            int index = 0;
            for (; index < jedisArray.length; index++) {

                if (flag[index] == 0) {
                    data[index][0] = nodeNames[index];
                    data[index][1] = "FAIL";
                }

                try {
                    info = jedisArray[index].info("memory");
                    keyValues = getMemoryFromInfo(info);
                    mx = jedisArray[index].configGet("maxmemory").get(1);
                    umh = keyValues.get("used_memory_human");
                    ump = keyValues.get("used_memory_peak");
                    umph = keyValues.get("used_memory_peak_human");
                    um = Integer.parseInt(keyValues.get("used_memory"));
                    mmh = Integer.parseInt(mx) / (1024 * 1024);
                    mm = Integer.parseInt(mx);

                    data[index][0] = nodeNames[index];
                    data[index][1] = "OK";
                    data[index][2] = um + " / " + umh;
                    data[index][3] = ump + " / " + umph;
                    data[index][4] = mx + " / " + mmh + "M";
                    data[index][5] = new Float((float) 100 * um / mm);
                } catch (Exception e) {
                    data[index][1] = "FAIL";
                    flag[index] = 0;
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
    public boolean migrateSlots(String source, String target, int slot_nums) {

        if (jedisArray == null)
            jedisArray = getJedis("MIGRATE SLOT");

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
            return false;
        }

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

        return true;
    }

    public void moveSlot(String source_ipport, String target_ipport, int slot,
            int db, int timeout) {

        List<String> keys = null;
        String[] ipPort = null;
        String nodeId, rt = null;

        Jedis source = ipPort2Jedis.get(source_ipport);
        Jedis target = ipPort2Jedis.get(target_ipport);

        nodeId = ipPort2nodeId.get(source_ipport);
        if (!source.clusterSetSlotMigrating(slot, nodeId).equals("OK")) {
            System.err.println("command clusterSetSlotMigrating error!");
            return;
        }
        nodeId = ipPort2nodeId.get(target_ipport);
        if (!target.clusterSetSlotImporting(slot, nodeId).equals("OK")) {
            System.err.println("command clusterSetSlotImporting error!");
            return;
        }

        while (true) {
            keys = source.clusterGetKeysInSlot(slot, 123);
            if (keys.size() == 0) {
                for (String item : ipPort2Jedis.keySet()) {
                    ipPort2Jedis.get(item).clusterSetSlotNode(slot,
                            ipPort2nodeId.get(item));
                }
                break;
            }

            for (String key : keys) {
                ipPort = target_ipport.split(":");
                rt = source.migrate(ipPort[0], Integer.parseInt(ipPort[1]),
                        key, db, timeout);
                if (rt == null || !rt.equals("OK")) {
                    System.err.println("migrate failed!");
                    return;
                }
            }
        }
    }

    /*
     * get the Jedis connection to each cluster node
     */
    public Jedis[] getJedis(String msg) {

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
                    clusterNodesStatus = getClusterNodesStatus(jedisArray[index]
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

    public Map<String, String> getMemoryFromInfo(Object from) {

        String[] eachItems = ((String) from).split("\r\n");
        String[] keyValue = null;

        if (memoryInfo == null)
            memoryInfo = new HashMap<String, String>();
        else
            memoryInfo.clear();

        for (int i = 1; i < eachItems.length; i++) {
            keyValue = eachItems[i].split(":");
            memoryInfo.put(keyValue[0], keyValue[1]);
        }
        return memoryInfo;
    }

    public Map<String, Boolean> getClusterNodesStatus(String info) {

        String[] clusterNodesInfo = info.split("\n");

        if (clusterNodesStatus == null)
            clusterNodesStatus = new HashMap<String, Boolean>();
        else
            clusterNodesStatus.clear();

        for (String item : clusterNodesInfo) {
            if (item.contains("fail"))
                clusterNodesStatus.put(item.split(" ")[1], false);
            else
                clusterNodesStatus.put(item.split(" ")[1], true);
        }
        return clusterNodesStatus;
    }

    /*
     * initiate the node information from the reply of `cluster nodes` 1、ip-port
     * to arraylist of node slot'; 2、 ip-port to node id;
     */
    public void initNodeInfo(String info) {

        String[] clusterNodesInfo = null;
        String[] eachNodeInfo = null;

        ipPort2nodeSlots = new HashMap<String, ArrayList<Integer>>();
        ipPort2nodeId = new HashMap<String, String>();
        clusterNodesInfo = info.split("\n");

        for (String item : clusterNodesInfo) {
            eachNodeInfo = item.split(" ");
            ipPort2nodeId.put(eachNodeInfo[1], eachNodeInfo[0]);

            if (!item.contains("master") || item.contains("fail"))
                continue;
            if (eachNodeInfo.length <= 8) {
                ipPort2nodeSlots.put(eachNodeInfo[1], new ArrayList<Integer>());
                continue;
            }

            ArrayList<Integer> slots = new ArrayList<Integer>();
            int start, end;
            for (int index = 8; index < eachNodeInfo.length; index++) {
                if (!eachNodeInfo[index].contains("-"))
                    slots.add(Integer.parseInt(eachNodeInfo[index]));
                else {
                    start = Integer.parseInt(eachNodeInfo[index].split("-")[0]);
                    end = Integer.parseInt(eachNodeInfo[index].split("-")[1]);
                    while (start <= end) {
                        slots.add(start);
                        start++;
                    }
                }
            }
            ipPort2nodeSlots.put(eachNodeInfo[1], slots);
        }
    }

    public static void main(String[] args) {
        System.err.println("123123123");
        Map<String, String> ipPort2nodeId = new HashMap<String, String>();
        ipPort2nodeId.put("abc", "123");
        ipPort2nodeId.put("def", "456");
        ipPort2nodeId.clear();
        ipPort2nodeId.put("123", "abc");
        ipPort2nodeId.put("456", "def");
    }
}
