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
package com.test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TestTimer.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 28, 2015
 */
public class TestTimer {

    private String name;
    private boolean isDeamon;
    private Timer _timer;
    private long _usageCount;
    
    public TestTimer(String name, boolean isDeamon){
        this.name = name;
        this.isDeamon = isDeamon;
        this._usageCount = 0;
    }
    
    public void schedule(TimerTask task, long delay, long period){
        _timer = new Timer(name, isDeamon);
        _timer.schedule(task, delay, period);
        _usageCount++;
    }
    
    public void cancel(TimerTask task){
        task.cancel();
        _usageCount--;
        if(_usageCount == 0){
            _timer.cancel();
            _timer = null;
        }
    }
    
    public static void main(String[] args) {
        TestTimer testTimer = new TestTimer("commons-pool-LgpTestTimer", true);
        testTimer.schedule(new MyTask(), 3000, 1000);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyTask extends TimerTask{

    private static AtomicLong counter = new AtomicLong(0);
    
    public void run() {
        System.out.println(counter.incrementAndGet());
    }
    
}
