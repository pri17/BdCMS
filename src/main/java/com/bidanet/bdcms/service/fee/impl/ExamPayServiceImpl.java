package com.bidanet.bdcms.service.fee.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.fee.ExamPayDao;
import com.bidanet.bdcms.entity.ExamPay;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;

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

    public void addExamPayT(ExamPay examPay) {
        dao.save(examPay);
    }

    @Override
    public void updateExamPayT(String idCardNum,String examNumber) {
        String sql = "update exam_pay set idCard_num = " + " '"
                + idCardNum + "' "
                + " where exam_number = "
                + " '"
                + examNumber
                + " '";

        dao.execUpdateSQL(sql);
    }

}
