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

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.test.junit.CalcMethodException;
import com.test.junit.ICalcMethod;
import com.test.junit.IncomeCalculator;
import com.test.junit.Position;
import com.test.junit.PositionException;

/**
 * Junit Test IncomeCalculator By Using EasyMock
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 10, 2015
 */
public class IncomeCalculatorTest {
    
    private ICalcMethod calcMethod;
    private IncomeCalculator calc;
    
    @Before
    public void setUp(){
        System.out.println("Hello World! Using EasyMock");
        calcMethod = EasyMock.createMock(ICalcMethod.class);
        calc = new IncomeCalculator();
    }
    
    @After
    public void tearDown(){
        System.out.println("Goodbye!");
    }
    
    @Test
    public void testCalc1(){
        EasyMock.expect(calcMethod.calc(Position.BOSS)).andReturn(70000.0).times(2);
        EasyMock.expect(calcMethod.calc(Position.PROGRAMMER)).andReturn(50000.0);
        EasyMock.replay(calcMethod);
        
        calc.setCalcMethod(calcMethod);
        try{
            calc.calc();
            fail("Exception did not occur");
        }catch(PositionException e){
        }
        
        calc.setPosition(Position.BOSS);
        assertEquals(70000.0, calc.calc(), 0.1);
        assertEquals(70000.0, calc.calc(), 0.1);
        calc.setPosition(Position.PROGRAMMER);
        assertEquals(50000.0, calc.calc(), 0.1);
        calc.setPosition(Position.SURFER);
        EasyMock.verify(calcMethod);
    }
    
    @Test(expected = PositionException.class)
    public void testNoPosition(){
        EasyMock.expect(calcMethod.calc(Position.BOSS)).andReturn(70000.0).times(2);
        EasyMock.replay(calcMethod);
        calc.setCalcMethod(calcMethod);
        calc.calc();
    }
    
    @Test(expected = CalcMethodException.class)
    public void testNoCalc(){
        calc.setPosition(Position.BOSS);
        calc.calc();
    }
    
    @Test(expected = PositionException.class)
    public void testCalc2(){
        EasyMock.expect(calcMethod.calc(Position.SURFER)).andThrow(new PositionException("Dont know this guy")).times(1);
        EasyMock.replay(calcMethod);
        
        calc.setPosition(Position.SURFER);
        calc.setCalcMethod(calcMethod);
        calc.calc();
    }
}
