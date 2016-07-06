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
package com.test.database.jedis;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.test.utils.Person;
import com.test.utils.SerializeUtil;
import com.xmu.cs.lgp.jedis.factory.JedisClusterFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;

/**
 * TestMyJedisClient.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Nov 28, 2014
 */
public class TestJedisAPI {

    public static void main(String[] args) {
        JedisCluster jedisCluster = JedisClusterFactory.getJedisCluster();
        JedisTestCase jtc = new JedisTestCase(jedisCluster);
        jtc.call();
    }
}

class JedisTestCase extends TestCase {

    private JedisCluster jedisCluster;

    public JedisTestCase(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public void specifiedMethod() {
            test();
//        pipeline();
    }

    public void test_set() {
        jedisCluster.sadd("lgp_prefer", "blue", "white", "purple");
        jedisCluster.sadd("lhy_prefer", "blue", "yellow", "black", "white");
        System.out.println(jedisCluster.spop("lgp_prefer"));
        System.out.println(jedisCluster.scard("lhy_prefer")); // count numbers
        jedisCluster.srem("lgp_prefer", "abc"); // remove
        System.out.println(jedisCluster.smembers("lgp_prefer")); // show
                                                                 // elements
        System.out.println(jedisCluster.smembers("lhy_prefer")); // show
                                                                 // elements
        System.out.println(jedisCluster.sismember("lgp_prefer", "white"));
        // ScanResult<String> sr = jedisCluster.sscan("lgp_prefer", 0); // Not
        // Support ?
        // for(String str : sr.getResult()){
        // System.out.println(str);
        // }
    }

    public void test_list() {
        jedisCluster.lpush("lgp", "pku", "xmu", "nku");
        jedisCluster.lpush("lhy", "xmu", "zju", "thu");

        System.out.println(jedisCluster.lpop("lhy") + " >> "
                + jedisCluster.lpop("lhy"));

        SortingParams sp = new SortingParams();
        jedisCluster.sort("lgp", sp.alpha()); // doesn't work ,why ??

        System.out.println(jedisCluster.lpop("lgp") + " >> "
                + jedisCluster.lpop("lgp"));

        System.out.println(jedisCluster.lrange("lgp", 0, -1));
        System.out.println(jedisCluster.lrange("lhy", 0, -1));

        jedisCluster.del("lgp");
        jedisCluster.del("lhy");
    }

    public void test_sds(long nums) { // sds : simple dynamic string

        for (int i = 0; i < nums; i++) {
            jedisCluster.set("lgp-" + i, "ligp-" + i);
            System.out.println(jedisCluster.get("lgp-" + i));
        }
        System.out.println(jedisCluster.get("lgp-1") + " : "
                + jedisCluster.strlen("lgp-1"));
    }

    public void test_JedisPool() {
        // JedisPool jp = new JedisPool(new JedisPoolConfig(), "172.29.88.117",
        // 7000);
        // Jedis jedisCluster = jp.getResource();
        Map<String, JedisPool> jcn = jedisCluster.getClusterNodes();
        Jedis[] jcc = new Jedis[20];
        int i = 0;
        for (String key : jcn.keySet()) {
            System.out.println(key);
            System.out.println(jcn.get(key));
            JedisPool jp = jcn.get(key); // Not Support Cluster
            jcc[i++] = jp.getResource();
        }
        System.out.println(jcc[3].get("hello")); // Error MOVED 866
    }

    public void test_AvaiableCommand() {
        // jedisCluster.slaveof("172.29.88.117", 7000); // Not Support
        // jedisCluster.info(); // Not Support
        // jedisCluster.dbSize(); // Not Support
        // jedisCluster.select(1); // Not Support
        // jedisCluster.bgsave(); // Not Support
        // jedisCluster.save(); // Not Support
    }

    /*
     * Redis LIST usage-1: An accountId can just login 5 times in one hour.
     */
    public boolean login_1(String accountId) {

        String pastTime = jedisCluster.lindex(accountId, 4);
        long curTime = System.currentTimeMillis();

        if (pastTime != null && (curTime - Long.parseLong(pastTime)) < 3600000) {
            return false;
        }

        jedisCluster.lpush(accountId, "" + curTime);
        return true;
    }

    /*
     * Redis expire() Usage-1: An accountId can just login 5 times in 10
     * seconds.
     */
    public boolean login_2(String accountId) {

        String times = jedisCluster.get(accountId);
        if (times != null && Integer.parseInt(times) >= 5) {
            return false;
        } else if (times == null) {
            jedisCluster.set(accountId, "1");
            jedisCluster.expire(accountId, 10);
        } else {
            jedisCluster.incr(accountId);
        }

        return true;
    }

    /*
     * LEADER BOARD, rank the scores of all users
     */
    public void leaderboardAdd(int score, String username) {
        jedisCluster.zadd("leaderboard", score, username);
    }

    public long leaderboardGet(String username) {
        return jedisCluster.zrank("leaderboard", username);
    }

    public Set<String> leaderboardRank() {
        return jedisCluster.zrevrange("leaderboard", 0, 5);
    }

    public void storeObject() {
        Person gy = new Person("guanyu", "male", 57);

        String ip = bundle.getString("redis1.ip");
        Integer port = Integer.parseInt(bundle.getString("redis1.port"));
        Jedis jedis = new Jedis(ip, port);

        jedis.set("guanyu".getBytes(), SerializeUtil.serialize(gy));

        byte[] gyArray = jedis.get("guanyu".getBytes());
        Person zs = (Person) SerializeUtil.unserialize(gyArray);
        System.out
                .println(zs.getName() + " " + zs.getSex() + " " + zs.getAge());

        jedis.close();
    }

    public void sentinel() {
        String ip = bundle.getString("redis1.ip");
        Jedis jedis = new Jedis(ip, 26379);
        System.out.println("sentinel masters : " + jedis.sentinelMasters());
        System.out.println("sentinel slaves : "
                + jedis.sentinelSlaves("master-1"));
        jedis.close();
    }

    public void sentinelPool() {
        Set<String> sentinels = new HashSet<String>();
        sentinels.add(bundle.getString("redis1.ip") + ":" + 26379);
        JedisSentinelPool pool = new JedisSentinelPool("master-1", sentinels);
        Jedis jedis = pool.getResource();

        for (int i = 1; i < 10; i++) {
            jedis.set("test-sentinel-"+i, "sentinel-test-"+i);
            String str = jedis.get("test-sentinel-"+i);
            System.out.println(str);
        }

        pool.returnResource(jedis);
        pool.close();
    }
    
    public void transaction(){
        _jedis.watch("ydd");
        Transaction tran = _jedis.multi();
        
        tran.set("ydd", "dss");
        tran.set("ddl", "ssb");
        tran.sadd("foo", "a");
        tran.sadd("foo", "b");
        tran.sadd("foo", "c");
        tran.scard("foo");
        
        List<Object> response = tran.exec();
        for(Object obj : response)
            System.out.println(obj);
    }
    
    public void pipeline(){
          
          final int TOTAL_OPERATIONS = 200000;
          _jedis.flushAll();

          long begin = Calendar.getInstance().getTimeInMillis();

          Pipeline pipeLine = _jedis.pipelined();
          for (int n = 0; n <= TOTAL_OPERATIONS; n++) {
            String key = "foo_lgp_" + n;
            pipeLine.set(key, "bar_lgp_" + n);
            pipeLine.get(key);
          }
          pipeLine.sync();
          
          long elapsed = Calendar.getInstance().getTimeInMillis() - begin;
          System.out.println(((1000 * 2 * TOTAL_OPERATIONS) / elapsed) + " ops");
    }
    
    public void test(){
        _jedis.set("lgp", "liguangpu", "NX",  "EX", 15L);
    }
}
