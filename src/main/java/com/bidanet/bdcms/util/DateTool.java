package com.bidanet.bdcms.util;

import sun.nio.ch.SelectorImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gao on 2017/2/20.
 */
public class DateTool {
//    public static void main(String[]args){

//    }
//一天的开始时间
    public static Long getNowTime(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date date=calendar.getTime();
        return date.getTime();

    }
    //一天的结束时间
    public static Long getEndTime(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        Date date=calendar.getTime();
        return date.getTime();

    }

}
