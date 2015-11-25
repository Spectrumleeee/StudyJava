/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-17
 *
 */
package com.xmu.testdubbo.provider;

import com.xmu.testdubbo.service.HelloService;

public class HelloImpl implements HelloService {

    @Override
    public String sayHello(String request) {
        String response = "";
        switch(request){
        case "hello":
            response = doHelloResponse();
            break;
        case "world":
            response = doWorldResponse();
            break;
        default:
            response = doNothingResponse();
            break;
        }
        return response;
    }
    
    public String doHelloResponse(){
        // do sleep
        sleep(5000);
        return "world";
    }
    
    public String doWorldResponse(){
        // do sleep
        return "hello";
    }
    
    public String doNothingResponse(){
        return "do nothing";
    }
    
    public void sleep(long mils) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
