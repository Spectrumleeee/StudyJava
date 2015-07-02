/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-6-5

 * updated: 2015-6-5
 * wenyong <liguangpu@tp-link.net>
 * Reason: 
 */
package com.test.mongodb;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.MapReduceOutput;

public class TestAggregate extends MongoBase {

    public static void main(String[] args) {
        TestAggregate ta = new TestAggregate();
        ta.testMapReduce();
    }

    public void test() {
        mongoColl.insert(new BasicDBObject("test", 1));
    }

    public void testAggregate() {
        // $match
        DBObject matchFilters = new BasicDBObject();
        DBObject matchContent = new BasicDBObject("status", "A");
        matchFilters.put("$match", matchContent);

        DBObject groupFilters = new BasicDBObject();
        DBObject sumContent = new BasicDBObject("$sum", "$amount");
        DBObject groupContent = new BasicDBObject("_id", "$cust_id").append(
                "total", sumContent);
        groupFilters.put("$group", groupContent);

        AggregationOutput ao = mongoColl.aggregate(matchFilters, groupFilters);

        for (DBObject rstObj : ao.results()) {
            System.out.println(rstObj);
        }
    }

    public void testMapReduce() {
        String map = "function(){ emit(this.cust_id, this.amount)}";
        String reduce = "function(key, values){return Array.sum(values)}";
        String outputTarget = "order_totals";
        DBObject query = new BasicDBObject("status", "A");
        MapReduceOutput mro = mongoColl.mapReduce(map, reduce, outputTarget,
                OutputType.REDUCE, query);
        for(DBObject rstObj : mro.results()){
            System.out.println(rstObj);
        }
    }
}
