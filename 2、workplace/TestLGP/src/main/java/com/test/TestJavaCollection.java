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

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

import com.test.database.jedis.TestCase;
import com.test.utils.Person;

/**
 * TestJavaCollection.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 29, 2015
 */
public class TestJavaCollection extends TestCase{

    public static void main(String[] args) {
        TestJavaCollection tjc = new TestJavaCollection();
        tjc.call();
    }
    
    public void test_LinkedBlockingDeque(){
        LinkedBlockingDeque<Person> idleQueue = new LinkedBlockingDeque<Person>();
        for (int i = 0; i < 10; i++) {
            Person person = new Person("guanyu-"+i, "male", 52+i);
            if(i%2==0)
                idleQueue.addLast(person);  // 2Tail
            else
                idleQueue.addFirst(person); // 2Head
        }
        
        Iterator<Person> iterator = idleQueue.descendingIterator(); // Tail2Head
        Iterator<Person> iterator1 = idleQueue.iterator();          // Head2Tail
        while(iterator.hasNext()){
            Person person = iterator.next();
            String name = person.getName();
            int age = person.getAge();
            System.out.println("Name:"+name + " Age:"+age);
            if(age%2 == 0)
                iterator.remove();
        }
        
        while(iterator1.hasNext()){
            Person person = iterator1.next();
            String name = person.getName();
            int age = person.getAge();
            System.out.println("Name:"+name + " Age:"+age);
        }
    }

    @Override
    public void specifiedMethod() {
        test_LinkedBlockingDeque();
    }
}
