/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.chain_of_responsibility.v1;

public class Response {
    String responseStr;
    
    public String getResponseStr(){
        return this.responseStr;
    }
    
    public void setResponseStr(String responseStr){
        this.responseStr = responseStr;
    }
}
