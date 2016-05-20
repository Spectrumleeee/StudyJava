/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

import com.tplink.cloud.api.Request;
import com.xmu.lgp.tpcsb.measurements.Measurements;

public class TPCSWrapper extends TPCS {
    private TPCS _tpcs;
    private Measurements _measurements;

    private boolean reportLatencyForEachError = false;
    private HashSet<String> latencyTrackedErrors = new HashSet<String>();

    private static final String REPORT_LATENCY_FOR_EACH_ERROR_PROPERTY = "reportlatencyforeacherror";
    private static final String REPORT_LATENCY_FOR_EACH_ERROR_PROPERTY_DEFAULT = "false";

    private static final String LATENCY_TRACKED_ERRORS_PROPERTY = "latencytrackederrors";

    public TPCSWrapper(TPCS tpcs) {
        _tpcs = tpcs;
        _measurements = Measurements.getMeasurements();
    }

    /**
     * Set the properties for this TPCS.
     */
    public void setProperties(Properties p) {
        _tpcs.setProperties(p);
    }

    /**
     * Get the set of properties for this TPCS.
     */
    public Properties getProperties() {
        return _tpcs.getProperties();
    }

    /**
     * Initialize any state for this TPCS. Called once per TPCS instance; there
     * is one TPCS instance per client thread.
     */
    public void init() throws TPCSException {
        _tpcs.init();

        this.reportLatencyForEachError = Boolean.parseBoolean(getProperties().getProperty(
                REPORT_LATENCY_FOR_EACH_ERROR_PROPERTY, REPORT_LATENCY_FOR_EACH_ERROR_PROPERTY_DEFAULT));

        if (!reportLatencyForEachError) {
            String latencyTrackedErrors = getProperties().getProperty(LATENCY_TRACKED_ERRORS_PROPERTY, null);
            if (latencyTrackedErrors != null) {
                this.latencyTrackedErrors = new HashSet<String>(Arrays.asList(latencyTrackedErrors.split(",")));
            }
        }

        // System.err.println("TPCSWrapper: report latency for each error is "
        // + this.reportLatencyForEachError
        // + " and specific error codes to track" + " for latency are: "
        // + this.latencyTrackedErrors.toString());
    }

    /**
     * Cleanup any state for this TPCS. Called once per TPCS instance; there is
     * one TPCS instance per client thread.
     */
    public void cleanup() throws TPCSException {
        long ist = _measurements.getIntendedtartTimeNs();
        long st = System.nanoTime();
        _tpcs.cleanup();
        long en = System.nanoTime();
        measure("CLEANUP", Status.OK, ist, st, en);
    }

    private void measure(String op, Status result, long intendedStartTimeNanos, long startTimeNanos, long endTimeNanos) {
        String measurementName = op;
        if (result != Status.OK) {
            if (this.reportLatencyForEachError || this.latencyTrackedErrors.contains(result.getName())) {
                measurementName = op + "-" + result.getName();
            } else {
                measurementName = op + "-FAILED";
            }
        }
        _measurements.measure(measurementName, (int) ((endTimeNanos - startTimeNanos) / 1000));
        _measurements.measureIntended(measurementName, (int) ((endTimeNanos - intendedStartTimeNanos) / 1000));
    }

    @Override
    public Status invoke(Request request) {
        long ist = _measurements.getIntendedtartTimeNs();
        long st = System.nanoTime();
        Status res = _tpcs.invoke(request);
        long en = System.nanoTime();
        measure("INVOKE", res, ist, st, en);
        _measurements.reportStatus("INVOKE", res);
        return res;
    }
}
