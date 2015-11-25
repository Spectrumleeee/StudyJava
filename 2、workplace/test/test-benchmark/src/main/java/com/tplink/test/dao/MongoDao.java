/**
 * Copyright (c) 2014, TP-Link Co.,Ltd. 
 * Author: liguangpu <liguangpu@tp-link.net> 
 * Created: 2015-4-3
 */

package com.tplink.test.dao;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDao {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MongoDao.class);
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private ReadPreference readPreference;
    private WriteConcern writeConcern;

    public MongoDao(String mongodbUrl) {
        try {
            MongoClientURI uri = new MongoClientURI(mongodbUrl);
            String dbName = uri.getDatabase();
            String collName = uri.getCollection();
            if (dbName == null || collName == null) {
                throw new RuntimeException("database or collection is null");
            }
            writeConcern = uri.getOptions().getWriteConcern();
            readPreference = uri.getOptions().getReadPreference();
            mongoClient = new MongoClient(uri);
            database = mongoClient.getDatabase(dbName)
                    .withReadPreference(readPreference)
                    .withWriteConcern(writeConcern);
            collection = database.getCollection(collName);
        } catch (Exception e) {
            LOGGER.error("init mongoDB Connection error!", e);
        }
    }

    public MongoDao(String ip, int port, String dbName, String collName,
            ReadPreference readPreference) {
        this(ip, port, dbName, collName, WriteConcern.ACKNOWLEDGED,
                readPreference);
    }

    public MongoDao(String ip, int port, String dbName, String collName,
            WriteConcern writeConcern) {
        this(ip, port, dbName, collName, writeConcern, ReadPreference.primary());
    }

    public MongoDao(String ip, int port, String dbName, String collName) {
        this(ip, port, dbName, collName, WriteConcern.ACKNOWLEDGED,
                ReadPreference.primary());
    }

    public MongoDao(String ip, int port, String dbName, String collName,
            WriteConcern writeConcern, ReadPreference readPreference) {
        try {
            mongoClient = new MongoClient(ip, port);
            database = mongoClient.getDatabase(dbName)
                    .withReadPreference(readPreference)
                    .withWriteConcern(writeConcern);
            collection = database.getCollection(collName);
        } catch (Exception e) {
            LOGGER.error("init mongoDB Connection error!", e);
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public void checkConnected() {
        collection.find();
    }

    public void stop() {
        mongoClient.close();
    }
}
