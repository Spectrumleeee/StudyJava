/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-6-3
 */
package com.test.mongodb;

import java.net.UnknownHostException;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.test.utils.ConfigUtils;

public abstract class MongoBase {
    protected MongoClient mongoDao = null;
    protected DBCollection mongoColl = null;

    public MongoBase() {
        try {
            mongoDao = new MongoClient(ConfigUtils.getString("mongodb.ip"),
                    ConfigUtils.getInt("mongodb.port"));
            mongoColl = mongoDao.getDB(ConfigUtils.getString("mongodb.db"))
                    .getCollection(ConfigUtils.getString("mongodb.coll"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
