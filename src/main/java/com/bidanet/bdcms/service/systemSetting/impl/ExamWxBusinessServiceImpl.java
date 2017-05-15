package com.bidanet.bdcms.service.systemSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.systemSetting.ExamWxBusinessDao;
import com.bidanet.bdcms.entity.ExamWxBusiness;

import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamWxBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
*
*/
@Service
public class ExamWxBusinessServiceImpl extends BaseServiceImpl<ExamWxBusiness> implements ExamWxBusinessService {
    @Autowired
    private ExamWxBusinessDao wxBusinessDao;
    @Override
    protected Dao getDao() {
        return wxBusinessDao;
    }


    @Override
    public List<ExamWxBusiness> getAllWxBisiness() {
        return wxBusinessDao.findAll();
    }
}
