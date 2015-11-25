/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-16
 */

package com.xmu.lgp.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {

    public static String getWeekUID(String currTime) throws Exception{
        // SimpleDateFormat is not ThreadSafe
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(currTime));
        return "" + c.getWeekYear() + c.get(Calendar.WEEK_OF_YEAR);
    }

    public static String getWeekUID() throws Exception{
        Calendar c = Calendar.getInstance();
        return "" + c.getWeekYear() + c.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static String getLastWeekUID() throws Exception{
        return getWeekUID(getDayDistantNow(-7));
    }
    
    public static String getDayDistantNow(int distant) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, distant);
        return sdf.format(calendar.getTime());
    }

    public static void sleepForever(){
        while(true){
            sleepMils(10000);
        }
    }
    
    public static void sleepMils(long mils){
        try{
            Thread.sleep(mils);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception{
        String rst = TimeUtil.getWeekUID();
        System.out.println(rst);
        rst = TimeUtil.getWeekUID("20150906");
        System.out.println(rst);
        rst = TimeUtil.getDayDistantNow(-1);
        System.out.println(rst);
        rst = TimeUtil.getLastWeekUID();
        System.out.println(rst);
    }
}
