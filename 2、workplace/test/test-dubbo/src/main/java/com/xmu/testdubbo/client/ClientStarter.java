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
    private ClassPathXmlApplicationContext ctx;
    private Consumer consumer;
    
    private List<String> responses;
    private CallbackListener callback;
    
    public ClientStarter(){
        // load spring context
        ctx = new ClassPathXmlApplicationContext("consumer.xml");
        consumer = (Consumer) ctx.getBean("consumer");
        
        responses = new ArrayList<String>();
        callback = new CallbackListener() {
            @Override
            public void getResponse(String msg) {
                System.out.println("Recv resp: " + msg);
                responses.add(msg);
            }
        };
        
    }

    public void testHelloService(){
        consumer.say();
    }
    
    public void testAsyncService(){
        consumer.asyncInvoke();
    }
    
    public void testCallbackService(){
        for (int i = 1; i <= 10; i++) {
          consumer.sayCallbackService("hello", callback);
          consumer.sayCallbackService("time", callback);
          consumer.sayCallbackService("world-"+i, callback);
          consumer.sayCallbackService("welcome-"+i, callback);
      }
    }
    
    public static void main(String[] args) throws MalformedURLException,
            InterruptedException {
        InitEnv();
        
        ClientStarter client = new ClientStarter();
        
        client.testHelloService();
//        client.testAsyncService();
//        client.testCallbackService();
        
        while (true) {
            Thread.sleep(5000);
        }
    }
    
    private static void InitEnv() throws MalformedURLException{
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
    }
    
    private static String getOs(){
        return System.getProperties().getProperty("os.name");
    }
}
