package com.bidanet.bdcms.service.examManage;

import com.bidanet.bdcms.driver.cache.ECardEntity;
import com.bidanet.bdcms.entity.ExamEcard;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.ExamEcardResponse;
import com.bidanet.bdcms.vo.ExamEcardVo;
import com.bidanet.bdcms.vo.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;


/**
* cf
*/
public interface ExamEcardService extends Service<ExamEcard> {

    void addExamEcardT(ExamEcard examEcard);

    void getExamEcardList(ExamEcard examEcard, Page<ExamEcard> page, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId,String beginIssueTime,String endIssueTime,String beginAuditTime,String endAuditTime);

    void getRedisExamEcardList(Page<ECardEntity> page, Long uid,String eCardNumber, String examNumber, String workUnit, String areaId, String isPrint, String memberName, String idCardNum, String beginExamTime,
                               String endExamTime, String openCategoryLevelTwo, String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime,String ecardAdd);

//    HSSFWorkbook uploadOrderExcel(ExamEcard examEcard, int pageCurrent,int pageSize, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId, String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime);

    //健康证数据导出
    HSSFWorkbook downOrderExcel(ExamEcard examEcard, String workUnit, String examNumber, String areaId, String eCardNumber, String isRechekPrint, String memberName, String idCardNum, String beginExamTime, String endExamTime, Long categoryId, String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime);

    List<ExamEcard> getExamEcardByMemberId(Long memberId);

//    //是否已经上传
//    List<ExamEcard> getExamEcardByUpload();

   void updateIsUploadT(List<ExamEcard> examEcards);
//获取未上传的
    List<ExamEcardVo> getExamEcardByUpload();
    List<ExamEcard> getExamEcardsByUpload();

//    ExamEcardResponse sendPost();


    List<ExamEcard> getUpdatePrintCrad(long mID,String headImage);
}
