/**
 * Copyright (c) 2014, TP-Link Co.,Ltd. 
 * Author: liguangpu <liguangpu@tp-link.net> 
 * Created: 2015-4-3
 */

package com.tplink.test.mongodb;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.tplink.test.utils.ConfigUtils;

public class TestMongoDB {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TestMongoDB.class);

    private MongoClient mongo = null;
    private DBCollection dbColl = null;
    private DB db = null;
    private AtomicLong count = new AtomicLong(0);
    private long startCon = 0;
    private boolean isWrite = true;

    public TestMongoDB() {
        try {
            mongo = new MongoClient(ConfigUtils.getString("mongodb.ip"),
                    ConfigUtils.getInt("mongodb.port"));

            db = mongo.getDB(ConfigUtils.getString("mongodb.database"));
            dbColl = db.getCollection(ConfigUtils.getString("mongodb.coll"));
            dbColl.setWriteConcern(new WriteConcern(ConfigUtils
                    .getInt("mongodb.mode")));
        } catch (Exception e) {
            LOGGER.error("Init mongoDB Connection error!");
        }
    }

    public void parseCommandLine(String[] args) {
        switch(args[0]){
        case "--w":
            isWrite = true;
            switch (args.length) {
            case 3:
                persist(Long.parseLong(args[2]), Integer.parseInt(args[1]));
                break;
            case 4:
                curPersist(Long.parseLong(args[2]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[3]));
                break;
            default:
                break;
            }
            break;
        case "--r":
            isWrite = false;
            switch (args.length){
            case 3:
                query(Long.parseLong(args[1]), Long.parseLong(args[2]));
                break;
            case 4:
                curQuery(Long.parseLong(args[1]), Long.parseLong(args[2]), Integer.parseInt(args[3]));
                break;
            default:
                break;
            }
        }
    }

    /**
     * test the insert performance
     * 
     * @param num
     * @param size
     */
    public void persist(long num, long size) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size; i++)
            str.append((char) (Math.random() * 26 + 65));
        BasicDBObject obj = new BasicDBObject().append(size + "B-" + num,
                str.toString());

        long start = System.currentTimeMillis();
        for (long i = 0; i < num; i++) {
            dbColl.insert(((BasicDBObject) obj.clone()).append("i", i).append(
                    "j", num - i - 1));
        }
        long finish = System.currentTimeMillis();
        count.getAndIncrement();

        System.out.println(size + "B-" + num + " --> " + num * 1000
                / (float)(finish - start));
    }

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

    public void curQuery(long num, long randSize, int con){
        MyThread[] mt = new MyThread[con];
        for (int i = 0; i < con; i++) {
            mt[i] = new MyThread(num, randSize, con);
        }

        startCon = System.currentTimeMillis();
        for (int i = 0; i < con; i++) {
            mt[i].start();
        }
    }
    
    public void query(long num, long randSize) {
        long start = System.currentTimeMillis();
        for(long i=0; i<randSize; i++){
            dbColl.findOne(new BasicDBObject().append("i", rd(num)));
        }
        long finish = System.currentTimeMillis();
        count.getAndIncrement();
        
        System.out.println("TPS: " + randSize * 1000 / (float)(finish - start));
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
            if(isWrite)
                persist(num, size);
            else
                query(num, size);
            
            if (count.get() == con) {
                long time = System.currentTimeMillis() - startCon;
                if(isWrite)
                    System.out.println(num * con * 1000 / (float)time);
                else
                    System.out.println(size * con * 1000 / (float)time);
            }
        }
    }
}
