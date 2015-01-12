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

import java.net.ConnectException;
import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.cs.lgp.redis.cluster.handler.ClientMessageHandler;

/**
 * MonitorClient.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd. Author: liguangpu
 * <liguangpu@tp-link.net> Created: Jan 9, 2015
 */

public class MonitorClient {

    private static Logger logger = LoggerFactory.getLogger(MonitorClient.class);

    private static final int DEFAULT_CONNECT_TIMEOUT = 10;
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private SocketConnector socketConnector;
    private SocketSessionConfig sessionConfig;
    private InetSocketAddress serverAddr;
    private IoSession session;

    public MonitorClient() {
        init();
    }

    public void init() {

        socketConnector = new NioSocketConnector();
        serverAddr = new InetSocketAddress(HOST, PORT);
        sessionConfig = socketConnector.getSessionConfig();

        sessionConfig.setBothIdleTime(DEFAULT_CONNECT_TIMEOUT);
        // close the tcp connection without step into TIME_WAIT
        sessionConfig.setSoLinger(0);
        socketConnector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory()));

        ClientMessageHandler ioHandler = new ClientMessageHandler();
        socketConnector.setHandler(ioHandler);
    }

    public void setServerAddress(String host, int port) {
        this.serverAddr = new InetSocketAddress(host, port);
    }

    public void sendMessage(final String msg) {

        if (null == session) {
            ConnectFuture cf = socketConnector.connect(serverAddr);
            cf.awaitUninterruptibly();
            session = cf.getSession();
        }
        try {
            session.write(msg);
            
            logger.info("Send message: [ " + msg + " ]");
        } catch (RuntimeIoException e) {
            if (e.getCause() instanceof ConnectException) {
                if(session != null){
                    session.close(true);
                    session = null;
                }
            }
        }
    }
    public static void mySleep(int secs){
        try {
            Thread.sleep(secs*1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        MonitorClient client = new MonitorClient();
        MonitorClient client1 = new MonitorClient();
        MonitorClient client2 = new MonitorClient();
        client.setServerAddress("localhost", 9999);

        client.sendMessage("getSlotsInfo");

        client1.sendMessage("getMemoryInfo");
        
        client2.sendMessage("Welcome to Mina");

        mySleep(2);
        System.exit(0);
    }
}
