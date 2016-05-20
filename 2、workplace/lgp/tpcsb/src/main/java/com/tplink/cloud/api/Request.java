/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-28
 *
 */
package com.tplink.cloud.api;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Request implements Serializable {

    private static final long serialVersionUID = -7112592590993324004L;
    // 0 is an invalid invoke id in cloud system.
    private long id = 0;
    private String method = null;
    private String params = null;

    public Request() {
    }

    public Request(long id) {
        this.id = id;
    }

    public Request(long id, String method) {
        this.id = id;
        this.method = method;
    }

    public Request(long id, String method, JSONObject params) {
        this.id = id;
        this.method = method;
        this.params = params.toString();
    }

    public long getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public JSONObject getParams() {
        try {
            if (params != null) {
                return new JSONObject(params);
            }
            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setParams(JSONObject params) {
        if (params != null) {
            this.params = params.toString();
        } else {
            this.params = null;
        }
    }

    public JSONObject toJsonObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        if (method != null && method.length() != 0) {
            object.put("method", method);
        }
        if (params != null) {
            JSONObject paramsTemp = getParams();
            if (paramsTemp != null) {
                object.put("params", paramsTemp);
            } else {
                throw new JSONException("Converse params = " + params + " to JSONObject fail");
            }
        }

        return object;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":" + id);
        if (method != null && method.length() != 0) {
            sb.append(",");
            sb.append("\"method\":\"" + method + "\"");
        }
        if (params != null) {
            sb.append(",");
            sb.append("\"params\":" + params);
        }
        sb.append("}");
        return sb.toString();
    }
}
