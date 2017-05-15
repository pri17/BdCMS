package com.bidanet.bdcms.entity;

import com.bidanet.bdcms.common.SystemContent;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;

/**
 * 血检或者肠检结果表. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-10 13:30
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table (name = "exam_blod_intestinal")
public class ExamBlodIntestinal {

    private long id;

    private Long memberId;

    private String projectName;

    private String examNumber;

    private Long projectId;

    /**
     * 是否合格  0：未判断  1：合格 2：不合格 3：已采集
     */
    private Integer isQualified;

    private String isQualifiedStr;

    /**
     * 对应的血检肠检问题描述表
     */
    private Long examResultId;

    /**
     * 对应生成的血检号
     */
    private Long examBlodId;

    /**
     * 是否复检   1：是 0：否
     */
    private Integer isRecheck;

    private String isRecheckStr;

    /**s
     * 肠检id
     */
    private Long examIntestinalId;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 录入状态 0 未录入 1 已录入
     */
    private Integer entryState;

    /**
     * 判断时间
     */
    private Long judgeTime;

    private String judgeTimeStr;

    private String remark;

    private Long memberExamId;

    private String examResult;

    private Long createTime;

    // 检查类型：1、内科检查 2、胸部X线检查 3、甲肝 4、戊肝 5、谷丙转氨酶 6、肠道带菌检查
    private Integer type;

//    private ExamBlod examBlod;
//
//    private ExamIntestinal examIntestinal;
    //复检标志：0 1 2 3 只要有复检项目需要就逐级递增    取最新的一条数据
    private Integer recheckTag;

    private String recheckTagStr;

    //更新标志 一开始默认为0 当复检项目进来时 写入1 当复检管理确认好后 在修改为0
    private Integer isShowRefresh;

    //是否最新标识：0、否 1、是  默认为1
    private Integer isNew;

    private ExamMemberExam memberExam;

    private ExamProject examProject;

    private Integer isCancel;

    private Integer isPush;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column (name = "exam_number")
    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    @Column (name = "member_id")
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Column (name = "project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column (name = "project_id")
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Column (name = "is_qualified")
    public Integer getIsQualified() {
        return isQualified;
    }

    public void setIsQualified(Integer isQualified) {
        this.isQualified = isQualified;
    }

    @Column (name = "exam_result_id")
    public Long getExamResultId() {
        return examResultId;
    }

    public void setExamResultId(Long examResultId) {
        this.examResultId = examResultId;
    }

    @Column (name = "exam_blod_id")
    public Long getExamBlodId() {
        return examBlodId;
    }

    public void setExamBlodId(Long examBlodId) {
        this.examBlodId = examBlodId;
    }

    @Column (name = "is_recheck")
    public Integer getIsRecheck() {
        return isRecheck;
    }

    public void setIsRecheck(Integer isRecheck) {
        this.isRecheck = isRecheck;
    }

    @Column (name = "exam_intestinal_id")
    public Long getExamIntestinalId() {
        return examIntestinalId;
    }

    public void setExamIntestinalId(Long examIntestinalId) {
        this.examIntestinalId = examIntestinalId;
    }

    @Column (name = "doctor_name")
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Column (name = "entry_state")
    public Integer getEntryState() {
        return entryState;
    }

    public void setEntryState(Integer entryState) {
        this.entryState = entryState;
    }

    @Column(name="judge_time")
    public Long getJudgeTime() {
        return judgeTime;
    }

    public void setJudgeTime(Long judgeTime) {
        this.judgeTime = judgeTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name="member_exam_id")
    public Long getMemberExamId() {
        return memberExamId;
    }

    public void setMemberExamId(Long memberExamId) {
        this.memberExamId = memberExamId;
    }

    @Column(name="exam_result")
    public String getExamResult() {
        return examResult;
    }

    public void setExamResult(String examResult) {
        this.examResult = examResult;
    }

    @Column(name="create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamProject getExamProject() {
        return examProject;
    }

    public void setExamProject(ExamProject examProject) {
        this.examProject = examProject;
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

    //    @ManyToOne
//    @JSONField(serialize = false)
//    @JoinColumn(name = "exam_blod_id",insertable = false,updatable = false)
//    public ExamBlod getExamBlod() {
//        return examBlod;
//    }
//
//    public void setExamBlod(ExamBlod examBlod) {
//        this.examBlod = examBlod;
//    }

//    @ManyToOne
//    @JoinColumn(name = "exam_intestinal_id",insertable = false,updatable = false)
//    public ExamIntestinal getExamIntestinal() {
//        return examIntestinal;
//    }
//
//    public void setExamIntestinal(ExamIntestinal examIntestinal) {
//        this.examIntestinal = examIntestinal;
//    }

    @Transient
    public String getJudgeTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.judgeTime!=null){
            return df.format(this.judgeTime);
        }else{
            return "";
        }
    }

    public void setJudgeTimeStr(String judgeTimeStr) {
        this.judgeTimeStr = judgeTimeStr;
    }

    //是否合格  0：未判断  1：合格 2：不合格 3：已采集
    @Transient
    public String getIsQualifiedStr() {
       if (this.isQualified==0){
            return SystemContent.QUALIFIED_NOJUDGE;
       }else if(this.isQualified==1){
           return SystemContent.QUALIFIED_SUCCESS;
       }else if(this.isQualified==2){
           return SystemContent.QUALIFIED_FAIL;
       }else if(this.isQualified==3){
           return SystemContent.QUALIFIED_GATHER;
       }

        return "";
    }

    public void setIsQualifiedStr(String isQualifiedStr) {
        this.isQualifiedStr = isQualifiedStr;
    }

    @Transient
    public String getIsRecheckStr() {
        if (this.isRecheck==0){

            return  "否";

        }else if(this.isRecheck == 1){
            return "是";
        }

        return "";
    }

    public void setIsRecheckStr(String isRecheckStr) {
        this.isRecheckStr = isRecheckStr;
    }

    @Column (name = "recheck_tag")
    public Integer getRecheckTag() {
        return recheckTag;
    }

    public void setRecheckTag(Integer recheckTag) {
        this.recheckTag = recheckTag;
    }

    @Transient
    public String getRecheckTagStr() {
        if (this.recheckTag>0){
            return "复检";
        }else{
            return "初检";
        }
    }

    public void setRecheckTagStr(String recheckTagStr) {
        this.recheckTagStr = recheckTagStr;
    }

    @Column (name = "is_show_refresh")
    public Integer getIsShowRefresh() {
        return isShowRefresh;
    }

    public void setIsShowRefresh(Integer isShowRefresh) {
        this.isShowRefresh = isShowRefresh;
    }

    @Column (name = "is_new")
    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    @Column (name = "is_cancel")
    public Integer getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Integer isCancel) {
        this.isCancel = isCancel;
    }

    @Column (name="is_push")
    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }
}
