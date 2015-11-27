package com.xmu.testnetty.connector.codec.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.Charset;
import java.util.List;

public abstract class AbstractProtocol {
    
    private final Charset charset;
    
    protected AbstractProtocol(Charset charset) {
        this.charset = charset;
    }
    
    protected Charset getCharset() {
        return this.charset;
    }
    
    public abstract void decode(ChannelHandlerContext ctx, ByteBuf in,
                        List<Object> out) throws Exception; 
}
