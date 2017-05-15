package com.bidanet.bdcms.dao.examBusiness.impl;

import com.bidanet.bdcms.dao.examBusiness.ExamMemberDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamMember;


import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
*/
@Repository
public class ExamMemberDaoImpl extends BaseDaoImpl<ExamMember> implements ExamMemberDao {
    @Override
    public List<ExamMember> findById(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder(" from ExamMember where 1=1");

        hql.append(" and id =:id");
        map.put("id", id);

        hql.append(" order by create_time DESC ");
        Query query = getSession().createQuery(hql.toString());
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }

    @Override
    public List<ExamMember> findByNameID(String name,String idCard){
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder(" from ExamMember where 1=1");

        hql.append(" and name like:name and idCardNum like:idCard");
        map.put("name", "%"+name+"%");
        map.put("idCard","%"+idCard+"%");

        hql.append(" order by create_time DESC ");
        Query query = getSession().createQuery(hql.toString());
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }



}
