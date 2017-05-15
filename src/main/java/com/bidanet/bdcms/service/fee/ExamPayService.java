package com.bidanet.bdcms.service.fee;

import com.bidanet.bdcms.entity.ExamPay;
import com.bidanet.bdcms.service.Service;
/**
*
*/
public interface ExamPayService extends Service<ExamPay> {
    void addExamPayT(ExamPay examPay);

    void updateExamPayT(String idCardNum,String examNumber);
}
