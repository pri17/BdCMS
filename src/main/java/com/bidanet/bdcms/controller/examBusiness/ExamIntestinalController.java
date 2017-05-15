package com.bidanet.bdcms.controller.examBusiness;/**
 * Created by MyPC on 2016/10/27.
 */

import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamBlod;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 肠检检查.
 * 类详细说明.
 * Copyright: Copyright (c) 16-10-27 09:09
 * Company: 苏州必答网络科技有限公司
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("examIntestinal")
@RequestMapping("/admin/examBusiness/examIntestinal")
public class ExamIntestinalController extends AdminBaseController{

    @Autowired
    private ExamIntestinalService intestinalService;
    @Autowired
    private ExamBlodIntestinalService blodIntestinalService;
    @Autowired
    private ExamMemberExamService memberExamService;

    /**
     * 肠检检查页面
     */
    @RequestMapping("/index")
    public void index(){

    }

    /**
     *生成肠检号  肠检检验号规则：自然年度中从1-5000循环使用，满5000就自动恢复从1开始，跨年度后清零，再进行1-5000的循环。
     * @param examNumber
     * @return
     */
    // TODO: 2017/4/18 肠检号 并发
    @RequestMapping("/generateIntestinalTestNumber")
    @ResponseBody
    public Long generateIntestinalTestNumber(String examNumber){
        Long testNumber = null;
        testNumber = intestinalService.saveIntestinalTS(examNumber);
//        testNumber = testNumber + 1;
//        saveIntestinal(examNumber, testNumber);
        return testNumber;
    }

//    /**
//     * 保存肠检号信息
//     * @param examNumber
//     * @param testNumber
//     */
//    private void saveIntestinal(String examNumber,Long testNumber) {
//        intestinalService.saveIntestinalT(examNumber,testNumber);
//    }

    //检测肠检体检号是否有效
    @RequestMapping("/checkMemberExamForIntestinal")
    @ResponseBody
    public Map<String,Object> checkMemberExamForIntestinal(String examNumber){
        Map<String,Object> map = new HashMap<String, Object>();
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setExamNumber(examNumber);
        //未作废
        examMemberExam.setIsCancel(0);
        examMemberExam.setIsNew(1);
        List<ExamMemberExam> examMemberExamList = memberExamService.findByExampleExact(examMemberExam);
        if(examMemberExamList!=null&&examMemberExamList.size()>0){
            memberExamService.checkMemberExamForIntestinalT(examNumber,map);
        }else{
            map.put("errorMsg","请输入有效的体检号。");
        }
        return map;
    }


}
