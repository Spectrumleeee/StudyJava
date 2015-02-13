/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.chain_of_responsibility.v1;

public class Main {

    public static void main(String[] args) {  
        String message = "敏感词汇，重庆，<script> 躲猫猫 :)";  
        Request request = new Request();  
        request.setRequestStr(message);  
        Response response = new Response();  
        response.setResponseStr("response");  
        FilterChain fc = new FilterChain();  
        fc.addFilter(new HTMLFilter()).addFilter(new SesitiveFilter());  
  
        FilterChain fc2 = new FilterChain();  
        fc2.addFilter(new FaceFilter());  
        fc.addFilter(fc2);  
        fc.doFilter(request, response,fc);  
        System.out.println("request = " + request.getRequestStr());  
        System.out.println("response = " + response.getResponseStr());  
    }

}
