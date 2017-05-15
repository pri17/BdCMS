package com.bidanet.bdcms.service.businessSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamDoctorDao;
import com.bidanet.bdcms.dao.systemSetting.ExamAgenciesDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.ExamDoctor;
import com.bidanet.bdcms.entity.ExamProject;
import com.bidanet.bdcms.service.businessSetting.ExamDoctorService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 体检医生
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Service
public class ExamDoctorServiceImpl extends BaseServiceImpl<ExamDoctor> implements ExamDoctorService {
    @Autowired
    private ExamDoctorDao examDoctorDao;
    @Override
    protected Dao getDao() {
        return examDoctorDao;
    }

    @Autowired
    private ExamProjectDao examProjectDao;

    @Autowired
    private ExamAgenciesDao examAgenciesDao;

    @Override
    public void getExamDoctorList(String doctorName,String projectName,Long areaId,Long agenciesId, Page<ExamDoctor> page){
//        List<ExamDoctor> list = examDoctorDao.findByExample(examDoctor, page.getPageCurrent(), page.getPageSize());
        List<ExamDoctor> list = examDoctorDao.findDoctorByExample( doctorName, projectName, areaId, agenciesId, page.getPageCurrent(), page.getPageSize());
        page.setList(list);
        page.setTotal((long)list.size());
    }

    @Override
    public void updateExamDoctorT(ExamDoctor examDoctor) {
        checkString(examDoctor.getDoctorName(),"请填写体检医生名称！");
        ExamDoctor examDoctor1 = examDoctorDao.get(examDoctor.getId());
        Date date =  new Date();
        examDoctor1.setProjectId(examDoctor.getProjectId());
        ExamProject examProject = examProjectDao.get(examDoctor.getProjectId());
//        examDoctor1.setProjectName(examProject.getProjectName());
        examDoctor1.setDoctorName(examDoctor.getDoctorName());
        examDoctor1.setCreateTime(examDoctor.getCreateTime());
        examDoctor1.setUpdateTime(date.getTime());
        examDoctorDao.update(examDoctor1);
    }

    @Override
    public void addExamDoctorT(ExamDoctor examDoctor,ExamDoctor parentExamDoctor) {
        checkString(examDoctor.getDoctorName(),"请填写医生姓名！");
        if(examDoctor.getProjectId()==null){
            errorMsg("请选择对应的体检项目！");
        }
        examDoctor.setCreateTime(new Date().getTime());
        ExamProject examProject = examProjectDao.get(examDoctor.getProjectId());
//        examDoctor.setProjectName(examProject.getProjectName());
//        ExamAgencies examAgencies = examAgenciesDao.get(user.getAgenciesId());
        examDoctor.setAgenciesName(parentExamDoctor.getAgenciesName());
        examDoctor.setAgenciesId(parentExamDoctor.getAgenciesId());

        //二级
        examDoctor.setAreaId(parentExamDoctor.getAreaId());
        examDoctor.setLevel(2);
        examDoctor.setParentId(parentExamDoctor.getId());
        examDoctor.setCode(parentExamDoctor.getCode());

        examDoctorDao.save(examDoctor);
    }

    @Override
    public ExamDoctor addExamDoctorT(ExamDoctor examDoctor) {

        return  examDoctorDao.saveBack(examDoctor);

    }

    @Override
    public List<ExamDoctor> getAllDoctor(Long agenciesId) {

        return examDoctorDao.getAllDoctor(agenciesId);


    }

}
