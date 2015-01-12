/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.xmu.cs.lgp.redis.cluster.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.cs.lgp.redis.cluster.handler.ServerMessageHandler;
import com.xmu.cs.lgp.redis.cluster.tools.RedisClusterProxy;

/**
 * MonitorServer.java
 *
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: Jan 9, 2015
 */
public class MonitorServer {

    private static final int PORT = 9999;
    private static Logger logger = LoggerFactory.getLogger(MonitorServer.class);
    private IoAcceptor acceptor;
    
    public MonitorServer(){
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset
                        .forName("UTF-8"))));

        acceptor.setHandler(new ServerMessageHandler(new RedisClusterProxy()));

        acceptor.getSessionConfig().setReadBufferSize(2048);
        // every 10 seconds idle will execute method sessionIdle()
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);
    }
    
    public void start(){
        try {
            acceptor.bind(new InetSocketAddress(PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("server started! ");
    }
    
    public static void main(String[] args) {
        MonitorServer server = new MonitorServer();
        server.start();
    }

}