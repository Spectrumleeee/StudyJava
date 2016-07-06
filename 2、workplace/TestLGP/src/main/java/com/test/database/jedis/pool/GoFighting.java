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
package com.test.database.jedis.pool;

/**
 * GoFighting.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Feb 3, 2015
 */
public class GoFighting {

    public static void main(String[] args) {
        Thread thread = null;
        BattlePlaneConfig config = new BattlePlaneConfig();
        config.setMaxTotal(5);
        config.setMaxIdle(5);
        config.setMinIdle(2);
        config.setTimeBetweenEvictionRunsMillis(60000);
        config.setMinEvictableIdleTimeMillis(1800000);
        config.setSoftMinEvictableIdleTimeMillis(1800000);
        
        config.setMaxWaitMillis(-1);
        BattlePlanePool battlerPool = new BattlePlanePool(config);

        for (int i = 0; i < 5; i++) {
            thread = new Fighter(battlerPool);
            thread.start();
        }
    }
}

class Fighter extends Thread {

    BattlePlanePool battlePool;

    public Fighter(BattlePlanePool battlePool) {
        this.battlePool = battlePool;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            BattlePlane battler = battlePool.getResource();
            System.out.println(battler.getName() + " start to fight!");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            battlePool.returnResource(battler);
            System.out.println(battler.getName() + " take off !");
        }
    }
}
