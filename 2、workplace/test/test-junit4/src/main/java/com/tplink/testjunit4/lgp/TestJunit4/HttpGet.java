/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 18, 2014
 */

package com.tplink.testjunit4.lgp.TestJunit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class HttpGet {
    
    public String getSomeThing(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setConnectTimeout(3000);
        urlConn.setRequestMethod("GET");
        urlConn.connect();

        InputStream ins = null;
        try {
//            System.out.println(urlConn.getResponseCode());
            if (200 == urlConn.getResponseCode()) {
                ins = urlConn.getInputStream();
                ByteArrayOutputStream outs = new ByteArrayOutputStream(1024);
                IOUtils.copy(ins, outs);
                return outs.toString("UTF-8");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(ins);
        }
        return null;
    }
}
