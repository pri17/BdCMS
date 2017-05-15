package com.bidanet.bdcms.service.businessSetting;

import com.bidanet.bdcms.entity.ExamPackage;
import com.bidanet.bdcms.entity.ExamProject;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

import java.util.List;

/**
 * Created by zhangbinbin on 2016-10-11 11:35:59.
 */
public interface ExamPackageService extends Service<ExamPackage> {

    void getExamPackageList(ExamPackage examPackage, Page<ExamPackage> page);

    void updateExamPackageT(ExamPackage examPackage,String projectIds);

    void addExamPackageT(ExamPackage examPackage,String projectIds);

    List<ExamProject> getExamProjectList();

    ExamProject getExamProjectById(long id);

    List<ExamProject> getExamPackageProjectList(long packageId);

    List<ExamPackage> getAllPackage();


}
