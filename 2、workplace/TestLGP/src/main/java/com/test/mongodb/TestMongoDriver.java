package com.test.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;
import com.test.entity.UserData;

/**
 * TestMongoDriver.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: 2015-3-20 REF:
 * http://www.cnblogs.com/yjmyzz/p/3865175.html
 */
public class TestMongoDriver {

    private static ResourceBundle bundle = ResourceBundle.getBundle("mongo");
    private MongoClient mongo = null;
    private DB db = null;

    public static void main(String[] args) throws Exception {
        TestMongoDriver tmd = new TestMongoDriver();
        // tmd.testCRUD();
        // tmd.test_replSet();
        tmd.test_sharding();
    }

    public TestMongoDriver() throws UnknownHostException {
        mongo = new MongoClient(bundle.getString("mongodb.ip"),
                Integer.parseInt(bundle.getString("single.port")));
    }

    public void testCRUD() {
        db = mongo.getDB("test");

        // 遍历数据库中所有集合的名字
        Set<String> colls = db.getCollectionNames();
        for (String s : colls) {
            System.out.println(s);
        }

        // 查询集合person中的所有文档对象
        DBCollection coll = db.getCollection("person");
        DBCursor cursor = coll.find();
        for (DBObject obj : cursor) {
            System.out.println("name:" + obj.get("name") + "\tage:"
                    + obj.get("age"));
        }

        // 删除指定文档对象
        coll.remove(new BasicDBObject().append("name", "richard"));

        // 插入一条记录
        BasicDBObject address = new BasicDBObject().append("country", "CN")
                .append("province", "GuangDong");
        BasicDBObject doc = new BasicDBObject("name", "richard").append("age",
                25).append("address", address);
        coll.insert(doc);

        // 按条件查找某条或某些记录
        DBCursor cursor1 = coll.find(new BasicDBObject().append("age", 21));
        for (DBObject obj : cursor1) {
            System.out.println(obj);
        }

        // 更新记录
        BasicDBObject docFind = new BasicDBObject("age", 25);
        doc.put("age", 82);
        coll.update(docFind, doc);
        DBObject findResult = coll.findOne(docFind);
        System.out.println(findResult);

        // 先删除所有索引（会留下默认的id索引）
        coll.dropIndexes();
        // 1代表升序
        coll.createIndex(new BasicDBObject("name", 1));

        // 删除记录
        coll.remove(new BasicDBObject().append("key", "jimmy"));
        // 插入复杂对象
        UserData userData = new UserData("jimmy", "123456");
        Set<String> pets = new HashSet<String>();
        pets.add("cat");
        pets.add("dog");
        Map<String, String> favoriteMovies = new HashMap<String, String>();
        favoriteMovies.put("dragons", "Dragons II");
        favoriteMovies.put("avator", "Avator I");
        userData.setFavoriteMovies(favoriteMovies);
        userData.setPets(pets);
        userData.setBirthday(new Date());
        BasicDBObject objUser = new BasicDBObject("key", "jimmy").append(
                "value", toDBObject(userData));
        coll.insert(objUser);
        System.out.println(coll.findOne(objUser));
    }

    public void test_replSet() throws Exception {
        init_replSet();

        db = mongo.getDB("test");
        DBCollection coll = db.getCollection("person");

        for (int i = 20; i < 2000; i++) {
            try {
                coll.insert(new BasicDBObject().append("name",
                        "spectrumleeee" + i).append("age", i));
            } catch (Exception e) {
                // e.printStackTrace();
                coll = db.getCollection("person");
                i--;
            } finally {
                // 每次中间停顿500ms，模拟副本集节点故障，副本集选举出新的master之前，客户端抛异常
                Thread.sleep(500);
            }
        }

        System.out.println("db.person.count():" + coll.count());

    }

    // 初始化 副本集 连接
    private void init_replSet() throws Exception {
        ServerAddress addr = null;
        String replSetIp = null;

        List<ServerAddress> addresses = new ArrayList<ServerAddress>();
        replSetIp = bundle.getString("mongodb.ip");

        for (int i = 1; i <= 3; i++) {
            addr = new ServerAddress(replSetIp, Integer.parseInt(bundle
                    .getString("replSet.port" + i)));
            addresses.add(addr);
        }
        mongo = new MongoClient(addresses);
    }

    public void test_sharding() throws Exception {
        mongo = new MongoClient(bundle.getString("mongodb.ip"),
                Integer.parseInt(bundle.getString("sharding.mongos.port1")));
        db = mongo.getDB("test");
        DBCollection coll = db.getCollection("person");
        
        for (int i = 0; i < 100; i++)
            coll.insert(new BasicDBObject().append("name", "sharding-" + i));
    }

    // 将一般Object转换为 Mongo DBObject
    private DBObject toDBObject(Object obj) {
        return (DBObject) JSON.parse(new Gson().toJson(obj));
    }
}
