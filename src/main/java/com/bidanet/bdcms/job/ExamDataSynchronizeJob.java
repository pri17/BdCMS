package com.bidanet.bdcms.job;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.entity.ExamSynchronize;
import com.bidanet.bdcms.service.examBusiness.ExamSynchronizeService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 同步省平台、方正数据. <br>
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
public class ExamDataSynchronizeJob extends QuartzJobBean {

    private Logger logger = (Logger.getLogger(getClass()));

    @Autowired
    private ExamSynchronizeService examSynchronizeService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("==============ExamDataSynchronizeJob begin ================");
        examSynchronizeService = SpringWebTool.getBean(ExamSynchronizeService.class);

        //查询出数据库exam_synchronize中is_success为0的数据进行同步
        //如果同步完成后，将该字段置为1成功 下次不再对该数据做处理
        ExamSynchronize examSynchronize = new ExamSynchronize();

        examSynchronize.setIsSuccess(0);

        List<ExamSynchronize> examSynchronizeList = examSynchronizeService.findByExample(examSynchronize);

        for (ExamSynchronize examSynchronize1 : examSynchronizeList){

           Boolean isSuccess =  examSynchronizeService.batchSynchronizeData(examSynchronize1);

            if (!isSuccess){

                logger.error("健康证ID:"+examSynchronize1.getId()+"同步失败！");

            }

        }



        logger.info("==============ExamDataSynchronizeJob end ================");


    }
}
