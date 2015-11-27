package com.xmu.testnetty.connector.exception;

public class CodecException extends RuntimeException {
    
    private static final long serialVersionUID = -8867069685975889074L;
    
    public CodecException() {
        super();
    }
    
    public CodecException(String message) {
        super(message);
    }
    
    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
