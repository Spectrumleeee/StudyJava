package com.xmu.testnetty.connector.codec;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.testnetty.connector.codec.protocol.CommonProtocol;
import com.xmu.testnetty.connector.codec.protocol.DataCollectorProtocol;
import com.xmu.testnetty.connector.codec.protocol.ElbProxyProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class MessageCodec extends ByteToMessageCodec<String> {
    private static Logger logger = LoggerFactory.getLogger(MessageCodec.class);
    
    private static final int MAGIC_HEADER_LEN = 2;
    
    private static final short HEADER = (short) 0xA1B2;
    
    private static final short COMMMON_MAGIC_HEADER = (short) 0xA1B2;
    
    private static final short DATA_COLLECTOR_HEADER = (short) 0xA1B3;
    
    private final CommonProtocol commonDecoder;
    
    private final DataCollectorProtocol dataColletorDecoder;
    
    private final ElbProxyProtocol elbProxyDecoder;
    
    private final Charset charset;
    
    public MessageCodec(){
        this(Charset.forName("UTF-8"));
    }
    
    public MessageCodec(final Charset charset){
        this.charset = charset;
        commonDecoder = new CommonProtocol(charset);
        dataColletorDecoder = new DataCollectorProtocol(charset);
        elbProxyDecoder = new ElbProxyProtocol(charset);
    }
    
    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in,
            List<Object> out) {
        // Mark start index.
        in.markReaderIndex();
        
        // Read length buffer.
        if (in.readableBytes() < MAGIC_HEADER_LEN) {
            return;
        }
        try {
            short magicHeader = in.readShort();
            if (magicHeader == COMMMON_MAGIC_HEADER) {
                commonDecoder.decode(context, in, out);
            } else if (magicHeader == DATA_COLLECTOR_HEADER) {
                dataColletorDecoder.decode(context, in, out);
            } else {
                in.resetReaderIndex();
                elbProxyDecoder.decode(context, in, out);
            }
        } catch (Exception e) {
            logger.error("Failed to decode message !");
        }
    }
    
    @Override
    protected void encode(ChannelHandlerContext arg0, String message, ByteBuf out)
            throws Exception {
        String data = (message != null) ? (message.toString()) : "";
        byte[] byteData = data.getBytes(charset);
        out.writeShort(HEADER);
        out.writeShort((short)byteData.length);
        out.writeBytes(byteData);
    }
}
