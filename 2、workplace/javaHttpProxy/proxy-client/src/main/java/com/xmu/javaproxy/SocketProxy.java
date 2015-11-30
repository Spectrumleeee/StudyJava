/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-18
 *
 */
package com.xmu.javaproxy;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketProxy {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8888);
        try {
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    new SocketThread(socket).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}