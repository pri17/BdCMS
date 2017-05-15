package com.bidanet.bdcms.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamProject;
import com.bidanet.bdcms.service.businessSetting.ExamDoctorService;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.service.systemSetting.ExamProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 体检项目及价格管理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-10 15:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Controller("examProject")
@RequestMapping ("/admin/systemSetting/examProject")
public class ExamProjectController extends AdminBaseController {

    @Autowired
    private ExamProjectService examProjectService;

    @Autowired
    private ExamAgenciesService examAgenciesService;

    @Autowired
    private ExamDoctorService examDoctorService;


    private String tableId = "";

    /**
     * 点击菜单进入页面
     * @param model
     */
    @RequestMapping("/index")
    public void index(Model model,String tabid){

        List<ExamProject> lists = examProjectService.getProjectList();
        model.addAttribute("projectLists",lists);

        List<ExamAgencies> agenciesList = examAgenciesService.getAllAgencies();
        model.addAttribute("agenciesLists",agenciesList);

        tableId = tabid;

    }


    @RequestMapping("/checkProjectName")
    @ResponseBody
    public JSONObject checkProjectName(String projectName){
        JSONObject json = examProjectService.checkProjectName(projectName);
        return  json;
    }



    /**
     * 跳转新增体检项目价格页面
     */
    @RequestMapping("/toAddExamProject")
    public void toAddExamProject(){

    }

    /**
     * 新增体检项目及价格
     * //当增加一个体检项目时，将所有的乡镇等加上该项目的医生 医生姓名为空
     * @param examProject
     * @return
     */
    @RequestMapping("/addExamProject")
    @ResponseBody
    public AjaxCallBack addExamProject(ExamProject examProject){
        Date date = new Date();
        examProject.setCreateTime(date.getTime());
        ExamProject examProject1 = examProjectService.addProjectT(examProject);



        //注意 添加新项目的时候 根据数据库现有的体检机构 增加对应的体检医生

        if (examProject1.getId()!=0){

            examProjectService.addProjectDoctor(examProject1);

            AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }else{

            AjaxCallBack ajaxCallBack = AjaxCallBack.error("添加体检项目失败!");
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }
    }

    /**
     * 跳转体检项目及价格修改页面
     */
    @RequestMapping("/toUpdateExamProject")
    public void toUpdateExamProject(Model model,Long id){

        ExamProject examProject = examProjectService.getExamProjectById(id);

        model.addAttribute("examProject",examProject);

    }

    /**
     * 更新体检项目及价格
     * @param examProject
     * @return
     */
    @RequestMapping("/updateExamProject")
    @ResponseBody
    public  AjaxCallBack updateExamProject(ExamProject examProject){
        Date date = new Date();
        examProject.setProjectName(examProject.getProjectName());
        examProject.setUpdateTime(date.getTime());
        examProjectService.updateProjectT(examProject);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除体检项目及价格
     * @param id
     * @return
     */
    @RequestMapping("/deleteExamProject")
    @ResponseBody
    public AjaxCallBack delete(Long id){
        examProjectService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getTime());
    }

}
