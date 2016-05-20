package com.tplink.cloud.test.vaservice.request;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.tplink.cloud.api.Request;

public class RequestMaker {

    List<Integer> types = new ArrayList<Integer>();
    String deviceId = "AAAABBBBCCCC0000000000000000000000000006";
    String token = "use1|TestToken$683iCPqe73p10KY4W28595";
    Random random = new Random();

    public void setToken(String token) {
        this.token = token;
    }

    public Request makeCheckVaBalance() {
        Request request = new Request();
        request.setId(9527l);
        request.setMethod("checkVaBalance");

        types.clear();
        types.add(1000);
        types.add(1001);

        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        params.put("type", types);
        request.setParams(params);

        return request;
    }

    public Request makePreDeductVaBalance() {
        Request request = new Request();
        request.setId(9528l);
        request.setMethod("preDeductVaBalance");

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
        Request request = new Request();
        request.setId(9529l);
        request.setMethod("ackDeductVaBalance");

        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        params.put("token", token);
        params.put("ack", random.nextInt(2));
        request.setParams(params);

        return request;
    }

    public Request makeCheckVaToken() {
        Request request = new Request();
        request.setId(9530l);
        request.setMethod("checkVaToken");

        JSONObject params = new JSONObject();
        params.put("token", token);
        request.setParams(params);

        return request;
    }

    public Request makeGetVaBalance() {
        Request request = new Request();
        request.setId(9530l);
        request.setMethod("getVaBalance");

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
