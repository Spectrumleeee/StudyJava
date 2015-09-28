/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-4-17

 * updated: 2015-4-17
 * wenyong <liguangpu@tp-link.net>
 * Reason: 
 */
package com.test.container;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainTest {
    private static final long DEFAULT_KEEP_ALIVE_TIME = 60;
    private LinkedBlockingQueue<String> queue;
    private ThreadPoolExecutor executor;

    public MainTest() {
        queue = new LinkedBlockingQueue<String>();
        executor = new ThreadPoolExecutor(3, 3, DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public static void main(String[] args) throws InterruptedException {
        MainTest mt = new MainTest();
        mt.addTask(mt.new Task("producer"));
        mt.addTask(mt.new Task("consumer"));
        while (true) {
            Thread.sleep(10000);
        }
    }

    public void addTask(Task task) {
        executor.execute(task);
    }

    public int size() {
        return queue.size();
    }

    class Task implements Runnable {
        private String identify;

        public Task(String identify) {
            this.identify = identify;
        }

        @Override
        public void run() {
            switch (this.identify) {
            case "producer":
                for (int i = 0; i < 10100; i++) {
                    String product = this.identify + " - "
                            + (int) (Math.random() * 100);
                    queue.add(product);
                    System.out.println("producer : " + product);
                }
                break;
            case "consumer":
                while (true) {
                    String product = queue.poll();
                    if(product != null)
                        System.out.println("consumer : " + product);
                }
            default:

            }
        }
    }
}
