package com.bidanet.bdcms.controller.system;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamWxBNotes;
import com.bidanet.bdcms.entity.ExamWxBQuestion;
import com.bidanet.bdcms.entity.ExamWxBusiness;
import com.bidanet.bdcms.service.systemSetting.ExamWxBNotesService;
import com.bidanet.bdcms.service.systemSetting.ExamWxBQuestionService;
import com.bidanet.bdcms.service.systemSetting.ExamWxBusinessService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 微信端业务设置. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-11 16:08
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("wxBusiness")
@RequestMapping("/admin/systemSetting/examWxBusiness")
public class WxBusinessController extends AdminBaseController{

    @Autowired
    private ExamWxBusinessService wxBusinessService;
    @Autowired
    private ExamWxBQuestionService wxBQuestionService;
    @Autowired
    private ExamWxBNotesService wxBNotesService;

    private String tableId = "";
    /**
     * 微信端业务页面
     * @param model
     */
    @RequestMapping("/index")
    public void index(Model model,String tabid){
        List<ExamWxBusiness> businessList =  wxBusinessService.getAllWxBisiness();
        model.addAttribute("businessList",businessList);

        tableId = tabid;
    }

    /**
     * 跳转常见问题维护列表页面
     */
    @RequestMapping("/wxBQuestionIndex")
    public void wxBQuestionIndex    (
            @ModelAttribute("query") ExamWxBQuestion query,
            @ModelAttribute Page<ExamWxBQuestion> page){
        wxBQuestionService.getWxBQuestionList(query,page);
    }

    /**
     * 跳转到添加常见问题维护
     */
    @RequestMapping("/toAddWxBQuestion")
    public void toAddWxBQuestion(){
    }

    /**
     * 添加常见问题维护
     */
    @RequestMapping("/addWxBQuestion")
    @ResponseBody
    public AjaxCallBack addWxBQuestion(ExamWxBQuestion wxBQuestion){
        wxBQuestionService.addWxBQuestionT(wxBQuestion);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
//        ajaxCallBack.setTabid(tableId);
        ajaxCallBack.setDialogid("upWxBusinessQuestion");
        return ajaxCallBack;
    }

    /**
     * 跳转去修改常见问题维护
     * @param id
     * @param model
     */
    @RequestMapping("/toUpWxBQuestion")
    public void toUpWxBQuestion(Long id,Model model){
        ExamWxBQuestion wxBQuestions = wxBQuestionService.get(id);
        model.addAttribute("wxBQuestions",wxBQuestions);
    }

    /**
     * 修改常见问题维护
     * @param wxBQuestion
     * @return
     */
    @RequestMapping("/upWxBQuestion")
    @ResponseBody
    public AjaxCallBack upWxBQuestion(ExamWxBQuestion wxBQuestion){
        wxBQuestionService.upWxBQuestionT(wxBQuestion);
        AjaxCallBack ajaxCallBack = AjaxCallBack.editSuccess();
        ajaxCallBack.setDialogid("upWxBusinessQuestion");
        return ajaxCallBack;
    }

    /**
     * 删除常见问题维护
     * @param id
     * @return
     */
    @RequestMapping("/deleteWxBQuestion")
    @ResponseBody
    public AjaxCallBack deleteWxBQuestion(Long id){
        wxBQuestionService.deleteWxBQuestionT(id);
        AjaxCallBack ajaxCallBack = AjaxCallBack.deleteSuccess();
        return ajaxCallBack;
    }

    /**
     * 跳转至修改从业人员须知页面
     * @param model
     */
    @RequestMapping("/toUpWxBNotes")
    public void toUpWxBNotes(Model model){
        List<ExamWxBNotes> wxBNotes = wxBNotesService.getList();
        if(wxBNotes!=null&&wxBNotes.size()>0){
            model.addAttribute("wxBNotes",wxBNotes.get(0));
        }
    }

    /**
     * 修改从业人员须知内容（首次为新增）
     * @param wxBNotes
     * @param upId
     * @return
     */
    @RequestMapping("/upWxBNotes")
    @ResponseBody
    public AjaxCallBack upWxBNotes(ExamWxBNotes wxBNotes,Long upId){
        wxBNotesService.upWxBNotesT(wxBNotes,upId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.editSuccess();
        return ajaxCallBack;
    }
}
