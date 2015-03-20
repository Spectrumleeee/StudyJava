package com.test.entity;

/**
 * TestEnum.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-12
 */
public enum Signal{
    RED(0, "红灯停"),
    GREEN(1, "绿灯行"),
    YELLOW(-1, "黄灯等");
    
    private int code;
    private String message;
    
    Signal(int code, String message){
        this.setCode(code);
        this.setMessage(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public static Signal getTestEnum(int code){
        Signal[] signals = values();
        for(Signal te : signals){
            if(te.getCode() == code)
                return te;
        }
        return null;
    }
}
