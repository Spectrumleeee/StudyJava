/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb.services;

import java.util.Random;

import com.tplink.cloud.api.Request;
import com.xmu.lgp.tpcsb.Status;
import com.xmu.lgp.tpcsb.TPCS;

public class BasicTPCS extends TPCS {
    private Random random = new Random();

    @Override
    public Status invoke(Request request) {
        if (random.nextInt(10) == 0) {
            return Status.ERROR;
        }
        return Status.OK;

    }

}
