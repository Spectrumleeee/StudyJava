package com.xmu.testnetty.connector.codec.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonProtocol extends AbstractProtocol {
    private static final Logger logger = LoggerFactory
            .getLogger(CommonProtocol.class);
            
    public CommonProtocol() {
        this(Charset.forName("UTF-8"));
    }

    public CommonProtocol(Charset charset) {
        super(charset);
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) {
        
        in.markReaderIndex();
        
        if(in.readableBytes() < 2){
            return;
        }
        
        short len = in.readShort();
        if(in.readableBytes() < len){
            in.resetReaderIndex();
            return;
        }
        
        try {
            out.add(new String(in.readBytes(len).array(), getCharset().name()));
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to encode bytes to String", e);
        }
    }

}
