/**
 * TestComments.java
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Oct 24, 2014
 */

package com.test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestComments {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        abstractPollingProcessor app = new abstractPollingProcessor();
        app.startup();
    }

    public static void pr(String s) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement ss : stackTrace) {
            System.out.println(ss);
        }
        System.out.println(s);
    }
}

class PollingProcessor extends Polling{  
}

abstract class Polling{
    public void pr(String s){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement ss : stackTrace) {
            System.out.println(ss);
        }
        System.out.println(s);
    };
}

class abstractPollingProcessor {
    private Executor executor;
    public abstractPollingProcessor(){
        this.executor = Executors.newCachedThreadPool();
    }
    
    void startup(){
        executor.execute(new abc());
    }
}

class abc implements Runnable{
    @Override
    public void run() {
        PollingProcessor pp = new PollingProcessor();
        pp.pr("dfewsdfwes");
    }
    
}

