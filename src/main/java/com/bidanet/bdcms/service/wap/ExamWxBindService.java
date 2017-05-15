package com.bidanet.bdcms.service.wap;

import com.bidanet.bdcms.entity.ExamWxBind;
import com.bidanet.bdcms.service.Service;

import java.util.List;

/**
 * Created by zhangbinbin on 2016-12-13 16:24:27
 */

public interface ExamWxBindService extends Service<ExamWxBind> {

    List<ExamWxBind> getExamWxBindByOpenId(String openId);

    void saveWxBind(ExamWxBind examWxBind);

}
