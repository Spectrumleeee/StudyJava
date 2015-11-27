package com.xmu.testnetty.connector.exception;

public class ConnectionException extends RuntimeException {
    
    private static final long serialVersionUID = -5207906540285103645L;
    
    public ConnectionException() {
        super();
    }
    
    public ConnectionException(String message) {
        super(message);
    }
    
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
