/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author: lukairui <lukairui@tp-link.net>
 * Created: 2015-09-28
 * LoadBalance Proxy, hold the node data in zookeeper, and choose available
 *      address by the given Strategy
 */

package com.xmu.lgp.common.loadbalance;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.lgp.common.loadbalance.ILoadBalance.Strategy;
/**
 * Proxy hold the newest data, and use ILoadBalance to select an available
 * server address
 */
public class LoadBalanceProxy {
    private Logger logger = LoggerFactory
            .getLogger(LoadBalanceProxy.class);

    List<ServerAddressNode> serverNodes = new ArrayList<ServerAddressNode>();
    private ILoadBalance loadBalance = null;

    public LoadBalanceProxy(List<ServerAddressNode> serverNodes,
            Strategy strategy) {
        this.serverNodes = serverNodes;
        switch (strategy) {
            case RANDOM_ROBIN:
                loadBalance = new RandomRobinLoadBalance();
                break;

            default:
                loadBalance = new RandomRobinLoadBalance();
                logger.info("Use random borin load balance!");
                break;
        }
    }

    public void notifyChange(List<ServerAddressNode> serverNodes) {
        this.serverNodes = serverNodes;
    }

    public String select() {
        return loadBalance.select(serverNodes);
    }
}
