package com.meituan.tools.proxy;

import java.io.IOException;

public class ParseException extends IOException {

    private static final long serialVersionUID = 7127748519082022941L;

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

}
