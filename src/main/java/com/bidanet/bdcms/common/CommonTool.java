package com.bidanet.bdcms.common;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/7/30.
 */
public class CommonTool {
    public static String ids2StringSort(List<Long> ids){
        Collections.sort(ids);
        return ids2String(ids);
    }

    public static String ids2String(List<Long> ids) {
        StringBuilder sb=new StringBuilder(",");
        if (ids!=null){
            for (Object id : ids) {
                sb.append(id).append(",");
            }
        }
        return sb.toString();
    }

    public static List<Long> string2IdsSort(String idsStr){
        ArrayList<Long> list = string2Ids(idsStr);
        Collections.sort(list);
        return list;
    }

    public static ArrayList<Long> string2Ids(String idsStr) {
        ArrayList<Long> list = new ArrayList<>();
        if (idsStr!=null&&!idsStr.isEmpty()){
            String[] split = idsStr.split(",");
            for (String s : split) {
                try {
                    long l = Long.parseLong(s);
                    list.add(l);
                }catch (Exception e){

                }

            }

        }
        return list;
    }

    public static String sqlLikeSort(List<Long> ids){
        Collections.sort(ids);
        return sqlLike(ids);
    }

    public static String sqlLike(List<Long> ids) {
        StringBuilder sb=new StringBuilder("%,");
        if (ids!=null){
            for (Object id : ids) {
                sb.append(id).append(",%");
            }
        }
        return sb.toString();
    }




    protected static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    protected static SimpleDateFormat simpleDateTimeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String nowDate(){
        return simpleDateFormat.format(new Date());
    }
    public static String stringDateTime(Long time){
        if (time==null){
            return "";
        }
        return simpleDateTimeFormat.format(new Date(time));
    }
    public static String stringDate(Long time){
        if (time==null){
            return "";
        }
        return simpleDateFormat.format(new Date(time));
    }


    /**
     * 计算金额，小于零则取零
     * @param brokerage
     * @param scale
     * @return
     */
    public static Float compute(Float brokerage,Float scale){
        if (brokerage==null||scale==null){
            return 0F;
        }
        float v = brokerage * scale * 100;
        float price = Float.parseFloat(String.valueOf((Math.floor(v) / 100)));
        return price>0?price:0;
    }

}
