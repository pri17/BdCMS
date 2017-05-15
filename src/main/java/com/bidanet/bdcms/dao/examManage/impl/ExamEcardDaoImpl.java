package com.bidanet.bdcms.dao.examManage.impl;

import com.bidanet.bdcms.dao.examManage.ExamEcardDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamEcard;
import com.bidanet.bdcms.util.DateTool;
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
public class ExamEcardDaoImpl extends BaseDaoImpl<ExamEcard> implements ExamEcardDao {
    @Override
    public List<ExamEcard> getExamEcardList(ExamEcard examEcard, int pageNo, int pageSize, String memberName,
                                            String idCardNum, String examBeginTime, String examEndTime,
                                            Long categoryId,String beginIssueTime,String endIssueTime,String beginAuditTime,String endAuditTime) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamEcard as eme where 1=1 ");
        if(StringUtils.isNotBlank(memberName)){
            hql.append(" and eme.examMember.name like:memberName");
            map.put("memberName","%"+memberName+"%");
        }
        if(StringUtils.isNotBlank(idCardNum)){
            hql.append(" and eme.examMember.idCardNum like:idCardNum");
            map.put("idCardNum","%"+idCardNum+"%");
        }
        if(examEcard.getAreaId()!=null){
            hql.append(" and eme.areaId=:areaId");
            map.put("areaId",examEcard.getAreaId());
        }
        if(StringUtils.isNotBlank(examEcard.getExamNumber())){
            hql.append(" and eme.examNumber like:examNumber");
            map.put("examNumber","%"+examEcard.getExamNumber()+"%");
        }
        if(StringUtils.isNotBlank(examEcard.getWorkUnit())){
            hql.append(" and eme.workUnit like:workUnit");
            map.put("workUnit","%"+examEcard.getWorkUnit()+"%");
        }
        if(StringUtils.isNotBlank(examEcard.geteCardNumber())){
            hql.append(" and eme.eCardNumber like:eCardNumber");
            map.put("eCardNumber","%"+examEcard.geteCardNumber()+"%");
        }
        if (StringUtils.isNotBlank(examBeginTime)) {
            hql.append(" and eme.examTime >=:beginTime");
            map.put("beginTime",dateStd(examBeginTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(examEndTime)) {
            hql.append(" and eme.examTime <=:endTime");
            map.put("endTime",dateStd(examEndTime+" 23:59:59"));
        }
        if (StringUtils.isNotBlank(beginIssueTime)) {
            hql.append(" and eme.issueTime >=:beginIssueTime");
            map.put("beginIssueTime",dateStd(beginIssueTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(endIssueTime)) {
            hql.append(" and eme.issueTime <=:endIssueTime");
            map.put("endIssueTime",dateStd(endIssueTime+" 23:59:59"));
        }
        if (StringUtils.isNotBlank(beginAuditTime)) {
            hql.append(" and eme.auditTime >=:beginAuditTime");
            map.put("beginAuditTime",dateStd(beginAuditTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(endAuditTime)) {
            hql.append(" and eme.auditTime <=:endAuditTime");
            map.put("endAuditTime",dateStd(endAuditTime+" 23:59:59"));
        }
        if (examEcard.getIsPrint()!=null) {
            hql.append(" and eme.isPrint =:isPrint");
            map.put("isPrint",examEcard.getIsPrint());
        }
        if (categoryId!=null) {
            hql.append(" and eme.categoryId =:categoryId");
            map.put("categoryId",categoryId);
        }
        Query query = getSession().createQuery(hql.toString());
        query.setFirstResult((pageNo-1) * pageSize);
        query.setMaxResults(pageSize);
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
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

    public List<ExamEcard> getExamEcardByMemberId(Long memberId) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamEcard as eme where 1=1 ");
        hql.append(" and eme.memberId="+memberId);
        Date date = new Date();
        hql.append(" and eme.expTime >=:expTime");
        map.put("expTime",dateStd(date.getTime()+" 00:00:00"));
        Query query = getSession().createQuery(hql.toString());
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }

    //健康证数据导出
    @Override
    public List<ExamEcard> getExamEcardExportList(ExamEcard examEcard, String workUnit, String examNumber, String areaId, String eCardNumber, String isRechekPrint, String memberName,
                                                  String idCardNum, String beginExamTime, String endExamTime, Long categoryId,
                                                  String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamEcard as eme where 1=1 ");
        if(StringUtils.isNotBlank(memberName)){
            hql.append(" and eme.examMember.name like:memberName");
            map.put("memberName","%"+memberName+"%");
        }
        if(StringUtils.isNotBlank(idCardNum)){
            hql.append(" and eme.examMember.idCardNum like:idCardNum");
            map.put("idCardNum","%"+idCardNum+"%");
        }
        if(StringUtils.isNotBlank(areaId)){
            hql.append(" and eme.areaId=:areaId");
            map.put("areaId",Long.valueOf(areaId));
        }
        if(StringUtils.isNotBlank(eCardNumber)){
            hql.append(" and eme.eCardNumber=:eCardNumber");
            map.put("eCardNumber",eCardNumber);
        }
        if(StringUtils.isNotBlank(isRechekPrint)){
            hql.append(" and eme.isPrint=:isRechekPrint");
            map.put("isRechekPrint",Integer.valueOf(isRechekPrint));
        }
        if(StringUtils.isNotBlank(examNumber)){
            hql.append(" and eme.examNumber like:examNumber");
            map.put("examNumber","%"+examNumber+"%");
        }
        if(StringUtils.isNotBlank(workUnit)){
            hql.append(" and eme.workUnit like:workUnit");
            map.put("workUnit","%"+workUnit+"%");
        }
        if(StringUtils.isNotBlank(eCardNumber)){
            hql.append(" and eme.eCardNumber like:eCardNumber");
            map.put("eCardNumber","%"+eCardNumber+"%");
        }
        if (StringUtils.isNotBlank(beginExamTime)) {
            hql.append(" and eme.examTime >=:beginExamTime");
            map.put("beginExamTime",dateStd(beginExamTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(endExamTime)) {
            hql.append(" and eme.examTime <=:endExamTime");
            map.put("endExamTime",dateStd(endExamTime+" 23:59:59"));
        }
        if (StringUtils.isNotBlank(beginIssueTime)) {
            hql.append(" and eme.issueTime >=:beginIssueTime");
            map.put("beginIssueTime",dateStd(beginIssueTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(endIssueTime)) {
            hql.append(" and eme.issueTime <=:endIssueTime");
            map.put("endIssueTime",dateStd(endIssueTime+" 23:59:59"));
        }
        if (StringUtils.isNotBlank(beginAuditTime)) {
            hql.append(" and eme.auditTime >=:beginAuditTime");
            map.put("beginAuditTime",dateStd(beginAuditTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(endAuditTime)) {
            hql.append(" and eme.auditTime <=:endAuditTime");
            map.put("endAuditTime",dateStd(endAuditTime+" 23:59:59"));
        }
        if (categoryId!=null) {
            hql.append(" and eme.categoryId =:categoryId");
            map.put("categoryId",Long.valueOf(categoryId));
        }
        Query query = getSession().createQuery(hql.toString());

        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }

    @Override
    public List<ExamEcard> getEcardNoPageList(String eCardNumner, String examNumber, String workUnit, String areaId, String isPrint, String memberName, String idCardNum, String beginTime, String endTime, String categoryId, String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamEcard as eCard where 1=1 ");

        if(StringUtils.isNotBlank(memberName)){
            hql.append(" and eCard.name like:memberName");
            map.put("memberName","%"+memberName+"%");
        }
        //wanglu
        //修改查询字段   改成idCardNumber
        if(StringUtils.isNotBlank(idCardNum)){
            hql.append(" and eCard.idCardNumber =:idCardNum");
            map.put("idCardNum",idCardNum);
        }
        if(StringUtils.isNotEmpty(areaId)){
            hql.append(" and eCard.areaId=:areaId");
            map.put("areaId",Long.parseLong(areaId));
        }
        if(StringUtils.isNotBlank(examNumber)){
            hql.append(" and eCard.examNumber =:examNumber");
            map.put("examNumber",examNumber);
        }
        if(StringUtils.isNotBlank(workUnit)){
            hql.append(" and eCard.workUnit like:workUnit");
            map.put("workUnit","%"+workUnit+"%");
        }
        if(StringUtils.isNotBlank(eCardNumner)){
            hql.append(" and eCard.eCardNumber =:eCardNumber");
            map.put("eCardNumber",eCardNumner);
        }
        if (StringUtils.isNotBlank(beginTime)) {
            hql.append(" and eCard.examTime >=:beginTime");
            map.put("beginTime",dateStd(beginTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(endTime)) {
            hql.append(" and eCard.examTime <=:endTime");
            map.put("endTime",dateStd(endTime+" 23:59:59"));
        }
        if (StringUtils.isNotBlank(beginIssueTime)) {
            hql.append(" and eCard.issueTime >=:beginIssueTime");
            map.put("beginIssueTime",dateStd(beginIssueTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(endIssueTime)) {
            hql.append(" and eCard.issueTime <=:endIssueTime");
            map.put("endIssueTime",dateStd(endIssueTime+" 23:59:59"));
        }
        if (StringUtils.isNotBlank(beginAuditTime)) {
            hql.append(" and eCard.auditTime >=:beginAuditTime");
            map.put("beginAuditTime",dateStd(beginAuditTime+" 00:00:00"));
        }
        if (StringUtils.isNotBlank(endAuditTime)) {
            hql.append(" and eCard.auditTime <=:endAuditTime");
            map.put("endAuditTime",dateStd(endAuditTime+" 23:59:59"));
        }
        if (StringUtils.isNotBlank(isPrint)) {
            hql.append(" and eCard.isPrint =:isPrint");
            map.put("isPrint",Integer.parseInt(isPrint));
        }
        if (StringUtils.isNotBlank(categoryId)) {
            hql.append(" and eCard.categoryId =:categoryId");
            map.put("categoryId",Long.parseLong(categoryId));
        }

        hql.append(" order by auditTime asc");
        Query query = getSession().createQuery(hql.toString());

        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }

    /**
     * 当天未上传的
     * @return
     */
    @Override
    public List<ExamEcard> getExamEcardByUpload() {
        StringBuilder hql = new StringBuilder("from ExamEcard as eme where 1=1 ");
/*        long startTime= DateTool.getNowTime();
        hql.append(" and eme.createTime>="+startTime);
        long endTime=DateTool.getEndTime();
        hql.append(" and eme.createTime<="+endTime);
        hql.append(" and eme.isUpload=0");*/
        Query query = getSession().createQuery(hql.toString());
        return query.list();
    }


    /**
     * 当天未上传的 todo 已经修改改为传时间段进行查询 wanglu

     * @return
     */
    @Override
    public List<ExamEcard> getExamEcardByUpload(long startTime,long endTime) {
        StringBuilder hql = new StringBuilder("from ExamEcard as eme where 1=1 ");
        hql.append(" and eme.createTime>="+startTime);;
        hql.append(" and eme.createTime<="+endTime);
        hql.append(" and eme.isUpload=0");
        Query query = getSession().createQuery(hql.toString());
        return query.list();
    }

//    /**
//     * 批量修改上传状态
//     * @param examEcards
//     */
//    @Override
//    public void updateIsUploadT(List<ExamEcard> examEcards) {
//
//    }
}
