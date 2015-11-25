/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-23
 */
package com.xmu.lgp.common.zookeeper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WatcherZk implements Watcher {

    private final Logger logger = LoggerFactory.getLogger(WatcherZk.class);

    protected static final String NODE_PATH_SEPERATOR = String.valueOf('/');

    protected static final String BASE_PATH = "/lgp";
    protected static final int DEFAULT_SESSION_TIMEOUT_MS = 15000;

    private static final int ZK_CONNECT_TIMEOUT_SECOND = 10;

    private CountDownLatch connectedSemaphore = null;
    private ZooKeeper zookeeper = null;
    private String address = null;
    private int sessionTimeoutMs = 0;

    public WatcherZk(String address, int sessionTimeout) {
        this.address = address;
        this.sessionTimeoutMs = sessionTimeout;
        createConnection();
    }

    protected void onNodeCreatedEvent(String path) {
    }

    protected void onNodeDataChangedEvent(String path) {
    }

    protected void onNodeDeletedEvent(String path) {
    }

    protected void onAuthFailedEvent() {
    }

    abstract protected void onDisconnectedEvent();

    abstract protected void onSessionExpiredEvent();

    protected void onChildrenChangedEvent(String path) {
    }

    private void createConnection() {
        connectedSemaphore = new CountDownLatch(1);
        // first close connection
        releaseConnection();
        try {
            zookeeper = new ZooKeeper(address, sessionTimeoutMs, this);
            if (logger.isDebugEnabled()) {
                logger.debug("Start to connect to the zookeeper server, "
                        + "address = " + address);
            }
            boolean connected =
                    connectedSemaphore.await(ZK_CONNECT_TIMEOUT_SECOND,
                            TimeUnit.SECONDS);
            if (!connected) {
                throw new RuntimeException(
                        "Create zookeeper connection timeout!");
            }
        } catch (Exception e) {
            logger.error("Create zookeeper connection failed", e);
            throw new RuntimeException(e);
        }
    }

    protected void reConnect() {
        try {
            createConnection();
        } catch (Exception e) {
            logger.error("ReConnect exception", e);
        }
    }

    /**
     * close zookeeper connector
     */
    public void releaseConnection() {
        if (zookeeper != null) {
            try {
                this.zookeeper.close();
                this.zookeeper = null;
            } catch (Exception e) {
                logger.error("Close zookeeper connection failed", e);
            }
        }
    }

    public boolean checkExist(String path) {
        boolean rst = true;
        try {
            if (zookeeper.exists(path, false) == null) {
                rst = false;
            }
        } catch (Exception e) {
            logger.error("Failed to checkExists path: ", path);
        }
        return rst;
    }
    
    protected Stat exists(String path, boolean needWatch) {
        try {
            return this.zookeeper.exists(path, needWatch);
        } catch (Exception e) {
            logger.error("check path exists failed, path = " + path, e);
        }
        return null;
    }

    protected void createNodeIfNotExist(String nodePath, byte[] nodeData,
            CreateMode createMode, boolean needWatch) {
        try {
            // the path is existed or not
            // create the path if it is not existed
            Stat stat = exists(nodePath, needWatch);
            if (stat == null) {
                this.zookeeper.create(nodePath, nodeData,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                if (logger.isDebugEnabled()) {
                    logger.debug("Node is created successfully, nodePath = "
                            + nodePath);
                }
            }
        } catch (Exception e) {
            logger.error("Create node fail, nodePath = " + BASE_PATH, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get byte from string, using "UTF-8" as default charset;
     * If "UTF-8" is unsupported, use system default charset;
     * 
     * @param message
     * @return
     */
    protected byte[] getByte(String message) {
        byte[] data = null;
        try {
            // Encode and decode Charset for zoookeeper is 'UTF-8';
            data = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            data = message.getBytes(Charset.defaultCharset());
        }
        return data;
    }

    protected void deleteNode(String nodePath) {
        try {
            // -1 ignore node version, delete all nodes with the given name;
            this.zookeeper.delete(nodePath, -1);
        } catch (Exception e) {
            logger.error("Delete node failed, nodePath = " + nodePath, e);
        }
    }

    protected byte[] getData(String nodePath, boolean needWatch) {
        try {
            return this.zookeeper.getData(nodePath, needWatch, new Stat());
        } catch (Exception e) {
            logger.error("Get node data failed, nodePath = " + nodePath, e);
            return null;
        }
    }

    /**
     * Get child path, contain the child node name list,
     * not a full path list;
     */
    protected List<String> getChildren(String nodePath, boolean needWatch) {
        try {
            return new ArrayList<String>(this.zookeeper.getChildren(nodePath,
                    needWatch));
        } catch (Exception e) {
            logger.error("Get children nodes failed, nodePath = " + nodePath,
                    e);
            return null;
        }
    }

    protected void setData(String nodePath, byte[] data, boolean needWatch) {
        try {
            // -1 ignore node version, set all nodes data with the given
            // name;
            this.zookeeper.setData(nodePath, data, -1);
        } catch (Exception e) {
            logger.error("Set node data failed, nodePath = " + nodePath, e);
        }
    }

    /**
     * Get the permissions control of the given node.
     * One permission is confirm of a user id the and read/write/delete
     * permission of this user;
     */
    public List<ACL> getACL(String nodePath) throws Exception {
        return this.zookeeper.getACL(nodePath, new Stat());
    }

    /**
     * Set the permissions control to the given node.
     * One permission is confirm of a user id the and read/write/delete
     * permission of this user;
     */
    public void setACL(String nodePath, List<ACL> acl) throws Exception {
        this.zookeeper.setACL(nodePath, acl, -1);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event == null) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(event.toString());
        }
        // connection state
        KeeperState keeperState = event.getState();
        // event type
        EventType eventType = event.getType();
        // path affected
        String path = event.getPath();
//        System.out.println(""+System.currentTimeMillis() + " " + eventType);
        if (KeeperState.SyncConnected == keeperState) {
            // connect success
            if (EventType.None == eventType) {
                // If keeperstate is SyncConnected and function such as
                // exists is called, which cause EventType.None event;
                // suggest that connection has been created successfully.
                if (connectedSemaphore.getCount() > 0) {
                    connectedSemaphore.countDown();
                }
            } else if (EventType.NodeCreated == eventType) {
                onNodeCreatedEvent(path);
            } else if (EventType.NodeDataChanged == eventType) {
                onNodeDataChangedEvent(path);
            } else if (EventType.NodeChildrenChanged == eventType) {
                onChildrenChangedEvent(path);
            } else if (EventType.NodeDeleted == eventType) {
                onNodeDeletedEvent(path);
            }
        } else if (KeeperState.Disconnected == keeperState) {
            onDisconnectedEvent();
        } else if (KeeperState.AuthFailed == keeperState) {
            onAuthFailedEvent();
        } else if (KeeperState.Expired == keeperState) {
            onSessionExpiredEvent();
        }
    }
}
