package com.bidanet.bdcms.service.businessSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamMemberPackageDao;
import com.bidanet.bdcms.entity.ExamMemberPackage;
import com.bidanet.bdcms.service.businessSetting.ExamMemberPackageService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbinbin on 2016-10-26 14:18:49.
 */
@Service
public class ExamMemberPackageServiceImpl extends BaseServiceImpl<ExamMemberPackage> implements ExamMemberPackageService {
    @Autowired
    private ExamMemberPackageDao examMemberPackageDao;
    @Override
    protected Dao getDao() {
        return examMemberPackageDao;
    }

    @Override
    public List<ExamMemberPackage> getExamMemberPackageListByMemberId(ExamMemberPackage examMemberPackage) {
        return examMemberPackageDao.findByExample(examMemberPackage);
    }

    @Override
    public void addExamPackageT(ExamMemberPackage examMemberPackage) {
        examMemberPackageDao.save(examMemberPackage);
    }

}
