package com.xmu.lgp.junit;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for simple App Based on Junit 4.11 .
 */
public class CommonTest {
    // 有无 static 的区别，说明执行的时候，AppTest有多个实例
    static AtomicInteger ct_before = new AtomicInteger(0);
    AtomicInteger ct_after = new AtomicInteger(0);
    @BeforeClass
    public static void allUp(){
        System.out.println("aaaaaa ");
    }
    
    @AfterClass
    public static void allDown(){
        System.out.println("zzzzzz ");
    }
    
    @Before
    public void setUp(){
        System.out.println("hahaha " + ct_before.getAndIncrement());
    }
    
    @After
    public void tearDown(){
        System.out.println("blabla " + ct_after.get());
    }
    
    @Test
    public void t1(){
        assertEquals("t1失败了","abc", "abc");
    }
    
    @Ignore
    public void t2(){
        assertEquals("abc", "abc");
    }
    
    @Test
    public void t3(){
        assertEquals("误差在精度范围内",1.00, 1.01, 0.1);
    }

    @Test
    public void t3_1(){
        assertEquals("误差不可以大于精度啦",1.00, 1.01, 0.001);
    }
    
    @Test
    public void t3_2(){
        assertEquals("误差等于精度也不可以",1.00, 1.01, 0.01);
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void t4(){
        int[] a = new int[3];
        a[3] = 1;
    }
}
