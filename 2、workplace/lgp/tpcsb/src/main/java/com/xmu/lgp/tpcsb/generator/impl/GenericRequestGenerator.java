/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb.generator.impl;

import org.json.JSONObject;

import com.tplink.cloud.api.Request;
import com.xmu.lgp.tpcsb.generator.RequestGenerator;
import com.xmu.lgp.tpcsb.generator.request.AppServiceRequest;
import com.xmu.lgp.tpcsb.generator.request.ValueAddedServiceRequest;

public class GenericRequestGenerator extends RequestGenerator {
    private String method;

    public GenericRequestGenerator(String method) {
        this.method = method;
    }

    @Override
    public Request buildRequest() {
        Request ret = new Request();

        ret.setId(id.getAndIncrement());
        ret.setMethod(method);
        ret.setParams(buildParams(method));

        return ret;
    }

    private JSONObject buildParams(String method) {
        JSONObject ret = new JSONObject();

        switch (method) {
        case "checkVaBalance":
            ValueAddedServiceRequest.buildCheckVaBalance();
            break;
        case "deductVaBalance":
            ValueAddedServiceRequest.buildDeductVaBalance();
            break;
        case "getNewestAppVersion":
            AppServiceRequest.buildGetNewestAppVersion();
        default:
            return ret;
        }
        return ret;
    }
}
