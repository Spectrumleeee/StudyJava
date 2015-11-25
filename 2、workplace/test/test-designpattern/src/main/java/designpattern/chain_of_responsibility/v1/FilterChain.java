/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Sep 22, 2014
 */

package designpattern.chain_of_responsibility.v1;

import java.util.ArrayList;
import java.util.List;

public class FilterChain implements Filter {
    List<Filter> filters = new ArrayList<Filter>();
    int index = 0;
    
    public FilterChain addFilter(Filter f){
        this.filters.add(f);
        return this;
    }
    
    @Override
    public void doFilter(Request request, Response response, Filter chain) {
          if(index == filters.size())
              return;
          Filter f = filters.get(index);
          index++;
          f.doFilter(request, response, chain);
    }

}
