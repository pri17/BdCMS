package com.bidanet.bdcms.dao.examManage;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamEcard;

import java.util.List;

/**
* ExamMemberExam DAO
 * cf
*/
public interface ExamEcardDao extends Dao<ExamEcard> {
    List<ExamEcard> getExamEcardList(ExamEcard examEcard, int pageNo, int pageSize, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId,String beginIssueTime,String endIssueTime,String beginAuditTime,String endAuditTime);
    List<ExamEcard> getExamEcardByMemberId(Long memberId);
    //健康证数据导出
    List<ExamEcard> getExamEcardExportList(ExamEcard examEcard, String workUnit, String examNumber, String areaId, String eCardNumber, String isRechekPrint, String memberName, String idCardNum, String beginExamTime, String endExamTime, Long categoryId, String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime);

    List<ExamEcard> getEcardNoPageList(String eCardNumner, String examNumber, String workUnit, String areaId, String isPrint,String memberName, String idCardNum, String beginTime, String endTime, String categoryId,String beginIssueTime,String endIssueTime,String beginAuditTime,String endAuditTime);
    List<ExamEcard> getExamEcardByUpload();

    List<ExamEcard> getExamEcardByUpload(long startTime,long endTime);
//    //批量修改上传状态
//    void updateIsUploadT(List<ExamEcard> examEcards);
}
