/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author: lukairui <lukairui@tp-link.net>
 * Created: 2015-09-28
 * RR loadbalance strategy.
 */

package com.xmu.lgp.common.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RR load balance.
 */
public class RandomRobinLoadBalance implements ILoadBalance {
    // Index of address latest in use, for RoundRobin load balance;
    private static AtomicInteger latestSelectIndex = new AtomicInteger(0);

    public RandomRobinLoadBalance() {
        super();
    }

    @Override
    public String select(List<ServerAddressNode> serverNodes) {
        if(0 == serverNodes.size()){
            throw new RuntimeException("no server was provided!");
        }
        for (;;) {
            int latest = latestSelectIndex.get();
            int current = (latest >= serverNodes.size() - 1) ? 0 : latest + 1;
            // Make sure the cluster index is set correctly.
            if (latestSelectIndex.compareAndSet(latest, current)) {
                return serverNodes.get(current).getAddress();
            }
        }
    }

}
