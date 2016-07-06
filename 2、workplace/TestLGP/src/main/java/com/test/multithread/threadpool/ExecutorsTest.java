/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-6-13
 *
 */
package com.test.multithread.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsTest {

    /**
     * Test Executors.newCachedThreadPool()
     */
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        
        for(int t=1; t<15; t++) {
            try {
                executor.execute(new TesterThread(t));
            } catch (Exception e) {
                System.out.println("task" + t + " " + e.getMessage());
            }
        }
    }

}
