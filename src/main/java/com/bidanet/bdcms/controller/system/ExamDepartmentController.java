package com.bidanet.bdcms.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamDepartment;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.service.systemSetting.ExamDepartmentService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 体检机构管理Controller
 */
@Controller("examDepartment")
@RequestMapping("/admin/systemSetting/examDepartment")
public class ExamDepartmentController extends AdminBaseController {

    @Autowired
    private ExamDepartmentService examDepartmentService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") ExamDepartment query,
            @ModelAttribute Page<ExamDepartment> page, String tabid){
        examDepartmentService.getExamDepartmentList(query,page);

        tableId = tabid;
    }



    /**
     * 检测体检机构名称唯一性
     * @param departmentName
     * @return
     */
    @RequestMapping("/checkDepartmentName")
    @ResponseBody
    public JSONObject checkDepartmentName(String departmentName){
        JSONObject json = examDepartmentService.checkDepartmentName(departmentName);
        return  json;
    }
    /**
     * 跳转添加科室页面
     */
    @RequestMapping("/toAddDepartment")
    public void toAddDepartment(){
    }

    /**
     * 添加体检机构
     * @param examDepartment
     * @return
     */
    @RequestMapping("/addDepartment")
    @ResponseBody
    public AjaxCallBack addDepartment(ExamDepartment examDepartment){

        examDepartmentService.addDepartmentT(examDepartment);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 跳转修改体检机构页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateDepartment")
    public void toUpdateDepartment(Long id,Model model){
        ExamDepartment examDepartment = examDepartmentService.get(id);
        model.addAttribute("examDepartment",examDepartment);
    }

    /**
     * 修改科室
     * @param examDepartment
     * @return
     */
    @RequestMapping("/updateDepartment")
    @ResponseBody
    public AjaxCallBack updateDepartment(ExamDepartment examDepartment){

        examDepartmentService.updateDepartmentT(examDepartment);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除科室
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxCallBack delete(Long id){
        examDepartmentService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }





}
