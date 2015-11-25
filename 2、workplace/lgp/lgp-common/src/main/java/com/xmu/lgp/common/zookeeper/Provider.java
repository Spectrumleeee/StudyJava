/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: wenyong <wenyong@tp-link.net>
 * Created: 2014-10-31
 */

/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: lukairui <lukairui@tp-link.net>
 * Modified: 2014-09-25
 * Add data to the service address node, to mark whether the node is going 
 *      to shutdown, for elegant shutdown of a service node.
 */

package com.xmu.lgp.common.zookeeper;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.lgp.common.loadbalance.ILoadBalance;

/**
 * Register local address for a service to zookeeper, the path
 * in the zookeeper is
 * /address/serviceName/oneProviderAddress
 * The data of the node is an extensible json string,
 * such as {"state":"online"}
 * example: serviceName=dispatcher, localAddress:127.0.0.1:8888
 * uesd: registerServiceAddress(dispatcher,127.0.0.1:8888)
 * node: /address/dispatcher/127.0.0.1:8888
 * date: stat:online
 */
public class Provider extends WatcherZk {
    private Logger logger = LoggerFactory.getLogger(Provider.class);

    private HashMap<String, String> serviceMap =
            new HashMap<String, String>();

    public Provider(String zookeeperAddress) {
        super(zookeeperAddress, DEFAULT_SESSION_TIMEOUT_MS);
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
        createNodeIfNotExist(BASE_PATH, getByte(BASE_PATH),
                CreateMode.PERSISTENT, false);
    }

    /**
     * Create the corresponding service path
     */
    private void saveServiceInfo(String serviceName, String serverAddress) {
        String servicePath = BASE_PATH + NODE_PATH_SEPERATOR + serviceName;
        createNodeIfNotExist(servicePath, getByte(servicePath),
                CreateMode.PERSISTENT, false);

        String addressPath =
                servicePath + NODE_PATH_SEPERATOR + serverAddress;
        String onLineDate =
                String.format("{\"%s\":\"%s\"}", ILoadBalance.NODE_STATE,
                        ILoadBalance.ServerState.ONLINE.getState());
        createNodeIfNotExist(addressPath, getByte(onLineDate),
                CreateMode.EPHEMERAL, false);
    }

    @Override
    protected void onDisconnectedEvent() {
        if (logger.isDebugEnabled()) {
            logger.debug("Disconnected event occured!");
        }
        reConnect();

        reRegisterService();
    }

    @Override
    protected void onSessionExpiredEvent() {
        if (logger.isDebugEnabled()) {
            logger.debug("Session timeout event occured!");
        }
        reConnect();

        reRegisterService();
    }

    private void reRegisterService() {
        createBaseNode();
        for (String serviceName : serviceMap.keySet()) {
            String addressValue = serviceMap.get(serviceName);
            saveServiceInfo(serviceName, addressValue);
        }
    }
}
