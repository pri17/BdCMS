package com.bidanet.bdcms.controller.examBusiness;/**
 * Created by CF on 2017/1/17.
 */

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamIntestinal;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.service.examBusiness.ExamBlodService;
import com.bidanet.bdcms.service.examBusiness.ExamIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 乡镇肠检采集
 * 标题、简要说明.
 * 类详细说明.
 * Copyright: Copyright (c) 17-01-17 14:56
 * Company: 苏州必答网络科技有限公司
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("examTownsIntestinal")
@RequestMapping("/admin/examBusiness/examTownsIntestinal")
public class ExamTownsIntestinalController extends AdminBaseController{

    @Autowired
    private ExamMemberExamService memberExamService;
    @Autowired
    private ExamIntestinalService intestinalService;

    /**
     * 乡镇肠检采集页面
     */
    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") ExamIntestinal query,
            @ModelAttribute Page<ExamIntestinal> page
    ){
        intestinalService.getPageByExample(query,page);
    }

    /**
     * 扫描条形码操作
     * @param examNumber
     * @return
     */
    @RequestMapping("/checkTownsIntestinal")
    @ResponseBody
    public Map<String,Object> checkTownsIntestinal(String examNumber){
        Map<String,Object> map = new HashMap<String, Object>();
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setExamNumber(examNumber);
        //未作废
        examMemberExam.setIsCancel(0);
        examMemberExam.setIsNew(1);
        List<ExamMemberExam> examMemberExamList = memberExamService.findByExampleExact(examMemberExam);
        if(examMemberExamList!=null&&examMemberExamList.size()>0){
            intestinalService.checkTownsIntestinalT(examNumber,map);
        }else{
            map.put("errorMsg","请输入有效的体检号!");
        }
        return map;
    }

    /**
     * 保存录入的肠检采样号信息
     * @return
     */
    @RequestMapping("/saveTownsIntestinalTestNumber")
    @ResponseBody
    public AjaxCallBack saveTownsIntestinalTestNumber(String examNumber,String sampleNumber){
        intestinalService.saveTownsIntestinalTestNumberT(examNumber,sampleNumber);
        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
        return ajaxCallBack;
    }

    /**
     * 通过体检号流水号查询采集信息
     * @return
     */
    @RequestMapping("/findTownsIntestinalByNumber")
    @ResponseBody
    public ExamIntestinal findTownsIntestinalByNumber(String examNumber,String sampleNumber){
        ExamIntestinal intestinal = new ExamIntestinal();
        intestinal.setExamNumber(examNumber);
        intestinal.setTestNumber(Long.valueOf(sampleNumber));
        List<ExamIntestinal> intestinalList = intestinalService.findByExampleExact(intestinal);
        if(intestinalList!=null&&intestinalList.size()>0){
            return intestinalList.get(0);
        }else{
            return null;
        }

    }

    /**
     * 生成乡镇体检号
     * @param examNumber
     * @return
     */
    @RequestMapping("/generateTownsIntestinalTestNumber")
    @ResponseBody
    public Long generateTownsIntestinalTestNumber(String examNumber){
        Long testNumber = null;
        testNumber = intestinalService.generateIntestinalTestNumber(examNumber);
        testNumber = testNumber + 1;
        saveTownsIntestinal(examNumber, testNumber);
        return testNumber;
    }

    /**
     * 保存乡镇肠检号
     * @param examNumber
     * @param testNumber
     */
    private void saveTownsIntestinal(String examNumber,Long testNumber) {
        intestinalService.saveIntestinalT(examNumber,testNumber);
    }

    /**
     * 乡镇肠检体检信息查找带回
     * @param query
     * @param page
     */
    @RequestMapping("/townsMemberExamLookBack")
    public void townsMemberExamLookBack(
            Model model,
            String examNumber,String memberName,
            String beginTime,String endTime,
            @ModelAttribute("query") ExamMemberExam query,
            @ModelAttribute Page<ExamMemberExam> page
    ) throws ParseException {

        memberExamService.getTownsMemberExamLookBack(page,examNumber,memberName,beginTime,endTime);
        model.addAttribute("examNumber",examNumber);
        model.addAttribute("memberName",memberName);
        model.addAttribute("beginTime",beginTime);
        model.addAttribute("endTime",endTime);
    }
}
