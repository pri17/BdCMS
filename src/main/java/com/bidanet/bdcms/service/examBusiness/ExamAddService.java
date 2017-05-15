package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamMember;
import com.bidanet.bdcms.service.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangbinbin on 2016-10-21 10:24:47
 */

public interface ExamAddService extends Service<ExamMember> {

    void addExamAddT(ExamMember examMember);

    List<ExamMember> getExamMemberByIdCardNum(String idCardNum);

    /**
     * 根据身份证号码判断
     * @param idCardNum
     * @return
     */
    Map<String,Object> judgeExamMember(String idCardNum, String judgeTag);

}
