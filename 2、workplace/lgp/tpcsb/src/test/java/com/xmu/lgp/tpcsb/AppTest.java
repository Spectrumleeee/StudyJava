package com.xmu.lgp.tpcsb;

public class AppTest {
    public static String staticStr1 = "init static String before";              // 初始化 1
    public static AppTest appTest = new AppTest();                              // 初始化 2
    public static String staticStr2 = "init static String after";
    
    static {                                                                    // 初始化3
        System.out.println(staticStr1);                                         // 初始化3.1
        System.out.println(staticStr2);                                         // 初始化3.2
        System.out.println("init static block");                                // 初始化3.3
    }
    
    public String genericStr1 = "init generic String before";                   // 初始化 2.1
    public String genericStr2 = "init generic String after";                    // 初始化 2.2
    
    private AppTest() {                                                         // 初始化 2
        System.out.println(genericStr1);                                        // 初始化 2.3
        System.out.println(genericStr2);                                        // 初始化 2.4
        System.out.println("init constructor");                                 // 初始化 2.5
    }
    
    public static void start() {
        System.out.println("按顺序初始化static变量，发现要调用构造器，就先初始化成员变量，再调用构造器");
    }
    
    public static void main(String[] args) {
        AppTest.start();
    }
}