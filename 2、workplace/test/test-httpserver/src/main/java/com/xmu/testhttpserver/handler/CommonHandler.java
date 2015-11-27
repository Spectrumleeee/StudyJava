/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-27
 *
 */
package com.xmu.testhttpserver.handler;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.xmu.testhttpserver.context.Context;
import com.xmu.testhttpserver.core.Handler;
import com.xmu.testhttpserver.core.impl.HttpRequest;
import com.xmu.testhttpserver.core.impl.HttpResponse;

public class CommonHandler implements HttpHandler{
    
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest request = new HttpRequest(httpExchange);  
        HttpResponse response = new HttpResponse(httpExchange);  
        Handler handler = Context.getHandler(request.getReuestURI().getPath());
        handler.service(request, response);  
    } 
    
}
