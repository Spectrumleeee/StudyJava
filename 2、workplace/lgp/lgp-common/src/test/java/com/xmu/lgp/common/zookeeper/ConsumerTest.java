/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-23
 *
 */
package com.xmu.lgp.common.zookeeper;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xmu.lgp.common.curator.Provider;
import com.xmu.lgp.common.loadbalance.ILoadBalance.Strategy;
import com.xmu.lgp.common.utils.TimeUtil;
import com.xmu.lgp.common.zookeeper.Consumer;

public class ConsumerTest {

    private static final String ZK_HOST = "172.31.1.162:2181";
    private static final String watchRoot = "/curator/device";
    private static Provider provider1;
    private static Provider provider2;
    private static Provider provider3;
    private static Consumer consumer;

    @BeforeClass
    public static void setall() {
        initProviders();
        consumer = new Consumer(ZK_HOST, watchRoot, Strategy.RANDOM_ROBIN);
    }

    private static void initProviders() {
        provider1 = new Provider(ZK_HOST);
        provider2 = new Provider(ZK_HOST);
        provider3 = new Provider(ZK_HOST);
        provider1.registerServiceAddress("device", "172.29.88.115");
        provider2.registerServiceAddress("device", "172.29.88.116");
    }

    @Test
    public void getAddress() {
        assertEquals(2, consumer.getOnlineServersSize());
        provider3.registerServiceAddress("device", "172.29.88.117");
        TimeUtil.sleepMils(100);
        assertEquals(3, consumer.getOnlineServersSize());
        
        provider1.releaseConnection();
        TimeUtil.sleepMils(100);
        assertEquals(2, consumer.getOnlineServersSize());
        
        provider2.releaseConnection();
        TimeUtil.sleepMils(100);
        assertEquals(1, consumer.getOnlineServersSize());
        
        provider3.releaseConnection();
        TimeUtil.sleepMils(100);
        assertEquals(0, consumer.getOnlineServersSize());
    }
}
