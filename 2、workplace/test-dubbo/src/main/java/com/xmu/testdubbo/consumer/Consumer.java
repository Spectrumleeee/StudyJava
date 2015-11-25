/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-17
 *
 */
package com.xmu.testdubbo.consumer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.alibaba.dubbo.rpc.RpcContext;
import com.xmu.testdubbo.callback.CallbackListener;
import com.xmu.testdubbo.service.AsyncService;
import com.xmu.testdubbo.service.CallbackService;
import com.xmu.testdubbo.service.HelloService;

public class Consumer {

    private HelloService helloService;
    private CallbackService callbackService;
    private AsyncService asyncService;

    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }
    
    public void setCallbackService(CallbackService callbackService){
        this.callbackService = callbackService;
    }
    
    public void setAsyncService(AsyncService asyncService){
        this.asyncService = asyncService;
    }

    public void say() {
        try {
            System.out.println(helloService.sayHello("world"));
            System.out.println(helloService.sayHello("hello"));
            System.out.println(helloService.sayHello("ksdjflkwejskldfjwe"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void sayCallbackService(String request, CallbackListener listener){
        callbackService.sayCallbackService(request, listener);
    }
    
    public void asyncInvoke() {
        asyncService.findResponse();
        Future<String> respFuture = RpcContext.getContext().getFuture();
        
        try {
            String resp = respFuture.get();
            System.out.println("asyncInvoke response: " + resp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
