package com.bidanet.bdcms.bean;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by asus on 2017/3/17.
 */
public class WXPay {
    @Excel(name = "体检号")
    private String examNumber;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "身份证号")
    private String idCard;
    @Excel(name = "工作单位")
    private String workUnit;
    @Excel(name = "地段区域")
    private String areaName;
    @Excel(name = "体检日期")
    private String examTimeStr;
    @Excel(name="数量")
    private String number;
    @Excel(name = "金额")
    private String payMoney;
    @Excel(name = "收费日期")
    private String payTimeStr;
    @Excel(name = "打印发票")
    private String isPrintStr;
    @Excel(name = "是否复检")
    private String recheckTagStr;

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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getExamTimeStr() {
        return examTimeStr;
    }

    public void setExamTimeStr(String examTimeStr) {
        this.examTimeStr = examTimeStr;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayTimeStr() {
        return payTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }

    public String getIsPrintStr() {
        return isPrintStr;
    }

    public void setIsPrintStr(String isPrintStr) {
        this.isPrintStr = isPrintStr;
    }

    public String getRecheckTagStr() {
        return recheckTagStr;
    }

    public void setRecheckTagStr(String recheckTagStr) {
        this.recheckTagStr = recheckTagStr;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
