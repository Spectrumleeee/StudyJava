package com.test.multithread.blockingqueue;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueTest {

    public static void main(String[] args) {
        SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();
        new Customer(queue).start();
        new Producer(queue).start();
    }

    static class Producer extends Thread {
        SynchronousQueue<Integer> queue;
        public Producer(SynchronousQueue<Integer> queue) {
            this.queue = queue;
        }
        
        @Override
        public void run() {
            int product = 0;
            while(true) {
                System.out.println("生产了一个产品：" + product);
                System.out.println("等待2秒后运送出去...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                queue.offer(product++);
            }
        }
    }
    
    static class Customer extends Thread {
        SynchronousQueue<Integer> queue;
        public Customer(SynchronousQueue<Integer> queue) {
            this.queue = queue;
        }
        
        @Override
        public void run() {
            while(true) {
                try {
                    System.out.println("消费了一个产品:" + queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("------------------------------------------"); 
            }
        }
    }
}
