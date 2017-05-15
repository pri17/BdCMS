package com.bidanet.bdcms.controller.examBusiness;/**
 * Created by Administrator on 2016/10/17 0017.
 */

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.businessSetting.ExamPackageProjectService;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberService;
import com.bidanet.bdcms.service.examBusiness.ExamResultDescriptionService;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.vo.ValueLabel;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 内科检查. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 16-10-17 17:55
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("examMedical")
@RequestMapping("/admin/examBusiness/examMedical")
public class ExamMedicalController extends AdminBaseController{

    @Autowired
    private ExamMemberExamService examMemberExamService;
    @Autowired
    private ExamResultDescriptionService examResultDescriptionService;
    @Autowired
    private ExamBlodIntestinalService blodIntestinalService;
    @Autowired
    private ExamPayService payService;
    @Autowired
    private ExamPackageProjectService packageProjectService;

    /**
     * 内科检查页面
     */
    @RequestMapping("/index")
    public void index(Model model){

        User user = uc.getUser();
        model.addAttribute("user",user);
        model.addAttribute("projectId",SystemContent.PROJECT_MEDICAL);
        model.addAttribute("yes",SystemContent.PROJECT_ISQUALIFIED_YES);
        model.addAttribute("no",SystemContent.PROJECT_ISQUALIFIED_NO);
    }

    /**
     * 通过体检号获取体检人信息
     * @param examNumber
     * @return
     */
    @RequestMapping("/findMemberExamByNumber")
    @ResponseBody
    public ExamMemberExam findMemberExamByNumber(String examNumber){
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setExamNumber(examNumber);
        //未作废
        examMemberExam.setIsCancel(0);
        List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExampleExact(examMemberExam);
        if(examMemberExamList!=null&&examMemberExamList.size()>0){
           /* examMemberExam = examMemberExamList.get(0);*/
            /*guchenxi  20170309*/
            examMemberExam = examMemberExamList.get(examMemberExamList.size()-1);
        }else{
            examMemberExam = null;
        }

        return examMemberExam;
    }

    /**
     * 获取页面信息跳转内科检查加入页面
     * @param qualified
     * @param drName
     * @param description
     * @param remark
     * @param model
     */
    @RequestMapping("/toJoinMedical")
    public void toJoinMedical(Long qualified,String drName,String description,
                              String remark,String descriptionId,Model model) throws UnsupportedEncodingException {
        model.addAttribute("qualified",qualified);
        drName= URLDecoder.decode(drName,"UTF-8");
        model.addAttribute("drName",drName );
        description=URLDecoder.decode(description,"UTF-8");
        model.addAttribute("description",description);
        model.addAttribute("remark",remark);
        model.addAttribute("descriptionId",descriptionId);
        model.addAttribute("projectId",SystemContent.PROJECT_MEDICAL);
    }

    /**
     * 获取页面信息跳转内科检查加入页面
     * @param projectId
     * @param model
     */
//    @RequestMapping("/toJoinMedical")
//    public void toJoinMedical(Long projectId,Model model){
//        model.addAttribute("projectId",projectId);
//    }

    /**
     * 获取内科问题描述
     * @return
     */
    @RequestMapping("/findMedicalDescription")
    @ResponseBody
    public List<ValueLabel> findMedicalDescription(Integer isQualified){
        List<ValueLabel> list = new ArrayList<ValueLabel>();
        ExamResultDescription resultDescription = new ExamResultDescription();
        resultDescription.setProjectId(SystemContent.PROJECT_MEDICAL);
        if(isQualified==SystemContent.PROJECT_ISQUALIFIED_YES) {
            resultDescription.setType(SystemContent.RESULT_TYPE_YES);
        }else if(isQualified==SystemContent.PROJECT_ISQUALIFIED_NO){
            resultDescription.setType(SystemContent.RESULT_TYPE_NO);
        }else if(isQualified==0){

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
//    @RequestMapping("/joinMedical")
//    @ResponseBody
//    public AjaxCallBack joinMedical(String description,String descriptionId){
//        examResultDescriptionService.joinMedicalT(description,descriptionId);
//        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
//        return ajaxCallBack;
//    }

    /**
     * 加入体检描述用语
     * @param description
     * @return
     */
    @RequestMapping("/joinMedical")
    @ResponseBody
    public AjaxCallBack joinMedical(String description,Integer type,Long projectId){
        examResultDescriptionService.joinMedicalT(description,type,projectId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     * 跳转编辑描述用语
     */
    @RequestMapping("/toUpMedical")
    public void toUpMedical(Model model){
        model.addAttribute("projectId",SystemContent.PROJECT_MEDICAL);
    }

    /**
     * 修改描述用语
     * @return
     */
    @RequestMapping("/upMedical")
    @ResponseBody
    public AjaxCallBack upMedical(String updescriptionId,String updescription){
        examResultDescriptionService.upMedicalT(updescriptionId,updescription);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

//    /**
//     * 保存内科或者X线体检结果
//     * @param isQualified
//     * @param descriptionVal
//     * @param descriptionId
//     * @param examNumber
//     * @param projectId
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    @RequestMapping("/saveMedicalResult")
//    @ResponseBody
//    public AjaxCallBack saveMedicalResult(Integer isQualified,String descriptionVal,Long descriptionId,String examNumber,Long projectId) throws UnsupportedEncodingException {
//        User user = uc.getUser();
//        descriptionVal = URLDecoder.decode(descriptionVal,"UTF-8");
//        if(StringUtils.isBlank(examNumber)){
//            errorMsg("请先扫描条形码获取体检人员信息！");
//        }
//        ExamMemberExam memberExam = findMemberExamByNumber(examNumber);
//        if(memberExam==null){
//            errorMsg("请扫描正确的条形码！");
//        }
//        blodIntestinalService.saveMedicalResultT(user,isQualified,descriptionVal,descriptionId,memberExam,projectId);
//        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
//        return ajaxCallBack;
//    }

    /**
     * 保存内科或者X线体检结果
     * @param isQualified
     * @param description
     * @param examNumber
     * @param projectId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/saveMedicalResult")
    @ResponseBody
    public AjaxCallBack saveMedicalResult(Integer isQualified,String description,String examNumber,Long projectId) throws UnsupportedEncodingException {
        User user = uc.getUser();
        if(StringUtils.isBlank(examNumber)){
            errorMsg("请先扫描条形码获取体检人员信息！");
        }
        ExamMemberExam memberExam = findMemberExamByNumber(examNumber);
        if(memberExam==null){
            errorMsg("请扫描正确的条形码！");
        }
        blodIntestinalService.saveMedicalResultT(user,isQualified,description,memberExam,projectId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        return ajaxCallBack;
    }

    //检测体检号是否有效
    @RequestMapping("/checkMemberExam")
    @ResponseBody
    public AjaxCallBack checkMemberExam(String examNumber){
        ExamMemberExam memberExam = findMemberExamByNumber(examNumber);
        if(memberExam==null){
            errorMsg("请扫描正确的条形码!");
        }
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     * 通过体检号,体检项目类别获取体检人信息，检测内科、X线体检状态
     * //查询examMemberExam表中is_new为1的数据
     * 如果is_new为1 再去查询verify_conclusion 体检结果:0、未判断 1：合格  2：不合格  默认值都为0
     * 如果这条记录不合格 说明有复检项目
     * 再去查询exam_blod_intestinal中projectid对应的数据
     * @param examNumber
     * @return
     */
    @RequestMapping("/findResultByProIdMeId")
    @ResponseBody
    public Map<String,Object> findResultByProIdMeId(String examNumber, String projectId){
        User user = uc.getUser();
        Map<String,Object> map = new HashMap<String,Object>();
        blodIntestinalService.checkResultByProIdMeIdT(map,examNumber,projectId,user.getRealName());
        return map;
    }

    /**
     * 根据项目类型查询该类型所有描述术语
     * @param projectId
     * @return
     */
    @RequestMapping("/findMedicalXDescription")
    @ResponseBody
    public List<ExamResultDescription> findMedicalXDescription(Long projectId){
        List<ExamResultDescription> descriptions = examResultDescriptionService.getAllResultDescription(projectId);
        return descriptions;
    }

    @RequestMapping("/toUpExamResultMedicalX")
    public void toUpExamResultMedicalX(String examNumber,Long projectId,Model model){
        //体检结果表id
        if(projectId.equals(SystemContent.PROJECT_MEDICAL)){
            model.addAttribute("projectId",SystemContent.PROJECT_MEDICAL);
        }else{
            model.addAttribute("projectId",SystemContent.PROJECT_X);
        }
        model.addAttribute("examNumber",examNumber);
    }

    /**
     *
     * @param upExamNumber
     * @param upIdLength    所有描述的合格与否类型
     * @param updescription 待修改的结果描述术语
     * @return
     */
    @RequestMapping("/upExamResultMedicalX")
    @ResponseBody
    public Map<String,Object> upExamResultMedicalX(String upExamNumber,String upIdLength,String updescription,Long projectId){
        Map<String,Object> map = new HashMap<String,Object>();
        User loginUser = uc.getUser();
        examResultDescriptionService.upExamResultMedicalXT(upExamNumber,upIdLength,updescription,loginUser,projectId,map);
        return map;
    }
}
