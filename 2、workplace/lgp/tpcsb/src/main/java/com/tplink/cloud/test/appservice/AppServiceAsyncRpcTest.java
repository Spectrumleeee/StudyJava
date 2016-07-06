package com.tplink.cloud.test.appservice;

import java.util.Scanner;

import com.tplink.cloud.api.Response;
import com.tplink.cloud.rpc.Application;
import com.tplink.cloud.rpc.Client;
import com.tplink.cloud.rpc.Refer;
import com.tplink.cloud.test.appservice.request.AppServiceRequestMaker;

public class AppServiceAsyncRpcTest {
    private static final String DEFAULT_ZK_URL = "zk://172.31.1.151";
    private static final String APP_SERVICE_NAME = "appService";
    private String zkUrl;
    private Application app;
    private Refer refer;
    private Client client;
    private AppServiceRequestMaker maker;

    public AppServiceAsyncRpcTest(String zkUrl) {
        this.zkUrl = zkUrl == null ? DEFAULT_ZK_URL : zkUrl;
        System.out.println("Using zkUrl:" + this.zkUrl);

        maker = new AppServiceRequestMaker();
        app = new Application();
        app.setZookeeperUrl(this.zkUrl);
        refer = app.refer(APP_SERVICE_NAME);
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

    public void testGetDstRule() throws Exception {
        Response resp = client.invoke(maker.makeGetDstRule()).get();
        System.out.println(resp.toString());
    }
    
    public void testGetNewestAppVersion() throws Exception {
        Response resp = client.invoke(maker.makeGetNewestAppVersion()).get();
        System.out.println(resp.toString());
    }
    
    public void testGetAppVersions() throws Exception {
        Response resp = client.invoke(maker.makeGetAppVersions()).get();
        System.out.println(resp.toString());
    }
    
    public void testValidateAppTcspVer() throws Exception {
        Response resp = client.invoke(maker.makeValidateAppTcspVer()).get();
        System.out.println(resp.toString());
    }
    
    public void testGetAppLegalInfos() throws Exception {
        Response resp = client.invoke(maker.makeGetAppLegalInfos()).get();
        System.out.println(resp.toString());
    }
    
    public void testPostPushInfo() throws Exception {
        Response resp = client.invoke(maker.makePostPushInfo()).get();
        System.out.println(resp.toString());
    }
    
    public void testSetBadge() throws Exception {
        Response resp = client.invoke(maker.makeSetBadge()).get();
        System.out.println(resp.toString());
    }

    public static void main(String[] args) throws Exception {
        String zkUrl = null;
        System.out.println("Usage: ./start.sh [zk://172.31.1.162]");

        if (args.length == 1) {
            zkUrl = args[0];
        }

        AppServiceAsyncRpcTest tester = new AppServiceAsyncRpcTest(zkUrl);
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        System.out.println("1、getDstRule  2、getNewestAppVersion  3、getAppVersions  4、validateAppTcspVer  ");
        System.out.println("5、getAppLegalInfos  6、postPushInfo  7、setBadge  0、exit!  ");
        System.out.print("Input a num: ");
        int i = 1;
        while (true) {
            int choose = scanner.nextInt();

            switch (choose) {
            case 1:
                tester.testGetDstRule();
                break;
            case 2:
                tester.testGetNewestAppVersion();
                break;
            case 3:
                tester.testGetAppVersions();
                break;
            case 4:
                tester.testValidateAppTcspVer();
                break;
            case 5:
                tester.testGetAppLegalInfos();
                break;
            case 6:
                tester.testPostPushInfo();
                break;
            case 7:
                tester.testSetBadge();
                break;
            case 0:
                tester.shutdown();
                System.exit(0);
                break;
            default:
                break;
            }

            if (i % 7 == 0) {
                System.out.println("1、getDstRule  2、getNewestAppVersion  3、getAppVersions  4、validateAppTcspVer  ");
                System.out.println("5、getAppLegalInfos  6、postPushInfo  7、setBadge  0、exit!  ");
                System.out.print("Input a num: ");
            }
            i++;
        }
    }
}
