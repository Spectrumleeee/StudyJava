/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb;

public class UnknowTPCSException extends TPCSException {
    private static final long serialVersionUID = 459099842269616836L;

    public UnknowTPCSException(String message) {
        super(message);
    }

    public UnknowTPCSException() {
        super();
    }

    public UnknowTPCSException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknowTPCSException(Throwable cause) {
        super(cause);
    }

}
