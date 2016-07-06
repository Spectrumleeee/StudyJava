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

import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import net.sf.json.JSONObject;

import org.apache.commons.lang.time.StopWatch;

import redis.clients.jedis.Jedis;

/**
 * TestJedisEvalLua.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 27, 2015
 */
public class TestJedisEvalLua {
    private static ResourceBundle bundle = ResourceBundle
            .getBundle("jedisPool");
    private static String host = bundle.getString("redis.ip");
    private static int port = 8888;
    private static int honBaoCount = 1000;
    private static int threadCount = 10;

    private static String hongBaoList = "hongBaoList";
    private static String hongBaoConsumedList = "hongBaoConsumedList";
    private static String hongBaoConsumedMap = "hongBaoConsumedMap";

    // -- 函数：尝试获得红包，如果成功，则返回json字符串，如果不成功，则返回空
    // -- 参数：红包队列名， 已消费的队列名，去重的Map名，用户ID
    // -- 返回值：nil 或者 json字符串，包含用户ID：userId，红包ID：id，红包金额：money
    private static String tryGetHongBaoScript = "if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then\n"
            + "return nil\n"
            + "else\n"
            + "local hongBao = redis.call('rpop', KEYS[1]);\n"
            + "if hongBao then\n"
            + "local x = cjson.decode(hongBao);\n"
            + "x['userId'] = KEYS[4];\n"
            + "local re = cjson.encode(x);\n"
            + "redis.call('hset', KEYS[3], KEYS[4], KEYS[4]);\n"
            + "redis.call('lpush', KEYS[2], re);\n"
            + "return re;\n"
            + "end\n"
            + "end\n" + "return nil";
    private static StopWatch watch = new StopWatch();

    public static void generateTestData() throws InterruptedException {
        Jedis jedis = new Jedis(host, port);
        jedis.flushAll();
        jedis.close();
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; ++i) {
            final int temp = i;
            Thread thread = new Thread() {
                public void run() {
                    Jedis jedis = new Jedis(host, port);
                    int per = honBaoCount / threadCount;
                    JSONObject object = new JSONObject();
                    for (int j = temp * per; j < (temp + 1) * per; j++) {
                        object.put("id", j);
                        object.put("money", j);
                        jedis.lpush(hongBaoList, object.toString());
                    }
                    latch.countDown();
                    jedis.close();
                }
            };
            thread.start();
        }
        latch.await();
    }

    public static void testTryGetHongBao() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(threadCount);
        System.err.println("start:" + System.currentTimeMillis() / 1000);
        watch.start();
        for (int i = 0; i < threadCount; ++i) {
            final int temp = i;
            Thread thread = new Thread() {
                public void run() {
                    String sha = null;
                    Jedis jedis = new Jedis(host, port);
                    sha = jedis.scriptLoad(tryGetHongBaoScript);
                    if (temp == 0) {
                        System.err.println("SCRIPT SHA: " + sha);
                    }
                    int j = honBaoCount / threadCount * temp;
                    while (true) {
                        // Object object = jedis.eval(tryGetHongBaoScript, 4,
                        // hongBaoList, hongBaoConsumedList,
                        // hongBaoConsumedMap, "" + j);
                        Object object = jedis
                                .evalsha(sha, 4, hongBaoList,
                                        hongBaoConsumedList,
                                        hongBaoConsumedMap, "" + j);
                        j++;
                        if (object != null) {
                            System.out.println("Get hongBao:" + object);
                        } else if (jedis.llen(hongBaoList) == 0) {
                            break;
                        }
                    }
                    latch.countDown();
                    jedis.close();
                }
            };
            thread.start();
        }

        latch.await();
        watch.stop();

        System.err.println("time:" + watch.getTime() + " ms");
        System.err.println("speed:" + honBaoCount / watch.getTime());
        System.err.println("end:" + System.currentTimeMillis() / 1000);
    }

    public static void main(String[] args) throws InterruptedException {
        generateTestData();
        testTryGetHongBao();
    }
}