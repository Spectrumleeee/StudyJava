/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-6-13
 *
 */
package com.test.multithread.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {

    /**
     * Test ThreadPoolExecutor()
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int corePoolSize = 1;
        int maximumPoolSize = 2;
        int keepAliveTime = 10;
        // BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        RejectedExecutionHandler handler2 = new ThreadPoolExecutor.CallerRunsPolicy();
        RejectedExecutionHandler handler3 = new ThreadPoolExecutor.DiscardPolicy();
        RejectedExecutionHandler handler4 = new ThreadPoolExecutor.DiscardOldestPolicy();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, workQueue, threadFactory, handler);

        for (int j = 1; j < 15; j++) {
            try {
                threadPoolExecutor.execute(new TesterThread(j));
            } catch (Exception e) {
                System.out.println("task" + j + " " + e.getMessage());
            }
        }

        System.out.println(threadPoolExecutor);

    }
}


