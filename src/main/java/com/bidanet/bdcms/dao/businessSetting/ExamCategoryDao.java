package com.bidanet.bdcms.dao.businessSetting;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamCategory;

import java.util.List;

/**
* ExamCategory DAO
*/
public interface ExamCategoryDao extends Dao<ExamCategory> {

    /**
     * 不排序查询行业分类数据
     * @return
     */
    List<ExamCategory> findCategoryByExample();
}
