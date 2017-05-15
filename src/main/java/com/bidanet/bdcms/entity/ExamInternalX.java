package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * 内科或者x线科检查结果. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-10 14:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table (name = "exam_internal_x")
public class ExamInternalX {

    private long id;

    private long memberId;

    /**
     * 是否合格 0：不合格 1：合格
     */
    private int isQualified;

    /**
     * 是否合格描述
     */
    private String description;

    /**
     * 备注
     */
    private  String remark;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 医生id
     */
    private long doctorId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column (name = "member_id")
    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Column (name = "is_qualified")
    public int getIsQualified() {
        return isQualified;
    }

    public void setIsQualified(int isQualified) {
        this.isQualified = isQualified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column (name = "doctor_name")
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Column (name = "doctor_id")
    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }
}
