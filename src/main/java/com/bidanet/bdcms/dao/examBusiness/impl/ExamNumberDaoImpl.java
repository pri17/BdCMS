package com.bidanet.bdcms.dao.examBusiness.impl;

import com.bidanet.bdcms.dao.examBusiness.ExamNumberDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamNumber;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检登记. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-28 14:15:19
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Repository
public class ExamNumberDaoImpl extends BaseDaoImpl<ExamNumber> implements ExamNumberDao {

    public List<ExamNumber> findListByCreateTime(Long todayStart,Integer areaCode) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamNumber where 1=1");

        if(todayStart!=null){
            hql.append(" and create_time >:todayStart");
            map.put("todayStart",todayStart);
        }
        if(areaCode!=null){
            hql.append(" and areaCode =:areaCode");
            map.put("areaCode",areaCode);
        }
        hql.append(" order by create_time DESC ");
        Query query = getSession().createQuery(hql.toString());
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        query.setMaxResults(1);
        return query.list();
    }
}
