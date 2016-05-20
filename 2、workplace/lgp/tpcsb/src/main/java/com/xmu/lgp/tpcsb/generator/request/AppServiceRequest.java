/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2016-3-29
 *
 */
package com.xmu.lgp.tpcsb.generator.request;

import java.util.Random;

import org.json.JSONObject;

public class AppServiceRequest {
    public static String[] locales = { "en_US", "zh_CN", "zh_TW" };
    public static String[] platforms = { "IOS", "Android" };
    public static String[] packageNames = { "com.cloudtest.app1" };

    private static Random random = new Random();

    public static JSONObject buildGetNewestAppVersion() {
        JSONObject ret = new JSONObject();

        ret.put("appPackageName", build_field_packageNames());
        ret.put("platform", build_field_platfrom());
        ret.put("locale", build_field_locale());

        return ret;
    }

    private static String build_field_locale() {
        int choose = random.nextInt(100);
        if (choose < 80) {
            return locales[0];
        }
        if (choose < 95) {
            return locales[1];
        }
        return locales[2];
    }

    private static String build_field_platfrom() {
        int len = platforms.length;
        return platforms[random.nextInt(len)];
    }

    private static String build_field_packageNames() {
        int len = packageNames.length;
        return packageNames[random.nextInt(len)];
    }
}
