package com.bidanet.bdcms.controller.businessSetting;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamPackage;
import com.bidanet.bdcms.entity.ExamProject;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 体检套餐维护Controller
 */
@Controller("examPackage")
@RequestMapping("/admin/businessSetting/examPackage")
public class ExamPackageController extends AdminBaseController {

    @Autowired
    private ExamPackageService examPackageService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") ExamPackage query,
            @ModelAttribute Page<ExamPackage> page,String tabid){
        examPackageService.getExamPackageList(query,page);

        tableId = tabid;
    }

    /**
     * 跳转添加体检套餐页面
     */
    @RequestMapping("/toAddExamPackage")
    public void toAddExamPackage(Model model){
        List<ExamProject> examProjectList = examPackageService.getExamProjectList();
        model.addAttribute("examProjectList",examProjectList);
    }

    @RequestMapping("/getExamProject")
    @ResponseBody
    public List<ExamProject> getExamProject(){
        List<ExamProject> examProjectList = examPackageService.getExamProjectList();
        return  examProjectList;
    }

    /**
     * 添加体检套餐
     * @param examPackage
     * @return
     */
    @RequestMapping("/addExamPackage")
    @ResponseBody
    public AjaxCallBack addExamPackage(ExamPackage examPackage,String projectIds){

        Date date = new Date();
        examPackage.setCreateTime(date.getTime());
        examPackageService.addExamPackageT(examPackage,projectIds);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 跳转修改体检套餐页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateExamPackage")
    public void toUpdateExamPackage(Long id,Model model){
        ExamPackage examPackage = examPackageService.get(id);
        List<ExamProject> examProjectList = examPackageService.getExamProjectList();
        if (examPackage!=null && examPackage.getProjectId()!=null) {
            String projectIdNameStr = "";
            List<String> projectIdList = Arrays.asList(examPackage.getProjectId().split(","));
            for (int i = 0; i < projectIdList.size(); i++) {
                ExamProject project = examPackageService.getExamProjectById(Long.parseLong(projectIdList.get(i)));
                projectIdNameStr = projectIdNameStr + projectIdList.get(i) + "|" + project.getProjectName() + ",";
            }
            projectIdNameStr = projectIdNameStr.substring(0,projectIdNameStr.length()-1);
            examPackage.setProjectIdNameStr(projectIdNameStr);
        }
        model.addAttribute("examProjectList",examProjectList);
        model.addAttribute("examPackage",examPackage);
    }

    /**
     * 修改体检套餐
     * @param examPackage
     * @return
     */
    @RequestMapping("/updateExamPackage")
    @ResponseBody
    public AjaxCallBack updateExamPackage(ExamPackage examPackage,String projectIds){
//        examPackage.setMoney(countPrice(projectIds));
        examPackageService.updateExamPackageT(examPackage, projectIds);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
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
        examPackageService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }

    /**
     * 计算套餐价格
     * @param projectIds
     * @return
     */
    @RequestMapping("/countPrice")
    @ResponseBody
    public BigDecimal countPrice(String projectIds){
        BigDecimal totalPrice = new BigDecimal(0);
        List<String> projectIdNameList = Arrays.asList(projectIds.split(","));
        if (projectIdNameList.size()>0) {
            for (int i = 0; i < projectIdNameList.size(); i++) {
                String[] projectIdList=projectIdNameList.get(i).split("\\|");
                if (projectIdList.length>0) {
                    ExamProject project = examPackageService.getExamProjectById(Long.valueOf(projectIdList[0]));
                    totalPrice = totalPrice.add(project.getProjectPrice());
                }
            }
        }
        return totalPrice;
    }

}
