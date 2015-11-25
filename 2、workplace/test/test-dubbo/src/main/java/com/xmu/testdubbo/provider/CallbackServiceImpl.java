/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-17
 *
 */
package com.xmu.testdubbo.provider;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmu.testdubbo.callback.CallbackListener;
import com.xmu.testdubbo.service.CallbackService;

/*
 * Server can process the client request asynchronously by
 * using callback feature of method in dubbo.
 */
public class CallbackServiceImpl implements CallbackService {
    private static final Logger logger = LoggerFactory
            .getLogger(CallbackServiceImpl.class);

    @Override
    public void sayCallbackService(String request, CallbackListener listener) {
        if (logger.isDebugEnabled()) {
            logger.debug("recv req: {}",request);
        }

        switch (request) {
        case "hello":
            // process the request synchronously
            listener.getResponse("hello, server received your request !");
            logger.debug("send resp of req: {}",request);
            break;
        case "time":
            // process the request synchronously
            listener.getResponse("current time is :" + new Date());
            logger.debug("send resp of req: {}",request);
            break;
        default:
            // process the request asynchronously
            new DoDefaultThread(request, listener).start();
            break;
        }
    }

    private void sleepMills(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class DoDefaultThread extends Thread {
        private String request;
        private CallbackListener listener;

        public DoDefaultThread(String request, CallbackListener listener) {
            this.request = request;
            this.listener = listener;
        }

        public void run() {
            sleepMills(10000);
            logger.debug("send resp of req: {}",request);
            listener.getResponse("Default resp of req " + request
                    + " from Server after 10s");
        }
    }
}
