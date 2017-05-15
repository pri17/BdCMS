package com.bidanet.bdcms.service.businessSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageDao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.ExamPackage;
import com.bidanet.bdcms.entity.ExamPackageProject;
import com.bidanet.bdcms.entity.ExamProject;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbinbin on 2016-10-11 11:35:59.
 */
@Service("examPackageService")
public class ExamPackageServiceImpl extends BaseServiceImpl<ExamPackage> implements ExamPackageService {
    @Autowired
    private ExamPackageDao examPackageDao;
    @Override
    protected Dao getDao() {
        return examPackageDao;
    }
    @Autowired
    private ExamProjectDao examProjectDao;
    @Autowired
    private ExamPackageProjectDao examPackageProjectDao;

    @Override
    public void getExamPackageList(ExamPackage examPackage, Page<ExamPackage> page){
        List<ExamPackage> list = examPackageDao.findByExample(examPackage, page.getPageCurrent(), page.getPageSize());
        long count = examPackageDao.countByExample(examPackage);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void updateExamPackageT(ExamPackage examPackage,String projectIds) {
        checkString(examPackage.getName(),"请填写体检单位名称！");

        if (!StringUtils.isEmpty(projectIds)){

            ExamPackage examPackage1 = examPackageDao.get(examPackage.getId());
            Date date =  new Date();
            examPackage1.setCreateTime(date.getTime());
            examPackage1.setMoney(examPackage.getMoney());
            examPackage1.setName(examPackage.getName());
            //将exam_package_project表中的旧数据删除，再添加新数据
            ExamPackageProject examPackageProject = new ExamPackageProject();
            examPackageProject.setPackageId(examPackage1.getId());
            List<ExamPackageProject> examPackageProjectList = examPackageProjectDao.findByExample(examPackageProject);
            if (examPackageProjectList.size()>0) {
                for (int i = 0; i < examPackageProjectList.size(); i++) {
                    examPackageProjectDao.delete(examPackageProjectList.get(i).getId());
                }
            }
            List<String> projectIdNameList = Arrays.asList(projectIds.split(","));
            if (projectIdNameList.size()>0) {
                String projectId = "";
                String projectName = "";
                for (int i = 0; i < projectIdNameList.size(); i++) {
                    String[] projectIdList=projectIdNameList.get(i).split("\\|");
                    if (projectIdList.length>0) {
                        projectName += projectIdList[1]+",";
                        projectId += projectIdList[0]+",";
                        ExamPackageProject project = new ExamPackageProject();
                        project.setPackageId(examPackage1.getId());
                        project.setProjectId(Long.parseLong(projectIdList[0]));
                        project.setCreateTime(date.getTime());
                        examPackageProjectDao.save(project);
                    }
                }
                projectName = projectName.substring(0,projectName.length()-1);
                examPackage1.setProjectName(projectName);
                examPackage1.setProjectId(projectId.substring(0,projectId.length()-1));
                examPackageDao.update(examPackage1);
            }

        }else{

            errorMsg("请选择体检套餐项目！");
        }

    }

    @Override
    public void addExamPackageT(ExamPackage examPackage,String projectIds) {
        checkString(examPackage.getName(),"请填写体检单位名称！");
        Date date =  new Date();
        examPackage.setCreateTime(date.getTime());
        examPackageDao.save(examPackage);



        if (!StringUtils.isEmpty(projectIds)){

            List<String> projectIdNameList = Arrays.asList(projectIds.split(","));

            if (projectIdNameList.size()>0) {
                String projectId = "";
                String projectName = "";
                for (int i = 0; i < projectIdNameList.size(); i++) {
                    String[] projectIdList=projectIdNameList.get(i).split("\\|");
                    if (projectIdList.length>0) {
                        projectName += projectIdList[1]+",";
                        projectId += projectIdList[0]+",";
                        ExamPackageProject project = new ExamPackageProject();
                        project.setPackageId(examPackage.getId());
                        project.setProjectId(Long.parseLong(projectIdList[0]));
                        project.setCreateTime(date.getTime());
                        examPackageProjectDao.save(project);
                    }
                }
                projectName = projectName.substring(0,projectName.length()-1);
                examPackage.setProjectName(projectName);
                examPackage.setProjectId(projectId.substring(0,projectId.length()-1));
                examPackageDao.update(examPackage);
            }

        }else{
            errorMsg("请选择体检套餐项目！");
        }


    }

    public List<ExamProject> getExamProjectList() {
        return examProjectDao.findAll();
    }

    public ExamProject getExamProjectById(long id) {
        return examProjectDao.get(id);
    }

    public List<ExamProject> getExamPackageProjectList(long packageId) {
        return examProjectDao.getExamPackageProjectList(packageId);
    }

    @Override
    public List<ExamPackage> getAllPackage() {
        List<ExamPackage> examPackageList = examPackageDao.findAll();
        return  examPackageList;
    }
}
