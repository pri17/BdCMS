package com.bidanet.bdcms.dao.fee.impl;

import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.dao.fee.FeeDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamPay;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务收费. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-18 09:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Repository
public class FeeDaoImpl extends BaseDaoImpl<ExamPay> implements FeeDao{


    @Override
    public List<ExamMemberExam> findPayList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck, int pageNo, int pageSize) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamMemberExam as memebrExam where 1=1 and memebrExam.examPay.isNew = 1 ");



        if(areaId!=null){
            hql.append(" and memebrExam.areaId =:areaId");
            map.put("areaId",areaId);
        }

        //姓名
        if(StringUtils.isNotBlank(name)){
            hql.append(" and memebrExam.examMember.name =:name");
            map.put("name",name);
        }
        //身份证
        if(StringUtils.isNotBlank(idCard)){
            hql.append(" and memebrExam.examMember.idCardNum =:idCardNum");
            map.put("idCardNum",idCard);
        }
        //工作单位
        if(StringUtils.isNotBlank(workUnit)){
            hql.append(" and memebrExam.workUnit like :workUnit");
            map.put("workUnit","%"+workUnit+"%");
        }
        //体检号
        if(StringUtils.isNotBlank(examNumber)){
            hql.append(" and memebrExam.examNumber = :examNumber");
            map.put("examNumber",examNumber);
        }
//        //时间
//        if(StringUtils.isNotBlank(mobile)){
//            hql.append(" and ur.user.mobile =:mobile");
//            map.put("mobile",mobile);
//        }
        //支付方式
        if(StringUtils.isNotBlank(payType)){
            hql.append(" and memebrExam.examPay.payType = :payType");
            map.put("payType",Integer.valueOf(payType));
        }

        //支付状态
        if(StringUtils.isNotBlank(payState)){
            hql.append(" and memebrExam.examPay.payState=:payState");
            map.put("payState",Integer.valueOf(payState));
        }

        //是否复检
        if(StringUtils.isNotBlank(isRecheck)){
            hql.append(" and memebrExam.isRecheck =:isRecheck");
            map.put("isRecheck",Integer.valueOf(isRecheck));
        }

        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and memebrExam.examTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and memebrExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        Query query = getSession().createQuery(hql.toString());

        query.setFirstResult((pageNo-1) * pageSize);
        query.setMaxResults(pageSize);

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    @Override
    public List<ExamPay> findPayNoPageList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck, String tollCollectorId) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("from ExamPay as examPay where 1=1  and examPay.payType!=4");

        //体检号
        if(StringUtils.isNotBlank(examNumber)){
            hql.append(" and examPay.examNumber = :examNumber");
            map.put("examNumber",examNumber);
        }

        //身份证
        if(StringUtils.isNotBlank(idCard)){
            hql.append(" and examPay.idCardNum =:idCardNum");
            map.put("idCardNum",idCard);
        }

        //开始时间
        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and examPay.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        //姓名
        if(StringUtils.isNotBlank(name)){
            hql.append(" and examPay.name like:name");
            map.put("name","%"+name+"%");
        }

        //工作单位
        if(StringUtils.isNotBlank(workUnit)){
            hql.append(" and examPay.workUnit like :workUnit");
            map.put("workUnit","%"+workUnit+"%");
        }

        //结束时间
        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and examPay.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        //收费人员
        if(StringUtils.isNotEmpty(tollCollectorId)){
            hql.append(" and examPay.tollCollectorId = :tollCollectorId");
            map.put("tollCollectorId", Long.parseLong(tollCollectorId));
        }

        //地段区域
        if(areaId!=null){
            hql.append(" and examPay.areaId =:areaId");
            map.put("areaId",areaId);
        }

//        //是否复检
//        if(StringUtils.isNotBlank(isRecheck)){
//            hql.append(" and memebrExam.isRecheck =:isRecheck");
//            map.put("isRecheck",Integer.valueOf(isRecheck));
//        }
        //是否复检
        //1 复检 0 正常
        if(StringUtils.isNotBlank(isRecheck)){
            if(Integer.parseInt(isRecheck)==0){
                hql.append(" and examPay.recheckTag =0");
            }else if(Integer.parseInt(isRecheck)==1){
                hql.append(" and examPay.recheckTag >0");
            }

        }


        //支付方式
        if(StringUtils.isNotBlank(payType)){
            hql.append(" and examPay.payType = :payType");
            map.put("payType",Integer.valueOf(payType));
        }

        //支付状态
        if(StringUtils.isNotBlank(payState)){
            hql.append(" and examPay.payState=:payState");
            map.put("payState",Integer.valueOf(payState));
        }

        //排序 查询未作废的收费订单
        hql.append(" and examPay.isCancel =0  order by createTime desc ");


        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    @Override
    public Long findCountPayList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck,String tollCollectorId) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("select count(examPay.id) from ExamPay as examPay where 1=1 and examPay.payType!=4 ");


        //体检号
        if(StringUtils.isNotBlank(examNumber)){
            hql.append(" and examPay.examNumber = :examNumber");
            map.put("examNumber",examNumber);
        }

        //身份证
        if(StringUtils.isNotBlank(idCard)){
            hql.append(" and examPay.idCardNum =:idCardNum");
            map.put("idCardNum",idCard);
        }

        //开始时间
        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and examPay.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        //姓名
        if(StringUtils.isNotBlank(name)){
            hql.append(" and examPay.name =:name");
            map.put("name",name);
        }

        //工作单位
        if(StringUtils.isNotBlank(workUnit)){
            hql.append(" and examPay.workUnit like :workUnit");
            map.put("workUnit","%"+workUnit+"%");
        }

        //结束时间
        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and examPay.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        //收费人员
        if(StringUtils.isNotEmpty(tollCollectorId)){
            hql.append(" and examPay.tollCollectorId = :tollCollectorId");
            map.put("tollCollectorId", Long.parseLong(tollCollectorId));
        }

        //地段区域
        if(areaId!=null){
            hql.append(" and examPay.areaId =:areaId");
            map.put("areaId",areaId);
        }

        //是否复检
        //是否复检
        //1 复检 0 正常
        if(StringUtils.isNotBlank(isRecheck)){
            if(Integer.parseInt(isRecheck)==0){
                hql.append(" and examPay.recheckTag =0");
            }else if(Integer.parseInt(isRecheck)==1){
                hql.append(" and examPay.recheckTag >0");
            }

        }


        //支付方式
        if(StringUtils.isNotBlank(payType)){
            hql.append(" and examPay.payType = :payType");
            map.put("payType",Integer.valueOf(payType));
        }

        //支付状态
        if(StringUtils.isNotBlank(payState)){
            hql.append(" and examPay.payState=:payState");
            map.put("payState",Integer.valueOf(payState));
        }

        //排序 查询未作废的收费订单
        hql.append(" and examPay.isCancel =0  ");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return (long)query.uniqueResult();
    }

    @Override
    public BigDecimal calculateTotalFee(Long userId,String startTime,String endTime) throws ParseException {

        Map<String,Object> map = new HashMap<String,Object>();

        StringBuilder hql = new StringBuilder("select sum(pay.payActualMoney) from ExamPay as pay where 1=1 and pay.payState = 2 ");

        //当天时间
        Date date = new Date();
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fromatDateStr = simpleFormat.format(date);


        if (StringUtils.isNotEmpty(startTime)){
            fromatDateStr = startTime;
        }

        hql.append(" and pay.payTime >=:payStartTime");
        map.put("payStartTime", DateTool.stringToLongStart(fromatDateStr));

        if (StringUtils.isNotEmpty(endTime)){

            fromatDateStr = endTime;
        }
        hql.append(" and pay.payTime <=:payEndTime");
        map.put("payEndTime", DateTool.stringToLongEnd(fromatDateStr));


        hql.append(" and pay.tollCollectorId = :tollCollectorId");
        map.put("tollCollectorId",userId);

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return (BigDecimal) query.uniqueResult();

    }


    @Override
    public BigDecimal calculateTotalWXFee(String startTime, String endTime) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();

        StringBuilder hql = new StringBuilder("select sum(pay.payActualMoney) from ExamPay as pay where 1=1 and pay.payState = 2 and pay.payType=4 ");

        //当天时间
        Date date = new Date();
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fromatDateStr = simpleFormat.format(date);


        if (StringUtils.isNotEmpty(startTime)){
            fromatDateStr = startTime;
        }

        hql.append(" and pay.payTime >=:payStartTime");
        map.put("payStartTime", DateTool.stringToLongStart(fromatDateStr));

        if (StringUtils.isNotEmpty(endTime)){

            fromatDateStr = endTime;
        }
        hql.append(" and pay.payTime <=:payEndTime");
        map.put("payEndTime", DateTool.stringToLongEnd(fromatDateStr));


        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return (BigDecimal) query.uniqueResult();

    }

    @Override
    public List<ExamPay> findWXPayNoPageList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String startTimePay, String endTimePay, String isRecheck, String isPrint) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("from ExamPay as examPay where 1=1 and examPay.payType=4");

        //体检号
        if(StringUtils.isNotBlank(examNumber)){
            hql.append(" and examPay.examNumber = :examNumber");
            map.put("examNumber",examNumber);
        }

        //身份证
        if(StringUtils.isNotBlank(idCard)){
            hql.append(" and examPay.idCardNum =:idCardNum");
            map.put("idCardNum",idCard);
        }

        //开始时间
        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and examPay.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        //姓名
        if(StringUtils.isNotBlank(name)){
            hql.append(" and examPay.name like:name");
            map.put("name","%"+name+"%");
        }

        //工作单位
        if(StringUtils.isNotBlank(workUnit)){
            hql.append(" and examPay.workUnit like :workUnit");
            map.put("workUnit","%"+workUnit+"%");
        }

        //结束时间
        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and examPay.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }


        //地段区域
        if(areaId!=null){
            hql.append(" and examPay.areaId =:areaId");
            map.put("areaId",areaId);
        }

        //是否复检
        //1 复检 0 正常
        if(StringUtils.isNotBlank(isRecheck)){
            if(Integer.parseInt(isRecheck)==0){
                hql.append(" and examPay.recheckTag =0");
            }else if(Integer.parseInt(isRecheck)==1){
                hql.append(" and examPay.recheckTag >0");
            }

        }

        //是否打印
        if(StringUtils.isNotBlank(isPrint)){
            if(Integer.parseInt(isPrint)==0){
                hql.append(" and examPay.isPrint =0");
            }else if(Integer.parseInt(isPrint)==1){
                hql.append(" and examPay.isPrint >0");
            }

        }
        //支付开始时间
        //开始时间
        if(StringUtils.isNotEmpty(startTimePay)){
            hql.append(" and examPay.payTime >=:startTimePay");
            map.put("startTimePay", DateTool.stringToLongStart(startTimePay));
        }

        //结束时间
        if(StringUtils.isNotEmpty(endTimePay)){
            hql.append(" and examPay.payTime <=:endTimePay");
            map.put("endTimePay", DateTool.stringToLongEnd(endTimePay));
        }
        //排序 查询未作废的收费订单
        hql.append(" and examPay.isCancel =0  order by createTime desc ");


        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    @Override
    public Long findCountWXPayList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String startTimePay, String endTimePay, String isRecheck, String isPrint) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("select count(examPay.id) from ExamPay as examPay where 1=1 and examPay.payType=4 ");


        //体检号
        if(StringUtils.isNotBlank(examNumber)){
            hql.append(" and examPay.examNumber = :examNumber");
            map.put("examNumber",examNumber);
        }

        //身份证
        if(StringUtils.isNotBlank(idCard)){
            hql.append(" and examPay.idCardNum =:idCardNum");
            map.put("idCardNum",idCard);
        }

        //开始时间
        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and examPay.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        //姓名
        if(StringUtils.isNotBlank(name)){
            hql.append(" and examPay.name =:name");
            map.put("name",name);
        }

        //工作单位
        if(StringUtils.isNotBlank(workUnit)){
            hql.append(" and examPay.workUnit like :workUnit");
            map.put("workUnit","%"+workUnit+"%");
        }

        //结束时间
        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and examPay.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }


        //地段区域
        if(areaId!=null){
            hql.append(" and examPay.areaId =:areaId");
            map.put("areaId",areaId);
        }

        //是否复检
        //是否复检
        //1 复检 0 正常
        if(StringUtils.isNotBlank(isRecheck)){
            if(Integer.parseInt(isRecheck)==0){
                hql.append(" and examPay.recheckTag =0");
            }else if(Integer.parseInt(isRecheck)==1){
                hql.append(" and examPay.recheckTag >0");
            }

        }

        //是否打印
        if(StringUtils.isNotBlank(isPrint)){
            if(Integer.parseInt(isPrint)==0){
                hql.append(" and examPay.isPrint =0");
            }else if(Integer.parseInt(isPrint)==1){
                hql.append(" and examPay.isPrint >0");
            }

        }
        //支付开始时间
        //开始时间
        if(StringUtils.isNotEmpty(startTimePay)){
            hql.append(" and examPay.payTime >=:startTimePay");
            map.put("startTimePay", DateTool.stringToLongStart(startTimePay));
        }

        //结束时间
        if(StringUtils.isNotEmpty(endTimePay)){
            hql.append(" and examPay.payTime <=:endTimePay");
            map.put("endTimePay", DateTool.stringToLongEnd(endTimePay));
        }

        //排序 查询未作废的收费订单
        hql.append(" and examPay.isCancel =0  ");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return (long)query.uniqueResult();
    }
}
