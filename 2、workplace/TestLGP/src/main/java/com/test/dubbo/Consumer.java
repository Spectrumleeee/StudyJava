/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-7-14

 * updated: 2015-7-14
 * wenyong <liguangpu@tp-link.net>
 * Reason: 
 */
package com.test.dubbo;

import java.util.Scanner;

public class Consumer {

    public static void main(String[] args) {
        DubboService.init();
        Scanner scan = new Scanner(System.in);

        IDispatchService service = DubboService.getDispatcherService();

        System.out.println(service.invoke("hello"));
        System.out.println(service.invoke("world"));

        try {
            while (true) {
                System.out.print("Please input a string: ");
                String inputStr = scan.nextLine();
                service.invoke(inputStr);
            }
        } finally {
            scan.close();
        }
    }
}
