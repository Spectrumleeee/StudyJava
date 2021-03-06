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
package com.test.junit;

/**
 * IncomeCalculator.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Feb 10, 2015
 */
public class IncomeCalculator {

    private ICalcMethod calcMethod;
    private Position position;
    
    public void setCalcMethod(ICalcMethod calcMethod){
        this.calcMethod = calcMethod;
    }
    
    public void setPosition(Position position){
        this.position = position;
    }
    
    public double calc(){
        if(calcMethod == null){
            throw new CalcMethodException("CalcMethod not yet maintained");
        }
        if(position == null){
            throw new PositionException("Position not yet maintained");
        }
        
        return calcMethod.calc(position);
    }
}
