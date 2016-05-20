/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-29
 *
 */
package com.xmu.lgp.tpcsb.generator.request;

import org.json.JSONArray;
import org.json.JSONObject;

public class ValueAddedServiceRequest {

    public static JSONObject buildCheckVaBalance() {
        JSONObject ret = new JSONObject();

        ret.put("deviceId", "FFFF249313C0CFED858FFA634BA883EE0000793C");
        ret.put("type", new JSONArray().put(1000).put(2000));

        return ret;
    }

    public static JSONObject buildDeductVaBalance() {
        JSONObject ret = new JSONObject();

        ret.put("deviceId", "FFFF249313C0CFED858FFA634BA883EE0000793C");
        ret.put("from", "IPC-CLOUD");
        ret.put("type", 1000);
        ret.put("amount", 1);
        ret.put("token", "28485Th0K2707t719y7AIc33Y7oP48IG");

        return ret;
    }
}
