package com.tplink.cloud.test.vaservice.request;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.tplink.cloud.api.Request;
import com.tplink.cloud.test.RequestUtils;

public class RequestMaker {

    List<Integer> types = new ArrayList<Integer>();
    String deviceId = "AAAABBBBCCCC00000000000000000000000000000006";
    String token = "use1|TestToken$683iCPqe73p10KY4W28595";
    Random random = new Random();

    public void setToken(String token) {
        this.token = token;
    }

    public Request makeCheckVaBalance() {
        Request request = RequestUtils.getVaServiceRequest("checkVaBalance");

        types.clear();
        types.add(1000);
        types.add(2000);

        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        params.put("type", types);
        request.setParams(params);

        return request;
    }

    public Request makePreDeductVaBalance() {
        Request request = RequestUtils.getVaServiceRequest("preDeductVaBalance");

        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        params.put("from", "IPC-CLOUD");
        params.put("type", 1000);
        params.put("amount", 1);
        params.put("token", token);
        request.setParams(params);

        return request;
    }

    public Request makeAckDeductVaBalance() {
        Request request = RequestUtils.getVaServiceRequest("ackDeductVaBalance");

        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        params.put("token", token);
        params.put("ack", random.nextInt(2));
        request.setParams(params);

        return request;
    }

    public Request makeCheckVaToken() {
        Request request = RequestUtils.getVaServiceRequest("checkVaToken");

        JSONObject params = new JSONObject();
        params.put("token", token);
        request.setParams(params);

        return request;
    }

    public Request makeGetVaBalance() {
        Request request = RequestUtils.getVaServiceRequest("getVaBalance");

        types.clear();

        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        // params.put("type", types);
        request.setParams(params);

        return request;
    }

    public static void main(String[] args) {
        System.out.println(DateFormat.getDateTimeInstance().format(new Date()));
    }
}
