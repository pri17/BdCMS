package com.bidanet.bdcms.dao.queryStatic;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamMemberExam;

import java.text.ParseException;
import java.util.List;

/**
 * Created by jizhaoqun on 2016/11/9.
 */
public interface ExamTogetherQueryDao extends Dao<ExamMemberExam>{

    //体检综合查询excel导出查询
    List<ExamMemberExam> queryTogetherList(Long togetherCategoryLevelTwoId, String channel, String eCardNumber, String name, String areaId, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType,
                                           int pageNo, int pageSize) throws ParseException;

    List<ExamMemberExam> queryTogetherNoPageList(String name,String eCardNumber,Long categoryId,String areaId, String idCard, String workUnit, String examNumber,String startTime, String endTime, String payState, String payType, String isRecheck,
                                           String isQualified,String sortByTime,String channel) throws ParseException;

    Long queryCountTogetherList(String name,String eCardNumber,Long categoryId,String areaId, String idCard, String workUnit, String examNumber,String startTime, String endTime, String payState, String payType, String isRecheck,
                                String isQualified,String sortByTime,String channel) throws ParseException;

    List<ExamMemberExam> queryTogetherList(String ids);
}



