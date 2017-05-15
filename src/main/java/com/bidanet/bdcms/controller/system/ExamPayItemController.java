package com.bidanet.bdcms.controller.system;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamPayItem;
import com.bidanet.bdcms.service.systemSetting.ExamPayItemService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 体检收费设置Controller
 */
@Controller("examPayItem")
@RequestMapping("/admin/systemSetting/examPayItem")
public class ExamPayItemController extends AdminBaseController {

    @Autowired
    private ExamPayItemService examPayItemService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") ExamPayItem query,
            @ModelAttribute Page<ExamPayItem> page,String tabid){
        examPayItemService.getExamPayItemList(query,page);

        tableId = tabid;
    }

    /**
     * 跳转添加收费设置页面
     */
    @RequestMapping("/toAddExamPayItem")
    public void toAddExamPayItem(){
    }

    /**
     * 添加收费设置
     * @param examPayItem
     * @return
     */
    @RequestMapping("/addExamPayItem")
    @ResponseBody
    public AjaxCallBack addExamPayItem(ExamPayItem examPayItem){
        Date date = new Date();
        examPayItem.setCreateTime(date.getTime());
        examPayItemService.addExamPayItemT(examPayItem);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 跳转修改收费设置页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateExamPayItem")
    public void toUpdateExamPayItem(Long id,Model model){
        ExamPayItem examPayItem = examPayItemService.get(id);
        model.addAttribute("examPayItem",examPayItem);
    }

    /**
     * 修改收费设置
     * @return
     */
    @RequestMapping("/updateExamPayItem")
    @ResponseBody
    public AjaxCallBack updateExamPayItem(ExamPayItem examPayItem){
        examPayItemService.updateExamPayItemT(examPayItem);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除收费设置
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxCallBack delete(Long id){
        examPayItemService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }

}
