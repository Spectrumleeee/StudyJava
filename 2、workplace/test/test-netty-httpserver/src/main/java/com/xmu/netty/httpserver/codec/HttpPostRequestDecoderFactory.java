/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-12-4
 *
 */
package com.xmu.netty.httpserver.codec;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;

public class HttpPostRequestDecoderFactory {

    public static InterfaceHttpPostRequestDecoder getDecoder(
            HttpRequest request, PostType postType) {
        InterfaceHttpPostRequestDecoder postDecoder = null;
        switch (postType) {
        case FORM:
            postDecoder = new HttpPostRequestDecoder(request);
            break;
        case JSON:
            postDecoder = new HttpPostRequestJsonDecoder(request);
            break;
        default:
            postDecoder = null;
            break;
        }

        return postDecoder;
    }
}
