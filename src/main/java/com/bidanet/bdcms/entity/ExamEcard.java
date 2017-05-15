package com.bidanet.bdcms.entity;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;

/**
 * 电子健康证
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_ecard")
public class ExamEcard {

    private Long id;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 健康证编号
     */
    private String eCardNumber;
    /**
     * 发证时间
     */
    private Long createTime;
    /**
     * 发证机构
     */
    private String eCardAgency;
    /**
     * 头像
     */
    private String memberPhoto;
    /**
     * 年龄
     */
    private Integer memberAge;

    /**
     * 体检号id
     */
    private Long  examNumberId;
    /**
     * 有效期
     */
    private Long expTime;

    private String expTimeStr;

    private String examNumber;

    private Long memberExamId;

    private String workUnit;

    private Long areaId;

    private Long categoryId;

    private Long examTime;

    private Long auditTime;

    private String auditTimeStr;

    private Long issueTime;

    private String issueTimeStr;

    private String issueTimeString;

    private Integer isPrint;

    private String qrCodeUrl;
    private String uuid;
    /**
     * 用户主数据
     */
//    private ExamMember examMember;
//
//    private ExamArea examArea;
//
//    private ExamCategory examCategory;

    private String name;

    private String idCardNumber;

    private Integer sex;

    private String sexStr;

    private String areaName;

    private String categoryName;



    //是否已上传
    private Integer isUpload;

    @Column(name = "is_upload")
    public Integer getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(Integer isUpload) {
        this.isUpload = isUpload;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "member_id")
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Column(name = "eCard_number")
    public String geteCardNumber() {
        return eCardNumber;
    }

    public void seteCardNumber(String eCardNumber) {
        this.eCardNumber = eCardNumber;
    }

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "eCard_agency")
    public String geteCardAgency() {
        return eCardAgency;
    }

    public void seteCardAgency(String eCardAgency) {
        this.eCardAgency = eCardAgency;
    }

    @Column(name = "member_photo")
    public String getMemberPhoto() {
        return memberPhoto;
    }

    public void setMemberPhoto(String memberPhoto) {
        this.memberPhoto = memberPhoto;
    }

    @Column(name = "member_age")
    public Integer getMemberAge() {
        return memberAge;
    }

    public void setMemberAge(Integer memberAge) {
        this.memberAge = memberAge;
    }

    @Column (name = "exam_number_id")
    public Long getExamNumberId() {
        return examNumberId;
    }

    public void setExamNumberId(Long examNumberId) {
        this.examNumberId = examNumberId;
    }

    @Column(name = "exp_time")
    public Long getExpTime() {
        return expTime;
    }

    public void setExpTime(Long expTime) {
        this.expTime = expTime;
    }

    @Transient
    public String getExpTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.expTime!=null){
            return df.format(this.expTime);
        }else{
            return "";
        }
    }

    public void setExpTimeStr(String expTimeStr) {
        this.expTimeStr = expTimeStr;
    }

    @Column (name = "exam_number")
    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    @Column (name = "member_exam_id")
    public Long getMemberExamId() {
        return memberExamId;
    }

    public void setMemberExamId(Long memberExamId) {
        this.memberExamId = memberExamId;
    }

    @Column (name = "work_unit")
    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    @Column (name = "area_id")
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Column (name = "category_id")
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Column (name = "exam_time")
    public Long getExamTime() {
        return examTime;
    }

    public void setExamTime(Long examTime) {
        this.examTime = examTime;
    }

    @Transient
    public String getExamTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.examTime!=null){
            return df.format(this.examTime);
        }else{
            return "";
        }
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamMember getExamMember() {
//        return examMember;
//    }
//
//    public void setExamMember(ExamMember examMember) {
//        this.examMember = examMember;
//    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "area_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamArea getExamArea() {
//        return examArea;
//    }
//
//    public void setExamArea(ExamArea examArea) {
//        this.examArea = examArea;
//    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamCategory getExamCategory() {
//        return examCategory;
//    }
//
//    public void setExamCategory(ExamCategory examCategory) {
//        this.examCategory = examCategory;
//    }

    @Column(name="audit_time")
    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    @Column(name="issue_time")
    public Long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Long issueTime) {
        this.issueTime = issueTime;
    }

    @Transient
    public String getIssueTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        if(this.issueTime!=null){
            return df.format(this.issueTime);
        }else{
            return "";
        }
    }

    public void setIssueTimeStr(String issueTimeStr) {
        this.issueTimeStr = issueTimeStr;
    }

    @Transient
    public String getIssueTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.issueTime!=null){
            return df.format(this.issueTime);
        }else{
            return "";
        }
    }

    public void setIssueTimeString(String issueTimeString) {
        this.issueTimeString = issueTimeString;
    }

    @Column(name="is_print")
    public Integer getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Integer isPrint) {
        this.isPrint = isPrint;
    }


    @Transient
    public String getAuditTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.auditTime!=null){
            return df.format(this.auditTime);
        }else{
            return "";
        }
    }

    public void setAuditTimeStr(String auditTimeStr) {
        this.auditTimeStr = auditTimeStr;
    }

    @Column(name="qr_code_url")
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    @Column (name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column (name = "idcard_number")
    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Transient
    public String getSexStr() {
        String sexstr = "";
        if (this.sex==0){
            sexstr = "女";
        }else if(this.sex==1){
            sexstr = "男";
        }

        return sexstr;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    ////////////////////////////2017-02-06新增////////////////////////////////////////////

    @Column (name = "area_name")
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Column (name = "category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    /**
     * wanng lu 替换img
     * @param fileDomainWei
     */
    @Transient
    public void replaceImg(String fileDomainWei){
        String memberPhoto = getMemberPhoto();

        if(memberPhoto == null || memberPhoto.equals("")){
            return;
        }

        String imgPhoto = "";
        int startStrIndex;
        startStrIndex = memberPhoto.indexOf("http://");
        String sectionPath = memberPhoto.replace("http://","");
        //图片末尾路径下标
        int endPathStartIndex = sectionPath.indexOf("/");

        if(endPathStartIndex != -1){
            String endPathImg = sectionPath.substring(endPathStartIndex);

            imgPhoto = "http://" + fileDomainWei + endPathImg;
            setMemberPhoto(imgPhoto);
        }
    }
}
