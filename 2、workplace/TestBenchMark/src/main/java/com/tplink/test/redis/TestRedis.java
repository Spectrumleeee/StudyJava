/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-5-12

 * updated: 2015-5-12
 * wenyong <liguangpu@tp-link.net>
 * Reason: 
 */
package com.tplink.test.redis;

import java.util.concurrent.atomic.AtomicLong;

import com.tplink.test.utils.JedisPoolUtils;

import redis.clients.jedis.Jedis;

public class TestRedis {
    private long start;
    private AtomicLong count = new AtomicLong(0l);
    
    public TestRedis(){
    }
    
    public TestRedis(boolean usePool) {
    }
    
    public void parseCommandLine(String[] args) {
        testSetTPS(Long.parseLong(args[0]), Integer.parseInt(args[1]));
    }

    public void testSetTPS(long nums, int concurrent){
        Thread[] threads = new MyThread[concurrent];
        Jedis[] jediss = new Jedis[concurrent];
        for(int i=0; i<concurrent; i++){
            jediss[i] = JedisPoolUtils.getJedis();
            threads[i] = new MyThread(jediss[i], nums, concurrent);
        }
        start = System.currentTimeMillis();
        for(int i=0; i<concurrent; i++){
            threads[i].start();
        }
    }
    
    public long set(Jedis jedis, long nums){
        long startTime = System.currentTimeMillis();
        for(long i=0; i<nums; i++){
            jedis.set("key"+i, ""+i);
        }
        long finishTime = System.currentTimeMillis();
        return finishTime - startTime;
    }
    
    class MyThread extends Thread{
        private long nums;
        private int con;
        private Jedis jedis;
        
        public MyThread(Jedis jedis, long nums, int con){
            this.jedis = jedis;
            this.nums = nums;
            this.con = con;
        }
        
        public void run(){
            set(jedis, nums);
            JedisPoolUtils.returnRes(jedis);
            if(count.incrementAndGet() == con){
                long spentTime = System.currentTimeMillis() - start;
                System.out.println("TPS : " + nums*con*1000 / spentTime);
            }
        }
    }
}
