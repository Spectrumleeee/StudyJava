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
package com.test.jedis.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * BattlePlanePool.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Feb 3, 2015
 */
public class BattlePlanePool extends Pool<BattlePlane> {

    public BattlePlanePool(String name, int weight, int size) {
        super(new BattlePlaneConfig(), new BattlePlaneFactory(name, weight, size));
    }
    
    public BattlePlanePool(GenericObjectPoolConfig config){
        super(config, new BattlePlaneFactory("J20", 3, 12));
    }

    public BattlePlane getResource() {
        BattlePlane battler = super.getResource();
        return battler;
    }

    public void returnResource(final BattlePlane battler) {
        if (battler != null) {
            try {
                battler.setTakeOff(false);
                super.returnResource(battler);
            } catch (Exception e) {
                returnBrokenResourceObject(battler);
                throw new BattlePlaneException("Can not return resource to Pool", e);
            }
        }
    }
    
    public void returnBrokenResource(final BattlePlane battler){
        if(battler != null){
            returnBrokenResourceObject(battler);
        }
    }
    
    public int getNumActive() {
        if (this.internalPool == null || this.internalPool.isClosed()) {
          return -1;
        }

        return this.internalPool.getNumActive();
      }
}
