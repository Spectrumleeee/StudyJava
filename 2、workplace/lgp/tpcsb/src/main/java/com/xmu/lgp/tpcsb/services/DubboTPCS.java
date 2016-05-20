/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-28
 *
 */
package com.xmu.lgp.tpcsb.services;

import java.util.Properties;

import com.tplink.cloud.api.ErrorCode;
import com.tplink.cloud.api.IAccountService;
import com.tplink.cloud.api.IAppService;
import com.tplink.cloud.api.IDeviceService;
import com.tplink.cloud.api.IDispatchService;
import com.tplink.cloud.api.IService;
import com.tplink.cloud.api.IValService;
import com.tplink.cloud.api.Request;
import com.tplink.cloud.api.Response;
import com.xmu.lgp.tpcsb.Status;
import com.xmu.lgp.tpcsb.TPCS;
import com.xmu.lgp.tpcsb.services.invoker.DubboService;

public class DubboTPCS extends TPCS {
    private static final String DEFAULT_ROUTER = "dispatcher";
    private static final String OPTION_ROUTER = "dispatcher|appservice|vaservice|accountservice|deviceservice";
    private static IService invoker;
    private Properties prop;
    private Response response;
    private String router;

    @Override
    public void init() {
        prop = getProperties();
        router = prop.getProperty("router", DEFAULT_ROUTER);
        try {
            switch (router) {
            case "dispatcher":
                DubboService.init(prop, IDispatchService.class);
                break;
            case "appService":
                DubboService.init(prop, IAppService.class);
                break;
            case "vaService":
                DubboService.init(prop, IValService.class);
                break;
            case "accountService":
                DubboService.init(prop, IAccountService.class);
                break;
            case "deviceService":
                DubboService.init(prop, IDeviceService.class);
                break;
            default:
                System.out.printf("router error: %s, option:[%s]", router, OPTION_ROUTER);
                System.exit(-1);
            }
        } catch (Exception e) {
            System.out.printf("Init router error! %s", e.getMessage());
            System.exit(-1);
        }

        invoker = DubboService.getServiceInvokder();
    }

    @Override
    public Status invoke(Request request) {
        Status ret = Status.OK;

        try {
            response = invoker.invoke(request);
            if (response == null || response.getErrorCode() != ErrorCode.ERROR_SUCCESS) {
                ret = Status.ERROR;
            }
        } catch (Exception e) {
            ret = Status.BAD_REQUEST;
        }
        return ret;
    }
}
