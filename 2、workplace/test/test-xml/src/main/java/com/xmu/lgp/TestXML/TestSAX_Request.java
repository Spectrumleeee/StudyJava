/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-29
 *
 */
package com.xmu.lgp.TestXML;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xmu.lgp.entity.Param;
import com.xmu.lgp.entity.Request;

public class TestSAX_Request extends DefaultHandler {
    private List<Request> requests;
    private List<Param> params;
    private Request request;
    private Param param;
    
    public List<Request> getRequests(InputStream in) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(in, this);
        return this.getRequests();
    }

    private List<Request> getRequests() {
        return requests;
    }
    
    @Override
    public void startDocument() throws SAXException {
        requests = new ArrayList<Request>();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("request".equals(qName)) {
            request = new Request();
            request.setMethod(attributes.getValue(0));
            params = new ArrayList<Param>();
        } else if ("param".equals(qName)) {
            param = new Param();
            param.setName(attributes.getValue(0));
            param.setType(attributes.getValue(1));
            param.setValue(attributes.getValue(2));
        }
        // System.out.println("Start-"+qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("request".equals(qName)) {
            request.setParams(params);
            requests.add(request);
            params = null;
            request = null;
        } else if ("param".equals(qName)) {
            params.add(param);
            param = null;
        }
        // System.out.println("End-"+qName);
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }
}
