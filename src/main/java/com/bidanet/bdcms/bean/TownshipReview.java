package com.bidanet.bdcms.bean;

import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamMemberExam;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */
public class TownshipReview {

    private ExamMemberExam examMemberExam;          //复检结果实体

    private List<ExamBlodIntestinal> examBlodIntestinalList;        //不合格项目集合

    public ExamMemberExam getExamMemberExam() {
        return examMemberExam;
    }

    public void setExamMemberExam(ExamMemberExam examMemberExam) {
        this.examMemberExam = examMemberExam;
    }

    public List<ExamBlodIntestinal> getExamBlodIntestinalList() {
        return examBlodIntestinalList;
    }

    public void setExamBlodIntestinalList(List<ExamBlodIntestinal> examBlodIntestinalList) {
        this.examBlodIntestinalList = examBlodIntestinalList;
    }
}
