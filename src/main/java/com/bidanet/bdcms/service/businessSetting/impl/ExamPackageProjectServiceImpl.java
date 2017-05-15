package com.bidanet.bdcms.service.businessSetting.impl;/**
 * Created by CF on 2016/12/25.
 */

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.entity.ExamPackageProject;
import com.bidanet.bdcms.service.businessSetting.ExamPackageProjectService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 标题、简要说明.
 * 类详细说明.
 * Copyright: Copyright (c) 16-12-25 0:40
 * Company: 苏州必答网络科技有限公司
 *
 * @author cf
 * @version 1.0.0
 */
@Service
public class ExamPackageProjectServiceImpl extends BaseServiceImpl<ExamPackageProject> implements ExamPackageProjectService{
    @Autowired
    private ExamPackageProjectDao packageProjectDao;
    @Override
    protected Dao getDao() {
        return packageProjectDao;
    }
}
