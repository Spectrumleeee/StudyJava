/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-7-14
 */
package com.test.dubbo;

public interface IService {
    // All rpc service base interface.
    // input a request and output a response
    // see Request & Response class for their detailed defination.
    public abstract String invoke(String request);
}
