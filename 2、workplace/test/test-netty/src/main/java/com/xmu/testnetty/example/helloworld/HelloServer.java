/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-17
 *
 */
package com.xmu.testnetty.example.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
    private static final int PORT = 7878;
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public static void main(String[] args) {
        HelloServer server = new HelloServer();
        server.startServer(8888);
    }
    
    public HelloServer() {
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HelloServerInitializer());
    }

    public void startServer() {
        startServer(PORT);
    }
    
    public void startServer(Integer port){
        try {
            ChannelFuture f = serverBootstrap.bind(port).sync();
            System.out.println("Server started...");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
