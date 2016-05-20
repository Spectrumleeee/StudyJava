/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-25
 *
 */
package com.xmu.lgp.tpcsb;

import java.util.Properties;

public class TPCSFactory {
    @SuppressWarnings("rawtypes")
    public static TPCS newTPCS(String tpcsname, Properties properties) throws UnknowTPCSException {
        ClassLoader classLoader = TPCSFactory.class.getClassLoader();

        TPCS ret = null;

        try {
            Class dbclass = classLoader.loadClass(tpcsname);
            // System.out.println("dbclass.getName() = " + dbclass.getName());

            ret = (TPCS) dbclass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        ret.setProperties(properties);

        return new TPCSWrapper(ret);
    }
}
