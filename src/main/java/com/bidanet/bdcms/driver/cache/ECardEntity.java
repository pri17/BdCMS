package com.bidanet.bdcms.driver.cache;

import java.io.Serializable;

/**
 * . <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c)
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public class ECardEntity implements Serializable {

    private Long id;

    private String eCardNumber;

    private String examNumber;

    private String name;

    private String sexStr;

    private String idCardNum;

    private String areaName;

    private String workUnit;

    private String categoryName;

    private String examTimeStr;

    private String auditTimeStr;

    private String issueTimeString;

    private String expTimeStr;

    private Integer memberAge;

    private String qrCodeUrl;

    private String iconUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String geteCardNumber() {
        return eCardNumber;
    }

    public void seteCardNumber(String eCardNumber) {
        this.eCardNumber = eCardNumber;
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getExamTimeStr() {
        return examTimeStr;
    }

    public void setExamTimeStr(String examTimeStr) {
        this.examTimeStr = examTimeStr;
    }

    public String getAuditTimeStr() {
        return auditTimeStr;
    }

    public void setAuditTimeStr(String auditTimeStr) {
        this.auditTimeStr = auditTimeStr;
    }

    public String getIssueTimeString() {
        return issueTimeString;
    }

    public void setIssueTimeString(String issueTimeString) {
        this.issueTimeString = issueTimeString;
    }

    public String getExpTimeStr() {
        return expTimeStr;
    }

    public void setExpTimeStr(String expTimeStr) {
        this.expTimeStr = expTimeStr;
    }

    public Integer getMemberAge() {
        return memberAge;
    }

    public void setMemberAge(Integer memberAge) {
        this.memberAge = memberAge;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
