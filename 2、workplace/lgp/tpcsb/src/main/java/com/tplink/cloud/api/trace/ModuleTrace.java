package com.tplink.cloud.api.trace;

import java.io.Serializable;

public class ModuleTrace implements Serializable {
    private static final long serialVersionUID = -5877210320575903698L;

    private static final String SEPARATOR = "_";

    private String moduleName = null;
    private String ip = null;
    private long timeCostMs = 0L;

    public ModuleTrace(String moduleName, String ip, long timeCostMs) {
        this.moduleName = moduleName;
        this.ip = ip;
        this.timeCostMs = timeCostMs;
    }

    public String toString() {
        StringBuilder moduleString = new StringBuilder();

        if (moduleName != null) {
            moduleString.append(moduleName);
        }

        moduleString.append(SEPARATOR);

        if (ip != null) {
            moduleString.append(ip);
        }

        moduleString.append(SEPARATOR);

        moduleString.append(timeCostMs);

        return moduleString.toString();
    }
}
