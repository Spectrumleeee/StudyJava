package com.tplink.test.TestBenchMark.server;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ServerStarter {
    public static ClassLoader loader;
    public static void main(String[] args) {
        start(args);
    }

    public static void start(String[] args) {
        try {
            String confDirString = System.getProperty("conf.dir", "src"
                    + File.separator + "main" + File.separator + "resources");
//            System.out.println("conf.dir : " + confDirString);
            File confDir = new File(confDirString);
            if (!confDir.exists()) {
                throw new RuntimeException("Conf directory "
                        + confDir.getAbsolutePath() + "does not exist.");
            }

            // load conf files
            loader = new URLClassLoader(new URL[] { confDir.toURI()
                    .toURL() });
            Thread.currentThread().setContextClassLoader(loader);

            new TestBenchMarkServer().parseCommandLine(args);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
