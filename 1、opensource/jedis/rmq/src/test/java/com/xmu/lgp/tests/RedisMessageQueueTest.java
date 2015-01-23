package com.xmu.lgp.tests;

import java.io.IOException;
import java.util.ResourceBundle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xmu.lgp.rmq.Consumer;
import com.xmu.lgp.rmq.Producer;

import redis.clients.jedis.Jedis;

public class RedisMessageQueueTest extends Assert {
    
    private String ip = null;
    private int port = 0;
    
    private void config(){
        ResourceBundle bundle = ResourceBundle.getBundle("config");
        ip = bundle.getString("ip");
        port = Integer.parseInt(bundle.getString("port"));
    }
    
    private void clearDB(){
        Jedis jedis = new Jedis(ip,port);
        jedis.flushAll();
        jedis.disconnect();
        jedis.close();
    }
    
    @Before
    public void setUp() throws IOException {

        config();
        clearDB();
    }

    @Test
    public void publishAndConsume() {
        Producer p = new Producer(new Jedis(ip,port), "foo");
        Consumer c1 = new Consumer(new Jedis(ip,port), "subscriber-1", "foo");
        Consumer c2 = new Consumer(new Jedis(ip,port), "subscriber-2", "foo");
        Consumer c3 = new Consumer(new Jedis(ip,port), "subscriber-3", "foo");
        
        for(int i=0; i<10; i++)
            p.publish("hello world! "+i);
        
        assertEquals("hello world! 0", c1.consume());
        assertEquals("hello world! 1", c1.consume());
        assertEquals("hello world! 2", c1.consume());
        assertEquals("hello world! 2", c2.consume());
        assertEquals("hello world! 3", c2.consume());
        assertEquals("hello world! 2", c3.consume());
        assertEquals("hello world! 3", c3.consume());
        assertEquals("hello world! 4", c3.consume());
        assertEquals("hello world! 5", c3.consume());
        assertEquals("hello world! 4", c2.consume());
        assertEquals("hello world! 3", c1.consume());
    }
}