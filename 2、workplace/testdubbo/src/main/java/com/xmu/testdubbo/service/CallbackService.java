/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-17
 *
 */
package com.xmu.testdubbo.service;

import com.xmu.testdubbo.callback.CallbackListener;

public interface CallbackService {
    
    public void sayCallbackService(String request, CallbackListener listener);
}
