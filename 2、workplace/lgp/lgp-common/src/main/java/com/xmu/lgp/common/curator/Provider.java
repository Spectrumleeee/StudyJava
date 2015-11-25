/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-23
 *
 */
package com.xmu.lgp.common.curator;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.lgp.common.loadbalance.ILoadBalance;

public class Provider extends WatcherCurator {
    private Logger logger = LoggerFactory.getLogger(Provider.class);

    private HashMap<String, String> serviceMap = new HashMap<String, String>();

    public Provider(String zkhost) {
        this(zkhost, CONNECT_TIMEOUT_MS, SESSION_TIMEOUT_MS, null);
    }

    public Provider(String zkhost, int connTimeoutMs, int sessionTimeoutMs,
            RetryPolicy rp) {
        super(zkhost, connTimeoutMs, sessionTimeoutMs, rp);
    }

    public void registerServiceAddress(String serviceName, String address) {
        if (StringUtils.isBlank(serviceName)) {
            return;
        }
        serviceMap.put(serviceName, address);
        createBaseNode();
        saveServiceInfo(serviceName, address);
    }

    private void createBaseNode() {
        if (!checkExist(BASE_PATH)) {
            createrOrUpdate(BASE_PATH, BASE_PATH);
        }
    }

    private void saveServiceInfo(String serviceName, String address) {
        String servicePath = BASE_PATH + NODE_PATH_SEPERATOR + serviceName;
        createNode(servicePath, CreateMode.PERSISTENT, servicePath);
        String addressPath = servicePath + NODE_PATH_SEPERATOR + address;
        String addressCont = String.format("{\"%s\":\"%s\"}",
                ILoadBalance.NODE_STATE,
                ILoadBalance.ServerState.ONLINE.getState());
        // createEphemeralNode(addressPath, addressCont);
        createNode(addressPath, CreateMode.EPHEMERAL, addressCont);
        logger.debug("Saved service info: {}/{}", serviceName, address);
    }
}
