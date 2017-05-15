package com.bidanet.bdcms.dao.businessSetting.impl;

import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.businessSetting.ExamDoctorDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.dao.systemSetting.ExamAgenciesDao;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamDoctor;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 体检医生daoImpl. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-18 14:30:15
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
@Repository
public class ExamDoctorDaoImpl extends BaseDaoImpl<ExamDoctor> implements ExamDoctorDao {

    @Autowired
    private ExamAgenciesDao examAgenciesDao;
    /**
     * 不排序查询行业分类数据
     * @return
     */
    @Override
    public List<ExamDoctor> findDoctorByExample(String doctorName,String projectName,Long areaId,Long agenciesId,int pageCurrent,int pageSize) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamDoctor ed where 1=1");
        if (doctorName!=null && doctorName!="") {
            hql.append(" and ed.doctorName=:doctorName");
            map.put("doctorName",doctorName);
        }
        if (projectName!=null && projectName!="") {
            hql.append(" and ed.projectName=:projectName");
            map.put("projectName",projectName);
        }
        if (areaId!=null) {
            hql.append(" and ed.areaId=:areaId");
            map.put("areaId",areaId);
        }
        if (agenciesId!=null) {
            hql.append(" and ed.agenciesId=:agenciesId");
            map.put("agenciesId",agenciesId);
        }
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
    public List<ExamDoctor> getAllDoctor(Long agenciesId) {

        ExamAgencies examAgencies = examAgenciesDao.get(agenciesId);

        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamDoctor ed where 1=1");

        if (agenciesId!=null && !examAgencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)) {
            hql.append(" and ed.agenciesId=:agenciesId");
            map.put("agenciesId",agenciesId);
        }
        hql.append(" order by ed.code asc,ed.parentId ASC ,ed.projectId  asc ");
        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }
}
