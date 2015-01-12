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
package com.xmu.cs.lgp.redis.cluster.operation.migrate;

import javax.swing.JButton;

import com.xmu.cs.lgp.redis.cluster.tools.JedisTools;

/**
 * MigrateThread.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Dec 29, 2014
 */
public class MigrateThread extends Thread {
    private JedisTools jd;
    private Object lock;
    private JButton jb;
    private MigrateStructure migrateStructure;
    
    public MigrateThread(JedisTools jd, Object lock, JButton jb, MigrateStructure migrateStructure) {
        this.jd = jd;
        this.lock = lock;
        this.jb = jb;
        this.migrateStructure = migrateStructure;
    }

    public void run() {
        synchronized (lock) {
            jd.migrateSlots(migrateStructure.getSource(), migrateStructure.getTarget(), migrateStructure.getMigrateSlotsNums());
            jb.setEnabled(true);
            migrateStructure.setFlag(true);
        }
    }
}
