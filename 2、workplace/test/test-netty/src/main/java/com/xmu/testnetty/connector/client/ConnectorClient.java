/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-17
 *
 */
package com.xmu.testnetty.connector.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ConnectorClient {
    private static String HOST = "127.0.0.1";
    private static Integer PORT = 7878;

    private EventLoopGroup group = new NioEventLoopGroup();
    private Bootstrap clientBootstrap;

    public static void main(String[] args) throws Exception {
        ConnectorClient client = new ConnectorClient();
        client.connect(HOST, 8888);
    }

    public ConnectorClient() {
        clientBootstrap = new Bootstrap();
        clientBootstrap.group(group);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.handler(new ConnectorClientInitializer());
    }

    public void connect() {
        connect(HOST, PORT);
    }

    public void connect(String host, Integer port) {
        try {
            Channel ch = clientBootstrap.connect(host, port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    continue;
                }
                for (int i = 0; i < 3; i++) {
                    ch.writeAndFlush(line + "-" + i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
