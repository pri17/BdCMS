package com.bidanet.bdcms.service.machine;

import com.bidanet.bdcms.entity.ExamMember;
import com.bidanet.bdcms.entity.ExamWxBind;
import com.bidanet.bdcms.service.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangbinbin on 2016-12-13 16:24:27
 */

public interface MachineService extends Service<ExamMember> {

    Map<String,Object> addExamTS(String name, String sex, String address, String idCardNum, String workUnit
            , String mobile, Long categoryId, Long packageId, String birthday, String imageBase64, String packagePrice);

    List<ExamMember> getExamMemberByIdCardNum(String idCardNum);

    List<ExamWxBind> getExamWxBindByOpenId(String openId);

    /**
     * 根据身份证号码判断
     * @param idCardNum
     * @return
     */
    Map<String,Object> judgeExamMember(String idCardNum,String judgeTag);

}
