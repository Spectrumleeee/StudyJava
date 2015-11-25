package com.test.zookeeper;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.retry.ExponentialBackoffRetry;

/**
 * TestZookeeper.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-3-27
 */
public class TestCurator {
    static CuratorFramework zkclient = null;
    static String nameSpace = "mongodb/mongos";
    static {
        String zkhost = "172.31.1.162:2181";
        RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);// 重试机制
        Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkhost).connectionTimeoutMs(5000)
                .sessionTimeoutMs(5000).retryPolicy(rp);
        zkclient = builder.namespace(nameSpace).build();
        zkclient.start();// 放在这前面执行
        zkclient.newNamespaceAwareEnsurePath(nameSpace);
    }

    public static void main(String[] args) throws Exception {
        TestCurator ct = new TestCurator();

        ct.upload("/proxy3/zk", "zk.txt");
//        ct.getListChildren("");
//        ct.checkExist("/proxy1");
//        ct.getListChildren("/proxy1");
//        ct.delete("/proxy1", true);
//        zkclient.close();
    }

    public void createrOrUpdate(String path, String content) throws Exception {
        zkclient.newNamespaceAwareEnsurePath(path).ensure(
                zkclient.getZookeeperClient());
        zkclient.setData().forPath(path, content.getBytes());
        System.out.println("添加成功！！！");
    }

    public void delete(String path, boolean recursive) throws Exception {
        if (!recursive)
            zkclient.delete().guaranteed().forPath(path);
        else {
            List<String> paths = zkclient.getChildren().forPath(path);
            for (String item : paths) {
                delete(path + "/" + item, recursive);
            }
            zkclient.delete().guaranteed().forPath(path);
        }
    }

    public void checkExist(String path) throws Exception {
        if (zkclient.checkExists().forPath(path) == null) {
            System.out.println("路径不存在!");
        } else {
            System.out.println("路径已经存在!");
        }
    }

    public void read(String path) throws Exception {
        String data = new String(zkclient.getData().forPath(path), "gbk");
        System.out.println("读取的数据:" + data);
    }

    public void getListChildren(String path) throws Exception {
        List<String> paths = zkclient.getChildren().forPath(path);
        for (String p : paths) {
            System.out.println(p);
        }
        if (paths.size() == 0)
            System.out.println(path + " is empty!");
    }

    public void upload(String zkPath, String localpath) throws Exception {
        createrOrUpdate(zkPath, "");
        byte[] bs = FileUtils.readFileToByteArray(new File(localpath));
        zkclient.setData().forPath(zkPath, bs);
        System.out.println("上传文件成功！");
    }
}
