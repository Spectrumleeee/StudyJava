/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author: lukairui <lukairui@tp-link.net>
 * Created: 2015-09-28
 * Entity of node data in zookeeper.
 */

package com.xmu.lgp.common.loadbalance;

import org.json.JSONObject;

public class ServerAddressNode {
    private String address = null;
    private JSONObject data = null;

    public ServerAddressNode(String address, JSONObject data) {
        super();
        this.address = address;
        this.data = data;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
