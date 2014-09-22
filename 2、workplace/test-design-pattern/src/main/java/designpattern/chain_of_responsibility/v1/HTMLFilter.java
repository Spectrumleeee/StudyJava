/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.chain_of_responsibility.v1;

public class HTMLFilter implements Filter {

    @Override
    public void doFilter(Request request, Response response, Filter chain) {  
            request.requestStr = request.getRequestStr().replace("<", "[")  
                    .replace(">", "] --------HTMLFilter");  
            chain.doFilter(request, response, chain);  
            response.responseStr += "--------HTMLFilter"; 
    }

}
