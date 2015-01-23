package com.xmu.lgp.tests;

import java.io.IOException;
import java.util.ResourceBundle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xmu.lgp.rmq.Callback;
import com.xmu.lgp.rmq.Consumer;
import com.xmu.lgp.rmq.Producer;

import redis.clients.jedis.Jedis;

public class ProducerTest extends Assert {
    
    private String ip = null;
    private int port = 0;
    
    private void config(){
        ResourceBundle bundle = ResourceBundle.getBundle("config");
        ip = bundle.getString("ip");
        port = Integer.parseInt(bundle.getString("port"));
    }
    
    @Before
    public void setUp() throws IOException {

        config();

        Jedis jedis = new Jedis(ip,port);
        jedis.flushAll();
        jedis.disconnect();
        jedis.close();
    }

    @Test
    public void publishAndConsume() {
        Producer p = new Producer(new Jedis(ip,port), "foo");
        Consumer c = new Consumer(new Jedis(ip,port), "a subscriber", "foo");

        p.publish("hello world!");
        assertEquals("hello world!", c.consume());
    }

    @Test
    public void publishAndRead() {
        Producer p = new Producer(new Jedis(ip,port), "foo");
        Consumer c = new Consumer(new Jedis(ip,port), "a subscriber", "foo");

        p.publish("hello world!");
        assertEquals("hello world!", c.read());
        assertEquals("hello world!", c.read());
    }

    @Test
    public void unreadMessages() {
        Producer p = new Producer(new Jedis(ip,port), "foo");
        Consumer c = new Consumer(new Jedis(ip,port), "a subscriber", "foo");

        assertEquals(0, c.unreadMessages());
        p.publish("hello world!");
        assertEquals(1, c.unreadMessages());
        p.publish("hello world!");
        assertEquals(2, c.unreadMessages());
        c.consume();
        assertEquals(1, c.unreadMessages());
    }

    @Test
    public void raceConditionsWhenPublishing() throws InterruptedException {
        Producer slow = new SlowProducer(new Jedis(ip,port), "foo");
        Consumer c = new Consumer(new Jedis(ip,port), "a subscriber", "foo");

        slow.publish("a");
        Thread t = new Thread(new Runnable() {
            public void run() {
                Producer fast = new Producer(new Jedis(ip,port), "foo");
                fast.publish("b");
            }
        });
        t.start();
        t.join();

        assertEquals("a", c.consume());
        assertEquals("b", c.consume());
    }

    @Test
    public void eraseOldMessages() {
        Producer p = new Producer(new Jedis(ip,port), "foo");
        Consumer c = new Consumer(new Jedis(ip,port), "a subscriber", "foo");

        p.publish("a");
        p.publish("b");

        assertEquals("a", c.consume());

        p.clean();

        Consumer nc = new Consumer(new Jedis(ip,port), "new subscriber",
                "foo");

        assertEquals("b", c.consume());
        assertEquals("b", nc.consume());
        assertNull(c.consume());
        assertNull(nc.consume());
    }

    class SlowProducer extends Producer {
        private long sleep;

        public SlowProducer(Jedis jedis, String topic) {
            this(jedis, topic, 500L);
        }

        public SlowProducer(Jedis jedis, String topic, long sleep) {
            super(jedis, topic);
            this.sleep = sleep;
        }

        protected Integer getNextMessageId() {
            Integer nextMessageId = super.getNextMessageId();
            sleep(sleep);
            return nextMessageId;
        }
    }

    class SlowConsumer extends Consumer {
        private long sleep;

        public SlowConsumer(Jedis jedis, String id, String topic) {
            this(jedis, id, topic, 500L);
        }

        public SlowConsumer(Jedis jedis, String id, String topic, long sleep) {
            super(jedis, id, topic);
            this.sleep = sleep;
        }

        @Override
        public String consume() {
            sleep(sleep);
            return super.consume();
        }

        @Override
        public void consume(Callback callback) {
            sleep(sleep);
            super.consume(callback);
        }
    }

    @Test
    public void expiredMessages() throws InterruptedException {
        Consumer c = new SlowConsumer(new Jedis(ip,port), "a consumer",
                "foo", 2000L);
        Producer p = new Producer(new Jedis(ip,port), "foo");
        p.publish("un mensaje", 1);
        assertNull(c.consume());
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
        }
    }

    @Test
    public void firstMessageExpired() throws InterruptedException {
        Consumer c = new SlowConsumer(new Jedis(ip,port), "a consumer",
                "foo", 2000L);
        Producer p = new Producer(new Jedis(ip,port), "foo");
        p.publish("1", 1);
        p.publish("2", 0);

        assertEquals("2", c.consume());
    }
}