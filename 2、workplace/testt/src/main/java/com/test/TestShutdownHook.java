/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 30, 2014
 */

package com.test;

public class TestShutdownHook {
    
    static{
        Runtime.getRuntime().addShutdownHook(new Thread(){
                public void run(){
                    System.out.println("exit!");
                }
        });
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Thread thread = new MyThread();
        thread.start();
    }
    
    static class MyThread extends Thread{
        
        private int i = 3;
        
        public MyThread(){
        }
        
        public void run(){
            while(i > 0){
                i--;
                System.out.println("running...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
