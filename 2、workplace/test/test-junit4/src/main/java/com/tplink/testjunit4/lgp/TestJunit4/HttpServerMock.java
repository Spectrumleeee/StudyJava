/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 18, 2014
 */

package com.tplink.testjunit4.lgp.TestJunit4;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

public class HttpServerMock {
    public final static int DEFAULT_PORT = 8080;
    public final static String DEFAULT_CONTENT_TYPE = "application/json";
    public final static int DEFAULT_STATUS_CODE = 200;

    private Server _httpServer;
    private int _port;

    public HttpServerMock() {
        _port = DEFAULT_PORT;
    }

    public HttpServerMock(int port) {
        _port = port;
    }

    /**
     * 启动Jetty服务器。默认的响应status code为"200"，content type为"application/json"。
     * @param content 响应内容
     */
    public void start(String content) throws Exception {
        start(content, DEFAULT_CONTENT_TYPE, DEFAULT_STATUS_CODE);
    }

    /**
     * 启动Jetty服务器。默认的响应status code为"200"。
     * @param content 响应内容
     * @param contentType 响应内容的MIME类型
     */
    public void start(String content, String contentType) throws Exception {
        start(content, contentType, DEFAULT_STATUS_CODE);
    }

    /**
     * 启动Jetty服务器。
     * @param content 响应内容
     * @param contentType 响应内容的MIME类型
     * @param statuCode 响应状态码
     */
    public void start(String content, String contentType, 
            int statuCode) throws Exception {
         
        _httpServer = new Server(_port);
  
        ContextHandler context = new ContextHandler();
        context.setContextPath("/hello");
        context.setResourceBase(".");
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        _httpServer.setHandler(context);
//        _httpServer.setHandler((Handler) new HelloHandler());
        _httpServer.setHandler(createHandler(content, contentType, statuCode));
        _httpServer.start();
        
    }

    /**
     * 停止Jetty服务器。
     */
    public void stop() throws Exception {
        if (null != _httpServer) {
            _httpServer.stop();
            _httpServer = null;
        }
    }

    class HelloHandler extends AbstractHandler {  

        /* (non-Javadoc)
         * @see org.eclipse.jetty.server.Handler#handle(java.lang.String, org.eclipse.jetty.server.Request, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
        public HelloHandler(){
            
        }
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest arg2,
                HttpServletResponse response) throws IOException, ServletException {
            // TODO Auto-generated method stub
            response.setContentType("text/html;charset=utf-8");  
            response.setStatus(HttpServletResponse.SC_OK);  
            baseRequest.setHandled(true);
            response.setStatus(404);
            response.getWriter().println("<h1>Hello World</h1>");  
            response.getWriter().println("Request url:"+target);  
        }   
    }
    
    private Handler createHandler(final String content, final String contentType, 
            final int statusCode) {
        return new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.setContentType(contentType);
                response.setStatus(statusCode);
                baseRequest.setHandled(true);
                response.getWriter().print(content);
            }
        };
    }
}
