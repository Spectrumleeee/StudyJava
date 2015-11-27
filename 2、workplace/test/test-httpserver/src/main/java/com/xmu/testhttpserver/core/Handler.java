/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-27
 *
 */
package com.xmu.testhttpserver.core;

public interface Handler {
    
    public void service(Request request, Response response);

    public void doGet(Request request, Response response);

    public void doPost(Request request, Response response);

}

