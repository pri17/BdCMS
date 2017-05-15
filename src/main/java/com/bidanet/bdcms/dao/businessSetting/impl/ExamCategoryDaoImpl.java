package com.bidanet.bdcms.dao.businessSetting.impl;

import com.bidanet.bdcms.dao.businessSetting.ExamCategoryDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.ExamCategory;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* cf
*/
@Repository
public class ExamCategoryDaoImpl extends BaseDaoImpl<ExamCategory> implements ExamCategoryDao {

    /**
     * 不排序查询行业分类数据
     * @return
     */
    @Override
    public List<ExamCategory> findCategoryByExample() {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from ExamCategory where 1=1");

        hql.append(" order by code ASC");

        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }
}
