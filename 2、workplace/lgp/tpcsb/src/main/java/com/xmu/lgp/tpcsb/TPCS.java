/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb;

import java.util.Properties;

import com.tplink.cloud.api.Request;

/**
 * Tp-link cloud service
 */
public abstract class TPCS {
    /**
     * Properties for configuring this TPCS.
     */
    Properties _p = new Properties();

    /**
     * Set the properties for this TPCS.
     */
    public void setProperties(Properties p) {
        _p = p;

    }

    /**
     * Get the set of properties for this TPCS.
     */
    public Properties getProperties() {
        return _p;
    }

    /**
     * Initialize any state for this TPCS. Called once per TPCS instance; there
     * is one TPCS instance per client thread.
     */
    public void init() throws TPCSException {
    }

    /**
     * Cleanup any state for this TPCS. Called once per TPCS instance; there is
     * one TPCS instance per client thread.
     */
    public void cleanup() throws TPCSException {
    }

    /**
     * send a request
     * 
     * @param method
     *            The method of request
     * @return The result of the operation.
     */
    public abstract Status invoke(Request request);
}
