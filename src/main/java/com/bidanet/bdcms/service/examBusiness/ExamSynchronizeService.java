package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamSynchronize;
import com.bidanet.bdcms.service.Service;

/**
*
*/
public interface ExamSynchronizeService extends Service<ExamSynchronize> {

    public Boolean batchSynchronizeData(ExamSynchronize examSynchronize);

}
