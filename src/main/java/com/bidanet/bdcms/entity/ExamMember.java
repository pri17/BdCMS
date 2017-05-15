package com.bidanet.bdcms.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 体检用户信息-公共. <br>
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
@Table(name = "exam_member")
public class ExamMember {

    private Long id;

    private String name;

    private Integer sex;

    /**
     * 民族
     */
    private String nation;

    private String birthday;

    /**
     * 身份证地址
     */

    private String idCardAddress;

    /**
     *身份证号码
     */

    private String idCardNum;

    /**
     * 是否已婚
     */
    private Integer marriage;

    private String mobile;

    /**
     * 既往病史
     */
    private String anamnesis;

    private String icon;

    private Long createTime;

    private Long updateTime;

    private String sexStr;

//    /**
//     * 用户体检相关信息
//     */
//    private ExamMemberExam examMemberExam;

    private String workUnit;

    private Integer isIdCardIcon;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Column(name = "idCard_address")
    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress;
    }

    @Column (name = "idCard_num")
    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public Integer getMarriage() {
        return marriage;
    }

    public void setMarriage(Integer marriage) {
        this.marriage = marriage;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAnamnesis() {
        return anamnesis;
    }

    public void setAnamnesis(String anamnesis) {
        this.anamnesis = anamnesis;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column (name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column (name = "update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Transient
    public String getSexStr() {

        if (this.sex == 1){
            return  "男";
        }else if(this.sex == 0){
            return  "女";
        }
        return null;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    @Column (name = "work_unit")
    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    @Column (name = "is_idCard_icon")
    public Integer getIsIdCardIcon() {
        return isIdCardIcon;
    }

    public void setIsIdCardIcon(Integer isIdCardIcon) {
        this.isIdCardIcon = isIdCardIcon;
    }

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamMemberExam getExamMemberExam() {
//        return examMemberExam;
//    }
//
//    public void setExamMemberExam(ExamMemberExam examMemberExam) {
//        this.examMemberExam = examMemberExam;
//    }

}
