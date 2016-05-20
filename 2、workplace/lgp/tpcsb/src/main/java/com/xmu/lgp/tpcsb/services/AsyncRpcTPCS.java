package com.xmu.lgp.tpcsb.services;

import com.tplink.cloud.api.ErrorCode;
import com.tplink.cloud.api.Request;
import com.tplink.cloud.api.Response;
import com.tplink.cloud.rpc.Client;
import com.xmu.lgp.tpcsb.Status;
import com.xmu.lgp.tpcsb.TPCS;
import com.xmu.lgp.tpcsb.services.invoker.AsyncRpcClient;

public class AsyncRpcTPCS extends TPCS {
    private Client invoker;

    @Override
    public void init() {
        AsyncRpcClient.init(getProperties());
        invoker = AsyncRpcClient.getClient();
    }

    @Override
    public Status invoke(Request request) {
        Status ret = Status.OK;
        try {
            Response response = invoker.invoke(request).get();
            if (response == null || response.getErrorCode() != ErrorCode.ERROR_SUCCESS) {
                ret = Status.ERROR;
            }
        } catch (Exception e) {
            ret = Status.BAD_REQUEST;
        }
        return ret;
    }

}
