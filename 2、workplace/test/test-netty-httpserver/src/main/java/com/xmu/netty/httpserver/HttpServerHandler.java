/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-12-1
 *
 */
package com.xmu.netty.httpserver;

import com.xmu.netty.httpserver.codec.HttpPostRequestDecoderFactory;
import com.xmu.netty.httpserver.codec.PostType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.util.CharsetUtil;

import static io.netty.buffer.Unpooled.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private HttpRequest request;
    private InterfaceHttpPostRequestDecoder decoder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
            throws Exception {
        // System.out.println("1");
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            // System.out.println(request.getUri());
            // System.out.println(request.getMethod());

            if (request.getMethod().equals(HttpMethod.GET)) {
                return;
            }

            // create a HTTP POST request decoder
            try {
                decoder = HttpPostRequestDecoderFactory.getDecoder(request,
                        PostType.JSON);
            } catch (Exception e) {
                ctx.channel().close();
                return;
            }
        }

        if (decoder != null) {
            if (msg instanceof HttpContent) {
                HttpContent chunk = (HttpContent) msg;

                try {
                    decoder.offer(chunk);
                } catch (Exception e) {
                    ctx.channel().close();
                    return;
                }

                if (chunk instanceof LastHttpContent) {
                    reset();
                    writeResponse(ctx.channel());
                    return;
                }
            }
        } else {
            writeResponse(ctx.channel());
        }

    }

    private void reset() {
        decoder.destroy();
        decoder = null;
        request = null;
    }

    private void writeResponse(Channel channel) {
        ByteBuf buf = copiedBuffer("Received request!\n", CharsetUtil.UTF_8);

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, buf.readableBytes());

        channel.writeAndFlush(response);
    }

}
