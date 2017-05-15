package com.bidanet.bdcms.dao.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamAddDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamMember;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检登记. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-14 14:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Repository
public class ExamAddDaoImpl extends BaseDaoImpl<ExamMember> implements ExamAddDao{

    public List<ExamMember> getExamMemberByIdCardNum(String idCardNum) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamMember where 1=1");

        if(StringUtils.isNotBlank(idCardNum)){
            hql.append(" and idCardNum =:idCardNum");
            map.put("idCardNum",idCardNum);
        }
        hql.append(" order by create_time DESC ");
        Query query = getSession().createQuery(hql.toString());
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }

}
