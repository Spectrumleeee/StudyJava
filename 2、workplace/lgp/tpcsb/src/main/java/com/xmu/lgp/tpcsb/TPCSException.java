/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb;

public class TPCSException extends Exception {
    private static final long serialVersionUID = 6646883591588721475L;

    public TPCSException(String message) {
        super(message);
    }

    public TPCSException() {
        super();
    }

    public TPCSException(String message, Throwable cause) {
        super(message, cause);
    }

    public TPCSException(Throwable cause) {
        super(cause);
    }

}
