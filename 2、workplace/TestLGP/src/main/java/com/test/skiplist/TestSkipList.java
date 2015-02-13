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
 * TestSkipList.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 9, 2015
 */
public class TestSkipList {

    public static void main(String[] args) {
        test();
    }
    
    public static void test() {
        SkipList sl = new SkipList("mySkipList", 5);
        int randInt = 0;
        int level = 0;
        long start1 = System.currentTimeMillis();
        System.out.println("UnSorted List:");
        for (int i = 1; i <= 100; i++) {
            randInt = (int) Math.round(Math.random() * 1000);
            level = (int) Math.round(Math.random() * 4 + 1);
            sl.insertNode(new SkipNode(randInt,level));
            System.out.print(randInt + "-"+level + " ");
//            if (i % 9 == 0)
//                System.out.println();
        }

        System.out.println("\nSorted List:");
         SkipNode startNode = sl.getHead().getNext()[0];
         while(startNode != null){
         System.out.print(startNode.getVal() + " ");
         startNode = startNode.getNext()[0];
         }
        long start = System.currentTimeMillis();
        if (sl.search(randInt + 10))
            System.out.println("\n找到啦！" + (randInt+10) + " "
                    + System.currentTimeMillis() + " " + start + " " + start1 + " " + sl.size());
        else
            System.out.println("\n没找到！" + (randInt+10) );
    }

}
