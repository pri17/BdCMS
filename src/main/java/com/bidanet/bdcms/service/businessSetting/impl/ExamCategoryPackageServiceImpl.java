package com.bidanet.bdcms.service.businessSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamCategoryPackageDao;
import com.bidanet.bdcms.entity.ExamCategoryPackage;

import com.bidanet.bdcms.service.businessSetting.ExamCategoryPackageService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
*
*/
@Service
public class ExamCategoryPackageServiceImpl extends BaseServiceImpl<ExamCategoryPackage> implements ExamCategoryPackageService {
    @Autowired
    private ExamCategoryPackageDao categoryProjectDao;
    @Override
    protected Dao getDao() {
        return categoryProjectDao;
    }


}
