package com.bidanet.bdcms.service.systemSetting.impl;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamDoctorDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.ExamDoctor;
import com.bidanet.bdcms.entity.ExamProject;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamProjectService;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 体检项目servicempl. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-10 15:30
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Service("examProjectService")
public class ExamProjectServiceImpl extends BaseServiceImpl<ExamProject> implements ExamProjectService {

    @Autowired
    private ExamProjectDao examProjectDao;

    @Override
    protected Dao getDao() {
        return examProjectDao;
    }

    @Autowired
    private ExamDoctorDao examDoctorDao;

    @Override
    public List<ExamProject> getProjectList() {

        List<ExamProject> lists = examProjectDao.findAll();

        return  lists;

    }

    @Override
    public ExamProject addProjectT(ExamProject examProject) {

        checkString(examProject.getProjectName(),"请填写项目名称！");
        checkPrice(examProject.getProjectPrice(),"请填写体检项目金额！");
        ExamProject examProject1 = examProjectDao.saveBack(examProject);

        return examProject1;

    }

    /**
     * 新增体检项目是批量添加体检医生
     * @param examProject
     */
    @Override
    public void addProjectDoctor(ExamProject examProject) {

        ExamDoctor examDoctor = new ExamDoctor();
        examDoctor.setParentId(0L);
        examDoctor.setLevel(1);

        List<ExamDoctor> examDoctorList = examDoctorDao.findByExample(examDoctor);

        for (ExamDoctor examDoctor1 : examDoctorList){

            //先获取examDoctor信息 再去添加
            ExamDoctor examDoctor2 = new ExamDoctor();

            examDoctor2.setAgenciesId(examDoctor1.getAgenciesId());
            examDoctor2.setProjectId(examProject.getId());
            examDoctor2.setProjectName(examProject.getProjectName());
            examDoctor2.setCreateTime(new Date().getTime());
            examDoctor2.setLevel(2);
            examDoctor2.setAreaId(examDoctor1.getAreaId());
            examDoctor2.setParentId(examDoctor1.getId());
            examDoctor2.setCode(examDoctor1.getCode());

            examDoctorDao.saveBack(examDoctor2);

        }

    }

    @Override
    public void updateProjectT(ExamProject examProject) {
        examProjectDao.update(examProject);
    }

    @Override
    public ExamProject getExamProjectById(long id) {
        return examProjectDao.get(id);
    }



    @Override
    public JSONObject checkProjectName(String projectName){

        JSONObject json =new JSONObject();
        ExamProject examProject = new ExamProject();
        examProject.setProjectName(projectName);

        List<ExamProject> examProjects = examProjectDao.findByExample(examProject, MatchMode.EXACT);
        if(examProjects!=null&&examProjects.size()>0){
            json.put("error","项目名称重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }
}
