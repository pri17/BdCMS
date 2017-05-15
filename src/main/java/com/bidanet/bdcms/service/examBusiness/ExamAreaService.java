package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamArea;
import com.bidanet.bdcms.service.Service;

import java.util.List;


/**
 * Created by zhangbinbin on 2016-10-28 11:00:15
 */

public interface ExamAreaService extends Service<ExamArea> {

    List<ExamArea> getRootExamArea();
}
