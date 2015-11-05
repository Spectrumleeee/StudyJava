/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-10-30
 *
 */
package com.tplink.test.factory;

import com.tplink.test.mongodb.TestMongoDB;
import com.tplink.test.redis.TestRedis;

public class TestDBFactory {
    
    public static TestDBInterface getTestDBInstance(String database){
        TestDBInterface ret = null;
        
        switch(database){
        case "mongodb":
            ret = new TestMongoDB();
            break;
        case "redis":
            ret = new TestRedis();
            break;
        }
        
        return ret;
    }
}
