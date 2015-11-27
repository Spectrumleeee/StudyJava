/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-27
 *
 */
package com.xmu.testhttpserver.collector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import com.xmu.testhttpserver.context.Context;
import com.xmu.testhttpserver.handler.CommonHandler;

public class DataCollector {

    public void start() throws IOException{
        start(8080);
    }
    
    // 启动服务，监听来自客户端的请求
    public void start(int port) throws IOException {
        Context.load();

        HttpServerProvider provider = HttpServerProvider.provider();
        // 监听端口8080,能同时接 受1000个请求
        HttpServer httpserver = provider.createHttpServer(
                new InetSocketAddress(port), 1000);
        httpserver.createContext(Context.contextPath, new CommonHandler());
        httpserver.setExecutor(Executors.newFixedThreadPool(100));
        System.out.println("Start server... ");
        httpserver.start();
        System.out.println("Server started !");
    }

    public static void main(String[] args) throws IOException {
        try{
            new DataCollector().start();
        }catch(Exception e){
            System.out.println("Failed to start server !");
        }
    }
}
