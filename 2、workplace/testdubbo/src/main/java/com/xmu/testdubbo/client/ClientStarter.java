package com.xmu.testdubbo.client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xmu.testdubbo.callback.CallbackListener;
import com.xmu.testdubbo.consumer.Consumer;

/**
 * Hello world!
 * 
 */
public class ClientStarter {
    private static ClassPathXmlApplicationContext ctx;

    public static void main(String[] args) throws MalformedURLException,
            InterruptedException {

        String confDirStr = System.getProperty("conf.dir", "../conf");
        if(getOs().startsWith("Windows")){
            confDirStr = System.getProperty("conf.dir", "src/main/resources");
        }
        
        File confDir = new File(confDirStr);
        if (!confDir.exists()) {
            throw new RuntimeException("Conf directory "
                    + confDir.getAbsolutePath() + " does not exist.");
        }

        // load conf files
        ClassLoader loader = new URLClassLoader(new URL[] { confDir.toURI()
                .toURL() });
        Thread.currentThread().setContextClassLoader(loader);

        // start spring context
        ctx = new ClassPathXmlApplicationContext("consumer.xml");

        Consumer consumer = (Consumer) ctx.getBean("consumer");
        // consumer.say();

        final List<String> responses = new ArrayList<String>();

        CallbackListener callback = new CallbackListener() {
            @Override
            public void getResponse(String msg) {
                System.out.println("Recv resp: " + msg);
                responses.add(msg);
            }
        };

        for (int i = 1; i <= 1000; i++) {
//            consumer.sayCallbackService("hello", callback);
            consumer.sayCallbackService("time", callback);
//            consumer.sayCallbackService("world-"+i, callback);
            consumer.sayCallbackService("welcome-"+i, callback);
        }

        while (true) {
            Thread.sleep(5000);
        }
    }
    
    private static String getOs(){
        return System.getProperties().getProperty("os.name");
    }
}
