package com.tplink.cloud.api.trace;

import java.io.Serializable;
import java.util.ArrayList;

public class Trace implements Serializable {
    private static final long serialVersionUID = 1433336234609754162L;
    private ArrayList<ModuleTrace> moduleTraceList = null;
    private ArrayList<ArrayList<ModuleTrace>> traceList = null;

    public Trace() {
        moduleTraceList = new ArrayList<ModuleTrace>();
        traceList = new ArrayList<ArrayList<ModuleTrace>>();
    }

    public void appendModuleTrace(ModuleTrace moduleTrace) {
        if (moduleTrace != null) {
            moduleTraceList.add(moduleTrace);
        }
        // do nothing
    }

    /*
     * used for Assembler module
     */
    public void appendSubTrace(Trace nextSubTrace) {
        if (nextSubTrace != null) {
            moduleTraceList.addAll(nextSubTrace.getModuleTraceList());
        }
        // do nothing
    }

    /*
     * used for AppServer module
     */
    public void appendTrace(Trace nextTrace) {
        if (nextTrace != null) {
            traceList.add(nextTrace.getModuleTraceList());
        }
        // do nothing
    }

    private ArrayList<ModuleTrace> getModuleTraceList() {
        return moduleTraceList;
    }

    @Override
    public String toString() {
        return moduleTraceList.isEmpty() ? traceList.toString() : moduleTraceList.toString();
    }
}
