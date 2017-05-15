package com.bidanet.bdcms.job;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.entity.ExamAutoDoctor;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamReadCardSet;
import com.bidanet.bdcms.service.examBusiness.ExamAutoDoctorService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.examBusiness.ExamReadCardSetService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 自动审核定时任务. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c)
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public class AutoResultDecideJob extends QuartzJobBean {

    private Logger log = (Logger.getLogger(getClass()));

    @Autowired
    private ExamMemberExamService examMemberExamService;

    @Autowired
    private ExamAutoDoctorService examAutoDoctorService;

    @Autowired
    private ExamReadCardSetService examReadCardSetService;



    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("==============AutoResultDecideJob begin ================");
        examMemberExamService = SpringWebTool.getBean(ExamMemberExamService.class);

        examAutoDoctorService = SpringWebTool.getBean(ExamAutoDoctorService.class);

        //首先获取数据库entry_state = 1 (判断结束) and verfiy_state = 2 (未审核) and verify_conclusion=0 (未判断)

        ExamAutoDoctor examAutoDoctor = examAutoDoctorService.get(1L);

        List<ExamMemberExam>  autoResultDecideList = examMemberExamService.getAutoResultDecideList();


        for (ExamMemberExam examMemberExam : autoResultDecideList){

            examMemberExamService.autoResultDecide(examMemberExam,examAutoDoctor);

        }

        //每天凌晨更新时，同时将自助机settag字段置为0，
        // 让自助机刚启动时就进行读卡，解决读卡器每次重启后第一次读取失败的问题
        ExamReadCardSet examReadCardSet = examReadCardSetService.get(1L);

        examReadCardSet.setTag(0);

        examReadCardSetService.updateT(examReadCardSet);

        log.info("==============AutoResultDecideJob end ================");

    }
}
