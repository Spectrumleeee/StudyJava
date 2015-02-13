/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.chain_of_responsibility.v1;

public class FaceFilter implements Filter {

    public void doFilter(Request request, Response response, Filter chain) {  
        request.requestStr = request.getRequestStr().replace(":)",  
                "^V^-------FaceFilter");  
        chain.doFilter(request, response, chain);  
        response.responseStr += "-------FaceFilter";  
  
    }

}
