package com.bidanet.bdcms.controller.businessSetting;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamArea;
import com.bidanet.bdcms.entity.ExamDoctor;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.businessSetting.ExamDoctorService;
import com.bidanet.bdcms.service.examBusiness.ExamAreaService;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 体检医生-controller
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Controller("examDoctor")
@RequestMapping("/admin/businessSetting/examDoctor")
public class ExamDoctorController extends AdminBaseController {

    @Autowired
    private ExamDoctorService examDoctorService;

    @Autowired
    private ExamAgenciesService examAgenciesService;

    @Autowired
    private ExamAreaService examAreaService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") ExamDoctor query, String tabid,String agenciesId,Model model){
        User user = uc.getUser();
        if (StringUtils.isNotEmpty(agenciesId)){
            user.setAgenciesId(Long.parseLong(agenciesId));
        }
//        examDoctorService.getExamDoctorList(doctorName,projectName,user.getAreaId(),user.getAgenciesId(),page);
        List<ExamDoctor> examDoctorList = examDoctorService.getAllDoctor(user.getAgenciesId());

        for (ExamDoctor examDoctor :examDoctorList){

            ExamAgencies examAgencies = examAgenciesService.get(examDoctor.getAgenciesId());
            if(examAgencies!=null){
                examDoctor.setAgenciesName(examAgencies.getAgenciesName());
            }


        }

        model.addAttribute("examDoctors",examDoctorList);

        model.addAttribute("agenciesId",agenciesId);

        ExamArea examArea = examAreaService.get(user.getAreaId());

        model.addAttribute("areaCode",examArea.getAreaCode());


        tableId = tabid;
    }


    /**
     * 跳转添加医生页面
     */
    @RequestMapping("/toAddExamDoctor")
    public void toAddExamDoctor(Model model,Long id){
        ExamDoctor examDoctor = examDoctorService.get(id);

        model.addAttribute("areaId",examDoctor.getAreaId());
        model.addAttribute("examDoctorId",examDoctor.getId());
    }


    /**
     * 添加医生套餐
     * 项目id 名称
     * 地区
     * 机构
     * @return
     */
    @RequestMapping("/addExamDoctor")
    @ResponseBody
    public AjaxCallBack addExamPackage(ExamDoctor examDoctor){

        ExamDoctor parentExamDoctor = examDoctorService.get(examDoctor.getId());

        examDoctorService.addExamDoctorT(examDoctor,parentExamDoctor);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }



    /**
     * 跳转修改体检医生页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateExamDoctor")
    public void toUpdateExamDoctor(Long id,Model model){
        ExamDoctor examDoctor = examDoctorService.get(id);
        model.addAttribute("examDoctor",examDoctor);
    }

    /**
     * 修改体检医生
     * @param examDoctor
     * @return
     */
    @RequestMapping("/updateExamDoctor")
    @ResponseBody
    public AjaxCallBack updateExamDoctor(ExamDoctor examDoctor){
        examDoctorService.updateExamDoctorT(examDoctor);
        AjaxCallBack ajaxCallBack = AjaxCallBack.editSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 修改体检医生名称
     * @param id,doctorName
     * @return
     */
    @RequestMapping("/updateExamDoctorName")
    @ResponseBody
    public AjaxCallBack updateExamDoctorName(Long id,String doctorName){
        ExamDoctor examDoctor = examDoctorService.get(id);
        examDoctor.setDoctorName(doctorName);
        examDoctorService.updateExamDoctorT(examDoctor);
        AjaxCallBack ajaxCallBack = AjaxCallBack.editSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除体检套餐
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxCallBack delete(Long id){
        examDoctorService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }

}
