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

import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONObject;

import com.xmu.cs.lgp.redis.cluster.executor.CommandExecutor;
import com.xmu.cs.lgp.redis.cluster.executor.MigrateExecutor;

/**
 * RedisClusterProxy.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 12, 2015
 */
public class RedisClusterProxy {

    private JedisTools jtl;
    private static RefreshThread rt;
    private static Object lock = new Object();
    private final ConcurrentLinkedQueue<CommandExecutor> blockQueue;
    private final JSONObject ANOTHER_RUNNING;

    public RedisClusterProxy() {
        blockQueue = new ConcurrentLinkedQueue<CommandExecutor>();
        ANOTHER_RUNNING = new JSONObject().put("ErrMsg",
                "Another Migrate Operation is Running!");
        jtl = new JedisTools(lock);
        rt = new RefreshThread(jtl, lock);
        rt.start();
    }
    
    public JedisTools getJedisTools(){
        return jtl;
    }

    public JSONObject execute(CommandExecutor ce) {
        if (ce instanceof MigrateExecutor) {
            synchronized (blockQueue) {
                if (blockQueue.isEmpty()) {
                    blockQueue.add(ce);
                } else
                    return ANOTHER_RUNNING;
            }
            try {
                return blockQueue.peek().execute(this);
            } finally {
                blockQueue.poll();
            }
        }

        return ce.execute(this);
    }

    public static void main(String[] args) {

    }

}
