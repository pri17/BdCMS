package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * 从业人员体检表-项目. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-14 14:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table (name = "exam_member_package")
public class ExamMemberPackage {

    private Long id;

    private Long memberId;

    private Long userId;

    private Long packageId;

    /**
     * 体检时间
     */
    private Long examTime;

    /**
     * 体检状态：   0： 未检查  1：待检验   2：不合格   3：合格
     */
    private int examState;

    /**
     * 体检结果
     */
    private String examResult;

    /**
     * 结果判断    1：合格  2：不合格
     */
    private int resultState;

    /**
     * 判断人
     */
    private Long judegeId;

    private Long judgeTime;

    /**
     * 审核状态   1：通过   2：不通过
     */
    private int verifyState;

    /**
     * 审核人id
     */
    private Long verifyId;

    /**
     * 审核时间
     */
    private Long verifyTime;

    /**
     * 体检结论
     */
    private String verifyConclusion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column (name = "package_id")
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    @Column (name = "member_id")
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Column (name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column (name = "exam_time")
    public Long getExamTime() {
        return examTime;
    }

    public void setExamTime(Long examTime) {
        this.examTime = examTime;
    }

    @Column (name = "exam_state")
    public int getExamState() {
        return examState;
    }

    public void setExamState(int examState) {
        this.examState = examState;
    }

    @Column (name = "exam_result")
    public String getExamResult() {
        return examResult;
    }

    public void setExamResult(String examResult) {
        this.examResult = examResult;
    }

    @Column (name = "result_state")
    public int getResultState() {
        return resultState;
    }

    public void setResultState(int resultState) {
        this.resultState = resultState;
    }

    @Column (name = "judge_id")
    public Long getJudegeId() {
        return judegeId;
    }

    public void setJudegeId(Long judegeId) {
        this.judegeId = judegeId;
    }

    @Column (name = "judge_time")
    public Long getJudgeTime() {
        return judgeTime;
    }

    public void setJudgeTime(Long judgeTime) {
        this.judgeTime = judgeTime;
    }

    @Column (name = "verify_state")
    public int getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(int verifyState) {
        this.verifyState = verifyState;
    }

    @Column (name = "verify_id")
    public Long getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(Long verifyId) {
        this.verifyId = verifyId;
    }

    @Column (name = "verify_time")
    public Long getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    @Column (name = "verify_conclusion")
    public String getVerifyConclusion() {
        return verifyConclusion;
    }

    public void setVerifyConclusion(String verifyConclusion) {
        this.verifyConclusion = verifyConclusion;
    }
}
