package com.bidanet.bdcms.dao.queryStatic.impl;

import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.dao.queryStatic.ExamTogetherQueryDao;
import com.bidanet.bdcms.entity.ExamMemberExam;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询统计-体检综合查询. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-09 20:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Repository
public class ExamTogetherQueryDaoImpl extends BaseDaoImpl<ExamMemberExam> implements ExamTogetherQueryDao {

    //体检综合查询excel导出查询
    @Override
    public List<ExamMemberExam> queryTogetherList(Long togetherCategoryLevelTwoId, String channel, String eCardNumber,String name,String areaId, String idCard, String workUnit,
                                                  String examNumber,String startTime, String endTime, String payState, String payType,int pageNo, int pageSize)  throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamMemberExam as memebrExam where 1=1");

        //姓名
        if(StringUtils.isNotBlank(name)){
            hql.append(" and memebrExam.member.name like :name");
            map.put("name","%"+name+"%");
        }
        if(StringUtils.isNotBlank(eCardNumber)){
            hql.append(" and memebrExam.examEcard.eCardNumber =:eCardNumber");
            map.put("eCardNumber",eCardNumber);
        }
        //身份证
        if(StringUtils.isNotBlank(idCard)){
            hql.append(" and memebrExam.member.idCardNum =:idCard");
            map.put("idCardNum",idCard);
        }
        if(StringUtils.isNotBlank(channel)){
            hql.append(" and memebrExam.channel =:channel");
            map.put("channel",channel);
        }
        if(StringUtils.isNotBlank(areaId)){
            hql.append(" and memebrExam.areaId =:areaId");
            map.put("areaId",Long.valueOf(areaId));
        }
        //工作单位
        if(StringUtils.isNotBlank(workUnit)){
            hql.append(" and memebrExam.workUnit like :workUnit");
            map.put("workUnit","%"+workUnit+"%");
        }
        //体检号
        if(StringUtils.isNotBlank(examNumber)){
            hql.append(" and memebrExam.examNumberId = :examNumber");
            map.put("examNumber",Long.parseLong(examNumber));
        }
        //时间
        if(StringUtils.isNotBlank(startTime)){
            hql.append(" and memebrExam.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        //时间
        if(StringUtils.isNotBlank(endTime)){
            hql.append(" and memebrExam.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

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

        if(togetherCategoryLevelTwoId!=null){
            hql.append(" and memebrExam.categoryId=:categoryId");
            map.put("categoryId",togetherCategoryLevelTwoId);
        }

        Query query = getSession().createQuery(hql.toString());

//        query.setFirstResult((pageNo-1) * pageSize);
//        query.setMaxResults(pageSize);

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    @Override
    public List<ExamMemberExam> queryTogetherNoPageList(String name,String eCardNumber,Long categoryId, String areaId, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck, String isQualified,String sortByTime,String channel
                                                        , String insState) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamMemberExam as memebrExam where 1=1");

        //姓名
        if(StringUtils.isNotBlank(name)){
            hql.append(" and memebrExam.name like :name");
            map.put("name","%"+name+"%");
        }
        //身份证
        if(StringUtils.isNotBlank(idCard)){
            hql.append(" and memebrExam.idCardNum =:idCardNum");
            map.put("idCardNum",idCard);
        }
        //地段区域
        if(StringUtils.isNotBlank(areaId)){
            hql.append(" and memebrExam.areaId =:areaId");
            map.put("areaId",Long.valueOf(areaId));
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
        //时间
        if(StringUtils.isNotBlank(startTime)){
            hql.append(" and memebrExam.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        //时间
        if(StringUtils.isNotBlank(endTime)){
            hql.append(" and memebrExam.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        //体检结果
        if(StringUtils.isNotBlank(isRecheck)){
            hql.append(" and memebrExam.isRecheck =:isRecheck");
            map.put("isRecheck", Integer.parseInt(isRecheck));
        }


        //支付方式
        if(StringUtils.isNotBlank(payType)){
            hql.append(" and memebrExam.payType = :payType");
            map.put("payType",Integer.valueOf(payType));
        }

        //支付状态
        if(StringUtils.isNotBlank(payState)){
            hql.append(" and memebrExam.payState=:payState");
            map.put("payState",Integer.valueOf(payState));
        }

        //行业
        if (categoryId!=null) {
            hql.append(" and memebrExam.categoryId =:categoryId");
            map.put("categoryId",categoryId);
        }

        if (StringUtils.isNotEmpty(eCardNumber)){
            hql.append(" and memebrExam.eCardNumber =:eCardNumber");
            map.put("eCardNumber",eCardNumber);
        }

        if (StringUtils.isNotEmpty(channel)){
            hql.append(" and memebrExam.channel =:channel");
            map.put("channel", Integer.parseInt(channel));
        }

        if (StringUtils.isNotBlank(insState)){
            hql.append(" and memebrExam.insState =:insState");
            map.put("insState", Integer.valueOf(insState));
        }

        //排序
        if (StringUtils.isNotEmpty(sortByTime)){

            //升序
            if(Integer.parseInt(sortByTime)==1){

                hql.append(" order by examTime asc");

            }else if(Integer.parseInt(sortByTime)==2){//降序

                hql.append(" order by examTime desc");

            }

        }else{

            hql.append(" order by examTime desc");
        }


        Query query = getSession().createQuery(hql.toString());



        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    @Override
    public Long queryCountTogetherList(String name,String eCardNumber,Long categoryId,String areaId, String idCard, String workUnit, String examNumber,String startTime, String endTime, String payState, String payType, String isRecheck,
                                       String isQualified,String sortByTime,String channel,String insState) throws ParseException{
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("select count(memebrExam.id) from ExamMemberExam as memebrExam where 1=1");
        //姓名
        if(StringUtils.isNotBlank(name)){
            hql.append(" and memebrExam.name =:name");
            map.put("name",name);
        }
        //身份证
        if(StringUtils.isNotBlank(idCard)){
            hql.append(" and memebrExam.idCardNum =:idCardNum");
            map.put("idCardNum",idCard);
        }
        //地段区域
        if(StringUtils.isNotBlank(areaId)){
            hql.append(" and memebrExam.areaId =:areaId");
            map.put("areaId",Long.parseLong(areaId));
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
        //时间
        if(StringUtils.isNotBlank(startTime)){
            hql.append(" and memebrExam.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        //时间
        if(StringUtils.isNotBlank(endTime)){
            hql.append(" and memebrExam.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }
        //支付方式
        if(StringUtils.isNotBlank(payType)){
            hql.append(" and memebrExam.payType = :payType");
            map.put("payType",Integer.valueOf(payType));
        }

        //支付状态
        if(StringUtils.isNotBlank(payState)){
            hql.append(" and memebrExam.payState=:payState");
            map.put("payState",Integer.valueOf(payState));
        }
        //行业
        if (categoryId!=null) {
            hql.append(" and memebrExam.categoryId =:categoryId");
            map.put("categoryId",categoryId);
        }

        if (StringUtils.isNotEmpty(eCardNumber)){
            hql.append(" and memebrExam.eCardNumber =:eCardNumber");
            map.put("eCardNumber",eCardNumber);
        }

        if (StringUtils.isNotEmpty(channel)){
            hql.append(" and memebrExam.channel =:channel");
            map.put("channel", Integer.parseInt(channel));
        }

        if (StringUtils.isNotBlank(insState)){
            hql.append(" and memebrExam.insState =:insState");
            map.put("insState", Integer.valueOf(insState));
        }

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return (long)query.uniqueResult();
    }

    @Override
    public List<ExamMemberExam> queryTogetherList(String ids) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamMemberExam as memebrExam where 1=1");

        if(StringUtils.isNotBlank(ids)){
            hql.append(" and memebrExam.id in ("+ids+") ");
        }
        Query query = getSession().createQuery(hql.toString());
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }
}
