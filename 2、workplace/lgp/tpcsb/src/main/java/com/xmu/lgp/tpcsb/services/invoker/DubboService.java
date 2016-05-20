/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: qiaoshikui <qiaoshikui@tp-link.net>
 * Created: 2014-11-3
 */

package com.xmu.lgp.tpcsb.services.invoker;

import java.util.List;
import java.util.Properties;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.tplink.cloud.api.IService;

public class DubboService {
    private static final int DUBBO_REGISTER_TIMEOUT_MS = 5000;
    private static final String DUBBO_ZKURL = "127.0.0.1:2181";
    private static final String DUBBO_REF_TIMEOUT_MS = "5000";

    private static IService iService;

    public static synchronized <T> void init(Properties prop, Class<T> clazz) {
        String zkUrl = prop.getProperty("dubbo.zookeeper.addr", DUBBO_ZKURL);
        String timeout = prop.getProperty("dubbo.timeout", DUBBO_REF_TIMEOUT_MS);

        if (iService == null) {
            init0(zkUrl, timeout, clazz);
        }
    }

    private static <T> void init0(String zkUrl, String timeout, Class<T> clazz) {
        System.out.println(zkUrl + " " + timeout);
        ApplicationConfig application = new ApplicationConfig();
        application.setName("TPCSB");

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(zkUrl);
        registry.setTimeout(DUBBO_REGISTER_TIMEOUT_MS);
        registry.setCheck(true);
        registry.setRegister(false);

        ReferenceConfig<T> reference = new ReferenceConfig<T>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(clazz);
        reference.setTimeout(Integer.parseInt(timeout));
        reference.setRetries(0);
        reference.setCheck(true);

        iService = (IService) reference.get();
    }

    public static IService getServiceInvokder() {
        return iService;
    }

    @SuppressWarnings("unused")
    private static String formatZkAddress(List<String> addresses) {
        StringBuilder address = new StringBuilder();
        if (addresses.size() == 0) {
            throw new RuntimeException("dubbo.zookeeper.addr is null");
        } else {
            for (int i = 0; i < addresses.size(); i++) {
                if (i == 0) {
                    address.append("zookeeper://");
                }
                if (i == 1) {
                    address.append("?backup=");
                }
                if (i > 1) {
                    address.append(",");
                }
                address.append(addresses.get(i));
            }
        }
        return address.toString();
    }
}
