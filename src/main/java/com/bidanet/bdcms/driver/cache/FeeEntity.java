package com.bidanet.bdcms.driver.cache;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用于redis存储查询数据. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-12-19
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public class FeeEntity implements Serializable {

//    <th style="text-align:center;">体检号</th>
//            <th style="text-align:center;">姓名</th>
//            <th style="text-align:center;" width="50px">身份证号</th>
//            <th style="text-align:center;">工作单位</th>
//            <th style="text-align:center;">地段区域</th>
//            <th style="text-align:center;">体检日期</th>
//            <th style="text-align:center;">金额</th>
//            <th style="text-align:center;">收费状态</th>
//            <th style="text-align:center;">收费员</th>
//            <th style="text-align:center;">收费日期</th>
//            <th style="text-align:center;">打印发票</th>
//            <th style="text-align:center;">支付方式</th>
//            <th style="text-align:center;">是否复检</th>

    private Long id;

    private Long payId;

    private String examNumber;

    private String name;

    private String idCard;

    private String workUnit;

    private String areaName;

    private String examTimeStr;

    private BigDecimal payMoney;

    //2 已收费  1 未收费
    private String payStateStr;

    private String realName;

    private String payTimeStr;

    // 1 已打印  0 未打印
    private String isPrintStr;

    private String payTypeStr;

    // 1 是 0 否
    private String isRecheckStr;

    //recheckTag>0 是 recheckTag=0 否
    private String recheckTagStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
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

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayStateStr() {
        return payStateStr;
    }

    public void setPayStateStr(String payStateStr) {
        this.payStateStr = payStateStr;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getPayTypeStr() {
        return payTypeStr;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public String getIsRecheckStr() {
        return isRecheckStr;
    }

    public void setIsRecheckStr(String isRecheckStr) {
        this.isRecheckStr = isRecheckStr;
    }

    public String getRecheckTagStr() {
        return recheckTagStr;
    }

    public void setRecheckTagStr(String recheckTagStr) {
        this.recheckTagStr = recheckTagStr;
    }
}
