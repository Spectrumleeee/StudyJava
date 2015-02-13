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
package com.test.utils;

import java.util.Set;

import redis.clients.jedis.Jedis;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * OnlineUtils.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 21, 2015
 */
public class OnlineUtils {

    private static final String SID_PREFIX = "online:sid:";
    private static final String UID_PREFIX = "online:uid:";
    private static final String VID_PREFIX = "online:vid:";
    private static final int OVERDATETIME = 30 * 60;
    private static final int BROADCAST_OVERDATETIME = 60;

    public static void login(String sid, Person user) {

        Jedis jedis = JedisPoolUtils.getJedis();
        
        jedis.setex(SID_PREFIX + sid, OVERDATETIME, userToString(user));
        jedis.setex(UID_PREFIX + user.getId(), OVERDATETIME, sid);
        
        JedisPoolUtils.returnRes(jedis);
    }
    
    public static void browse(String uid, String pageId){
        
        browse(uid, pageId, BROADCAST_OVERDATETIME);
    }
    
    public static void browse(String uid, String pageId, int timeout){
        
        if(null == uid || "".equals(uid))
            return;
        
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.auth("123456");
        
        jedis.setex(VID_PREFIX+pageId+":"+uid, timeout, uid);
        
        JedisPoolUtils.returnRes(jedis);
    }
    
    public static int countPageUsers(String pageId){
        
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.auth("123456");
        
        Set<String> thisPageUsers = jedis.keys(VID_PREFIX+pageId+"*");
        
        JedisPoolUtils.returnRes(jedis);
        
        return thisPageUsers.size();
    }
    
    public static String userToString(Person user) {
        JsonConfig config = new JsonConfig();
        JSONObject obj = JSONObject.fromObject(user, config);
        
        return obj.toString();
    }
    
    public static void main(String[] args) {
        for(int i=1; i<100; i++){
            OnlineUtils.browse(""+i, ""+3, (int)Math.round((Math.random()*(10-5)+5)));
        }
        int nums = OnlineUtils.countPageUsers(""+3);
        System.out.println(nums);
    }
}
