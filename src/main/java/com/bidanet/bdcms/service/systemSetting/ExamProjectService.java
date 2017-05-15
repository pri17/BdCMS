package com.bidanet.bdcms.service.systemSetting;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.entity.ExamProject;
import com.bidanet.bdcms.service.Service;

import java.util.List;


/**
 * 体检项目service. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-10 15:35
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public interface ExamProjectService extends Service<ExamProject> {

    /**
     * 获取全部体检的项目
     * @param
     */
    List<ExamProject> getProjectList();

    /**
     * 新增体检项目及价格
     * @param examProject
     */
    ExamProject addProjectT(ExamProject examProject);

    /**
     * 批量添加体检医生
     * @param examProject
     */
    void addProjectDoctor(ExamProject examProject);

    /**
     * 更新体检项目及价格
     * @param examProject
     */
    void updateProjectT(ExamProject examProject);

    /**
     * 根据id获取体检项目及价格
     * @param id
     * @return
     */
    ExamProject getExamProjectById(long id);

    /**
     * 判断添加的项目名称是否可用
     * @param projectName
     * @return
     */
    JSONObject checkProjectName(String projectName);


}
