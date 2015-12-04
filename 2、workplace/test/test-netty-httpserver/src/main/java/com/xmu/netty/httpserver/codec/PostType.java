/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-12-4
 *
 */
package com.xmu.netty.httpserver.codec;

public enum PostType {
    FORM(1, "form"), JSON(2, "json"), NULL(3, "null");

    int id;
    String type;

    private PostType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
