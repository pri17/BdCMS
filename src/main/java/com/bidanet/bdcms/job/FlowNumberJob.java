package com.bidanet.bdcms.job;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.entity.ExamFlowNumber;
import com.bidanet.bdcms.service.examBusiness.ExamFlowNumberService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 流水号. <br>
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
public class FlowNumberJob extends QuartzJobBean{

    private Logger log = (Logger.getLogger(getClass()));

    @Autowired
    private ExamFlowNumberService examFlowNumberService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("==============FlowNumberJob begin ================");

        examFlowNumberService = SpringWebTool.getBean(ExamFlowNumberService.class);
        ExamFlowNumber examFlowNumber = examFlowNumberService.get(1L);

        Date newDate = new Date();

        //日期格式转换进行判断
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

        String oldYear = simpleDateFormat.format(examFlowNumber.getCreateTime());

        String newYear = simpleDateFormat.format(newDate);

        if(Integer.parseInt(oldYear) == Integer.parseInt(newYear)){//老年份跟新年份相同
            //更新这条数据记录
            examFlowNumber.setCreateTime(newDate.getTime());

           examFlowNumber =  examFlowNumberService.updateBack(examFlowNumber);

            if (examFlowNumber.getId()!=null){
                log.info("==============FlowNumberJob update success ================");
            }

        }else if (Integer.parseInt(newYear)>Integer.parseInt(oldYear)){//新年份大于老年份
            //更新记录同时将流水号置为0
            examFlowNumber.setCreateTime(newDate.getTime());

            examFlowNumber.setFlowNumber(0L);

            examFlowNumber =  examFlowNumberService.updateBack(examFlowNumber);

            if (examFlowNumber.getId()!=null){
                log.info("==============FlowNumberJob updateYear success ================");
            }

        }


        log.info("==============FlowNumberJob end  ================");

    }

    public static void main(String args[]) {

        String oldTime = "1483060364740";

        Date newDate = new Date();

        //日期格式转换进行判断
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

        String oldYear = simpleDateFormat.format(Long.parseLong(oldTime));

        String newYear = simpleDateFormat.format(newDate);

        if(Integer.parseInt(oldYear) == Integer.parseInt(newYear)) {//老年份跟新年份相同

        }else{

        }

    }
}
