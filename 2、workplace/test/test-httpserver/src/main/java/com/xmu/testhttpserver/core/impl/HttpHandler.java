/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-27
 *
 */
package com.xmu.testhttpserver.core.impl;

import com.xmu.testhttpserver.core.Handler;
import com.xmu.testhttpserver.core.Request;
import com.xmu.testhttpserver.core.Response;

public abstract class HttpHandler implements Handler {

    @Override
    public void service(Request request, Response response) {
        request.initRequestHeader();
        request.initRequestParam();
        if(request.getMethod().equals(Request.GET)){
            doGet(request,response);
        }else if(request.getMethod().equals(Request.POST)){
            request.initRequestBody();
            doPost(request,response);
        }
    }

    @Override
    public abstract void doGet(Request request, Response response);

    @Override
    public abstract void doPost(Request request, Response response);

    
}

