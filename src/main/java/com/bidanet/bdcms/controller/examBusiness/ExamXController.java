package com.bidanet.bdcms.controller.examBusiness;/**
 * Created by Administrator on 2016/10/19 0019.
 */

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamResultDescription;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberService;
import com.bidanet.bdcms.service.examBusiness.ExamResultDescriptionService;
import com.bidanet.bdcms.vo.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 胸部X线检查. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 16-10-19 17:34
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("examX")
@RequestMapping("/admin/examBusiness/examX")
public class ExamXController extends AdminBaseController{
    @Autowired
    private ExamMemberExamService examMemberExamService;
    @Autowired
    private ExamResultDescriptionService examResultDescriptionService;

    /**
     * 胸部X线检查页面
     */
    @RequestMapping("/index")
    public void index(Model model){
        User user = uc.getUser();
        model.addAttribute("user",user);
        model.addAttribute("yes", SystemContent.PROJECT_ISQUALIFIED_YES);
        model.addAttribute("no",SystemContent.PROJECT_ISQUALIFIED_NO);
        model.addAttribute("projectId",SystemContent.PROJECT_X);
    }

    @RequestMapping("/toJoinX")
    public void toJoinX(Long qualified,String drName,String description,
                              String remark,String descriptionId,Model model) throws UnsupportedEncodingException {
        model.addAttribute("qualified",qualified);
        model.addAttribute("drName",URLDecoder.decode(drName,"UTF-8") );
        model.addAttribute("description",URLDecoder.decode(description,"UTF-8"));
        model.addAttribute("remark",remark);
        model.addAttribute("descriptionId",descriptionId);
        model.addAttribute("projectId",SystemContent.PROJECT_X);
    }

//    @RequestMapping("/toJoinX")
//    public void toJoinX(Long projectId,Model model){
//        model.addAttribute("projectId",projectId);
//    }

    /**
     * 获取内科问题描述
     * @return
     */
    @RequestMapping("/findXDescription")
    @ResponseBody
    public List<ValueLabel> findXDescription(Integer isQualified){
        List<ValueLabel> list = new ArrayList<ValueLabel>();
        ExamResultDescription resultDescription = new ExamResultDescription();
        resultDescription.setProjectId(SystemContent.PROJECT_X);
        if(isQualified==SystemContent.PROJECT_ISQUALIFIED_YES) {
            resultDescription.setType(SystemContent.RESULT_TYPE_YES);
        }else if(isQualified==SystemContent.PROJECT_ISQUALIFIED_NO){
            resultDescription.setType(SystemContent.RESULT_TYPE_NO);
        }
        List<ExamResultDescription> resultDescriptionList = examResultDescriptionService.findByExample(resultDescription);
        if(resultDescriptionList!=null&&resultDescriptionList.size()>0){
            for(ExamResultDescription rd:resultDescriptionList){
                list.add(new ValueLabel(String.valueOf(rd.getId()),rd.getDescription()));
            }
        }else{
            return null;
        }
        return list;
    }

//    /**
//     * 加入体检描述用语
//     * @param description
//     * @return
//     */
//    @RequestMapping("/joinX")
//    @ResponseBody
//    public AjaxCallBack joinX(String description, String descriptionId){
//        examResultDescriptionService.joinXT(description,descriptionId);
//        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
//        return ajaxCallBack;
//    }

    /**
     * 加入体检描述用语
     * @param description
     * @return
     */
    @RequestMapping("/joinX")
    @ResponseBody
    public AjaxCallBack joinX(String description,Integer type,Long projectId){
        examResultDescriptionService.joinMedicalT(description,type,projectId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     * 跳转编辑描述用语
     */
    @RequestMapping("/toUpX")
    public void toUpX(Model model){
        model.addAttribute("projectId",SystemContent.PROJECT_X);
    }


}
