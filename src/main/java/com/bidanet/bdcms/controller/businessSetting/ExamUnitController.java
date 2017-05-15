package com.bidanet.bdcms.controller.businessSetting;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamUnit;
import com.bidanet.bdcms.service.businessSetting.ExamUnitService;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 体检单位维护Controller
 */
@Controller("examUnit")
@RequestMapping("/admin/businessSetting/examUnit")
public class ExamUnitController extends AdminBaseController {

    @Autowired
    private ExamUnitService examUnitService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") ExamUnit query,
            @ModelAttribute Page<ExamUnit> page,String tabid){
        examUnitService.getExamUnitList(query,page);

        tableId = tabid;
    }

    /**
     * 跳转添加体检单位页面
     */
    @RequestMapping("/toAddExamUnit")
    public void toAddExamUnit(){
    }

    /**
     * 添加体检单位
     * @param examUnit
     * @return
     */
    @RequestMapping("/addExamUnit")
    @ResponseBody
    public AjaxCallBack addExamUnit(ExamUnit examUnit){
        if(StringUtils.isBlank(examUnit.getUnitName())){
            AjaxCallBack ajaxCallBack = AjaxCallBack.error("工作单位名称不能为空");
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }
        ExamUnit eU=new ExamUnit();
        eU.setUnitName(examUnit.getUnitName());
        List<ExamUnit> examUnitList=examUnitService.query(eU);
        if(examUnitList.size()>0){
            AjaxCallBack ajaxCallBack = AjaxCallBack.error("工作单位名称已经存在");
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }

        Date date = new Date();
        examUnit.setCreateTime(date.getTime());
        examUnitService.addExamUnitT(examUnit);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 跳转修改体检单位页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateExamUnit")
    public void toUpdateExamUnit(Long id,Model model){
        ExamUnit examUnit = examUnitService.get(id);
        model.addAttribute("examUnit",examUnit);
    }

    /**
     * 修改体检单位
     * @param examUnit
     * @return
     */
    @RequestMapping("/updateExamUnit")
    @ResponseBody
    public AjaxCallBack updateExamUnit(ExamUnit examUnit){

  /*      ExamUnit eU=new ExamUnit();
        eU.setUnitName(examUnit.getUnitName());
        List<ExamUnit> examUnitList=examUnitService.query(eU);
        if(examUnitList.size()>0){
            AjaxCallBack ajaxCallBack = AjaxCallBack.error("工作单位名称已经存在");
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }*/


        boolean flag = false;

        ExamUnit tempExamUnit = examUnitService.get(examUnit.getId());
        List<ExamUnit> examUnitList = examUnitService.query(new ExamUnit());

        examUnitList.remove(tempExamUnit);


        for (ExamUnit x: examUnitList) {

            if(x.getUnitName().equals(examUnit.getUnitName())){
                flag = true;
                break;
            }
        }

        if(flag){
            AjaxCallBack ajaxCallBack = AjaxCallBack.error("工作单位名称已经存在");
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }



        examUnitService.updateExamUnitT(examUnit);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除体检单位
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxCallBack delete(Long id){
        examUnitService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }


    @RequestMapping("/completeUnit")
    @ResponseBody
    public List<ExamUnit> queryUnitNameList(String q){
        ExamUnit unit = new ExamUnit();
        unit.setUnitName(q);
        return examUnitService.findByExample(unit);

//        List<String> strList = new ArrayList<String>();
//
//        for (ExamUnit units : unitList){
//
//            strList.add(units.getUnitName());
//        }
//
//        return strList;


    }

    @RequestMapping("/checkUnitName")
    @ResponseBody
    public JSONObject checkUnitName(String unitName){
        ExamUnit examUnit=new ExamUnit();
        examUnit.setUnitName(unitName);
        List<ExamUnit> examUnitList=examUnitService.query(examUnit);

        HttpSession session = SpringWebTool.getSession();
        JSONObject jsonObject = new JSONObject();

        if(examUnitList.size()<=0){
            jsonObject.put("ok", "验证通过");
        }else{
            jsonObject.put("error", "工作单位名称已经存在");
        }

        return  jsonObject;
    }


}
