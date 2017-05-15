package com.bidanet.bdcms.entity;


import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 体检医生
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_doctor")

public class ExamDoctor {

    private Long id;
    /**
     * 体检机构id-乡村卫生院
     */
    private Long agenciesId;
    /**
     * 体检机构名称
     */
    private String agenciesName;
    /**
     * 体检项目id
     */
    private Long projectId;

    /**
     * 体检项目名称
     */
    private String projectName;

    /**
     * 医生名称
     */
    private String doctorName;

    private Long createTime;

    private Long updateTime;

    private Long areaId;

    private ExamArea examArea;

    private  Integer level;

    private Long parentId;

    private Integer code;

    @JSONField(serialize = false)
    private ExamDoctor parentExamDoctor;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "agencies_id")
    public Long getAgenciesId() {
        return agenciesId;
    }

    public void setAgenciesId(Long agenciesId) {
        this.agenciesId = agenciesId;
    }

//    @Column(name = "agencies_name")
//    public String getAgenciesName() {
//        return agenciesName;
//    }
//
//    public void setAgenciesName(String agenciesName) {
//        this.agenciesName = agenciesName;
//    }

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

    @Column(name = "doctor_name")
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id",insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @JSONField(serialize = false)
    public ExamArea getExamArea() {
        return examArea;
    }

    public void setExamArea(ExamArea examArea) {
        this.examArea = examArea;
    }

    @Column( name="area_id")
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(name = "parent_id")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id",insertable = false,updatable = false,foreignKey = @ForeignKey(name = "none"))
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamDoctor getParentExamDoctor() {
        return parentExamDoctor;
    }

    public void setParentExamDoctor(ExamDoctor parentExamDoctor) {
        this.parentExamDoctor = parentExamDoctor;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Transient
    public String getAgenciesName() {
        return agenciesName;
    }

    public void setAgenciesName(String agenciesName) {
        this.agenciesName = agenciesName;
    }
}
