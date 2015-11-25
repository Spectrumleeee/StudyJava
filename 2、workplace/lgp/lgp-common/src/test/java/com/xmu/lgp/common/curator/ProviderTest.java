/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-23
 *
 */
package com.xmu.lgp.common.curator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xmu.lgp.common.curator.Provider;
import com.xmu.lgp.common.utils.TimeUtil;

public class ProviderTest {
    private static final String ZK_HOST = "172.31.1.162:2181";
    private static Provider provider;
    
    @BeforeClass
    public static void setup(){
        provider = new Provider(ZK_HOST);
    }
    
    @Test
    public void test_registerServiceAddress(){
        provider.deleteNode("/curator/account/172.29.88.115");
        assertFalse(provider.checkExist("/curator/account/172.29.88.115"));
        provider.registerServiceAddress("account", "172.29.88.115");
        TimeUtil.sleepMils(100);
        assertTrue(provider.checkExist("/curator/account/172.29.88.115"));
        provider.releaseConnection();
    }
}
