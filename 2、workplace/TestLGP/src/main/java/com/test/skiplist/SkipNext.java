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
package com.test.skiplist;

/**
 * SkipNext.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 5, 2015
 */
public class SkipNext {
    private SkipNode[] next;
    private int level;
    
    public SkipNext(int level){
        this.setLevel(level);
        next = new SkipNode[level];
        initSkipNext();
    }
    
    private void initSkipNext(){
        for(int i=0; i<level; i++)
            next[i] = null;
    }
    
    public void updateSkipNext(SkipNode node){
        for(int i=0; i<node.getLevel(); i++)
            next[i] = node;
    }
    
    public SkipNode[] getNext(){
        return next;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
