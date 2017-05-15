package com.bidanet.bdcms.entity;/**
 * Created by CF on 2016/11/18.
 */

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;

/**
 * 体检人员复检.
 * 类详细说明.
 * Copyright: Copyright (c) 16-11-18 10:47
 * Company: 苏州必答网络科技有限公司
 *
 * @author cf
 * @version 1.0.0
 */

@Entity
@Table(name = "exam_review")
public class ExamReview {
    private long id;

    private Long projectId;

    private String projectName;

    private Long memberExamId;

    private Integer isPrint;

    private String examNumber;


    private Long examTime;

    private String verifyResult;

    private Long areaId;

    private Long categoryId;

    /**
     * 用户主数据
     */
    private ExamMember examMember;

    private ExamArea examArea;

    private ExamCategory examCategory;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "project_id")
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Column(name = "project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    @Column(name = "is_print")
    public Integer getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Integer isPrint) {
        this.isPrint = isPrint;
    }

    @Column(name = "member_exam_id")
    public Long getMemberExamId() {
        return memberExamId;
    }

    public void setMemberExamId(Long memberExamId) {
        this.memberExamId = memberExamId;
    }

    @Column(name = "exam_number")
    public String getExamNumber() {
        return examNumber;
    }


    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }


    @Column(name = "exam_time")
    public Long getExamTime() {
        return examTime;
    }

    public void setExamTime(Long examTime) {
        this.examTime = examTime;
    }

    @Column(name = "verify_result")
    public String getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(String verifyResult) {
        this.verifyResult = verifyResult;
    }

    @Column(name = "area_id")
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Column(name = "category_id")
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Transient
    public String getExamTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(this.examTime!=null){
            return df.format(this.examTime);
        }else{
            return "";
        }
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id",insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamArea getExamArea() {
        return examArea;
    }

    public void setExamArea(ExamArea examArea) {
        this.examArea = examArea;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamCategory getExamCategory() {
        return examCategory;
    }

    public void setExamCategory(ExamCategory examCategory) {
        this.examCategory = examCategory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamMember getExamMember() {
        return examMember;
    }

    public void setExamMember(ExamMember examMember) {
        this.examMember = examMember;
    }
}
