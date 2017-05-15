package com.bidanet.bdcms.dao.examBusiness.impl;

import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberExamDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamMember;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /**
* cf
*/
@Repository
public class ExamMemberExamDaoImpl extends BaseDaoImpl<ExamMemberExam> implements ExamMemberExamDao {
     /**
      * 查询所有的数据
      */
    @Override
    public List <ExamMemberExam> findAllNo(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies) throws ParseException{
        Map<String, Object> map = new HashMap<String, Object>();
        //未作废
        StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1 and eme.isNew=1 and eme.isCancel=0");

        if(!agencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN) ){
            hql.append(" and eme.agenciesCode=:agenciesCode");
            map.put("agenciesCode", agencies.getAgenciesCode());
        }else{
            if (StringUtils.isNotEmpty(areaId)) {
                hql.append(" and eme.areaId =:areaId");
                map.put("areaId", Long.valueOf(areaId));
            }
        }

        if (StringUtils.isNotEmpty(startTime)) {
            hql.append(" and eme.examTime >=:beginTime");
            map.put("beginTime", DateTool.stringToLongStart(startTime));
        }

        if (StringUtils.isNotEmpty(endTime)) {
            hql.append(" and eme.examTime <=:endTime");
            map.put("endTime", DateTool.stringToLongEnd(endTime));
        }

        if (StringUtils.isNotBlank(memberName)) {
            hql.append(" and eme.name like:memberName");
            map.put("memberName", "%"+memberName+"%");
        }

        if (StringUtils.isNotBlank(idNum)) {
            hql.append(" and eme.idCardNum =:idNum");
            map.put("idNum", idNum);
        }

        //复检 1 复检 0 不复检
        if (isRecheck != null) {

            if (isRecheck == 0){
                hql.append(" and eme.recheckTag =0");
            }else if(isRecheck == 1){
                hql.append(" and eme.recheckTag >0");
            }


        }

        if (verifyConclusion != null) {
            hql.append(" and eme.verifyConclusion =:verifyConclusion");
            map.put("verifyConclusion", verifyConclusion);
        }

        if (StringUtils.isNotBlank(examNumber)) {
            hql.append(" and eme.examNumber =:examNumber");
            map.put("examNumber", examNumber);
        }

        if(payState!=null){
            hql.append(" and eme.payState =:payState");
            map.put("payState", payState);
        }

        hql.append(" order by eme.createTime DESC ");

        Query query = getSession().createQuery(hql.toString());
        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }




     /**
      * 查询疫区人员
      *
      * @param memIds
      * @param beginTime
      * @param endTime
      * @param areaId
      * @param pageCurrent
      *@param pageSize @return
      * @throws ParseException
      */
     @Override
     public List<ExamMemberExam> findAffectedExamMember(String memIds, String beginTime, String endTime, String areaId, int pageCurrent, int pageSize) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder("select eme  from ExamMemberExam as eme,ExamBlod as blod  where 1=1  and eme.id = blod.memberExamId ");

         if (StringUtils.isNotEmpty(memIds)) {
             hql.append(" and eme.memberId in (" + memIds + ")");
         } else {
             hql.append(" and eme.memberId in (" + 0 + ")");
         }

         if (StringUtils.isNotEmpty(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
         }

         if (StringUtils.isNotEmpty(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         if (StringUtils.isNotEmpty(areaId)) {
             hql.append(" and eme.areaId =:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }

         /*hql.append(" order by  blod.testNumber asc,eme.examTime desc ");*/
         hql.append(" order by  eme.id desc ");

         Query query = getSession().createQuery(hql.toString());
         query.setFirstResult((pageCurrent-1) * pageSize);
         query.setMaxResults(pageSize);

         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return query.list();
     }


     /**
      * 查询疫区人员
      *
      * @param memIds
      * @param beginTime
      * @param endTime
      * @param areaId
      * @throws ParseException
      */
     @Override
     public List<ExamMemberExam> findExportAffectedExamMember(String memIds, String beginTime, String endTime, String areaId) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();
        /* StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1   ");*/
       /*gcx*/
         StringBuilder hql = new StringBuilder("select eme  from ExamMemberExam as eme,ExamBlod as blod  where 1=1  and eme.id = blod.memberExamId ");
         if (StringUtils.isNotEmpty(memIds)) {
             hql.append(" and eme.memberId in (" + memIds + ")");
         } else {
             hql.append(" and eme.memberId in (" + 0 + ")");
         }

         if (StringUtils.isNotEmpty(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
         }

         if (StringUtils.isNotEmpty(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         if (StringUtils.isNotEmpty(areaId)) {
             hql.append(" and eme.areaId =:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }

         Query query = getSession().createQuery(hql.toString());

         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return query.list();
     }

     @Override
     public Long countAffectedExamMember(String memIds, String beginTime, String endTime, String areaId) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();
        /* StringBuilder hql = new StringBuilder("select count(eme.id) from ExamMemberExam as eme where 1=1");*/
          //Gcx
         StringBuilder hql = new StringBuilder("select count(eme.id) from ExamMemberExam as eme ,ExamBlod blod  where 1=1  and eme.id = blod.memberExamId");
         if (StringUtils.isNotEmpty(memIds)) {
             hql.append(" and eme.memberId in (" + memIds + ")");
         } else {
             hql.append(" and eme.memberId in (" + 0 + ")");
         }

         if (StringUtils.isNotEmpty(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
         }

         if (StringUtils.isNotEmpty(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         if (StringUtils.isNotEmpty(areaId)) {
             hql.append(" and eme.areaId =:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }

         Query query = getSession().createQuery(hql.toString());

         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return (long) query.uniqueResult();
     }

     @Override
     public List<ExamMemberExam> getExamMemberExamList(ExamMemberExam examMemberExam, int pageNo, int pageSize,String areaId,
                                                       String memberName, String idCardNum, String beginTime, String endTime, Long categoryId) {
         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1 ");
         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.examMember.name like:memberName");
             map.put("memberName", "%" + memberName + "%");
         }
         if (StringUtils.isNotBlank(idCardNum)) {
             hql.append(" and eme.examMember.idCardNum like:idCardNum");
             map.put("idCardNum", "%" + idCardNum + "%");
         }
         /*if (examMemberExam.getAreaId() != null) {
             hql.append(" and eme.areaId=:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }*/
         if (areaId!= null) {
             hql.append(" and eme.areaId=:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }
         if (StringUtils.isNotBlank(examMemberExam.getExamNumber())) {
             hql.append(" and eme.examNumber like:examNumber");
             map.put("examNumber", "%" + examMemberExam.getExamNumber() + "%");
         }
         if (StringUtils.isNotBlank(examMemberExam.getWorkUnit())) {
             hql.append(" and eme.workUnit like:workUnit");
             map.put("workUnit", "%" + examMemberExam.getWorkUnit() + "%");
         }
         if (StringUtils.isNotBlank(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", dateStd(beginTime + " 00:00:00"));
         }
         if (StringUtils.isNotBlank(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", dateStd(endTime + " 23:59:59"));
         }
         if (examMemberExam.getVerifyConclusion() != null) {
             hql.append(" and eme.verifyConclusion =:verifyConclusion");
             map.put("verifyConclusion", examMemberExam.getVerifyConclusion());
         }
         if (examMemberExam.getVerifyState() != null) {
             hql.append(" and eme.verifyState =:verifyState");
             map.put("verifyState", examMemberExam.getVerifyState());
         }
         if (categoryId != null) {
             hql.append(" and eme.categoryId =:categoryId");
             map.put("categoryId", categoryId);
         }



         hql.append(" and eme.entryState = 1");

         hql.append(" order by examTime desc");

         Query query = getSession().createQuery(hql.toString());
         query.setFirstResult((pageNo - 1) * pageSize);
         query.setMaxResults(pageSize);
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     @Override
     public Long getCountExamMemberExamList(ExamMemberExam examMemberExam,String areaId, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId) {
         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder("select count(eme.id) from ExamMemberExam as eme where 1=1 ");
         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.examMember.name like:memberName");
             map.put("memberName", "%" + memberName + "%");
         }
         if (StringUtils.isNotBlank(idCardNum)) {
             hql.append(" and eme.examMember.idCardNum like:idCardNum");
             map.put("idCardNum", "%" + idCardNum + "%");
         }
         if (examMemberExam.getAreaId() != null) {
             hql.append(" and eme.areaId=:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }
         if (StringUtils.isNotBlank(examMemberExam.getExamNumber())) {
             hql.append(" and eme.examNumber like:examNumber");
             map.put("examNumber", "%" + examMemberExam.getExamNumber() + "%");
         }
         if (StringUtils.isNotBlank(examMemberExam.getWorkUnit())) {
             hql.append(" and eme.workUnit like:workUnit");
             map.put("workUnit", "%" + examMemberExam.getWorkUnit() + "%");
         }
         if (StringUtils.isNotBlank(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", dateStd(beginTime + " 00:00:00"));
         }
         if (StringUtils.isNotBlank(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", dateStd(endTime + " 23:59:59"));
         }
         if (examMemberExam.getVerifyConclusion() != null) {
             hql.append(" and eme.verifyConclusion =:verifyConclusion");
             map.put("verifyConclusion", examMemberExam.getVerifyConclusion());
         }
         if (examMemberExam.getVerifyState() != null) {
             hql.append(" and eme.verifyState =:verifyState");
             map.put("verifyState", examMemberExam.getVerifyState());
         }
         if (categoryId != null) {
             hql.append(" and eme.categoryId =:categoryId");
             map.put("categoryId", categoryId);
         }
         hql.append(" and eme.entryState = 1");

         Query query = getSession().createQuery(hql.toString());

         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return (long) query.uniqueResult();
     }

     @Override
     public List<ExamMemberExam> queryResultQueryList(String areaId, String name, String idCard, String examNumber, String verifyConclusion, String startTime, String endTime, int pageNo, int pageSize, String payState) throws ParseException {

         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1 and eme.isNew=1 and eme.isCancel=0 ");

         if (StringUtils.isNotBlank(name)) {
             hql.append(" and eme.name like:name");
             map.put("name", "%" + name + "%");
         }
         if (StringUtils.isNotBlank(idCard)) {
             hql.append(" and eme.idCardNum =:idCardNum");
             map.put("idCardNum", idCard );
         }
         if (StringUtils.isNotBlank(areaId)) {
             hql.append(" and eme.areaId=:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }
         if (StringUtils.isNotEmpty(examNumber)) {
             hql.append(" and eme.examNumber =:examNumber");
             map.put("examNumber", examNumber);
         }

         if (StringUtils.isNotBlank(startTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(startTime));
         }
         if (StringUtils.isNotBlank(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }
         if (StringUtils.isNotEmpty(verifyConclusion)) {
             hql.append(" and eme.verifyConclusion =:verifyConclusion");
             map.put("verifyConclusion", Integer.parseInt(verifyConclusion));
         }
         if (StringUtils.isNotEmpty(payState)) {
             hql.append(" and eme.payState =:payState");
             map.put("payState", Integer.valueOf(payState));
         }

         hql.append(" order by eme.examTime desc");


         Query query = getSession().createQuery(hql.toString());
         query.setFirstResult((pageNo - 1) * pageSize);
         query.setMaxResults(pageSize);
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     @Override
     public Long queryCountResultQuery(String areaId, String name, String idCard, String examNumber, String verifyConclusion, String startTime, String endTime, String payState) throws ParseException {


         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder("select count(eme.id) from ExamMemberExam as eme where 1=1 and eme.isNew=1 and eme.isCancel=0");

         if (StringUtils.isNotBlank(name)) {
             hql.append(" and eme.name like:name");
             map.put("name", "%" + name + "%");
         }
         if (StringUtils.isNotBlank(idCard)) {
             hql.append(" and eme.idCardNum =:idCardNum");
             map.put("idCardNum", idCard);
         }
         if (StringUtils.isNotBlank(areaId)) {
             hql.append(" and eme.areaId =:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }
         if (StringUtils.isNotEmpty(examNumber)) {
             hql.append(" and eme.examNumber =:examNumber");
             map.put("examNumber", examNumber);
         }

         if (StringUtils.isNotBlank(startTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(startTime));
         }
         if (StringUtils.isNotBlank(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }
         if (StringUtils.isNotEmpty(verifyConclusion)) {
             hql.append(" and eme.verifyConclusion =:verifyConclusion");
             map.put("verifyConclusion", Integer.parseInt(verifyConclusion));
         }

         if (StringUtils.isNotEmpty(payState)) {
             hql.append(" and eme.payState =:payState");
             map.put("payState", Integer.valueOf(payState));
         }

         hql.append(" order by eme.examTime desc");


         Query query = getSession().createQuery(hql.toString());

         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return (long) query.uniqueResult();
     }

     /**
      * 体检结果判断页面信息查询
      * @param memberName
      * @param idNum
      * @param startTime
      * @param endTime
      * @param payState         @return
      * @param areaId
      * @param isRecheck
      * @param verifyConclusion
      * @param examNumber
      * @param agencies
      * @param pageCurrent
      * @param pageSize
      */
     @Override
     public List<ExamMemberExam> findExamMemberResultDecide(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies, int pageCurrent, int pageSize) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();
         //未作废
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1 and eme.isNew=1 and eme.isCancel=0");

         if(!agencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN) ){
             hql.append(" and eme.agenciesCode=:agenciesCode");
             map.put("agenciesCode", agencies.getAgenciesCode());
         }else{
             if (StringUtils.isNotEmpty(areaId)) {
                 hql.append(" and eme.areaId =:areaId");
                 map.put("areaId", Long.valueOf(areaId));
             }
         }

         if (StringUtils.isNotEmpty(startTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(startTime));
         }

         if (StringUtils.isNotEmpty(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.name like:memberName");
             map.put("memberName", "%"+memberName+"%");
         }

         if (StringUtils.isNotBlank(idNum)) {
             hql.append(" and eme.idCardNum =:idNum");
             map.put("idNum", idNum);
         }

         //复检 1 复检 0 不复检
         if (isRecheck != null) {

             if (isRecheck == 0){
                 hql.append(" and eme.recheckTag =0");
             }else if(isRecheck == 1){
                 hql.append(" and eme.recheckTag >0");
             }


         }

         if (verifyConclusion != null) {
             hql.append(" and eme.verifyConclusion =:verifyConclusion");
             map.put("verifyConclusion", verifyConclusion);
         }

         if (StringUtils.isNotBlank(examNumber)) {
             hql.append(" and eme.examNumber =:examNumber");
             map.put("examNumber", examNumber);
         }

         if(payState!=null){
             hql.append(" and eme.payState =:payState");
             map.put("payState", payState);
         }

         hql.append(" order by eme.createTime DESC ");

         Query query = getSession().createQuery(hql.toString());
         query.setFirstResult((pageCurrent - 1) * pageSize);
         query.setMaxResults(pageSize);
         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return query.list();
     }

     @Override
     public Long countExamMemberResultDecide(Page<ExamMemberExam> page, String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();
         //未作废
         StringBuilder hql = new StringBuilder("select count(eme.id) from ExamMemberExam as eme where 1=1 and eme.isNew=1 and eme.isCancel=0");


         if(!agencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)){
             hql.append(" and eme.agenciesCode=:agenciesCode");
             map.put("agenciesCode", agencies.getAgenciesCode());
         }else{
             if (StringUtils.isNotEmpty(areaId)) {
                 hql.append(" and eme.areaId =:areaId");
                 map.put("areaId", Long.valueOf(areaId));
             }
         }

         if (StringUtils.isNotEmpty(startTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(startTime));
         }

         if (StringUtils.isNotEmpty(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

//         if (StringUtils.isNotEmpty(areaId)) {
//             hql.append(" and eme.areaId =:areaId");
//             map.put("areaId", Long.parseLong(areaId));
//         }

         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.name like:memberName");
             map.put("memberName", "%"+memberName+"%");
         }

         if (StringUtils.isNotBlank(idNum)) {
             hql.append(" and eme.idCardNum =:idNum");
             map.put("idNum", idNum);
         }

         if (isRecheck != null) {
             if (isRecheck == 0){
                 hql.append(" and eme.recheckTag =0");
             }else if(isRecheck == 1){
                 hql.append(" and eme.recheckTag >0");
             }
         }

         if (verifyConclusion != null) {
             hql.append(" and eme.verifyConclusion =:verifyConclusion");
             map.put("verifyConclusion", verifyConclusion);
         }

         if (StringUtils.isNotBlank(examNumber)) {
             hql.append(" and eme.examNumber =:examNumber");
             map.put("examNumber", examNumber);
         }

         if(payState!=null){
             hql.append(" and eme.payState =:payState");
             map.put("payState", payState);
         }

         Query query = getSession().createQuery(hql.toString());

         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return (long) query.uniqueResult();
     }

     //时间格式转换
     public Long dateStd(String time) {
         Date date = new Date();
         try {
             DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             date = df.parse(time);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return date.getTime();
     }

     public List<ExamMemberExam> findAffectedExamMemberByIds(String ids) {
         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where eme.id in (" + ids + ") order by id desc");
         Query query = getSession().createQuery(hql.toString());
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     ///////////////////////////////////复检管理/////////////////////////////////////
     @Override
     public List<ExamMemberExam> findReviceMemberList(int pageNo, int pageSize, String areaId, String workUnit, String memberName, String idCardNum, String beginTime, String endTime, String categoryId, String isRechekPrint, String examNumber) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1 and eme.recheckTag>0  ");

         if (StringUtils.isNotEmpty(workUnit)) {
             hql.append(" and eme.workUnit like :workUnit");
             map.put("workUnit", "%" + workUnit + "%");
         }

         if (StringUtils.isNotEmpty(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
         }

         if (StringUtils.isNotEmpty(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         if (StringUtils.isNotEmpty(areaId)) {
             hql.append(" and eme.areaId =:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }

         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.name like:memberName");
             map.put("memberName", "%"+memberName+"%");
         }

         if (StringUtils.isNotBlank(idCardNum)) {
             hql.append(" and eme.idCardNum =:idNum");
             map.put("idNum", idCardNum);
         }

         if (StringUtils.isNotBlank(isRechekPrint)) {
             hql.append(" and eme.isRecheckPrint =:isRecheckPrint");
             map.put("isRecheckPrint", Integer.parseInt(isRechekPrint));
         }


         if (StringUtils.isNotBlank(examNumber)) {
             hql.append(" and eme.examNumber =:examNumber");
             map.put("examNumber", examNumber);
         }

         if (StringUtils.isNotBlank(categoryId)) {
             hql.append(" and eme.categoryId =:categoryId");
             map.put("categoryId",  Long.parseLong(categoryId));
         }

         hql.append(" order by eme.createTime desc");

         Query query = getSession().createQuery(hql.toString());
         query.setFirstResult((pageNo - 1) * pageSize);
         query.setMaxResults(pageSize);
         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return query.list();
     }

     @Override
     public Long findReviceMemberCount(String areaId, String workUnit, String memberName, String idCardNum, String beginTime, String endTime, String categoryId, String isRechekPrint, String examNumber) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();

         StringBuilder hql = new StringBuilder(" select count(eme.id) from ExamMemberExam as eme where 1=1 and eme.recheckTag>0 ");

         if (StringUtils.isNotEmpty(workUnit)) {
             hql.append(" and eme.workUnit like :workUnit");
             map.put("workUnit", "%" + workUnit + "%");
         }

         if (StringUtils.isNotEmpty(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
         }

         if (StringUtils.isNotEmpty(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         if (StringUtils.isNotEmpty(areaId)) {
             hql.append(" and eme.areaId =:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }

         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.name like:memberName");
             map.put("memberName","%" + memberName + "%" );
         }

         if (StringUtils.isNotBlank(idCardNum)) {
             hql.append(" and eme.idCardNum =:idNum");
             map.put("idNum", idCardNum);
         }

         if (StringUtils.isNotBlank(isRechekPrint)) {
             hql.append(" and eme.isRecheckPrint =:isRecheckPrint");
             map.put("isRecheckPrint", Integer.parseInt(isRechekPrint));
         }


         if (StringUtils.isNotBlank(examNumber)) {
             hql.append(" and eme.examNumber =:examNumber");
             map.put("examNumber", examNumber);
         }

         if (StringUtils.isNotBlank(categoryId)) {
             hql.append(" and eme.categoryId =:categoryId");
             map.put("categoryId", Long.parseLong(categoryId));
         }

         Query query = getSession().createQuery(hql.toString());

         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return (long) query.uniqueResult();
     }

     @Override
     public List<ExamMemberExam> findByExamNumber(String examNumber) {
         Map<String,Object> map = new HashMap<String,Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1  ");

         if(StringUtils.isNotBlank(examNumber)){
             hql.append(" and eme.examNumber =:examNumber");
             map.put("examNumber", examNumber);
         }

         hql.append(" order by eme.recheckTag desc");


         Query query = getSession().createQuery(hql.toString());

         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return query.list();
     }

     //复检管理导出数据查询
     @Override
     public List<ExamMemberExam> getExportReviewList(ExamMemberExam examMemberExam, String areaId, String workUnit, String memberName, String idCardNum, String beginTime, String endTime, String categoryId, String isRechekPrint, String examNumber) throws ParseException {

         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1 and eme.recheckTag>0 ");

         if (StringUtils.isNotEmpty(workUnit)) {
             hql.append(" and eme.workUnit like :workUnit");
             map.put("workUnit", "%" + workUnit + "%");
         }

         if (StringUtils.isNotEmpty(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
         }

         if (StringUtils.isNotEmpty(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         if (StringUtils.isNotEmpty(areaId)) {
             hql.append(" and eme.areaId =:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }

         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.name =:memberName");
             map.put("memberName", memberName);
         }

         if (StringUtils.isNotBlank(idCardNum)) {
             hql.append(" and eme.idCardNum =:idNum");
             map.put("idNum", idCardNum);
         }

         if (StringUtils.isNotBlank(isRechekPrint)) {
             hql.append(" and eme.isRechekPrint =:isRechekPrint");
             map.put("isRechekPrint", Integer.parseInt(isRechekPrint));
         }


         if (StringUtils.isNotBlank(examNumber)) {
             hql.append(" and eme.examNumber =:examNumber");
             map.put("examNumber", examNumber);
         }

         if (StringUtils.isNotBlank(categoryId)) {
             hql.append(" and eme.categoryId =:categoryId");
             map.put("categoryId",  Long.parseLong(categoryId));
         }

         Query query = getSession().createQuery(hql.toString());

         for (String key :
                 map.keySet()) {
             query.setParameter(key, map.get(key));
         }

         return query.list();
     }

     @Override
     public List<ExamMemberExam> queryExamReviewList(String ids) {
         Map<String,Object> map = new HashMap<String,Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as er where 1=1");

         if(StringUtils.isNotBlank(ids)){
             hql.append(" and er.id in ("+ids+") ");
         }
         Query query = getSession().createQuery(hql.toString());
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     @Override
     public List<ExamMemberExam> getMemberExamNew(String examNumber) {
         Map<String,Object> map = new HashMap<String,Object>();
         /*StringBuilder hql = new StringBuilder(" from ExamMemberExam as er where 1=1 and er.isNew = 1 ");*/
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as er where 1=1");
         if(StringUtils.isNotBlank(examNumber)){
             hql.append(" and er.examNumber = :examNumber ");
             map.put("examNumber",  examNumber);
         }
         hql.append(" order by id desc ");
         Query query = getSession().createQuery(hql.toString());
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     @Override
     public List<ExamMemberExam> getAutoResultDecideList() {
         Map<String,Object> map = new HashMap<String,Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as er where 1=1 and er.isNew = 1 ");

         //全部判断完成
         hql.append("and er.entryState = 1 ");

         //未判断
         hql.append(" and er.verifyConclusion = 0");

        //未审核
         hql.append("and er.verifyState = 2");

         Query query = getSession().createQuery(hql.toString());
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     @Override
     public List<ExamMemberExam> findUnFinishExam() throws ParseException {
         Map<String,Object> map = new HashMap<String,Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as er where er.isNew=1 and er.isPush = 0 ");
         Date date = new Date();
         SimpleDateFormat beginTimeSdf = new SimpleDateFormat("yyyy-MM-dd");//转换格式
         String beginTime = beginTimeSdf.format(date);
         if (StringUtils.isNotBlank(beginTime)) {
             hql.append(" and eme.lastJudgeTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
             hql.append(" and eme.lastJudgeTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(beginTime));
         }

         Query query = getSession().createQuery(hql.toString());
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     // 结论判断审核excel导出信息查询
     @Override
     public List<ExamMemberExam> getExamMemberExamListExcel(ExamMemberExam memberExam, int pageCurrent, int pageSize, String areaId, String memberName, String idCardNum, String beginTime, String endTime, Long openCategoryLevelTwo, String examNumber, String workUnit, String verifyConclusion, String verifyState) {
         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where 1=1 ");
         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.name like:memberName");
             map.put("memberName", "%" + memberName + "%");
         }
         if (StringUtils.isNotBlank(idCardNum)) {
             hql.append(" and eme.idCardNum like:idCardNum");
             map.put("idCardNum", "%" + idCardNum + "%");
         }
         if (StringUtils.isNotBlank(areaId)) {
             hql.append(" and eme.areaId=:areaId");
             map.put("areaId", Long.parseLong(areaId));
         }
         if (StringUtils.isNotBlank(examNumber)) {
             hql.append(" and eme.examNumber like:examNumber");
             map.put("examNumber", "%" + examNumber + "%");
         }
         if (StringUtils.isNotBlank(workUnit)) {
             hql.append(" and eme.workUnit like:workUnit");
             map.put("workUnit", "%" + workUnit + "%");
         }
         if (StringUtils.isNotBlank(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", dateStd(beginTime + " 00:00:00"));
         }
         if (StringUtils.isNotBlank(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", dateStd(endTime + " 23:59:59"));
         }
         if (StringUtils.isNotBlank(verifyConclusion)) {
             hql.append(" and eme.verifyConclusion =:verifyConclusion");
             map.put("verifyConclusion", verifyConclusion);
         }
         if (StringUtils.isNotBlank(verifyState)) {
             hql.append(" and eme.verifyState =:verifyState");
             map.put("verifyState", verifyState);
         }
         if (openCategoryLevelTwo != null) {
             hql.append(" and eme.categoryId =:openCategoryLevelTwo");
             map.put("openCategoryLevelTwo", openCategoryLevelTwo);
         }



         hql.append(" and eme.entryState = 1");

         hql.append(" order by eme.examTime desc");

         Query query = getSession().createQuery(hql.toString());
//         query.setFirstResult((pageCurrent - 1) * pageSize);
//         query.setMaxResults(pageSize);
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     //乡镇肠检体检信息
     @Override
     public List<ExamMemberExam> getTownsMemberExamLookBack(int pageCurrent, int pageSize, String examNumber, String memberName, String beginTime, String endTime) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder(" from ExamMemberExam as eme where eme.isNew=1 and eme.isCancel=0");
         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.examMember.name like:memberName");
             map.put("memberName", "%" + memberName + "%");
         }
         if (StringUtils.isNotBlank(examNumber)) {
             hql.append(" and eme.examNumber like:examNumber");
             map.put("examNumber", "%" + examNumber + "%");
         }
         if (StringUtils.isNotBlank(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
         }
         if (StringUtils.isNotBlank(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         hql.append(" order by eme.examTime desc");

         Query query = getSession().createQuery(hql.toString());
         query.setFirstResult((pageCurrent - 1) * pageSize);
         query.setMaxResults(pageSize);
         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return query.list();
     }

     //乡镇肠检体检条数
     @Override
     public Long countTownsMemberExamLookBack(Page<ExamMemberExam> page, String examNumber, String memberName, String beginTime, String endTime) throws ParseException {
         Map<String, Object> map = new HashMap<String, Object>();
         StringBuilder hql = new StringBuilder("select count(eme.id) from ExamMemberExam as eme where eme.isNew=1 and eme.isCancel=0");

         if (StringUtils.isNotBlank(memberName)) {
             hql.append(" and eme.examMember.name like:memberName");
             map.put("memberName", "%" + memberName + "%");
         }
         if (StringUtils.isNotBlank(examNumber)) {
             hql.append(" and eme.examNumber like:examNumber");
             map.put("examNumber", "%" + examNumber + "%");
         }
         if (StringUtils.isNotBlank(beginTime)) {
             hql.append(" and eme.examTime >=:beginTime");
             map.put("beginTime", DateTool.stringToLongStart(beginTime));
         }
         if (StringUtils.isNotBlank(endTime)) {
             hql.append(" and eme.examTime <=:endTime");
             map.put("endTime", DateTool.stringToLongEnd(endTime));
         }

         Query query = getSession().createQuery(hql.toString());

         for (String key : map.keySet()) {
             query.setParameter(key, map.get(key));
         }
         return (long) query.uniqueResult();
     }


 }