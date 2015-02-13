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
package com.github.jedis.issue863;

/**
 * MyAppRedisWriteClient.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 30, 2015
 */

public class MyAppRedisWriteClient {
    
    public static void main(String[] args) {
        for (int i = 0; i < 300; i++) {  // 300
            Runnable thread = null;
            for (int j = 0; j < 2500; j = j + 500) {
                thread = new MyAppRedisWriter("Thread-" + i, j, (j + 500));
                new Thread(thread).start();
            }
        }
    }
}
