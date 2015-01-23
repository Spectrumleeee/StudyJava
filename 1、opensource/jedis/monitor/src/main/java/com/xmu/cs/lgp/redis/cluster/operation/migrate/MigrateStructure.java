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

/**
 * IsMigrateFinished.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Dec 29, 2014
 */
public class MigrateStructure{
    private boolean flag;
    private int currentSlotsNums;
    private int migrateSlotsNums;
    private String source;
    private String target;
    
    public MigrateStructure(){
        this.setFlag(true);
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public int getCurrentSlotsNums() {
        return currentSlotsNums;
    }
    public void setCurrentSlotsNums(int currentSlotsNums) {
        this.currentSlotsNums = currentSlotsNums;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }

    public int getMigrateSlotsNums() {
        return migrateSlotsNums;
    }
    
    public void setMigrateSlotsNums(int migrateSlotsNums) {
        this.migrateSlotsNums = migrateSlotsNums;
    }
    
    public boolean checkInputSlotsNums(String slotsNums){
        char[] slots = slotsNums.toCharArray();
        if (slotsNums.length() == 0 || (slotsNums.length() > 0
                && slots[0] == '0') || slots[0] == '<') {
            System.err.println("[INFO] input error, invalid slot num!!");
            return false;
        }
        this.migrateSlotsNums = Integer.parseInt(slotsNums);
        if(migrateSlotsNums > currentSlotsNums){
            System.err.println("[INFO] input slot num is too large!!");
            return false;
        }
        return true;
    }
}
