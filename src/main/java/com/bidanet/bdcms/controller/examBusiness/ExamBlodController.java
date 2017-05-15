package com.bidanet.bdcms.controller.examBusiness;/**
 * Created by MyPC on 2016/10/24.
 */

import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamBlodService;
import com.bidanet.bdcms.service.examBusiness.ExamIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 血检检查.
 * 类详细说明.
 * Copyright: Copyright (c) 16-10-24 11:11
 * Company: 苏州必答网络科技有限公司
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("examBlod")
@RequestMapping("/admin/examBusiness/examBlod")
public class ExamBlodController extends AdminBaseController{

    @Autowired
    private ExamBlodService blodService;
    @Autowired
    private ExamIntestinalService intestinalService;
    @Autowired
    private ExamBlodIntestinalService blodIntestinalService;
    @Autowired
    private ExamMemberExamService memberExamService;

    /**
     * 血检检查页面
     */
    @RequestMapping("/index")
    public void index(){

    }

    /**
     * 生成血检号  血检检验号规则：每天都是从1开始（相当于每天清零）。
     * @return
     */
    @RequestMapping("/generateTestNumber")
    @ResponseBody
    public Long generateTestNumber(String examNumber){
//        Long testNumber = null;
//        Calendar calendarStart = Calendar.getInstance();
//        calendarStart.set(Calendar.HOUR_OF_DAY,0);
//        calendarStart.set(Calendar.MINUTE,0);
//        calendarStart.set(Calendar.SECOND,0);
//        Long todayStart = calendarStart.getTime().getTime();
//        Calendar calendarEnd = Calendar.getInstance();
//        calendarEnd.set(Calendar.HOUR_OF_DAY,23);
//        calendarEnd.set(Calendar.MINUTE,59);
//        calendarEnd.set(Calendar.SECOND,59);
//        Long todayEnd = calendarEnd.getTime().getTime();
//        testNumber = blodService.findBigNumberToday(todayStart,todayEnd,examNumber);
//        testNumber = testNumber+1;
       return blodService.saveExamBlodTS(examNumber);
//        return testNumber;
    }

//    /**
//     * 保存血检号，创建时间等
//     * @param examNumber
//     * @param testNumber
//     */
//    private void saveExamBlod(String examNumber, Long testNumber) {
//        blodService.saveExamBlodTS(examNumber,testNumber);
//    }

    //检测体检号是否有效
    @RequestMapping("/checkMemberExam")
    @ResponseBody
    public Map<String,Object> checkMemberExam(String examNumber){
        Map<String,Object> map = new HashMap<String, Object>();
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setExamNumber(examNumber);
        //未作废
        examMemberExam.setIsCancel(0);
        List<ExamMemberExam> examMemberExamList = memberExamService.findByExampleExact(examMemberExam);
        if(examMemberExamList!=null&&examMemberExamList.size()>0){
            memberExamService.checkMemberExamT(examNumber,map);
        }else{
            map.put("errorMsg","请输入有效的体检号！");
        }
        return map;
    }


}
