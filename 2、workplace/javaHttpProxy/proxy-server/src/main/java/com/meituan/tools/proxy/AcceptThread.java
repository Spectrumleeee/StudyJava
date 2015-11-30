package com.meituan.tools.proxy;

import com.meituan.tasks.Executors;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

class AcceptThread implements Callable<Object> {

    HttpProxyServer parent;
    Logger logger= LoggerFactory.getLogger(this.getClass());

    public AcceptThread(HttpProxyServer parent) {
        this.parent=parent;
    }

    @Override
    public Object call() throws Exception {
        ServerSocket serverSocket=parent.serverSocket;
        while(serverSocket.isBound()) {
            Socket socket=serverSocket.accept();
            logger.debug("Connection from %s",socket.getRemoteSocketAddress());
            Executors.getInstance().submitCommon(new ProcessThread(parent,socket));
        }
        return this;
    }
    
}
