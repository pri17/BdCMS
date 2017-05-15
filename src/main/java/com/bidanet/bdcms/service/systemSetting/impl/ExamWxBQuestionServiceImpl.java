package com.bidanet.bdcms.service.systemSetting.impl;

import com.bidanet.bdcms.dao.*;
import com.bidanet.bdcms.dao.systemSetting.ExamWxBQuestionDao;
import com.bidanet.bdcms.entity.*;

import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamWxBQuestionService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
*
*/
@Service
public class ExamWxBQuestionServiceImpl extends BaseServiceImpl<ExamWxBQuestion> implements ExamWxBQuestionService {
    @Autowired
    private ExamWxBQuestionDao wxBQuestionDao;
    @Override
    protected Dao getDao() {
        return wxBQuestionDao;
    }


    /**
     * 查询常见问题维护数据
     * @param query
     * @param page
     */
    @Override
    public void getWxBQuestionList(ExamWxBQuestion query, Page<ExamWxBQuestion> page) {
        List<ExamWxBQuestion> list = wxBQuestionDao.findByExample(query, page.getPageCurrent(), page.getPageSize());
        long count = wxBQuestionDao.countByExample(query);
        page.setList(list);
        page.setTotal(count);
    }

    /**
     * 添加常见问题维护
     * @param wxBQuestion
     */
    @Override
    public void addWxBQuestionT(ExamWxBQuestion wxBQuestion) {
        checkString(wxBQuestion.getAsk(),"请填写问题！");
        checkString(wxBQuestion.getAnswer(),"请填写回答！");
        wxBQuestion.setCreateTime(new Date().getTime());
        wxBQuestionDao.save(wxBQuestion);
    }

    /**
     * 修改常见问题维护
     * @param wxBQuestion
     */
    @Override
    public void upWxBQuestionT(ExamWxBQuestion wxBQuestion) {
        checkString(wxBQuestion.getAsk(),"请填写问题！");
        checkString(wxBQuestion.getAnswer(),"请填写回答！");
        ExamWxBQuestion upExamWxBQuestion = wxBQuestionDao.get(wxBQuestion.getId());
        checkNull(upExamWxBQuestion,"没有找到此常见问题！");
        upExamWxBQuestion.setAsk(wxBQuestion.getAsk());
        upExamWxBQuestion.setAnswer(wxBQuestion.getAnswer());
        wxBQuestionDao.update(upExamWxBQuestion);

    }

    /**
     * 删除常见问题维护
     * @param id
     */
    @Override
    public void deleteWxBQuestionT(Long id) {
        wxBQuestionDao.delete(id);
    }
}
