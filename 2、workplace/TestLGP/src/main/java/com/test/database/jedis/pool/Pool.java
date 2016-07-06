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

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Pool.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 3, 2015
 */
public abstract class Pool<T> implements Closeable {
    protected GenericObjectPool<T> internalPool;
    
    public Pool(){
        
    }
    
    public Pool(GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory){
        initPool(poolConfig, factory);
    }
    
    public void initPool(GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory){
        if(this.internalPool != null){
            try{
                closeInternalPool();
            }catch(Exception e){
            }
        }
        
        this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
    }
    
    
    @Override
    public void close() throws IOException {
        closeInternalPool();
    }
    
    public void destroy(){
        closeInternalPool();
    }
    
    public T getResource(){
        try{
            return internalPool.borrowObject();
        }catch(Exception e){
            throw new BattlePlaneException("Can not borrow from Pool", e);
        }
    }
    
    public void returnResourceObject(final T obj){
        if(obj == null)
            return;
        try{
            internalPool.returnObject(obj);
        }catch(Exception e){
            throw new BattlePlaneException("Can not return to Pool", e);
        }
    }
    
    public void returnResource(final T obj){
        if(obj != null)
            returnResourceObject(obj);
    }
    
    protected void returnBrokenResourceObject(final T obj){
        try {
            internalPool.invalidateObject(obj);
          } catch (Exception e) {
            throw new BattlePlaneException("Could not return the resource to the pool", e);
          }
    }
    
    protected void closeInternalPool(){
        try{
            internalPool.close();
        }catch(Exception e){
            throw new BattlePlaneException("Can not close BattlePlanePool", e);
        }
    }

}
