package com.bidanet.bdcms.entity;


import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * 血检报告
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_blod")

public class ExamBlod {

    private long id;
    /**
     * 用户的体检号
     */
    private String examNumber;
    /**
     * 血检检验号规则：每天都是从1开始（相当于每天清零）。
     */
    private Long testNumber;

    /**
     * 疫区
     */
    private Long affectedId;

    private Long createTime;

    private String createTimeStr;

    private Long examNumberId;

    private Long memberExamId;

    private String IHA;

    private String DDIA;

    private String COPT;

    private String STOOL;

    private ExamMemberExam memberExam;

    private ExamAffected examAffected;

    private List<ExamBlodIntestinal> blodIntestinals;

    private String examRecheckStr;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "exam_number")
    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    @Column(name = "test_number")
    public Long getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(Long testNumber) {
        this.testNumber = testNumber;
    }

    @Column(name = "affected_id")
    public Long getAffectedId() {
        return affectedId;
    }

    public void setAffectedId(Long affectedId) {
        this.affectedId = affectedId;
    }

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "exam_number_id")
    public Long getExamNumberId() {
        return examNumberId;
    }

    public void setExamNumberId(Long examNumberId) {
        this.examNumberId = examNumberId;
    }

    @Column(name = "member_exam_id")
    public Long getMemberExamId() {
        return memberExamId;
    }

    public void setMemberExamId(Long memberExamId) {
        this.memberExamId = memberExamId;
    }

    public String getIHA() {
        return IHA;
    }

    public void setIHA(String IHA) {
        this.IHA = IHA;
    }

    public String getDDIA() {
        return DDIA;
    }

    public void setDDIA(String DDIA) {
        this.DDIA = DDIA;
    }

    public String getCOPT() {
        return COPT;
    }

    public void setCOPT(String COPT) {
        this.COPT = COPT;
    }

    public String getSTOOL() {
        return STOOL;
    }

    public void setSTOOL(String STOOL) {
        this.STOOL = STOOL;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_exam_id",insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamMemberExam getMemberExam() {
        return memberExam;
    }

    public void setMemberExam(ExamMemberExam memberExam) {
        this.memberExam = memberExam;
    }



    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_blod_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public List<ExamBlodIntestinal> getBlodIntestinals() {
        return blodIntestinals;
    }

    public void setBlodIntestinals(List<ExamBlodIntestinal> blodIntestinals) {
        this.blodIntestinals = blodIntestinals;
    }


    @Transient
    public String getCreateTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(this.createTime!=null){
            return df.format(this.createTime);
        }else{
            return "";
        }
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    @Transient
    public String getExamRecheckStr() {
        return examRecheckStr;
    }

    public void setExamRecheckStr(String examRecheckStr) {
        this.examRecheckStr = examRecheckStr;
    }
}
