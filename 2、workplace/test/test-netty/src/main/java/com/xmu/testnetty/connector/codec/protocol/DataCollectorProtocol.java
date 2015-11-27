package com.xmu.testnetty.connector.codec.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decode data collection message into CollectionData object. The layout of data
 * collection message is like:
 * 
 *     header json_length json_data binary_length binary_data
 * 
 * header: short
 * json_length: short
 * binary_length: integer
 */
public class DataCollectorProtocol extends AbstractProtocol {
    private static final Logger logger = LoggerFactory
            .getLogger(DataCollectorProtocol.class);

    public DataCollectorProtocol() {
        this(Charset.forName("UTF-8"));
    }
    
    public DataCollectorProtocol(Charset charset) {
        super(charset);
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        logger.debug("Decoded the datacollector data");
    }
    
}
