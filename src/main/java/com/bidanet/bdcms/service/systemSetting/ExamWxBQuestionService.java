package com.bidanet.bdcms.service.systemSetting;

import com.bidanet.bdcms.entity.ExamWxBQuestion;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

/**
*
*/
public interface ExamWxBQuestionService extends Service<ExamWxBQuestion> {

    /**
     * 查询常见问题维护数据
     * @param query
     * @param page
     */
    void getWxBQuestionList(ExamWxBQuestion query, Page<ExamWxBQuestion> page);

    /**
     * 添加常见问题维护
     * @param wxBQuestion
     */
    void addWxBQuestionT(ExamWxBQuestion wxBQuestion);

    /**
     * 修改常见问题维护
     * @param wxBQuestion
     */
    void upWxBQuestionT(ExamWxBQuestion wxBQuestion);

    /**
     * 删除常见问题维护
     * @param id
     */
    void deleteWxBQuestionT(Long id);
}
