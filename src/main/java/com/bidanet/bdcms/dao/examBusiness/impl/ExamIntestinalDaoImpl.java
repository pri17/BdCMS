package com.bidanet.bdcms.dao.examBusiness.impl;

import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.dao.examBusiness.ExamIntestinalDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamIntestinal;


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
public class ExamIntestinalDaoImpl extends BaseDaoImpl<ExamIntestinal> implements ExamIntestinalDao {

    @Override
    public List<ExamIntestinal> queryIntestinalReocrdQueryList(String startTime, String endTime, String areaId, int pageNo, int pageSize) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamIntestinal as b where 1=1");

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

        hql.append(" order by createTime desc");


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
    public List<ExamIntestinal> queryIntestinalReocrdExcelQueryList(String startTime, String endTime, String areaId) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamIntestinal as b where 1=1");

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

        hql.append(" order by createTime desc");


        Query query = getSession().createQuery(hql.toString());


        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    @Override
    public Long queryCountIntestinalReocrdQuery(String startTime, String endTime, String areaId) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("select count(b.id) from ExamIntestinal b where 1=1");

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

    @Override
    public List<ExamIntestinal> findIntestinalMember(Page<ExamIntestinal> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber, int pageCurrent, int pageSize) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        //未作废
        StringBuilder hql = new StringBuilder(" from ExamIntestinal as i where i.memberExam.isCancel=0");

        if(StringUtils.isNotEmpty(beginTime)){
            hql.append(" and i.memberExam.examTime >=:beginTime");
            map.put("beginTime", DateTool.stringToLongStart(beginTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and i.memberExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

//        if(entryState!=null){
//            hql.append(" and b.entryState =:entryState");
//            map.put("entryState", entryState);
//        }

        if(areaId!=null){
            hql.append(" and i.memberExam.areaId =:areaId");
            map.put("areaId", areaId);
        }

        if(packageId!=null){
            hql.append(" and i.memberExam.packageId =:packageId");
            map.put("packageId", packageId);
        }

        if(StringUtils.isNotEmpty(examNumber)){
            hql.append(" and i.examNumber =:examNumber");
            map.put("examNumber", examNumber);
        }

//        if(StringUtils.isNotBlank(realName)){
//            hql.append(" and ur.user.realName =:realName");
//            map.put("realName",realName);
//        }

        hql.append(" order by i.createTime DESC ");

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
    public Long countIntestinalMember(Page<ExamIntestinal> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        //未作废
        StringBuilder hql = new StringBuilder("select count(i.id) from ExamIntestinal i where i.memberExam.isCancel=0");

        if(StringUtils.isNotEmpty(beginTime)){
            hql.append(" and i.memberExam.examTime >=:beginTime");
            map.put("beginTime", DateTool.stringToLongStart(beginTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and i.memberExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

//        if(entryState!=null){
//            hql.append(" and b.entryState =:entryState");
//            map.put("entryState", entryState);
//        }

        if(areaId!=null){
            hql.append(" and i.memberExam.areaId =:areaId");
            map.put("areaId", areaId);
        }

        if(packageId!=null){
            hql.append(" and i.memberExam.packageId =:packageId");
            map.put("packageId", packageId);
        }

        if(StringUtils.isNotEmpty(examNumber)){
            hql.append(" and i.examNumber =:examNumber");
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
     * 查询疾控肠检采集信息
     * @param code
     * @return
     */
    @Override
    public List<ExamIntestinal> findIntestinalByCode(String code) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamIntestinal as i where 1=1");

        if(StringUtils.isNotBlank(code)){
            hql.append(" and i.code=:code");
            map.put("code",code);
        }

        hql.append(" order by i.createTime DESC ");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        query.setFirstResult(0);
        query.setMaxResults(1);

        return query.list();
    }
}
