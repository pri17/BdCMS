package com.bidanet.bdcms.service.queryStatic;

import com.bidanet.bdcms.driver.cache.TogetherQueryEntity;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.text.ParseException;
import java.util.List;

/**
 * Created by jizhaoqun on 2016/11/9.
 */
public interface ExamTogetherQueryService  extends Service<ExamMemberExam>  {


    void queryExamTogetherList(Page<TogetherQueryEntity> page,Long uid, String name,String eCardNumber, Long categoryId,String areaId, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck,
                               String isQualified,String sortByTime,String channel,String isInsPrint)throws ParseException;

    List<ExamMemberExam> getExamMemberExams(String ids);

    //体检综合查询excel导出查询
    HSSFWorkbook exportTogetherExcel(int pageCurrent, int pageSize, String name, String areaId, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, Long togetherCategoryLevelTwoId, String channel, String eCardNumber) throws ParseException;
}
