package com.xmu.netty.httpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.lgp.common.utils.ConfigUtil;

public class HttpServer {
    private static final Logger logger = LoggerFactory
            .getLogger(HttpServer.class);
    private static final boolean SSL = false;
    private static final int PORT = 8080;

    private ServerBootstrap server;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private SslContext sslCtx;
    private boolean ssl;
    private int port;

    public HttpServer() {
        ssl = ConfigUtil.getBoolean("ssl", SSL);
        port = ConfigUtil.getInt("port", PORT);

        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();


        try {
            if (ssl) {
                SelfSignedCertificate ssc;
                ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(),
                        ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }
        } catch (Exception e) {
            logger.error("Failed to init ssl cert !", e);
            sslCtx = null;
        }

        server = new ServerBootstrap();
        server.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer(sslCtx));
    }

    public void start() {
        try {
            Channel ch = server.bind(port).sync().channel();
            logger.info("Server started in port: ", port);
            System.out.println("Server started in port: " + port);
            ch.closeFuture().sync();
        } catch (Exception e) {
            logger.error("Failed to start server !", e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpServer().start();
    }
}
