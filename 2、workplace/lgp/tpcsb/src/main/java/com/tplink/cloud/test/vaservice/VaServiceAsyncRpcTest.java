package com.tplink.cloud.test.vaservice;

import java.util.Scanner;

import com.tplink.cloud.api.Response;
import com.tplink.cloud.rpc.Application;
import com.tplink.cloud.rpc.Client;
import com.tplink.cloud.rpc.Refer;
import com.tplink.cloud.test.vaservice.request.RequestMaker;

public class VaServiceAsyncRpcTest {
    private static final String DEFAULT_ZKURL = "zk://172.31.1.151";
    private String service;
    private String zkUrl;
    private Application app;
    private Refer refer;
    private Client client;
    private RequestMaker maker;
    private String token = "use1|YX546836llamq3iCPqe73p10KY4W2859";

    public VaServiceAsyncRpcTest(String zkUrl) {
        this.zkUrl = zkUrl == null ? DEFAULT_ZKURL : zkUrl;
        service = "vaService";

        System.out.println("Using zkUrl:" + this.zkUrl);

        maker = new RequestMaker();
        app = new Application();
        app.setZookeeperUrl(this.zkUrl);
        refer = app.refer(service);
        client = refer.connect();
    }

    public void shutdown() {
        try {
            client.close();
            refer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCheckVaBalance() throws Exception {
        Response resp = client.invoke(maker.makeCheckVaBalance()).get();
        System.out.println(resp.toString());
        if (resp != null && resp.getResult() != null && resp.getResult().has("token")) {
            token = resp.getResult().getString("token");
        }
    }

    public void testPreDeductVaBalance() throws Exception {
        maker.setToken(this.token);
        Response resp = client.invoke(maker.makePreDeductVaBalance()).get();
        System.out.println(resp.toString());
    }

    public void testAckDeductVaBalance() throws Exception {
        maker.setToken(this.token);
        Response resp = client.invoke(maker.makeAckDeductVaBalance()).get();
        System.out.println(resp.toString());
    }

    public void testCheckVaToken() throws Exception {
        maker.setToken(this.token);
        Response resp = client.invoke(maker.makeCheckVaToken()).get();
        System.out.println(resp.toString());
    }

    public void testGetVaBalance() throws Exception {
        maker.setToken(this.token);
        Response resp = client.invoke(maker.makeGetVaBalance()).get();
        System.out.println(resp.toString());
    }

    public static void main(String[] args) throws Exception {
        String zkUrl = null;
        System.out.println("Usage: ./start.sh [zk://172.31.1.162]");

        if (args.length == 1) {
            zkUrl = args[0];
        }

        VaServiceAsyncRpcTest tester = new VaServiceAsyncRpcTest(zkUrl);
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        System.out.print("1、checkVaBalance  2、predeductVaBalance  3、ackDedeductVaBalance  ");
        System.out.println("4、checkVaToken  5、getVaBalance  0、exit!  ");
        System.out.print("Input a num: ");
        int i = 1;
        while (true) {
            int choose = scanner.nextInt();

            switch (choose) {
            case 1:
                tester.testCheckVaBalance();
                break;
            case 2:
                tester.testPreDeductVaBalance();
                break;
            case 3:
                tester.testAckDeductVaBalance();
                break;
            case 4:
                tester.testCheckVaToken();
                break;
            case 5:
                tester.testGetVaBalance();
                break;
            case 0:
                tester.shutdown();
                System.exit(0);
                break;
            default:
                break;
            }

            if (i % 5 == 0) {
                System.out.print("1、checkVaBalance  2、predeductVaBalance  3、ackDedeductVaBalance  ");
                System.out.println("4、checkVaToken  5、getVaBalance  0、exit!  ");
                System.out.print("Input a num: ");
            }
            i++;
        }
    }
}
