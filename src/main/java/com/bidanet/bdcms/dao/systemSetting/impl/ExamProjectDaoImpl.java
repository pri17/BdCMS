package com.bidanet.bdcms.dao.systemSetting.impl;

import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.ExamProject;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检项目daoImpl. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-10 15:35
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Repository
public class ExamProjectDaoImpl extends BaseDaoImpl<ExamProject> implements ExamProjectDao{

    @Override
    public List<ExamProject> getExamPackageProjectList(long packageId) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" FROM ExamProject ep WHERE ep.id in (SELECT epp.projectId FROM ExamPackageProject epp WHERE epp.packageId=:packageId)");
        map.put("packageId",packageId);
        Query query = getSession().createQuery(hql.toString());
        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }

}
