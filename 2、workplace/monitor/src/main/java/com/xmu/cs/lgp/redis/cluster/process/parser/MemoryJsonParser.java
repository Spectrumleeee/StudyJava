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
package com.xmu.cs.lgp.redis.cluster.process.parser;

import java.util.Set;

import org.json.JSONObject;

/**
 * MemoryJsonParser.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 12, 2015
 */
public class MemoryJsonParser extends JsonParser {

    @Override
    public Object[][] parse(String str) {
        if(str.equals("FAIL"))
            return null;
        
        JSONObject obj = new JSONObject(str);
        @SuppressWarnings("unchecked")
        Set<String> keys = obj.keySet();
        int rows = keys.size();
        rstObj = new Object[rows][6];
        int index = 0;
        for(String key : keys){
            rstObj[index][0] = key;
            String temp = obj.getString(key);
            String[] tmp = temp.split("#");
            rstObj[index][1] = tmp[0];
            if(tmp[0].equals("FAIL")){
                index++;
                continue;
            }
            rstObj[index][2] = tmp[1];
            rstObj[index][3] = tmp[2];
            rstObj[index][4] = tmp[3];
            rstObj[index++][5] = Float.parseFloat(tmp[4]);
        }
        
        return rstObj;
    }

}
