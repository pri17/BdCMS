package com.bidanet.bdcms.service.systemSetting;

import com.bidanet.bdcms.entity.ExamPayItem;
import com.bidanet.bdcms.entity.SysLog;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

/**
 * Created by zhangbinbin on 2016-10-11 11:35:59.
 */
public interface ExamPayItemService extends Service<ExamPayItem> {

    void getExamPayItemList(ExamPayItem examPayItem, Page<ExamPayItem> page);

    void updateExamPayItemT(ExamPayItem examPayItem);

    void addExamPayItemT(ExamPayItem examPayItem);

}
