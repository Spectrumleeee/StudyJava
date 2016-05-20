/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-29
 *
 */
package com.xmu.lgp.tpcsb.parser.entity;

import java.util.List;

public class XmlRequest {
    private String method;
    private List<XmlParam> params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<XmlParam> getParams() {
        return params;
    }

    public void setParams(List<XmlParam> params) {
        this.params = params;
    }
}
