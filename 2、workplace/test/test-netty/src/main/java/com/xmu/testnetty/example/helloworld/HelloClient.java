/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-17
 *
 */
package com.xmu.testnetty.example.helloworld;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HelloClient {
    private static String HOST = "127.0.0.1";
    private static Integer PORT = 7878;

    private EventLoopGroup group = new NioEventLoopGroup();
    private Bootstrap clientBootstrap;

    public static void main(String[] args) throws Exception {
        HelloClient client = new HelloClient();
        client.connect(HOST, 8888);
    }
    
    public HelloClient() {
        clientBootstrap = new Bootstrap();
        clientBootstrap.group(group);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.handler(new HelloClientInitializer());
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

                ch.writeAndFlush(line + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
