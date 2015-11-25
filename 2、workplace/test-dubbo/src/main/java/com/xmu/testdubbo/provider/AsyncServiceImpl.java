/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-19
 *
 */
package com.xmu.testdubbo.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.RpcContext;
import com.xmu.testdubbo.service.AsyncService;

public class AsyncServiceImpl implements AsyncService {
    private static final Logger logger = LoggerFactory
            .getLogger(AsyncServiceImpl.class);

    /* 
     * dubbo似乎没有获取与消费者连接channel的接口,所以这种方式只能同步处理
     * 如果需要异步处理再返回结果，可以考虑CallbackService实现方式。
     */
    @Override
    public String findResponse() {
        String clientAddr = RpcContext.getContext().getRemoteAddressString();
        logger.debug("receive request from client: " + clientAddr);
        try {
            logger.debug("Server will sleep 5000 ms");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.debug("send response to client: " + clientAddr);
        return "Response from server";
    }
}
