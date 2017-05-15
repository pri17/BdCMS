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
public class TogetherQueryEntity implements Serializable {

    private Long id;

    private String eCardNumber;

    private String name;

    private String sexStr;

    private String idCardNum;

    private String areaName;

    private String workUnit;

    private String categoryStr;

    private String examNumber;

    private String examTimeStr;

    private String expTimeStr;

    private String payTypeStr;
    //2 收费 1 未收
    private String payStateStr;

    private String remarks;

    //是否复检
    private String isReCheckStr;

    private Integer isRecheck;

    private Integer recheckTag;

    private String recheckTagStr;

    private String channelStr;

    private String mobile;

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

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    public String getExamTimeStr() {
        return examTimeStr;
    }

    public void setExamTimeStr(String examTimeStr) {
        this.examTimeStr = examTimeStr;
    }

    public String getExpTimeStr() {
        return expTimeStr;
    }

    public void setExpTimeStr(String expTimeStr) {
        this.expTimeStr = expTimeStr;
    }

    public String getPayTypeStr() {
        return payTypeStr;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public String getPayStateStr() {
        return payStateStr;
    }

    public void setPayStateStr(String payStateStr) {
        this.payStateStr = payStateStr;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCategoryStr() {
        return categoryStr;
    }

    public void setCategoryStr(String categoryStr) {
        this.categoryStr = categoryStr;
    }

    public String getIsReCheckStr() {
        return isReCheckStr;
    }

    public void setIsReCheckStr(String isReCheckStr) {
        this.isReCheckStr = isReCheckStr;
    }

    public Integer getIsRecheck() {
        return isRecheck;
    }

    public void setIsRecheck(Integer isRecheck) {
        this.isRecheck = isRecheck;
    }

    public Integer getRecheckTag() {
        return recheckTag;
    }

    public void setRecheckTag(Integer recheckTag) {
        this.recheckTag = recheckTag;
    }

    public String getRecheckTagStr() {
        return recheckTagStr;
    }

    public void setRecheckTagStr(String recheckTagStr) {
        this.recheckTagStr = recheckTagStr;
    }

    public String getChannelStr() {
        return channelStr;
    }

    public void setChannelStr(String channelStr) {
        this.channelStr = channelStr;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
