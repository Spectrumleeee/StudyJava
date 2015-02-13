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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TestLockCondition.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Feb 3, 2015
 */
public class TestLockCondition {

    private int counter = 0;

    private ReentrantLock lock = new ReentrantLock();

    Condition notEmpty = lock.newCondition();
    Condition cycle = lock.newCondition();

    public void take() throws InterruptedException {
        lock.lockInterruptibly();
        notEmpty.await();
        System.out.println("get one counter " + counter);
        counter--;
        lock.unlock();
    }

    public void interrupt() {
        lock.lock();
        counter = (counter == 0) ? 6 : counter;
        notEmpty.signal();
        lock.unlock();
    }
    
    public void wait(int timeout) throws InterruptedException{
        lock.lockInterruptibly();
        cycle.await(timeout, TimeUnit.SECONDS);
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        TestLockCondition tc = new TestLockCondition();
        new Notifier(tc).start();
        for (int i = 0; i < 11; i++)
            tc.take();
    }

}

class Notifier extends Thread {

    private TestLockCondition tc;

    public Notifier(TestLockCondition tc) {
        this.tc = tc;
    }

    public void run() {
        while (true) {
            try {
                tc.wait(1);
                tc.interrupt();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}