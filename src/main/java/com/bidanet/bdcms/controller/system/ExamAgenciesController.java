package com.bidanet.bdcms.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.service.systemSetting.ExamProjectService;
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
@Controller("examAgencies")
@RequestMapping("/admin/systemSetting/examAgencies")
public class ExamAgenciesController extends AdminBaseController {

    @Autowired
    private ExamAgenciesService examAgenciesService;

    @Autowired
    private ExamProjectService examProjectService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") ExamAgencies query,
            @ModelAttribute Page<ExamAgencies> page,String tabid){
        examAgenciesService.getExamAgenciesList(query,page);

        tableId = tabid;
    }

    /**
     * 检测机构代码唯一性
     * @param agenciesCode
     * @return
     */
    @RequestMapping("/checkAgenciesCode")
    @ResponseBody
    public JSONObject checkAgenciesCode(String agenciesCode){
        JSONObject json = examAgenciesService.checkAgenciesCode(agenciesCode);
        return  json;
    }

    /**
     * 检测体检机构名称唯一性
     * @param agenciesName
     * @return
     */
    @RequestMapping("/checkAgenciesName")
    @ResponseBody
    public JSONObject checkAgenciesName(String agenciesName){
        JSONObject json = examAgenciesService.checkAgenciesName(agenciesName);
        return  json;
    }
    /**
     * 跳转添加体检机构页面
     */
    @RequestMapping("/toAddAgencies")
    public void toAddAgencies(){
    }

    /**
     * 添加体检机构
     * @param examAgencies
     * @return
     */
    @RequestMapping("/addAgencies")
    @ResponseBody
    public AjaxCallBack addAgencies(ExamAgencies examAgencies){
        examAgencies = examAgenciesService.addAgenciesT(examAgencies);



        if (examAgencies.getId()!=0){
            //新增体检机构成功后

            //重点注意：查询出所有的体检项目 然后循环给这个体检机构增加体检项目跟医生
            examAgenciesService.addAgenciesDoctor(examAgencies);

            AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }else{
            AjaxCallBack ajaxCallBack = AjaxCallBack.error("添加体检机构失败");
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }
    }

    /**
     * 跳转修改体检机构页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateAgencies")
    public void toUpdateAgencies(Long id,Model model){
        ExamAgencies examAgencies = examAgenciesService.get(id);
        model.addAttribute("examAgencies",examAgencies);
    }

    /**
     * 修改体检机构
     * @param examAgencies
     * @return
     */
    @RequestMapping("/updateAgencies")
    @ResponseBody
    public AjaxCallBack updateAgencies(ExamAgencies examAgencies){
        examAgenciesService.updateAgenciesT(examAgencies);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除体检机构
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxCallBack delete(Long id){
/*        examAgenciesService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();*/

        //返回提示，不允许删除

        return AjaxCallBack.error("系统字段不允许删除");

    }

    @RequestMapping("/findAgenciesById")
    @ResponseBody
    public Integer findAgenciesById(Long id){

        ExamAgencies examAgencies = examAgenciesService.findAgenciesById(id);

        return examAgencies.getAgenciesCode();
    }



}
