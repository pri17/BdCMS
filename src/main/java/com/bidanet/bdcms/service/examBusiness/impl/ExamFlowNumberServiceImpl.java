package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamFlowNumberDao;
import com.bidanet.bdcms.entity.ExamFlowNumber;
import com.bidanet.bdcms.service.examBusiness.ExamFlowNumberService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
*
*/
@Service
public class ExamFlowNumberServiceImpl extends BaseServiceImpl<ExamFlowNumber> implements ExamFlowNumberService {
    @Autowired
    private ExamFlowNumberDao dao;
    @Override
    protected Dao getDao() {
        return dao;
    }


}
