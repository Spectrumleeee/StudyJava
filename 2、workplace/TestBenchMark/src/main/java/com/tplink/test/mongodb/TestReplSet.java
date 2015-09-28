/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-9-2
 *
 */
package com.tplink.test.mongodb;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tplink.test.dao.MongoDao;
import com.tplink.test.utils.ConfigUtil;

public class TestReplSet {
    private MongoDao mongoDao;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private int threadCount = 1;
    private long opsCount = 300000;

    public TestReplSet() {
        mongoDao = new MongoDao(ConfigUtil.getString("mongodb.url"));
        database = mongoDao.getDatabase();
        collection = mongoDao.getCollection();
    }

    public static void main(String[] args) throws Exception {
        new TestReplSet().startTest();
    }

    public void startTest() throws Exception {
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new TesterThread(collection, database, opsCount);
        }

        long startMs = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
        long duraMs = System.currentTimeMillis() - startMs;

        System.out.println(opsCount * threadCount * 1000 / duraMs);
    }

    class TesterThread extends Thread {
        private MongoCollection<Document> collection;
        private long opsCount;

        public TesterThread(MongoCollection<Document> collection,
                MongoDatabase database, long opsCount) {
            this.collection = collection;
            this.opsCount = opsCount;
        }

        public void run() {
            String threadName = Thread.currentThread().getName();
            for (long i = 0; i < opsCount; i++) {
                collection.insertOne(new Document("_id", threadName + i));
            }
        }
    }
}
