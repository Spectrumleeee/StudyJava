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
package com.xmu.cs.lgp.redis.cluster.executor;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.xmu.cs.lgp.redis.cluster.tools.JedisTools;
import com.xmu.cs.lgp.redis.cluster.tools.RedisClusterProxy;

/**
 * MigrateExecutor.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 13, 2015
 */
public class MigrateExecutor implements CommandExecutor {

    private Map<String,String> params;
    private JedisTools jtl;
    
    public MigrateExecutor(Map<String, String> params){
        this.params = params;
    }
    @Override
    public JSONObject execute(RedisClusterProxy proxy) {
        jtl = new JedisTools(new Object());
        JSONObject jsonobj = new JSONObject();
        for(String key : params.keySet()){
            jsonobj.put(key, params.get(key));
        }
        System.out.println(jsonobj.toString());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonobj;
    }
    
    public static void main(String[] args){
        Map<String, String> params = new HashMap<String, String>();
        params.put("param-1", "127.0.0.1:7001");
        params.put("param-2", "127.0.0.1:7002");
        params.put("param-3", "24");
        MigrateExecutor me = new MigrateExecutor(params);
        me.execute(null);
    }
}
