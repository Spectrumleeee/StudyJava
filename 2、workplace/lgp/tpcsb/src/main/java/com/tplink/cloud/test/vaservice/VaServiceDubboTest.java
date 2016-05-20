package com.tplink.cloud.test.vaservice;

import java.util.Properties;
import java.util.Scanner;

import com.tplink.cloud.api.IService;
import com.tplink.cloud.api.IValService;
import com.tplink.cloud.api.Response;
import com.tplink.cloud.test.vaservice.request.RequestMaker;
import com.xmu.lgp.tpcsb.services.invoker.DubboService;

public class VaServiceDubboTest {
    private IService invoker;
    private Properties prop;
    private RequestMaker maker;
    private String token = "YX546836llamq3iCPqe73p10KY4W2859";

    public VaServiceDubboTest() {
        prop = initProperties();
        DubboService.init(prop, IValService.class);
        invoker = DubboService.getServiceInvokder();
        maker = new RequestMaker();
    }

    public static void main(String[] args) {
        VaServiceDubboTest tester = new VaServiceDubboTest();
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input a num:");
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
            default:
                break;
            }
        }
    }

    private Properties initProperties() {
        Properties prop = new Properties();
        prop.setProperty("dubbo.zookeeper.addr", "zookeeper://172.31.1.151:2181");
        prop.setProperty("dubbo.timeout", "5000");
        return prop;
    }

    public void testCheckVaBalance() {
        Response resp = invoker.invoke(maker.makeCheckVaBalance());
        System.out.println(resp.toString());
        if (resp != null && resp.getResult() != null && resp.getResult().has("token")) {
            token = resp.getResult().getString("token");
        }
    }

    public void testPreDeductVaBalance() {
        maker.setToken(this.token);
        Response resp = invoker.invoke(maker.makePreDeductVaBalance());
        System.out.println(resp.toString());
    }

    public void testAckDeductVaBalance() {
        maker.setToken(this.token);
        Response resp = invoker.invoke(maker.makeAckDeductVaBalance());
        System.out.println(resp.toString());
    }

    public static void sleep() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
