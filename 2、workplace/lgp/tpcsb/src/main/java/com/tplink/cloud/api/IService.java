/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-28
 *
 */
package com.tplink.cloud.api;

public interface IService {
    public abstract Response invoke(Request request);
}
