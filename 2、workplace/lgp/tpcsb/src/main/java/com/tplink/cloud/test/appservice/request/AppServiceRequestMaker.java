package com.tplink.cloud.test.appservice.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.tplink.cloud.api.Request;
import com.tplink.cloud.test.RequestUtils;

public class AppServiceRequestMaker {
    List<Integer> types = new ArrayList<Integer>();
    String deviceId = "AAAAABBBBBCCCCC0000000000000000000000LGP";
    Random random = new Random();

    public Request makeGetNewestAppVersion() {
        Request request = RequestUtils.getAppServiceRequest("getNewestAppVersion");
        
        JSONObject params = new JSONObject();
        params.put("appPackageName", "com.cloudtest.app1");
        params.put("platform", "Android");
        params.put("locale", "en_US");
        
        request.setParams(params);
        return request;
    }
    
    public Request makeGetAppVersions() {
        Request request = RequestUtils.getAppServiceRequest("getAppVersions");
        
        JSONObject params = new JSONObject();
        params.put("appPackageName", "com.cloudtest.app1");
        params.put("versionCode", 1);
        params.put("platform", "Android");
        params.put("locale", "en_US");
        
        request.setParams(params);
        return request;
    }
    
    public Request makeValidateAppTcspVer() {
        Request request = RequestUtils.getAppServiceRequest("validateAppTcspVer");
        
        JSONObject params = new JSONObject();
        params.put("tcspVer", "1.0");
        params.put("appType", "IPC-CLOUD");
        params.put("appPackageName", "com.cloudtest.app1");
        params.put("terminalUUID", "3");
        
        request.setParams(params);
        return request;
    }
    
    public Request makeGetAppLegalInfos() {
        Request request = RequestUtils.getAppServiceRequest("getAppLegalInfos");
        
        JSONObject params = new JSONObject();
        params.put("legalKey", "productId-example:privacy_policy:en_US");
        
        request.setParams(params);
        return request;
    }

    public Request makeGetDstRule() {
        Request request = RequestUtils.getAppServiceRequest("getDstRule");

        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        params.put("zoneId", "Paris");
        
        request.setParams(params);
        return request;
    }
    
    public Request makePostPushInfo() {
        Request request = RequestUtils.getAppServiceRequest("postPushInfo");
        
        JSONObject params = new JSONObject();
        params.put("accountId", 1002);
        params.put("mobileType", "IOS");
        params.put("appType", "TestApp");
        params.put("versionCode", 5);
        params.put("appPackageName", "com.tplink.cloudrouter");
        params.put("deviceToken", "deviceTokenForTest");
        params.put("token", "tokenForTest");
        params.put("locale", "en_US");
        
        request.setParams(params);
        return request;
    }
    
    public Request makeSetBadge() {
        Request request = RequestUtils.getAppServiceRequest("setBadge");
        
        JSONObject params = new JSONObject();
        params.put("badge", 9528);
        params.put("token", "LOGINTOKEN_SETBADGE");
        
        request.setParams(params);
        return request;
    }
    
}
