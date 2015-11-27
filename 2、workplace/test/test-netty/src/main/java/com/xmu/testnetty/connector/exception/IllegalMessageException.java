/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-26
 *
 */
package com.xmu.testnetty.connector.exception;

public class IllegalMessageException extends RuntimeException {

    private static final long serialVersionUID = -4166291037096520819L;

    public IllegalMessageException() {}

    public IllegalMessageException(String message) {
        super(message);
    }

    public IllegalMessageException(Throwable cause) {
        super(cause);
    }

    public IllegalMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
