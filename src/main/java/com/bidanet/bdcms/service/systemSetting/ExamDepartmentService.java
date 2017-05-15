package com.bidanet.bdcms.service.systemSetting;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.entity.ExamDepartment;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

import java.util.List;

/**
*
*/
public interface ExamDepartmentService extends Service<ExamDepartment> {

    void getExamDepartmentList(ExamDepartment department, Page<ExamDepartment> page);

//    void toStartStopT(Long uid);
//
//    void changePwdT(String oldPwd, String newPwd, User loginUser);
//
    /**
     * 修改科室名称
     * @param examDepartment
     */
    void updateDepartmentT(ExamDepartment examDepartment);

    void addDepartmentT(ExamDepartment examDepartment);

    JSONObject checkDepartmentName(String departmentName);

    /**
     * 获取所有的体检机构用于体检选取
     * @return
     */
    List<ExamDepartment> getAllDepartment();

}
