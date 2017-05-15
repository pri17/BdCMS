package com.bidanet.bdcms.service.businessSetting;

import com.bidanet.bdcms.entity.ExamMemberPackage;
import com.bidanet.bdcms.service.Service;

import java.util.List;

/**
 * Created by zhangbinbin on 2016-10-26 14:18:10.
 */
public interface ExamMemberPackageService extends Service<ExamMemberPackage> {

    List<ExamMemberPackage> getExamMemberPackageListByMemberId(ExamMemberPackage examMemberPackage);

    void addExamPackageT(ExamMemberPackage examMemberPackage);

}
