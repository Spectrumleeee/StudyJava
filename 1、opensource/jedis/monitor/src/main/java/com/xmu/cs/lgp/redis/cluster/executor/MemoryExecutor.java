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

import org.json.JSONObject;

import com.xmu.cs.lgp.redis.cluster.tools.RedisClusterProxy;

/**
 * MemoryExecutor.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 12, 2015
 */
public class MemoryExecutor implements CommandExecutor {

    @Override
    public JSONObject execute(RedisClusterProxy proxy) {
        Object[][] rst = proxy.getJedisTools().getMemoryInfo();
        JSONObject jsonobj = new JSONObject();
        for(int i=0; i<rst.length; i++){
            String value = "";
            int j = 1;
            for(j=1; j<rst[i].length - 1; j++)
                value = value + rst[i][j] + "#";
            value += rst[i][j];
            jsonobj.put((String) rst[i][0], value);
        }
        return jsonobj;
    }
}
