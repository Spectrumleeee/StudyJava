package com.xmu.lgp.tpcsb.services.invoker;

import java.util.Properties;

import com.tplink.cloud.rpc.Application;
import com.tplink.cloud.rpc.Client;
import com.tplink.cloud.rpc.Refer;

public class AsyncRpcClient {
    private static final String DEFAULT_RPC_ZK_ADDR = "zk://172.31.1.162";
    private static final String DEFUALT_SERVICE_NAME = "vaService";
    private static Application app;
    private static Refer refer;
    private static Client client;

    public synchronized static void init(Properties prop) {
        if (refer != null) {
            return;
        }

        String zkUrl = prop.getProperty("rpc.zookeeper.addr", DEFAULT_RPC_ZK_ADDR);
        String service = prop.getProperty("rpc.service.name", DEFUALT_SERVICE_NAME);

        app = new Application();
        app.setZookeeperUrl(zkUrl);
        refer = app.refer(service);
        client = refer.connect();
    }

    public synchronized static Client getClient() {
        if (client == null) {
            client = refer.connect();
        }
        return client;
    }

}
