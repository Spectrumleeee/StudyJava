/**
 * Copyright (c) 2014, TP-Link Co.,Ltd. 
 * Author: liguangpu <liguangpu@tp-link.net> 
 * Created: 2015-4-3
 */

package com.tplink.test.mongodb;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.tplink.test.dao.MongoDao;
import com.tplink.test.factory.TestDBInterface;
import com.tplink.test.utils.ConfigUtil;

public class TestMongoDB implements TestDBInterface{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TestMongoDB.class);
    private UpdateOptions UPDATE_WITH_UPSERT = new UpdateOptions().upsert(true);
    private MongoDao mongoDao;
    private MongoCollection<Document> collection;
    private AtomicLong count = new AtomicLong(0);
    private long startCon = 0;
    private boolean isWrite = true;
    private boolean upsert = false;

    public TestMongoDB() {
        try {
            mongoDao = new MongoDao(ConfigUtil.getString("mongodb.url.1"));
            collection = mongoDao.getCollection();
            upsert = ConfigUtil.getBoolean("mongodb.upsert");
            if (upsert) {
                System.out.println("useReplaceOne");
            } else {
                System.out.println("useInsertOne");
            }
        } catch (Exception e) {
            LOGGER.error("Init mongoDB Connection error!");
        }
    }



    public void insertDuplicateID() {
        collection.insertOne(new Document("name", "a"));
    }

    /**
     * test the insert performance of single thread
     * 
     * @param num
     *            total records
     * @param size
     *            size of each record
     */
    public void persist(long num, long size) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size; i++) {
            str.append((char) (Math.random() * 26 + 65));
        }
        Document template = new Document("content", str.toString());

        long start = System.currentTimeMillis();
        insert(num, template);
        long finish = System.currentTimeMillis();
        count.getAndIncrement();

        System.out.println(size + "B-" + num + " --> " + num * 1000
                / (float) (finish - start));
    }

    private boolean insert(long num, Document tmp) {
        return upsert ? useReplaceOne(num, tmp) : useInsertOne(num, tmp);
    }

    private boolean useInsertOne(long num, Document template) {
        String threadName = getThreadName();
        for (long i = 0; i < num; i++) {
            Document toInsert = Document.parse(template.toJson())
                    .append("_id", threadName + i).append("j", num - i);
            collection.insertOne(toInsert);
        }
        return true;
    }

    private boolean useReplaceOne(long num, Document template) {
        String threadName = getThreadName();
        for (long i = 0; i < num; i++) {
            Document toInsert = new Document(template).append("_id",
                    threadName + i).append("j", num - i);
            collection.replaceOne(new Document("_id", threadName + i),
                    toInsert, UPDATE_WITH_UPSERT);
        }
        return true;
    }

    /**
     * test the insert performance of multiple thread
     * 
     * @param num
     *            total records
     * @param size
     *            size of each record
     */
    public void curPersist(long num, long randSize, int con) {
        MyThread[] mt = new MyThread[con];
        for (int i = 0; i < con; i++) {
            mt[i] = new MyThread(num, randSize, con);
        }

        startCon = System.currentTimeMillis();
        for (int i = 0; i < con; i++) {
            mt[i].start();
        }
    }

    /**
     * test the random read performance of single thread
     * 
     * @param num
     *            total records
     * @param randSize
     *            size of each record
     */
    public void query(long num, long randSize) {
        long start = System.currentTimeMillis();
        for (long i = 0; i < randSize; i++) {
            collection.find(new Document().append("i", rd(num)));
        }
        long finish = System.currentTimeMillis();
        count.getAndIncrement();

        System.out
                .println("TPS: " + randSize * 1000 / (float) (finish - start));
    }

    /**
     * test the random read performance of multiple thread
     * 
     * @param num
     *            total records
     * @param randSize
     *            size of each record
     * @param con
     *            total threads number
     */
    public void curQuery(long num, long randSize, int con) {
        MyThread[] mt = new MyThread[con];
        for (int i = 0; i < con; i++) {
            mt[i] = new MyThread(num, randSize, con);
        }

        startCon = System.currentTimeMillis();
        for (int i = 0; i < con; i++) {
            mt[i].start();
        }
    }

    private long rd(long num) {
        return (long) (Math.random() * num);
    }

    class MyThread extends Thread {
        private long size;
        private long num;
        private long con;

        public MyThread(long num, long size, int con) {
            this.size = size;
            this.num = num;
            this.con = con;
        }

        public void run() {
            if (isWrite)
                persist(num, size);
            else
                query(num, size);

            if (count.get() == con) {
                long time = System.currentTimeMillis() - startCon;
                if (isWrite)
                    System.out.println(num * con * 1000 / (float) time);
                else
                    System.out.println(size * con * 1000 / (float) time);
            }
        }
    }

    protected String getThreadName() {
        return Thread.currentThread().getName();
    }

    public static void main(String[] params) {
        // new TestMongoDB().persist(100000, 1000);
        new TestMongoDB().insertDuplicateID();
    }

    @Override
    public void parseCommandLine(String[] params) {
        LOGGER.debug(Arrays.toString(params));
        switch (params[0]) {
        case "--w":
            isWrite = true;
            switch (params.length) {
            case 3:
                persist(Long.parseLong(params[2]), Integer.parseInt(params[1]));
                break;
            case 4:
                curPersist(Long.parseLong(params[2]), Integer.parseInt(params[1]),
                        Integer.parseInt(params[3]));
                break;
            default:
                break;
            }
            break;
        case "--r":
            isWrite = false;
            switch (params.length) {
            case 3:
                query(Long.parseLong(params[1]), Long.parseLong(params[2]));
                break;
            case 4:
                curQuery(Long.parseLong(params[1]), Long.parseLong(params[2]),
                        Integer.parseInt(params[3]));
                break;
            default:
                break;
            }
        }
    }
}
