/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-27
 *
 */
package com.xmu.testhttpserver.core;

import java.net.URI;

public interface Request {
    public final static String GET = "GET";
    public final static String POST = "POST";

    public String getParamter(String param);

    public String getMethod();

    public URI getReuestURI();

    public void initRequestHeader();
    
    public void initRequestParam();

    public void initRequestBody();

    public String getRequestBody();
    
}

