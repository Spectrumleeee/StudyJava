/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-29
 *
 */
package com.xmu.lgp.tpcsb.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xmu.lgp.tpcsb.parser.entity.XmlParam;
import com.xmu.lgp.tpcsb.parser.entity.XmlRequest;

public class XmlSaxParser extends DefaultHandler {
    private Map<String, XmlRequest> requests;
    private List<XmlParam> params;
    private XmlRequest request;
    private XmlParam param;

    public Map<String, XmlRequest> getRequests(InputStream in) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(in, this);
        return this.getRequests();
    }

    private Map<String, XmlRequest> getRequests() {
        return requests;
    }

    @Override
    public void startDocument() throws SAXException {
        requests = new HashMap<String, XmlRequest>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("request".equals(qName)) {
            initXmlRequest(attributes);
        } else if ("param".equals(qName)) {
            initParam(attributes);
        }
        // System.out.println("Start-"+qName);
    }
    
    private void initXmlRequest(Attributes attributes) {
        request = new XmlRequest();
        request.setMethod(attributes.getValue("method"));
        params = new ArrayList<XmlParam>();
    }
    
    private void initParam(Attributes attributes) {
        param = new XmlParam();
        param.setName(attributes.getValue("name"));
        String type = attributes.getValue("type");
        String values = attributes.getValue("value");
        String prefix = attributes.getValue("prefix");
        if(prefix != null) {
            param.setPrefix(prefix);
        }
        param.setType(type);
        param.setValue(values);
        setTypeValues(param, type, values);
    }

    private void setTypeValues(XmlParam param, String type, String values) {
        if (values == null) {
            param.setValues(null);
            return;
        }

        if ("".equals(values)) {
            param.setValues(new ArrayList<String>());
            return;
        }

        switch (type) {
        case "Str":
        case "StrList":
            List<String> strValues = new ArrayList<String>();
            for (String val : values.split(",")) {
                strValues.add(val);
            }
            param.setValues(strValues);
            break;
        case "Int":
        case "IntList":
        case "Long":
        case "LongList":
            List<Long> longValues = new ArrayList<Long>();
            for (String val : values.split(",")) {
                longValues.add(Long.parseLong(val));
            }
            param.setValues(longValues);
            break;
        default:
            break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("request".equals(qName)) {
            request.setParams(params);
            requests.put(request.getMethod(), request);
            params = null;
            request = null;
        } else if ("param".equals(qName)) {
            params.add(param);
            param = null;
        }
        // System.out.println("End-"+qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }
}
