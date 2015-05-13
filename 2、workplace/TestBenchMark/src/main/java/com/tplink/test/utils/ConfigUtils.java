package com.tplink.test.utils;
/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  qiaoshikui <qiaoshikui@tp-link.net>
 * Created: 2014-6-26
 */


import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigUtils {
    private static final String CONFIG_FILE_NAME = "sys.properties";
    private static Configuration config;
    
    static {
        try {
            config = new PropertiesConfiguration(CONFIG_FILE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Configuration loading error: "
                    + "configFileName: " + CONFIG_FILE_NAME, e);
        }
    }
    
    public static String getString(String key) {
        return config.getString(key);
    }

    public static String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public static int getInt(String key) {
        return config.getInt(key);
    }

    public static int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public static long getLong(String key) {
        return config.getLong(key);
    }

    public static long getLong(String key, long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public static float getFloat(String key) {
        return config.getFloat(key);
    }

    public static float getFloat(String key, float defaultValue) {
        return config.getFloat(key, defaultValue);
    }

    public static double getDouble(String key) {
        return config.getDouble(key);
    }

    public static double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public static String[] getStringArray(String key) {
        return config.getStringArray(key);
    }

    public static void setProperty(String key, Object value) {
        config.setProperty(key, value);
    }
    
    @SuppressWarnings("rawtypes")
    public static List getList(String key) {
        return config.getList(key);
    }
    
    @SuppressWarnings("rawtypes")
    public static List getList(String key, List defaultValue) {
        return config.getList(key, defaultValue);
    }
}
