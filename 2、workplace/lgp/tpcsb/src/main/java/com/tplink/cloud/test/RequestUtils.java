package com.tplink.cloud.test;

import com.tplink.cloud.api.Request;

public class RequestUtils {
    
    public static Request getVaServiceRequest(String method) {
        return getRequest(method, Constants.VA_SERVICE_DAL_REQUEST_ID);
    }
    
    public static Request getAppServiceRequest(String method) {
        return getRequest(method, Constants.APP_SERVICE_DAL_REQUEST_ID);
    }
    
    private static Request getRequest(String method, long requestId) {
        Request request = new Request();
        request.setId(requestId);
        request.setMethod(method);
        
        return request;
    }
}
