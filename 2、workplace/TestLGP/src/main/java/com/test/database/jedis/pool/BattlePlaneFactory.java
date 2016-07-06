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

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * BattlePlaneFactory.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 4, 2015
 */
public class BattlePlaneFactory implements PooledObjectFactory<BattlePlane>{

    private final String name;
    private final int weight;
    private final int size;
    
    public BattlePlaneFactory(String name, int weight, int size){
        this.name = name;
        this.weight = weight;
        this.size = size;
    }
    
    @Override
    public PooledObject<BattlePlane> makeObject() throws Exception {
        BattlePlane battler = new BattlePlane(name, weight, size);
        return new DefaultPooledObject<BattlePlane>(battler);
    }

    @Override
    public void destroyObject(PooledObject<BattlePlane> p) throws Exception {
        p.getObject().setTakeOff(false);
    }

    @Override
    public boolean validateObject(PooledObject<BattlePlane> p) {
        return p.getObject().isTakeOff();
    }

    @Override
    public void activateObject(PooledObject<BattlePlane> p) throws Exception {
        p.getObject().setTakeOff(true);
    }

    @Override
    public void passivateObject(PooledObject<BattlePlane> p) throws Exception {
        
    }

}
