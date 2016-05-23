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
    private static final String DEFAULT_XML_PATH = "src/main/resources/requests.xml";
    private Map<String, XmlRequest> xmlRequests;
    private XmlRequest xmlRequest;
    private String method;

    private Random random;

    public XmlRequestGenerator(String method, String xmlFile) {
        this.method = method;
        this.random = new Random();
        initXmlSaxParser(xmlFile);
    }

    public XmlRequestGenerator(String method) {
        this(method, DEFAULT_XML_PATH);
    }

    private void initXmlSaxParser(String xmlFile) {
        XmlSaxParser sax = new XmlSaxParser();
        InputStream in;
        try {
            in = new FileInputStream(xmlFile);
            xmlRequests = sax.getRequests(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        xmlRequest = xmlRequests.get(method);
    }

    @Override
    public Request buildRequest() {
        Request ret = new Request();

        ret.setId(id.incrementAndGet());
        ret.setMethod(method);
        ret.setParams(buildParams(ret.getId()));

        return ret;
    }

    private JSONObject buildParams(long id) {
        JSONObject ret = new JSONObject();

        if (xmlRequest == null) {
            return ret;
        }

        for (XmlParam param : xmlRequest.getParams()) {
            if(param.getPrefix() != null) {
                buildParamsIfHasPrefix(ret, param, id);
                continue;
            }
            
            if (!param.getType().contains("List")) {
                buildParamsIfNotArray(ret, param);
            } else {
                buildParamsIfArray(ret, param);
            }
        }

        return ret;
    }

    private JSONObject buildParamsIfHasPrefix(JSONObject inOut, XmlParam param, long id) {
        inOut.put(param.getName(), param.getPrefix() + id);
        
        return inOut;
    }
    
    /**
     * build field in a request when the filed is not an Array
     */
    private JSONObject buildParamsIfNotArray(JSONObject inOut, XmlParam param) {
        List<?> values = param.getValues();
        int choose = random.nextInt(values.size());
        inOut.put(param.getName(), values.get(choose));

        return inOut;
    }

    /**
     * build field in a request when the filed is an Array
     */
    private JSONObject buildParamsIfArray(JSONObject inOut, XmlParam param) {
        
//        inOut.put(param.getName(), param.getValues());
        
        if (random.nextInt(10) == 0) {
            inOut.put(param.getName(), new ArrayList<Integer>());
        } else {
            inOut.put(param.getName(), param.getValues());
        }

        return inOut;
    }

    public static void main(String[] args) {
//         XmlRequestGenerator generator = new XmlRequestGenerator("getNewestAppVersion");
//         XmlRequestGenerator generator = new XmlRequestGenerator("getAppVersions");
//         XmlRequestGenerator generator = new XmlRequestGenerator("preDeductVaBalance");
        XmlRequestGenerator generator = new XmlRequestGenerator("ackDeductVaBalance");
        for (int i = 0; i < 100; i++) {
            System.out.println(generator.nextValue());
        }
    }

}
