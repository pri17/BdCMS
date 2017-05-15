package com.bidanet.bdcms.bean;

import java.util.Date;

/**
 * Created by xuejike-pc on 2017/2/21.
 */
public class PhysicalItemDetails {

    /*编号*/
    private Long id;
    /*体检号*/
    private String medicalNumber ;
    /*体检项目编号*/
    private String physicalItemNumber;
    /*体检结果*/
    private String physicalResult;
    /*合格标志*/
    private String qualifiedMark;
    /*判断人*/
    private String judgePeople;
    /*判断日期*/
    private Date judgeDate;
    /*备注*/
    private String remarks;
    /*复检标志*/
    private String reSign;
    /*更新标志*/
    private String updateFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicalNumber() {
        return medicalNumber;
    }

    public void setMedicalNumber(String medicalNumber) {
        this.medicalNumber = medicalNumber;
    }

    public String getPhysicalItemNumber() {
        return physicalItemNumber;
    }

    public void setPhysicalItemNumber(String physicalItemNumber) {
        this.physicalItemNumber = physicalItemNumber;
    }

    public String getPhysicalResult() {
        return physicalResult;
    }

    public void setPhysicalResult(String physicalResult) {
        this.physicalResult = physicalResult;
    }

    public String getQualifiedMark() {
        return qualifiedMark;
    }

    public void setQualifiedMark(String qualifiedMark) {
        this.qualifiedMark = qualifiedMark;
    }

    public String getJudgePeople() {
        return judgePeople;
    }

    public void setJudgePeople(String judgePeople) {
        this.judgePeople = judgePeople;
    }

    public Date getJudgeDate() {
        return judgeDate;
    }

    public void setJudgeDate(Date judgeDate) {
        this.judgeDate = judgeDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReSign() {
        return reSign;
    }

    public void setReSign(String reSign) {
        this.reSign = reSign;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }
}
