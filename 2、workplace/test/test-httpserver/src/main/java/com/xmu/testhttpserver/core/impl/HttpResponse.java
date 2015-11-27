/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-27
 *
 */
package com.xmu.testhttpserver.core.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.xmu.testhttpserver.core.Response;

public class HttpResponse implements Response {
    private HttpExchange httpExchange;

    public HttpResponse(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    @Override
    public void write(String result) {
        try {
            // 设置响应头属性及响应信息的长度
            httpExchange.sendResponseHeaders(200, result.length());
            // 获得输出流
            OutputStream out = httpExchange.getResponseBody();
            out.write(result.getBytes());
            out.flush();
            httpExchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
