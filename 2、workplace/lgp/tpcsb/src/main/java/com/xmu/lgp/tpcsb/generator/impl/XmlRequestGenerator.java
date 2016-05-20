/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-29
 *
 */
package com.xmu.lgp.tpcsb.generator.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import com.tplink.cloud.api.Request;
import com.xmu.lgp.tpcsb.generator.RequestGenerator;
import com.xmu.lgp.tpcsb.parser.XmlSaxParser;
import com.xmu.lgp.tpcsb.parser.entity.XmlParam;
import com.xmu.lgp.tpcsb.parser.entity.XmlRequest;

public class XmlRequestGenerator extends RequestGenerator {
    private Map<String, XmlRequest> requests;
    private XmlRequest request;
    private String method;

    private Random random;

    public XmlRequestGenerator(String method, String xmlFile) {
        this.method = method;
        this.random = new Random();
        initXml(xmlFile);
    }

    public XmlRequestGenerator(String method) {
        this(method, "src/main/resources/requests.xml");
    }

    private void initXml(String xmlFile) {
        XmlSaxParser sax = new XmlSaxParser();
        InputStream in;
        try {
            in = new FileInputStream(xmlFile);
            requests = sax.getRequests(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        request = requests.get(method);
    }

    @Override
    public Request buildRequest() {
        Request ret = new Request();

        ret.setId(id.getAndIncrement());
        ret.setMethod(method);
        ret.setParams(buildParams());

        return ret;
    }

    private JSONObject buildParams() {
        JSONObject ret = new JSONObject();

        if (request == null) {
            return ret;
        }

        List<?> values;
        String type;
        int choose;
        for (XmlParam param : request.getParams()) {
            values = param.getValues();
            type = param.getType();
            if (!type.contains("List")) {
                choose = random.nextInt(values.size());
                ret.put(param.getName(), values.get(choose));
            } else {
                if (random.nextInt(10) == 0) {
                    ret.put(param.getName(), new ArrayList<Integer>());
                } else {
                    ret.put(param.getName(), values);
                }
            }
        }

        return ret;
    }

    public static void main(String[] args) {
        // XmlRequestGenerator generator = new
        // XmlRequestGenerator("getNewestAppVersion");
        // XmlRequestGenerator generator = new
        // XmlRequestGenerator("getAppVersions");
        XmlRequestGenerator generator = new XmlRequestGenerator("deductVaBalance");
        // XmlRequestGenerator generator = new
        // XmlRequestGenerator("checkVaBalance");
        for (int i = 0; i < 100; i++) {
            System.out.println(generator.nextValue());
        }
    }

}
