package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamAreaCodeDao;
import com.bidanet.bdcms.entity.ExamAreaCode;

import com.bidanet.bdcms.service.examBusiness.ExamAreaCodeService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
* cf
*/
@Service
public class ExamAreaCodeServiceImpl extends BaseServiceImpl<ExamAreaCode> implements ExamAreaCodeService {
    @Autowired
    private ExamAreaCodeDao areaCodeDao;
    @Override
    protected Dao getDao() {
        return areaCodeDao;
    }


}
