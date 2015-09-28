/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  qiaoshikui <qiaoshikui@tp-link.net>
 * Created: 2014-5-15
 */

package com.test.dubbo;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ServerStarter {

    public static void main(String[] args) {
        try {
            new ServerStarter().start();
            while (true) {
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            System.out.println("Exception while running dispatcher: "
                    + e.toString());
            System.exit(1);
        }
    }

    public void start() {
        try {
            String confDirString = System.getProperty("conf.dir", "src"
                    + File.separator + "main" + File.separator + "resources");
            File confDir = new File(confDirString);
            if (!confDir.exists()) {
                throw new RuntimeException("Conf directory "
                        + confDir.getAbsolutePath() + "does not exist.");
            }
            // load conf files
            ClassLoader loader = new URLClassLoader(new URL[] { confDir.toURI()
                    .toURL() });
            Thread.currentThread().setContextClassLoader(loader);
            
            new DispatcherServer().start();
            
            System.out.println("cloud dispatch service is running ...");
        } catch (Exception e) {
            System.out.println("Exception while start dispatcher: "
                    + e.toString());
            System.exit(1);
        }
    }
}
