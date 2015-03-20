package com.test.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.test.entity.Signal;


/**
 * TestReflect.java
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Created: 2015-3-12
 */
public class TestReflect {

    public static void main(String[] args) {
        TestReflect tr = new TestReflect();
        tr.testReflect();
    }

    // 枚举类型
    public void method1(){
        Signal te = Signal.GREEN;
        System.out.println(te.getMessage());
        te.setMessage(te.getMessage()+",注意安全！");
        System.out.println(te.getMessage());
        te = Signal.RED;
        System.out.println(te.getMessage());
    }
    
    public void method2(String from, int ordinal){
        System.out.println("method2 invoked from "+from + " " + ordinal);
    }
    
    public void testReflect(){
        // 获取类TestReflect的所有方法
        Method[] methods = TestReflect.class.getDeclaredMethods();
        // 获取类TestReflect的名字（含包名），用getSimpleName()就只有类文件名：TestReflect
        String className = TestReflect.class.getName();
        // 获取当前方法的名字：test
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        
        for(Method method : methods){
            System.out.println(className + " : " +method.getName());
            
            try {
                if(method.getName() == "method2"){
                    // 构造参数
                    Object[] paramsValue = new Object[2];
                    paramsValue[0] = currentMethodName;
                    paramsValue[1] = 1;
                    // 调用 this(或者其它对象)的method方法
                    method.invoke(this, paramsValue);
                }
                if(method.getName() == "method1"){
                    // 无参调用this对象的method方法
                    method.invoke(this);
                }
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
   
    
}
