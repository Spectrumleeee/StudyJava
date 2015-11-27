package com.xmu.testnetty.connector.codec.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

import com.xmu.testnetty.connector.exception.IllegalMessageException;

/**
 * In aws, we use elb for loading balance. Elb will send a elb message at first.
 * Get real ip from elb message by ElbMessageDecoder. Elb Message is a string
 * contains device's address. For example:
 * 
 *     "PROXY TCP4 111.111.111.111 222.222.222.222 3333 4444\r\n"
 * 
 * 111.111.111.111 is the real ip for device. The port is 3333.
 * 222.222.222.222 is the ip of elb's node. The port is 4444. 
 */
public class ElbProxyProtocol extends AbstractProtocol {
    private static final int MIN_ELB_PROXY_LEN = "PROXY TCP4 "
            .getBytes().length;

    public ElbProxyProtocol() {
        this(Charset.forName("UTF-8"));
    }
    
    public ElbProxyProtocol(Charset charset) {
        super(charset);
    }
    
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        if (in.readableBytes() < MIN_ELB_PROXY_LEN) {
            return;
        }
        
        in.markReaderIndex();
        int start = in.readerIndex();
        byte previous = 0, current = 0;

        while (in.isReadable()) {
            current = in.readByte();
            if (previous == 0x0d && current == 0x0a) {
                byte[] data = new byte[in.readerIndex() - start];
                in.resetReaderIndex();
                in.readBytes(data);
                out.add(getAddress(data));
                return;
            }
            previous = current;
        }
        return;
    }
    
    private InetSocketAddress getAddress(byte[] data) throws Exception {
        String address = new String(data, 0, data.length, getCharset()
                .name());
        String[] parts = address.split(" ");
        if (parts.length != 6) {
            throw new IllegalMessageException("illegal elb message parts");
        }
        if (!parts[0].equals("PROXY") && !parts[1].equals("TCP4")) {
            throw new IllegalMessageException("illegal elb message header");
        }
        try {
            return new InetSocketAddress(parts[2], Integer.parseInt(parts[4]));
        } catch (NumberFormatException e) {
            throw new IllegalMessageException("illegal elb port", e);
        }
    }

}
