package com.bidanet.bdcms.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.bidanet.bdcms.entity.entityEnum.UserStatus;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 用户
 */
@Entity
@Table(name = "sys_user" )
@Inheritance(strategy= InheritanceType.JOINED)
public class User {
    private long uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码加密策略，md5(原始密码+salt)
     */
    private String password;

    private String confirmPassword;

    private String salt;

    private String mobile;

    /**
     * 地段区域ID
     */
    private Long areaId;

    //    /**
//     *  城市
//     */
//    private ExamArea areas;

    private String areaName;

    private Integer areaCode;


    /**
     * 人员状态：  1：通过  0 ：未通过
     */
    private UserStatus status;

    private Long createTime;

    /**
     * 机构id
     */
    private Long agenciesId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 机构
     */
//    private ExamAgencies agencies;

    private String agenciesName;

    private Integer agenciesCode;


    private Long departmentId;


    private String departmentName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Enumerated(EnumType.ORDINAL)
    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "agencies_id")
    public Long getAgenciesId() {
        return agenciesId;
    }

    public void setAgenciesId(Long agenciesId) {
        this.agenciesId = agenciesId;
    }

    @Column(name = "real_name")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Column(name = "area_id")
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }




//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "area_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamArea getAreas() {
//        return areas;
//    }
//
//    public void setAreas(ExamArea areas) {
//        this.areas = areas;
//    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "agencies_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamAgencies getAgencies() {
//        return agencies;
//    }
//
//    public void setAgencies(ExamAgencies agencies) {
//        this.agencies = agencies;
//    }

    @Transient
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Column (name = "department_id")
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Transient
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Transient
    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    @Column (name = "agencies_name")
    public String getAgenciesName() {
        return agenciesName;
    }

    public void setAgenciesName(String agenciesName) {
        this.agenciesName = agenciesName;
    }

    @Column (name = "agencies_code")
    public Integer getAgenciesCode() {
        return agenciesCode;
    }

    public void setAgenciesCode(Integer agenciesCode) {
        this.agenciesCode = agenciesCode;
    }
}
