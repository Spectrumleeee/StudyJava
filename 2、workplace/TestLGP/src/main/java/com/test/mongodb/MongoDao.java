/**
 * Copyright (c) 2014, TP-Link Co.,Ltd. 
 * Author: liguangpu <liguangpu@tp-link.net> 
 * Created: 2015-4-3
 */

package com.test.mongodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;


public class MongoDao{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MongoDao.class);

    private MongoClient mongo = null;
    private DBCollection dbColl = null;
    private DB db = null;

    public MongoDao(String database, String collection) {
        try {
            mongo = new MongoClient("172.29.88.117", 27017);

            db = mongo.getDB(database);
            dbColl = db.getCollection(collection);
        } catch (Exception e) {
            LOGGER.error("Init mongoDB Connection error!");
        }
    }

    public void persist(long num, int size){
        StringBuilder str = new StringBuilder();
        for(int i=0; i<size; i++)
            str.append((char)(Math.random()*26 + 65));
        BasicDBObject obj = new BasicDBObject().append(size + "B-"+num, str.toString());
        
        long start = System.currentTimeMillis();
        for(int i=0; i<num; i++){
            
            dbColl.insert((BasicDBObject)obj.clone());
        }
        long finish = System.currentTimeMillis();
        
        System.out.println(size + "B-"+num + " --> " + num*1000 / (finish - start));
    }
    
    public static void main(String[] args){
        MongoDao dao = new MongoDao("test", "collector");
        dao.persist(Long.parseLong(args[1]), Integer.parseInt(args[0]));
    }

    /**
     * @return
     */
    public DBCollection getCollection() {
        // TODO Auto-generated method stub
        return null;
    }
}
