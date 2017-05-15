package com.bidanet.bdcms.job;

import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamWxBind;
import com.bidanet.bdcms.plugin.wechat.service.SendMessageService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.wap.ExamWxBindService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.ParseException;
import java.util.List;


/**
 * 判断体检人员是否完成体检项目. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c)
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
public class CheckExamFinishJob extends QuartzJobBean{

    private Logger log = (Logger.getLogger(getClass()));

    @Autowired
    private ExamMemberExamService examMemberExamService;
    @Autowired
    private ExamWxBindService examWxBindService;
    @Autowired
    private SendMessageService sendMessageService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("==============CheckExamFinishJob begin ================");

        List<ExamMemberExam> examMemberExamList = null;
        try {
            examMemberExamList = examMemberExamService.getUnFinishExam();
            if (examMemberExamList.size()>0) {
                for (int i = 0; i < examMemberExamList.size(); i++) {
                    ExamMemberExam exam = examMemberExamList.get(i);
                    if (exam.getMemberId()!=null) {
                        ExamWxBind examWxBind = new ExamWxBind();
                        examWxBind.setMemberId(exam.getMemberId());
                        List<ExamWxBind> examWxBinds = examWxBindService.findByExample(examWxBind);
                        if (examWxBinds.size()>0) {
                            try {
                                sendMessageService.sendTextMessage(examWxBinds.get(0).getWxOpenId(),"您还有未完成的体检项目！");
                                log.info("==============CheckExamFinishJob push  ================");
                            } catch (WxErrorException e) {
                                log.info("==============CheckExamFinishJob error ================");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        log.info("==============CheckExamFinishJob end  ================");

    }

}
