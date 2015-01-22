package com.xmu.lgp.benchmarks;

import java.io.IOException;
import java.util.Calendar;
import java.util.ResourceBundle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xmu.lgp.rmq.Consumer;
import com.xmu.lgp.rmq.Producer;

import redis.clients.jedis.Jedis;

public class PublishConsumeBenchmarkTest extends Assert {
    
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
    public void publish() {
        final String topic = "foo";
        final String message = "hello world!";
        final int MESSAGES = 10000;
        Producer p = new Producer(new Jedis(ip,port), topic);

        long start = Calendar.getInstance().getTimeInMillis();
        for (int n = 0; n < MESSAGES; n++) {
            p.publish(message);
        }
        long elapsed = Calendar.getInstance().getTimeInMillis() - start;
        System.out.println(((1000 * MESSAGES) / elapsed) + " ops");
    }
    
    @Test
    public void consume() {
        final String topic = "foo";
        final String message = "hello world!";
        final int MESSAGES = 10000;
        Producer p = new Producer(new Jedis(ip,port), topic);
        Consumer c = new Consumer(new Jedis(ip,port), "consumer", topic);
        for (int n = 0; n < MESSAGES; n++) {
            p.publish(message);
        }
        
        long start = Calendar.getInstance().getTimeInMillis();
        String m = null;
        do {
            m = c.consume();
        } while (m != null);
        long elapsed = Calendar.getInstance().getTimeInMillis() - start;
        
        System.out.println(((1000 * MESSAGES) / elapsed) + " ops");
    }
}