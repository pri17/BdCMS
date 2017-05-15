package com.bidanet.bdcms.service.businessSetting;

import com.bidanet.bdcms.entity.ExamDoctor;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

import java.util.List;


/**
 * 体检医生
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
public interface ExamDoctorService extends Service<ExamDoctor> {

    void getExamDoctorList(String doctorName,String projectName,Long areaId,Long agenciesId, Page<ExamDoctor> page);

    void updateExamDoctorT(ExamDoctor examDoctor);

    void addExamDoctorT(ExamDoctor examDoctor,ExamDoctor parentExamDoctor);

    ExamDoctor addExamDoctorT(ExamDoctor examDoctor);

    /**
     * 不排序查询行业分类数据
     * @return
     */
    List<ExamDoctor> getAllDoctor(Long agenciesId);

}
