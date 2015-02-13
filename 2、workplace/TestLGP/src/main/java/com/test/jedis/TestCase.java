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
package com.test.jedis;

import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;

/**
 * TestCase.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 19, 2015
 */
public abstract class TestCase {
    
    protected static ResourceBundle bundle;
    protected static Jedis _jedis;
    
    public TestCase(){
        bundle = ResourceBundle.getBundle("shardedJedisPool");
        _jedis = new Jedis(bundle.getString("redis1.ip"), 8889);
    }
    
    public void startTest(){
        System.out.println(">>>>>>>>>>>>>>>> START TESTING <<<<<<<<<<<<<<<<");
    }
    
    public void finishTest(){
        System.out.println(">>>>>>>>>>>>>>>> STOP  TESTING <<<<<<<<<<<<<<<<");
    }
    
    public void call(){
        startTest();
        specifiedMethod();
        finishTest();
        _jedis.close();
    }
    
    public abstract void specifiedMethod();
}
