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

import java.util.ArrayList;
import java.util.List;

/**
 * MyAppRedisReadClient.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 30, 2015
 */
public class MyAppRedisReadClient {

    public static void main(String[] args) {

//      String t1 = "7:spuser6:6:e5cc606d7f00000113159de7b49f89e9:6:Demand by Geo";
//      String t2 = ":CAM~~SW";
      String t1 = "T1-";
      String t2 = "-T2";
        for (int i = 0; i < 1; i++) { //380
            List<String> keysList = new ArrayList<String>();
            String key =null;   
            Runnable thread=null;
            for (int j = 0,k=1; j<2500; j++) {
                key = t1+j+t2;
                keysList.add(key);
                if(keysList.size()==500){
                    //System.out.println("Size:"+keysList.size());
                    thread=new MyAppRedisReader("Thread-"+i+"-"+k,keysList);
                    new Thread(thread).start();
                    keysList=new ArrayList<>();
                }
            }
            
        } 
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
