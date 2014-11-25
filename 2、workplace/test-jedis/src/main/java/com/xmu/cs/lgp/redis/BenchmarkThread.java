// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TestJedis.java

package com.xmu.cs.lgp.redis;

// Referenced classes of package com.xmu.cs.lgp.redis:
//			TestJedis

class BenchmarkThread extends Thread {

    private String name;
    private TestJedis jc;
    private long nums;

    public BenchmarkThread(String name, TestJedis jc, long nums) {
        this.name = name;
        this.jc = jc;
        this.nums = nums;
    }

    public void run() {
        System.out.println((new StringBuilder(String.valueOf(name))).append(
                " start !").toString());
        String value = "jedis-";
        for (int i = 0; (long) i < nums; i++)
            jc.set((new StringBuilder(String.valueOf(name))).append("-")
                    .append(i).toString(),
                    (new StringBuilder(String.valueOf(value))).append(i)
                            .toString());

        System.out.println((new StringBuilder(String.valueOf(name))).append(
                " stop !").toString());
    }
}
