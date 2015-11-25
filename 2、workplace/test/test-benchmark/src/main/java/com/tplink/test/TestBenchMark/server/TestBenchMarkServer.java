/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-10-30
 *
 */
package com.tplink.test.TestBenchMark.server;

import java.util.Arrays;

import com.tplink.test.factory.TestDBFactory;
import com.tplink.test.factory.TestDBInterface;

public class TestBenchMarkServer {

    public void parseCommandLine(String[] args){
        
        System.out.println(Arrays.asList(args));
        String db = args[0];
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        
        TestDBInterface testInstance = TestDBFactory.getTestDBInstance(db);
        if(testInstance == null){
            System.out.println("Parameters error : " + Arrays.asList(args));
            System.exit(0);
        }
        testInstance.parseCommandLine(params);
    }
}
