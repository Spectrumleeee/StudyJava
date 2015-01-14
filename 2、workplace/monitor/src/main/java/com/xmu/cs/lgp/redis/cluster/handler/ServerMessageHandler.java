/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 6, 2014
 */

package com.xmu.cs.lgp.redis.cluster.handler;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.cs.lgp.redis.cluster.executor.CommandExecutor;
import com.xmu.cs.lgp.redis.cluster.process.parser.MessageParser;
import com.xmu.cs.lgp.redis.cluster.tools.RedisClusterProxy;

public class ServerMessageHandler extends IoHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(ServerMessageHandler.class);
    private static int count = 1;
    private RedisClusterProxy redisClusterProxy;
    
    public ServerMessageHandler(RedisClusterProxy redisClusterProxy) {
        this.redisClusterProxy = redisClusterProxy;
    }
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        logger.info("session closed " + count++);
//        cause.printStackTrace();
    }
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        logger.info("receive a message from client : " + session.getRemoteAddress());
        JSONObject jsonobj;
        String str = message.toString();
        MessageParser messageParser = new MessageParser();
        CommandExecutor ce = messageParser.parseMessage(str);
        if(ce != null){
            jsonobj = redisClusterProxy.execute(ce);
        }else{
            jsonobj = new JSONObject();
            Date date = new Date();
            jsonobj.put("OK", str + " " + date.toString());
        }
        session.write(jsonobj);
        logger.info("Message written...");
    }
    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        logger.info("IDLE " + session.getIdleCount(status));
    }
}
