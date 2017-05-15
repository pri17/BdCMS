package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamReview;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
* cf
*/
public interface ExamReviewService extends Service<ExamReview> {

    void getExamReviewList(ExamReview examReview, Page<ExamReview> page, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId);

    /**
     * 通过memberExamId删除相应的复检数据
     * @param memeberExamId
     */
    void deleteReviewByMEIdT(Long memeberExamId);

    List<ExamReview> getExamReviews(String ids);

    HSSFWorkbook exportReviewExcel(ExamReview query, int pageCurrent, int pageSize, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId);
}
