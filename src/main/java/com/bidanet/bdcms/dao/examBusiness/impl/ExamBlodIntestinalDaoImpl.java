package com.bidanet.bdcms.dao.examBusiness.impl;

import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.examBusiness.ExamBlodIntestinalDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;


import com.bidanet.bdcms.entity.User;
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
public class ExamBlodIntestinalDaoImpl extends BaseDaoImpl<ExamBlodIntestinal> implements ExamBlodIntestinalDao {

    @Override
    public List<ExamBlodIntestinal> findByMemberId(Long id){
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuffer hql = new StringBuffer();
        hql.append("from ExamBlodIntestinal as eBi where 1=1 and eBi.memberId =:id");
        map.put("id",id);
        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();

    }




    @Override
    public List<ExamBlodIntestinal> findBIRecordQueryList(String startTime, String endTime, String areaId, String sampleType, int pageNo, int pageSize) {

        Map<String,Object> map = new HashMap<String,Object>();

        StringBuffer  hql = new StringBuffer();

        if (Integer.parseInt(sampleType) == 1){

            hql.append("  from ExamBlodIntestinal as eBi,ExamBlod b where 1=1 and eBi.examBlodId = b.id  ");

            //体检开始时间
            if(StringUtils.isNotBlank(startTime)){
                hql.append(" and b.creatTime >=:startTime");
                map.put("startTime",startTime+"00:00:00");
            }

            //体检结束时间
            if(StringUtils.isNotBlank(endTime)){
                hql.append(" and b.createTime <=:endTime");
                map.put("endTime",endTime+"23:59:59");
            }

        }else if(Integer.parseInt(sampleType) ==2){

            hql.append("  from ExamBlodIntestinal as eBi,ExamIntestinal i  where 1=1 and eBi.examIntestinalId = i.id  ");


            //体检开始时间
            if(StringUtils.isNotBlank(startTime)){
                hql.append(" and i.creatTime >=:startTime");
                map.put("startTime",startTime+"00:00:00");
            }

            //体检结束时间
            if(StringUtils.isNotBlank(endTime)){
                hql.append(" and i.createTime <=:endTime");
                map.put("endTime",endTime+"23:59:59");
            }

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
    public Long findCountBIRecordList(String startTime, String endTime, String areaId, String sampleType) {
        return null;
    }

    /**
     * 根据memberExamId获取所有对应的数据 便于删除操作
     * @param memberExamId
     * @return
     */
    @Override
    public List<ExamBlodIntestinal> findByMemberExamId(Long memberExamId) {
        Map<String,Object> map = new HashMap<String,Object>();

        StringBuffer  hql = new StringBuffer();


        hql.append("  from ExamBlodIntestinal as eBi where 1=1   ");

        hql.append(" and eBi.memberExamId =:memberExamId");
        map.put("memberExamId",memberExamId);

        Query query = getSession().createQuery(hql.toString());


        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    /**
     * 查询筛选范围内的血检信息对应的血检结果
     * @param beginTime
     * @param endTime
     * @param entryState
     * @param areaId
     * @param packageId
     * @param examNumber
     * @param type
     * @return
     */
    @Override
    public List<ExamBlodIntestinal> defaultSelectedResultByCondition(String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber, String type) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamBlodIntestinal as bi where bi.isNew=1");

        if(StringUtils.isNotEmpty(beginTime)){
            hql.append(" and bi.memberExam.examTime >=:beginTime");
            map.put("beginTime", DateTool.stringToLongStart(beginTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and bi.memberExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

//        if(entryState!=null){
//            hql.append(" and b.entryState =:entryState");
//            map.put("entryState", entryState);
//        }

        if(areaId!=null){
            hql.append(" and bi.memberExam.areaId =:areaId");
            map.put("areaId", areaId);
        }

        if(packageId!=null){
            hql.append(" and bi.memberExam.packageId =:packageId");
            map.put("packageId", packageId);
        }

        if(StringUtils.isNotEmpty(examNumber)){
            hql.append(" and bi.examNumber =:examNumber");
            map.put("examNumber", examNumber);
        }

        if("blod".equals(type)){
            hql.append(" and bi.examBlodId is not null");
        }else if("intestinal".equals(type)){
            hql.append(" and bi.examIntestinalId is not null");
        }

        hql.append(" order by bi.memberExamId DESC ");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    /**
     * 设置筛选范围内的体检结果都设为合格
     * @param memberName
     * @param idNum
     * @param startTime
     * @param endTime
     * @param payState
     * @param areaId
     * @param isRecheck
     * @param verifyConclusion
     * @param examNumber
     * @param loginUser
     * @return
     */
    @Override
    public List<ExamBlodIntestinal> defaultSelectedResultDecide(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, User loginUser) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamBlodIntestinal as bi where bi.isNew=1");

        if(StringUtils.isNotEmpty(memberName)){
            hql.append(" and bi.memberExam.examMember.name =:memberName");
            map.put("memberName",memberName);
        }

        if(StringUtils.isNotEmpty(idNum)){
            hql.append(" and bi.memberExam.examMember.idCardNum =:idNum");
            map.put("idNum",idNum);
        }

        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and bi.memberExam.startTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and bi.memberExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        if(payState!=null){
            hql.append(" and b.payState =:payState");
            map.put("payState", payState);
        }

        if(areaId!=null){
            hql.append(" and bi.memberExam.areaId =:areaId");
            map.put("areaId", Long.valueOf(areaId));
        }

        if(isRecheck!=null){
            hql.append(" and bi.memberExam.isRecheck =:isRecheck");
            map.put("isRecheck", isRecheck);
        }

        if(verifyConclusion!=null){
            hql.append(" and bi.memberExam.verifyConclusion =:verifyConclusion");
            map.put("verifyConclusion", verifyConclusion);
        }

        if(StringUtils.isNotEmpty(examNumber)){
            hql.append(" and bi.examNumber =:examNumber");
            map.put("examNumber", examNumber);
        }

        hql.append(" order by bi.memberExamId DESC ");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    //体检结果判断信息导出查询
    @Override
    public List<ExamBlodIntestinal> findExcelResultDecide(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies, int pageCurrent, int pageSize) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamBlodIntestinal as bi where bi.isCancel=0 and bi.memberExam.isNew=1");

        if(!agencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN) ){
            hql.append(" and bi.memberExam.agenciesCode=:agenciesCode");
            map.put("agenciesCode", agencies.getAgenciesCode());
        }else{
            if (StringUtils.isNotEmpty(areaId)) {
                hql.append(" and bi.memberExam.areaId =:areaId");
                map.put("areaId", Long.valueOf(areaId));
            }
        }

        if(StringUtils.isNotEmpty(memberName)){
            hql.append(" and bi.memberExam.examMember.name =:memberName");
            map.put("memberName",memberName);
        }

        if(StringUtils.isNotEmpty(idNum)){
            hql.append(" and bi.memberExam.examMember.idCardNum =:idNum");
            map.put("idNum",idNum);
        }

        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and bi.memberExam.examTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and bi.memberExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        if(payState!=null){
            hql.append(" and bi.memberExam.examPay.payState =:payState");
            map.put("payState", payState);
        }

//        if(areaId!=null){
//            hql.append(" and bi.memberExam.areaId =:areaId");
//            map.put("areaId", Long.valueOf(areaId));
//        }

//        if(isRecheck!=null){
//            hql.append(" and bi.memberExam.isRecheck =:isRecheck");
//            map.put("isRecheck", isRecheck);
//        }
        //复检 1 复检 0 不复检
        if (isRecheck != null) {

            if (isRecheck == 0){
                hql.append(" and bi.recheckTag =0");
            }else if(isRecheck == 1){
                hql.append(" and bi.recheckTag >0");
            }


        }

        if(verifyConclusion!=null){
            hql.append(" and bi.memberExam.verifyConclusion =:verifyConclusion");
            map.put("verifyConclusion", verifyConclusion);
        }

        if(StringUtils.isNotEmpty(examNumber)){
            hql.append(" and bi.examNumber =:examNumber");
            map.put("examNumber", examNumber);
        }

        hql.append(" order by bi.examNumber DESC ");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    ////体检结果查询信息导出查询
    @Override
    public List<ExamBlodIntestinal> findExcelResultQuery(String name, String idCard, String startTime, String endTime, String payState, String areaId,
                                                         String verifyConclusion, String examNumber, int pageCurrent, int pageSize) throws ParseException {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamBlodIntestinal as bi where bi.isCancel=0 and bi.memberExam.isNew=1");

        if (StringUtils.isNotEmpty(areaId)) {
            hql.append(" and bi.memberExam.areaId =:areaId");
            map.put("areaId", Long.valueOf(areaId));
        }

        if(StringUtils.isNotEmpty(name)){
            hql.append(" and bi.memberExam.examMember.name =:name");
            map.put("name",name);
        }

        if(StringUtils.isNotEmpty(idCard)){
            hql.append(" and bi.memberExam.examMember.idCardNum =:idCard");
            map.put("idCard",idCard);
        }

        if(StringUtils.isNotEmpty(startTime)){
            hql.append(" and bi.memberExam.examTime >=:startTime");
            map.put("startTime", DateTool.stringToLongStart(startTime));
        }

        if(StringUtils.isNotEmpty(endTime)){
            hql.append(" and bi.memberExam.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        if(StringUtils.isNotEmpty(payState)){
            hql.append(" and bi.memberExam.examPay.payState =:payState");
            map.put("payState", Integer.valueOf(payState));
        }

        if(StringUtils.isNotEmpty(verifyConclusion)){
            hql.append(" and bi.memberExam.verifyConclusion =:verifyConclusion");
            map.put("verifyConclusion", Integer.valueOf(verifyConclusion));
        }

        if(StringUtils.isNotEmpty(examNumber)){
            hql.append(" and bi.examNumber =:examNumber");
            map.put("examNumber", examNumber);
        }

        hql.append(" order by bi.examNumber DESC ");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }




}
