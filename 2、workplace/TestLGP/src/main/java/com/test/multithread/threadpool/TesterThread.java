/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-6-13
 *
 */
package com.test.multithread.threadpool;

import java.util.concurrent.TimeUnit;

class TesterThread implements Runnable {
    private int i;
    
    public TesterThread(int i) {
        this.i = i;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("task " + i + " " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}