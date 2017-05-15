package com.bidanet.bdcms.dao.examBusiness.impl;

import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.dao.examBusiness.ExamBlodDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamBlod;


import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
*/
@Repository
public class ExamBlodDaoImpl extends BaseDaoImpl<ExamBlod> implements ExamBlodDao  {

    @Override
    public List<ExamBlod> findBigNumberToday(Long todayStart, Long todayEnd) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamBlod as b where 1=1");

        if(todayStart!=null){
            hql.append(" and b.createTime >:todayStart");
            map.put("todayStart",todayStart);
        }
        if(todayEnd!=null){
            hql.append(" and b.createTime <:todayEnd");
            map.put("todayEnd",todayEnd);
        }

        hql.append(" order by b.createTime DESC ");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));

        }

        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.list();
    }

    @Override
    public List<ExamBlod> findBlodMember(Page<ExamBlod> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber, int pageCurrent, int pageSize) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        //未作废
        StringBuilder hql = new StringBuilder(" from ExamBlod as b where b.memberExam.isCancel=0");

        if(StringUtils.isNotEmpty(beginTime)){
            hql.append(" and b.memberExam.examTime >=:beginTime");
            map.put("beginTime", DateTool.stringToLongStart(beginTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and b.memberExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

//        if(entryState!=null){
//            hql.append(" and b.entryState =:entryState");
//            map.put("entryState", entryState);
//        }

        if(areaId!=null){
            hql.append(" and b.memberExam.areaId =:areaId");
            map.put("areaId", areaId);
        }

        if(packageId!=null){
            hql.append(" and b.memberExam.packageId =:packageId");
            map.put("packageId", packageId);
        }

        if(StringUtils.isNotEmpty(examNumber)){
            hql.append(" and b.examNumber =:examNumber");
            map.put("examNumber", examNumber);
        }

//        if(StringUtils.isNotBlank(realName)){
//            hql.append(" and ur.user.realName =:realName");
//            map.put("realName",realName);
//        }

        hql.append(" order by b.createTime DESC ");

        Query query = getSession().createQuery(hql.toString());
        query.setFirstResult((pageCurrent-1) * pageSize);
        query.setMaxResults(pageSize);

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    @Override
    public Long countBlodMember(Page<ExamBlod> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber) throws ParseException  {
        Map<String,Object> map = new HashMap<String,Object>();
        //未作废
        StringBuilder hql = new StringBuilder("select count(b.id) from ExamBlod b where b.memberExam.isCancel=0");

        if(StringUtils.isNotEmpty(beginTime)){
            hql.append(" and b.memberExam.examTime >=:beginTime");
            map.put("beginTime", DateTool.stringToLongStart(beginTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and b.memberExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

//        if(entryState!=null){
//            hql.append(" and b.entryState =:entryState");
//            map.put("entryState", entryState);
//        }

        if(areaId!=null){
            hql.append(" and b.memberExam.areaId =:areaId");
            map.put("areaId", areaId);
        }

        if(packageId!=null){
            hql.append(" and b.memberExam.packageId =:packageId");
            map.put("packageId", packageId);
        }

        if(StringUtils.isNotEmpty(examNumber)){
            hql.append(" and b.examNumber =:examNumber");
            map.put("examNumber", examNumber);
        }

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return (long)query.uniqueResult();
    }

    /**
     * 采样结果查询-血检采样集
     * @param startTime
     * @param endTime
     * @param areaId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ParseException
     */
    @Override
    public List<ExamBlod> queryBlodReocrdQueryList(String startTime, String endTime, String areaId,int pageNo, int pageSize) throws ParseException {


        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamBlod as b where 1=1");

        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and b.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and b.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        if(StringUtils.isNotEmpty(areaId)){
            hql.append(" and b.memberExam.areaId =:areaId");
            map.put("areaId", Long.parseLong(areaId));
        }

        hql.append(" order by createTime asc");


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
    public List<ExamBlod> queryBlodReocrdExcelQueryList(String startTime, String endTime, String areaId) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamBlod as b where 1=1");

        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and b.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and b.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        if(StringUtils.isNotEmpty(areaId)){
            hql.append(" and b.memberExam.areaId =:areaId");
            map.put("areaId", Long.parseLong(areaId));
        }

        hql.append(" order by createTime asc");


        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    /**
     * 采样结果查询-血检采样集
     * @param startTime
     * @param endTime
     * @param areaId
     * @return
     * @throws ParseException
     */
    @Override
    public Long queryCountBlodReocrdQuery(String startTime, String endTime, String areaId) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("select count(b.id) from ExamBlod b where 1=1");

        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and b.createTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and b.createTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        if(StringUtils.isNotEmpty(areaId)){
            hql.append(" and b.memberExam.areaId =:areaId");
            map.put("areaId", Long.parseLong(areaId));
        }

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return (long)query.uniqueResult();
    }
}
