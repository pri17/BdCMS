package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamReadCardSetDao;
import com.bidanet.bdcms.entity.ExamReadCardSet;

import com.bidanet.bdcms.service.examBusiness.ExamReadCardSetService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
*
*/
@Service
public class ExamReadCardSetServiceImpl extends BaseServiceImpl<ExamReadCardSet> implements ExamReadCardSetService {
    @Autowired
    private ExamReadCardSetDao dao;
    @Override
    protected Dao getDao() {
        return dao;
    }


}
