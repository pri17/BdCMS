package com.bidanet.bdcms.service.systemSetting;

import com.bidanet.bdcms.entity.ExamWxBusiness;
import com.bidanet.bdcms.service.Service;

import java.util.List;

/**
*
*/
public interface ExamWxBusinessService extends Service<ExamWxBusiness> {

    List<ExamWxBusiness> getAllWxBisiness();
}
