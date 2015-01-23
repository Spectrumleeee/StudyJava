/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 6, 2014
 */

package com.xmu.cs.lgp.redis.cluster.handler;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageHandler extends IoHandlerAdapter {
    
    private Logger logger = LoggerFactory.getLogger(ClientMessageHandler.class);
    
    public void releaseSession(IoSession session) throws Exception {
//        logger.info("releaseSession");
        if (session.isConnected()) {
            session.close(true);
        }
    }

    public void sessionOpened(IoSession session) throws Exception {
//        logger.info("sessionOpened");
    }

    public void sessionClosed(IoSession session) throws Exception {
//        logger.info("sessionClosed");
    }

    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
//        logger.info("sessionIdle");
        try {
            releaseSession(session);
        } catch (RuntimeIoException e) {
        }
    }

    public void messageReceived(IoSession session, Object message)
            throws Exception {
//        logger.info("Receive message [ " + message + " ] from Server");
        super.messageReceived(session, message);
    }

    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        logger.info("exceptionCaught");
        cause.printStackTrace();
    }

    public void messageSent(IoSession session, Object message) throws Exception {
//        logger.info("messageSent");
        super.messageSent(session, message);
    }
}
