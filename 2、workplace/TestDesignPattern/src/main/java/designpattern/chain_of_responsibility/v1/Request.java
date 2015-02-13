/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.chain_of_responsibility.v1;

public class Request {
    String requestStr;
    
    public String getRequestStr(){
        return this.requestStr;
    }
    
    public void setRequestStr(String requestStr){
        this.requestStr = requestStr;
    }
}
