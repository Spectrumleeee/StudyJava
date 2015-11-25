/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: wenyong <wenyong@tp-link.net>
 * Created: 2014-10-31
 */

package com.xmu.lgp.common.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.lgp.common.loadbalance.ILoadBalance;
import com.xmu.lgp.common.loadbalance.ILoadBalance.ServerState;
import com.xmu.lgp.common.loadbalance.ILoadBalance.Strategy;
import com.xmu.lgp.common.loadbalance.LoadBalanceProxy;
import com.xmu.lgp.common.loadbalance.ServerAddressNode;

/**
 * get addresses of a service by zookeeper, the path
 * in the zookeeper is
 * /address/serviceName/oneProviderAddress
 * example: serviceName=dispatcher
 * used:getServiceAddress(dispatcher)
 * 
 * you should implements method onAddressChanged
 * because addresses may be changed sometimes
 */
public class Consumer extends WatcherZk {
    private Logger logger = LoggerFactory.getLogger(Consumer.class);

    private ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private String watchRoot = null;
    private LoadBalanceProxy proxy = null;

    public Consumer(String address, String watchRoot, Strategy strategy) {
        super(address, DEFAULT_SESSION_TIMEOUT_MS);
        this.watchRoot = watchRoot;
        this.proxy = new LoadBalanceProxy(loadServerNodes(), strategy);
    }

    public int getOnlineServersSize(){
        return loadServerNodes().size();
    }
    
    private List<ServerAddressNode> loadServerNodes() {
        List<String> addresses = getChildren(watchRoot, true);
        List<ServerAddressNode> onlineServerList =
                new ArrayList<ServerAddressNode>();
        String addrNode = null;
        JSONObject data = null;
        ServerState serverState = null;
        for (String addr : addresses) {
            addrNode = watchRoot + NODE_PATH_SEPERATOR + addr;
            data = getJSONData(addrNode);
            if (data == null) {
                logger.info("Node " + addrNode + " has no data!");
                continue;
            }

            serverState =
                    ServerState.getServerState(data
                            .getString(ILoadBalance.NODE_STATE));
            if (serverState == null) {
                logger.error("Invalid node date, " + getJSONData(addrNode));
                continue;
            }

            if (serverState == ServerState.ONLINE) {
                onlineServerList.add(new ServerAddressNode(addr, data));
            } else {
                logger.info("Node " + addrNode + " is not online now!"
                        + getJSONData(addrNode));
            }
        }
        return onlineServerList;
    }

    /**
     * Read address from the proxy file;
     * Codis write utf-8 bytes for json string;
     */
    private JSONObject getJSONData(String nodePath) {
        String data = null;
        try {
            byte[] byteData = getData(nodePath, false);
            if (null == byteData) {
                return null;
            }
            data = new String(byteData, "UTF-8");
            return new JSONObject(data);
        } catch (Exception e) {
            StringBuilder errorInfo = new StringBuilder();
            errorInfo.append("Format JSONObject failed!");
            errorInfo.append("The data sequence is ").append(data)
                    .append('.');
            logger.error(errorInfo.toString(), e);
        }
        return null;
    }

    protected void onChildrenChangedEvent(String path) {
        System.out.println("onChildrenChangedEvent " + path);
        handleChange();
    }
    
    // NodeCreate, NodeDataChange and NodeDeleted event cause reload;
    @Override
    protected void onNodeCreatedEvent(String path) {
        System.out.println("node created event");
        handleChange();
    }

    // NodeCreate, NodeDataChange and NodeDeleted event cause reload;
    @Override
    protected void onNodeDataChangedEvent(String path) {
        System.out.println("node data changed event");
        handleChange();
    }

    // NodeCreate, NodeDataChange and NodeDeleted event cause reload;
    @Override
    protected void onNodeDeletedEvent(String path) {
        System.out.println("node deleted event");
        handleChange();
        notifyAddressDeleted(path.split("/")[path.split("/").length - 1]);
    }

    @Override
    protected void onDisconnectedEvent() {
        reConnect();
    }

    @Override
    protected void onSessionExpiredEvent() {
        reConnect();
    }

    /**
     * Return an available address selected by given LoadBalanceStrategy.
     */
    public String getAddress() {
        try {
            // Lock read lock, if proxy change event occurs, current thread
            // will be blocked until the event finished;
            rwlock.readLock().lock();
            return proxy.select();
        } finally {
            // To insure the readlock will be released even if
            // <code>RuntimeException</code> occurs.
            rwlock.readLock().unlock();
        }
    }

    /**
     * Once a cluster change event occures, load addresses again, and
     * check all the addrs whether it's valid;
     */
    private void handleChange() {
        try {
            rwlock.writeLock().lock();
            proxy.notifyChange(loadServerNodes());
        } finally {
            // To insure the writelock will be released even if
            // <code>RuntimeException</code> occurs.
            rwlock.writeLock().unlock();
        }
    }

    /**
     * Reserved interface, to notify the users that delete event occures,
     * for elegant shutdown
     */
    public void notifyAddressDeleted(String deleteAddress) {

    }
}
