/**
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author:  liguangpu <liguangpu@tp-link.net>
 * Created: 2015-11-16
 */

package com.xmu.lgp.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyyMMddHHmmss");
    private static final long MS_PER_DAY = 24 * 60 * 60 * 1000;

    public static Timer startDayTimer(TimerTask task, String time, int days) {
        return startTimer(task, time, days * MS_PER_DAY);
    }
    
    public static Timer startTimer(TimerTask task, String time, long mils) {
        try {
            Timer timer = new Timer();
            Date startTime = sdf.parse(time);
            startTime = regulateStartTime(startTime);
            timer.schedule(task, startTime, mils);
            return timer;
        } catch (ParseException e) {
            return null;
        }
    }

    private static Date regulateStartTime(Date startTime) {
        if(startTime.before(new Date())){
            long durationMs = System.currentTimeMillis() - startTime.getTime();
            int durationDay = (int) (durationMs / MS_PER_DAY) + 1;
            return new Date(startTime.getTime() + durationDay * MS_PER_DAY);
        }
        return startTime;
    }

    public static void main(String[] args) {
        TimerUtil.startTimer(new TimerTask(){
            @Override
            public void run() {
                System.out.println("hello world");
            }
        }, "20150817163001", 1);
    }
}
