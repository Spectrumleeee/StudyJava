/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-27
 *
 */
package com.xmu.testhttpserver.handler;

import com.xmu.testhttpserver.core.Request;
import com.xmu.testhttpserver.core.Response;
import com.xmu.testhttpserver.core.impl.HttpHandler;

public class FirstHandler extends HttpHandler{

    @Override
    public void doGet(Request request, Response response) {
        System.out.println("doGet");
        
        System.out.println(request.getParamter("aaa"));
        System.out.println(request.getParamter("bbb"));
        
        response.write("helloWorld.....get\n");
    }

    @Override
    public void doPost(Request request, Response response) {
//        System.out.println("doPost");
//        System.out.println(request.getRequestBody());
        System.out.println(Thread.currentThread().getName());
        
//        response.write("helloWorld.....post\n");
        response.write("");
    }

}

