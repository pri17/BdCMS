package com.bidanet.bdcms.dao.examBusiness.impl;

import com.bidanet.bdcms.dao.examBusiness.ExamReviewDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamReview;


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
public class ExamReviewDaoImpl extends BaseDaoImpl<ExamReview> implements ExamReviewDao {

    @Override
    public List<ExamReview> getExamReviewList(ExamReview examReview, int pageNo, int pageSize, String memberName,
                                            String idCardNum, String examBeginTime, String examEndTime,
                                            Long categoryId) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamReview as eme where 1=1 ");
        if(StringUtils.isNotBlank(memberName)){
            hql.append(" and eme.examMember.name like:memberName");
            map.put("memberName","%"+memberName+"%");
        }
        if(StringUtils.isNotBlank(idCardNum)){
            hql.append(" and eme.examMember.idCardNum like:idCardNum");
            map.put("idCardNum","%"+idCardNum+"%");
        }
        if(StringUtils.isNotBlank(examReview.getExamNumber())){
            hql.append(" and eme.examNumber like:examNumber");
            map.put("examNumber","%"+examReview.getExamNumber()+"%");
        }
        if (StringUtils.isNotBlank(examBeginTime)) {
            hql.append(" and eme.examTime >=:beginTime");
            map.put("beginTime",dateStd(examBeginTime+" 00:00:00","yyyy-MM-dd 00:00:00"));
        }
        if (StringUtils.isNotBlank(examEndTime)) {
            hql.append(" and eme.examTime <=:endTime");
            map.put("endTime",dateStd(examEndTime+" 23:59:59","yyyy-MM-dd 23:59:59"));
        }
        if (examReview.getIsPrint()!=null) {
            hql.append(" and eme.isPrint =:isPrint");
            map.put("isPrint",examReview.getIsPrint());
        }
        if (categoryId!=null) {
            hql.append(" and eme.categoryId =:categoryId");
            map.put("categoryId",categoryId);
        }
        if (examReview.getAreaId()!=null) {
            hql.append(" and eme.areaId =:areaId");
            map.put("areaId",examReview.getAreaId());
        }
//        if (StringUtils.isNotBlank(examReview.getWorkUnit())) {
//            hql.append(" and eme.workUnit =:workUnit");
//            map.put("workUnit",examReview.getWorkUnit());
//        }
        Query query = getSession().createQuery(hql.toString());
        query.setFirstResult((pageNo-1) * pageSize);
        query.setMaxResults(pageSize);
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }

    /**
     * 根据memberExamId获取所有的复检数据
     * @param memberExamId
     * @return
     */
    @Override
    public List<ExamReview> findReviewByMemberExamId(Long memberExamId) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamReview as eme where 1=1 ");

        hql.append(" and eme.memberExamId =:memberExamId");
        map.put("memberExamId",memberExamId);

        Query query = getSession().createQuery(hql.toString());

        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }

    //时间格式转换
    public Long dateStd(String time,String timeFormat) {
        Date date = new Date();
        try {
            DateFormat df = new SimpleDateFormat(timeFormat);
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    @Override
    public List<ExamReview> queryExamReviewList(String ids) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamReview as er where 1=1");

        if(StringUtils.isNotBlank(ids)){
            hql.append(" and er.id in ("+ids+") ");
        }
        Query query = getSession().createQuery(hql.toString());
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }
}
