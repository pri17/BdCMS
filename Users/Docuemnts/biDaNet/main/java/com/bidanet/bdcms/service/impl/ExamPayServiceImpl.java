package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.ExamPayDao;
import com.bidanet.bdcms.entity.ExamPay;
import com.bidanet.bdcms.service.ExamPayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
*
*/
@Service
public class ExamPayServiceImpl extends BaseServiceImpl<ExamPay> implements ExamPayService {
    @Autowired
    private ExamPayDao dao;
    @Override
    protected Dao getDao() {
        return dao;
    }


}
