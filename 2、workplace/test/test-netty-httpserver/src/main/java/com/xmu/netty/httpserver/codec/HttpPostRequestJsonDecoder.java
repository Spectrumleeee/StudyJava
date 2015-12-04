/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-12-4
 *
 */
package com.xmu.netty.httpserver.codec;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;

public class HttpPostRequestJsonDecoder implements
        InterfaceHttpPostRequestDecoder {

    // 10MB
    private static final int DEFAULT_DISCARD_THRESHOLD = 10 * 1024 * 1024;

    private HttpRequest request;

    private boolean isLastChunk;

    private ByteBuf undecodedChunk;

    private byte[] requestBytes;

    private int discardThreshold = DEFAULT_DISCARD_THRESHOLD;

    public HttpPostRequestJsonDecoder(HttpRequest request) {
        this.request = request;
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public void setDiscardThreshold(int discardThreshold) {

    }

    @Override
    public int getDiscardThreshold() {
        return 0;
    }

    @Override
    public List<InterfaceHttpData> getBodyHttpDatas() {
        return null;
    }

    @Override
    public List<InterfaceHttpData> getBodyHttpDatas(String name) {
        return null;
    }

    @Override
    public InterfaceHttpData getBodyHttpData(String name) {
        return null;
    }

    @Override
    public InterfaceHttpPostRequestDecoder offer(HttpContent content) {

        // Maybe we should better not copy here for performance reasons but this
        // will need
        // more care by the caller to release the content in a correct manner
        // later
        // So maybe something to optimize on a later stage
        ByteBuf buf = content.content();
        if (undecodedChunk == null) {
            undecodedChunk = buf.copy();
        } else {
            undecodedChunk.writeBytes(buf);
        }
        if (content instanceof LastHttpContent) {
            isLastChunk = true;
        }
        parseBody();
        if (undecodedChunk != null
                && undecodedChunk.writerIndex() > discardThreshold) {
            undecodedChunk.discardReadBytes();
        }
        return this;
    }

    private void parseBody() {
        if (isLastChunk) {
            int readableBytes = undecodedChunk.readableBytes();
            System.out.println("readableBytes:" + readableBytes);
            byte[] bodyBytes = new byte[readableBytes];
            undecodedChunk.readBytes(bodyBytes);

            JSONObject jsonObj = new JSONObject(new String(bodyBytes));

            // 172.29.88.113 ~/home/cloud/liguangpu/test/test.json
            for (int i = 0; i < 1000; i++) {
                System.out.println("key: id-" + i + " value: "
                        + jsonObj.getString("id-" + i));
            }
        }
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public InterfaceHttpData next() {
        return null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void cleanFiles() {

    }

    @Override
    public void removeHttpDataFromClean(InterfaceHttpData data) {

    }

    public static void main(String[] args) throws IOException {
        JSONObject json = new JSONObject();
        for (int i = 0; i < 1000; i++) {
            json.put("id-" + i, "82934123414392310231" + i);
        }

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File("D:/test.json")));

        bos.write(json.toString().getBytes());

        bos.close();

    }

}
