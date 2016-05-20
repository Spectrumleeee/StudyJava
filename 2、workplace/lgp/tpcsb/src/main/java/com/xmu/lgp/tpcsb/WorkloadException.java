/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb;

/**
 * The workload tried to do something bad.
 */
public class WorkloadException extends Exception {
    /**
	 * 
	 */
    private static final long serialVersionUID = 8844396756042772132L;

    public WorkloadException(String message) {
        super(message);
    }

    public WorkloadException() {
        super();
    }

    public WorkloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkloadException(Throwable cause) {
        super(cause);
    }

}
