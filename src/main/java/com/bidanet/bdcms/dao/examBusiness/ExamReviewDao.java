package com.bidanet.bdcms.dao.examBusiness;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamReview;

import java.util.List;

/**
* ExamReview DAO
 *  cf
*/
public interface ExamReviewDao extends Dao<ExamReview> {
    List<ExamReview> getExamReviewList(ExamReview examReview, int pageNo, int pageSize, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId);

    /**
     * 根据memberExamId获取所有的复检数据
     * @param memberExamId
     * @return
     */
    List<ExamReview> findReviewByMemberExamId(Long memberExamId);

    List<ExamReview> queryExamReviewList(String ids);
}
