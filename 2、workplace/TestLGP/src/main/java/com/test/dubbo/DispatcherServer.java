/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-7-14

 * updated: 2015-7-14
 * wenyong <liguangpu@tp-link.net>
 * Reason: 
 */
package com.test.dubbo;

public class DispatcherServer {
    
    public void start(){
        DubboService.init();
        
        DispatcherService service = new DispatcherService();
        DubboService.registryDispatcher(service);
        
    }
}
