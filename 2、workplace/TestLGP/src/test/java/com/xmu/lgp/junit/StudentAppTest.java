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
package com.xmu.lgp.junit;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.test.junit.IStudent;
import com.test.junit.StudentApp;

/**
 * JUnit Test for StudentApp
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 10, 2015
 */
public class StudentAppTest {
    IStudent student;
    StudentApp studentApp;
    
    @Before
    public void setUp(){
        student = EasyMock.createMock(IStudent.class);
        studentApp = new StudentApp();
    }
    
    @Test
    public void test(){
        
        EasyMock.expect(student.doMethod1()).andReturn("a");
        EasyMock.expect(student.doMethod2()).andReturn("b");
        EasyMock.expect(student.doMethod3()).andReturn("c");
        
        EasyMock.replay(student);
        
        studentApp.setStudent(student);
        String rstStr = studentApp.doMethod();
        assertEquals(rstStr, "abc");
        
        EasyMock.verify(student);
    }
}
