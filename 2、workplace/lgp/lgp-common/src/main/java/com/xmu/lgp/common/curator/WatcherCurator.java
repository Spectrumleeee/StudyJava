/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-23
 *
 */
package com.xmu.lgp.common.curator;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode.Mode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.lgp.common.utils.FileUtil;

public class WatcherCurator {
    private static final Logger logger = LoggerFactory
            .getLogger(WatcherCurator.class);
    protected static final String NODE_PATH_SEPERATOR = String.valueOf('/');
    protected static final int SESSION_TIMEOUT_MS = 10000;
    protected static final int CONNECT_TIMEOUT_MS = 6000;

    protected static String BASE_PATH = "/curator";

    private CuratorFramework curator;
    private PersistentEphemeralNode node;
    private PathChildrenCache watcher;

    public WatcherCurator(String zkhost, int connTimeoutMs,
            int sessionTimeoutMs, RetryPolicy rp) {
        if (null == rp) {
            rp = new ExponentialBackoffRetry(1000, 3);
        }

        curator = CuratorFrameworkFactory.newClient(zkhost, sessionTimeoutMs,
                connTimeoutMs, rp);
        curator.start();
        curator.newNamespaceAwareEnsurePath(BASE_PATH);
        logger.debug("Curator started!");
    }

    public void createrOrUpdate(String path, String content) {
        try {
            curator.newNamespaceAwareEnsurePath(path).ensure(
                    curator.getZookeeperClient());
            curator.setData().forPath(path, content.getBytes());
        } catch (Exception e) {
            logger.error("failed to createOrUpdate Node:", path);
        }
    }

    public void createEphemeralNode(String path, String content) {
        node = new PersistentEphemeralNode(curator, Mode.EPHEMERAL, path,
                content.getBytes());
        node.start();
        logger.debug("Created ephemeral node: {}:{}", path, content);
    }

    public void delete(String path, boolean recursive) throws Exception {
        if (!recursive)
            curator.delete().guaranteed().forPath(path);
        else {
            List<String> paths = curator.getChildren().forPath(path);
            for (String item : paths) {
                delete(path + "/" + item, recursive);
            }
            curator.delete().guaranteed().forPath(path);
        }
    }

    public boolean checkExist(String path) {
        boolean rst = true;
        try {
            if (curator.checkExists().forPath(path) == null) {
                rst = false;
            }
        } catch (Exception e) {
            logger.error("Failed to checkExists path: ", path);
        }
        return rst;
    }

    protected void deleteNode(String nodePath) {
        try {
            curator.delete().guaranteed().forPath(nodePath);
        } catch (Exception e) {
            logger.error("Delete node failed, nodePath = " + nodePath, e);
        }
    }
    
    public byte[] getData(String path, boolean needWatch) {
        byte[] rst = null;

        try {
            if (needWatch) {
                rst = curator.getData().watched().forPath(path);
            } else {
                rst = curator.getData().forPath(path);
            }
        } catch (Exception e) {
            logger.error("Failed to getData from node: ", path);
        }

        return rst;
    }

    protected List<String> getChildren(String path, boolean needWatch) {
        List<String> rst = null;
        try {
            if (needWatch) {
                rst = curator.getChildren().watched().forPath(path);
            } else {
                rst = curator.getChildren().watched().forPath(path);
            }
        } catch (Exception e) {
            logger.error("Failed to getChildren from path: ", path);
        }

        return rst;
    }

    protected void onNodeCreatedEvent(String path) {
    }

    protected void onNodeDataChangedEvent(String path) {
    }

    protected void onNodeDeletedEvent(String path) {
    }
    
    protected void registerWatcher(String watchRoot) {
        watcher = new PathChildrenCache(curator, watchRoot, true);
        watcher.getListenable().addListener(new PathChildrenCacheListener(){

            @Override
            public void childEvent(CuratorFramework client,
                    PathChildrenCacheEvent event) throws Exception {
                String eventPath = event.getData().getPath();
                switch(event.getType()){
                case CHILD_ADDED:
                    onNodeCreatedEvent(eventPath);
                    break;
                case CHILD_REMOVED:
                    onNodeDeletedEvent(eventPath);
                    break;
                case CHILD_UPDATED:
                    onNodeDataChangedEvent(eventPath);
                    break;
                default:
                    break;
                }
            }
        });
        
        try {
            watcher.start(StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            logger.error("Failed to start watcher");
        }
    }

    public void upload(String zkPath, String localpath) throws Exception {
        createrOrUpdate(zkPath, "");
        byte[] bs = FileUtil.toByteArray(localpath);
        curator.setData().forPath(zkPath, bs);
        System.out.println("上传文件成功！");
    }

    public void releaseConnection() {
        if(curator != null){
            curator.close();
        }
    }

}
