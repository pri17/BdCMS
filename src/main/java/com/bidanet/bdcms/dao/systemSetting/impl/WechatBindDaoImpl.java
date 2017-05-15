package com.bidanet.bdcms.dao.systemSetting.impl;

import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.dao.systemSetting.WechatBindDao;
import com.bidanet.bdcms.entity.ExamMember;
import com.bidanet.bdcms.entity.ExamWxBind;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pri17 on 2017/5/9.
 */
@Repository
public class WechatBindDaoImpl extends BaseDaoImpl<ExamWxBind> implements WechatBindDao {
    @Override
    public List<ExamWxBind> findByMemberId(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder(" from ExamWxBind where 1=1");

        hql.append(" and memberId =:id");
        map.put("id", id);

        Query query = getSession().createQuery(hql.toString());
        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }
}
