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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.test.junit.Calculator;

/**
 * CalculatorTest.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 10, 2015
 */
public class CalculatorTest {

    Calculator calc;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        calc = new Calculator();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.test.junit.Calculator#add(int, int)}.
     */
    @Test
    public void testAdd() {
        assertEquals(5, calc.add(2,3));
    }

    /**
     * Test method for {@link com.test.junit.Calculator#sub(int, int)}.
     */
    @Test
    public void testSub() {
        assertEquals(3, calc.sub(5, 2));
    }

    /**
     * Test method for {@link com.test.junit.Calculator#multi(int, int)}.
     */
    @Test
    public void testMulti() {
        assertEquals(6, calc.multi(2, 3));
    }

    /**
     * Test method for {@link com.test.junit.Calculator#div(int, int)}.
     */
    @Test
    public void testDiv() {
        assertEquals(2f, calc.div(6, 3), 0.1);
    }

}
