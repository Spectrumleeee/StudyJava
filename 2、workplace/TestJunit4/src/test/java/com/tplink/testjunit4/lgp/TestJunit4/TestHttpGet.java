/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 18, 2014
 */

package com.tplink.testjunit4.lgp.TestJunit4;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestHttpGet {
   
    private HttpServerMock _mock;
    private HttpGet _httpGet = new HttpGet();
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */

    @Before
    public void setUp() throws Exception {
        _mock = new HttpServerMock();
    }

    @After
    public void tearDown() throws Exception {
        if (null != _mock) {
            _mock.stop();
        }
    }

    /**
     * response code is 200
     * @throws Exception
     */
    @Test
    public void testGetSomeThing4Success() throws Exception {
        String response = "Hello, The World!";
        _mock.start(response, "text/plain");

        String content = _httpGet.getSomeThing("http://localhost:8080/hello");
        assertEquals(response, content);
    }

    /**
     * response code is not 200
     * @throws Exception
     */
    @Test
    public void testGetSomeThing4Fail() throws Exception {
        String response = "Hello, The World!";
        _mock.start(response, "text/plain", 500);

        String content = _httpGet.getSomeThing("http://localhost:8080/hello");
        assertNull(content);
    }

}
