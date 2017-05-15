package com.bidanet.bdcms.entity;


import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 肠检
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_intestinal")

public class ExamIntestinal {

    private Long id;
    /**
     * 肠检检验号规则：自然年度中从1-5000循环使用，满5000就自动恢复从1开始，跨年度后清零，再进行1-5000的循环。
     */
    private Long testNumber;
    /**
     * 体检号规则：其中16代表年份，132代表地区，32262为流水码，每自然年度结束后清零。例：16 132 32262
     */
    private String examNumber;
    /**
     * 检验日期
     */
    private Long createTime;

    private String createTimeStr;

    private Long examNumberId;

    private Long memberExamId;

    private String code; //所属机构，乡镇编码

    private ExamMemberExam memberExam;

    private List<ExamBlodIntestinal> intestinalBlods;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "test_number")
    public Long getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(Long testNumber) {
        this.testNumber = testNumber;
    }

    @Column(name = "exam_number")
    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
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

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToOne
    @JoinColumn(name = "member_exam_id",insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamMemberExam getMemberExam() {
        return memberExam;
    }

    public void setMemberExam(ExamMemberExam memberExam) {
        this.memberExam = memberExam;
    }

    @OneToMany
    @JoinColumn(name = "exam_intestinal_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public List<ExamBlodIntestinal> getIntestinalBlods() {
        return intestinalBlods;
    }

    public void setIntestinalBlods(List<ExamBlodIntestinal> intestinalBlods) {
        this.intestinalBlods = intestinalBlods;
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

}
