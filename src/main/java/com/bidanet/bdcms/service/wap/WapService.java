package com.bidanet.bdcms.service.wap;

import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamMember;
import com.bidanet.bdcms.entity.ExamWxBind;
import com.bidanet.bdcms.service.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangbinbin on 2016-12-13 16:24:27
 */

public interface WapService extends Service<ExamMember> {

    void addExamTS(int sex, int age, String idCardNum, String company
            , String phoneNum, Long categoryId, Long packageId, BigDecimal packagePrice, String birthday);


    void addExamOfCompanyAddTS(int sex, int age, String idCardNum, String company
            , String phoneNum, Long categoryId, Long packageId, BigDecimal packagePrice, String birthday);

    void addNewExamMemberTS(String openId, int sex, int age, String idCardNum, String company
            , String phoneNum, Long categoryId, Long packageId, BigDecimal packagePrice, String birthday);

    List<ExamMember> getExamMemberByIdCardNum(String idCardNum);

    List<ExamWxBind> getExamWxBindByOpenId(String openId);

    List<ExamBlodIntestinal> getBlodByExamId(Long examId);

}
