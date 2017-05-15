package com.bidanet.bdcms.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/11.
 */
public class DateTool {

    public static final Calendar calendar = Calendar.getInstance();

    private void initCalendar(){
        Date date = new Date();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public DateTool(){
        initCalendar();
    }



    public Date getBeginTime(){
        calendar.add(Calendar.DATE, 0);
        return calendar.getTime();
    }

    public Date getEndTime(){
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static long FromMonth(Date time){
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.setTime(time);
        nowDate.set(Calendar.MINUTE,0);
        nowDate.set(Calendar.SECOND,0);
        nowDate.set(Calendar.MILLISECOND,0);
        nowDate.set(Calendar.DAY_OF_MONTH,1);

        Date date = nowDate.getTime();
        long begin = date.getTime();


        return  begin;

    }

    public static long ToMonth(Date time){
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.setTime(time);
        nowDate.set(Calendar.MINUTE,0);
        nowDate.set(Calendar.SECOND,0);
        nowDate.set(Calendar.MILLISECOND,0);
        nowDate.set(Calendar.DAY_OF_MONTH,1);
        nowDate.add(Calendar.MONTH,1);
        Date date = nowDate.getTime();
        long end = date.getTime();
        return  end;
    }


    public static long stringToLongStart(String strTime)
            throws ParseException {

        Date date = stringToDate(strTime+" 00:00:00"); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static long stringToLongEnd(String endTime)
            throws ParseException {

        Date date = stringToDate(endTime+" 23:59:59"); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date stringToDate(String strTime)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    static SimpleDateFormat ymd= new SimpleDateFormat("yyyy-MM-dd");

    static SimpleDateFormat ymdms= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static SimpleDateFormat ymdCN= new SimpleDateFormat("yyyy年MM月dd日");

    public static String timeToStrYmd(Long time){
        return ymd.format(new Date(time));
    }

    public static String timeToStrYmdCN(Long time){
        return ymdCN.format(new Date(time));
    }
}
