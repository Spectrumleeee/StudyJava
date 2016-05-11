/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-29
 *
 */
package com.xmu.lgp.entity;

import java.util.List;

public class Request {
    private String method;
    private List<Param> params;
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public List<Param> getParams() {
        return params;
    }
    
    public void setParams(List<Param> params) {
        this.params = params;
    }
    
}
