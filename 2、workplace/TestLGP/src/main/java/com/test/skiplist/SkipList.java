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
 * SkipList.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Feb 5, 2015
 */
public class SkipList {
    private String name;
    private int level;
    private SkipNode head;
    private int size;

    public SkipList(String name, int level) {
        this.name = name;
        this.level = level;
        this.head = new SkipNode(Integer.MIN_VALUE, level);
        this.size = 0;
    }

    public void insertNode(SkipNode node) {

        SkipNode cur = head;
        SkipNode tmp = null;
        SkipNode[] sn = new SkipNode[level];

        if (node == null || level < node.getLevel())
            return;

        int index = level;
        while (index > 0) {
            tmp = cur.getNext()[--index];
            sn[index] = cur;

            while (tmp != null && tmp.getVal() <= node.getVal()) {
                cur = tmp;
                sn[index] = cur;
                tmp = cur.getNext()[index];
            }
        }

        for (int i = 0; i < node.getLevel(); i++) {
            tmp = sn[i].getNext()[i];
            sn[i].getNext()[i] = node;
            node.getNext()[i] = tmp;
        }
        size++;
    }

    public boolean search(int val) {
        int index = level;
        SkipNode pre = head;
        SkipNode start = null;
        while (index > 0) {
            start = pre.getNext()[--index];
            while (start != null && start.getVal() < val) {
                pre = start;
                start = start.getNext()[index];
            }
        }
        if (pre.getNext()[0] != null && pre.getNext()[0].getVal() == val)
            return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public SkipNode getHead() {
        return head;
    }

    public void setHead(SkipNode head) {
        this.head = head;
    }

    public int size() {
        return size;
    }
}
