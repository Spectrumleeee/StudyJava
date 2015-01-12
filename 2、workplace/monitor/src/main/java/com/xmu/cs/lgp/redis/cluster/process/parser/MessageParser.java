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

import java.util.HashMap;
import java.util.Map;

import com.xmu.cs.lgp.redis.cluster.executor.CommandExecutor;
import com.xmu.cs.lgp.redis.cluster.executor.MemoryExecutor;
import com.xmu.cs.lgp.redis.cluster.executor.SlotsExecutor;

/**
 * MessageParser.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 12, 2015
 */
public class MessageParser {

    private String command;
    private Map<String, String> params;
    
    public MessageParser(){
        params = new HashMap<String, String>();
    }
    
    public CommandExecutor parseMessage(String str){
        CommandExecutor ce = null ;
        
        try {
            parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(command.equals("getSlotsInfo"))
            ce = new SlotsExecutor();
        else if(command.equals("getMemoryInfo"))
            ce = new MemoryExecutor();
        
        return ce;
    }
    
    public void parse(String str) throws Exception{
        String[] sep = str.split("#");
        command = sep[0];
        
        for(int i=1; i<sep.length; i++){
            String[] param = sep[i].split("=");
            if(param.length == 2){
                params.put(param[0], param[1]);
            }
            else if(param.length == 1){
                params.put("param", param[0]);
            }
            else
                throw new Exception();
        }
    }
    
    public static void main(String[] args) {
        MessageParser messageParser = new MessageParser();
        try {
            messageParser.parse("getSlotsInfo#sdkfjwel#jsldkjfe#a=b");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
