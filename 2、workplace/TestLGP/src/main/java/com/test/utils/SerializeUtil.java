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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * SerializeUtil.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 20, 2015
 */
public class SerializeUtil {
    
    public static byte[] serialize(Object obj){
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] rst = null;
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            rst = baos.toByteArray();
            return rst;
        }catch(Exception e){
            
        }
        return null;
    }
    
    public static Object unserialize(byte[] objByteArray){
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try{
            bais = new ByteArrayInputStream(objByteArray);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
            return obj;
        }catch(Exception e){
            
        }
        return null;
    }
}
