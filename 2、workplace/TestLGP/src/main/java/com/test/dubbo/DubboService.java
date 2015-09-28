/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  qiaoshikui <qiaoshikui@tp-link.net>
 * Created: 2014-12-4
 */

package com.test.dubbo;

import java.util.List;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.test.utils.ConfigUtil;

public class DubboService {
    
    private static final int DEFAULT_REGISTER_TIMEOUT = 5000;
    
    private static final int DEFAULT_PORT = 9190;
    
    private static final int DEFAULT_DUBBO_THREADS = 100;
    
    private static final int DEFULT_DUBBO_QUEUE_SIZE = 0;
    
    private static final int DEFAULT_DUBBO_IOTHREADS = 9;
    
    private static final int BUFFER_SIZE = 8192;
    
    private static final int DEFAULT_DUBBO_ACCEPTS = 1000;
    
    private static final int PAYLOAD = 8388608;
    
    private static final int DEFAULT_RETRIES = 0;
    
    private static final int DEFAULT_DUBBO_TIMEOUT = 5000;
    
    private static ApplicationConfig applicationConfig;
    
    private static RegistryConfig registry;
    
    private static ProtocolConfig protocol;
    
    private static IDispatchService dispatchService;
    
    public static void init() {
        
        configApplication();
        
        configRegistry();
        
        configProtocol();
    }
    
    private static void configApplication() {
        applicationConfig = new ApplicationConfig();
        applicationConfig.setName("test-dispatcher-app");
    }
    
    private static void configRegistry() {
        registry = new RegistryConfig();
        registry.setAddress(getZkAddress());
        System.out.println(getZkAddress());
        registry.setTimeout(DEFAULT_REGISTER_TIMEOUT);
        registry.setCheck(true);
    }
    
    private static void configProtocol() {
        protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(ConfigUtil.getInt("dubbo.port", DEFAULT_PORT));
        protocol.setServer("netty");
        protocol.setClient("netty");
        protocol.setCodec("dubbo");
        protocol.setSerialization("hessian2");
        protocol.setCharset("UTF-8");
        protocol.setThreadpool("fixed");
        protocol.setThreads(ConfigUtil.getInt("dubbo.threads", 
                DEFAULT_DUBBO_THREADS));
        protocol.setQueues(ConfigUtil.getInt("dubbo.queues", 
                DEFULT_DUBBO_QUEUE_SIZE));
        protocol.setIothreads(ConfigUtil.getInt("dubbo.iothreads",
                DEFAULT_DUBBO_IOTHREADS));
        protocol.setBuffer(BUFFER_SIZE);
        protocol.setAccepts(ConfigUtil.getInt("dubbo.accept",
                DEFAULT_DUBBO_ACCEPTS));
        protocol.setPayload(PAYLOAD);
    }
    
    public static void registryDispatcher(IDispatchService service) {
        ServiceConfig<IDispatchService> serviceConfig =
                new ServiceConfig<IDispatchService>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setRegistry(registry);
        serviceConfig.setProtocol(protocol);
        serviceConfig.setInterface(IDispatchService.class);
        serviceConfig.setRef(service);
        serviceConfig.export();
    }
    
    public static IDispatchService getDispatcherService(){
        if(null == dispatchService){
            dispatchService = getConsumer(IDispatchService.class);
        }
        
        return dispatchService;
    }
    
    private static <T extends IService> T getConsumer(Class<T> cls) {
        ReferenceConfig<T> reference = new ReferenceConfig<T>();
        reference.setApplication(applicationConfig);
        reference.setRegistry(registry);
        reference.setInterface(cls);
        reference.setCheck(false);
        reference.setRetries(ConfigUtil.getInt("dubbo.retries",
                DEFAULT_RETRIES));
        reference.setTimeout(ConfigUtil.getInt("dubbo.timeout", 
                DEFAULT_DUBBO_TIMEOUT));
        return reference.get();
    }
    
    @SuppressWarnings("unchecked")
    private static String getZkAddress() {
        List<String> addrs = ConfigUtil.getList("zookeeper.addr");
        StringBuilder address = new StringBuilder();
        if (addrs.size() == 0) {
            throw new RuntimeException("dubbo.zookeeper.addr is null");
        } else {
            for (int i = 0; i < addrs.size(); i++) {
                if (i == 0) {
                    address.append("zookeeper://");
                }
                if (i == 1) {
                    address.append("?backup=");
                }
                if (i > 1) {
                    address.append(",");
                }
                address.append(addrs.get(i));
            }
        }
        return address.toString();
    }
}