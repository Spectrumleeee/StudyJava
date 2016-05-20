/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb.workloads;

import java.util.Properties;

import com.tplink.cloud.api.Request;
import com.xmu.lgp.tpcsb.TPCS;
import com.xmu.lgp.tpcsb.Workload;
import com.xmu.lgp.tpcsb.WorkloadException;
import com.xmu.lgp.tpcsb.generator.RequestGenerator;
import com.xmu.lgp.tpcsb.generator.impl.XmlRequestGenerator;

/**
 * The core benchmark scenario. Represents a set of clients doing simple CRUD
 * operations. The relative proportion of different kinds of operations, and
 * other properties of the workload, are controlled by parameters specified at
 * runtime.
 * 
 * Properties to control the client:
 * <UL>
 * <LI><b>method</b>: the method to be tested (default: 10)
 * </ul>
 */
public class CoreWorkload extends Workload {
    private static final String DEFAULT_METHOD = "checkVaBalance";
    private static final String DEFAULT_XML_FILE = "../conf/requests.xml";
    private RequestGenerator requestGenerator;

    private String method;
    private String xmlFile;

    /**
     * Initialize the scenario. Called once, in the main client thread, before
     * any operations are started.
     */
    @Override
    public void init(Properties p) throws WorkloadException {
        method = p.getProperty("method", DEFAULT_METHOD);
        xmlFile = p.getProperty("xml", DEFAULT_XML_FILE);
        requestGenerator = new XmlRequestGenerator(method, xmlFile);
    }

    @Override
    public boolean doInvoke(TPCS tpcs, Object threadstate) {
        Request request = requestGenerator.nextValue();

        tpcs.invoke(request);

        return true;
    }
}
