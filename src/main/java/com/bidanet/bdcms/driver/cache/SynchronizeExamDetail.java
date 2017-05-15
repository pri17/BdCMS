package com.bidanet.bdcms.driver.cache;

import java.io.Serializable;
import java.util.List;

/**
 * 省平台上传体检信息. <br>
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
public class SynchronizeExamDetail implements Serializable {
    //主键
    private String uuid;
    //体检编号
    private String bhkCode;
    //人员姓名
    private String empName;
    //性别  1男2女
    private String sex;
    //婚否
    private String isxmrd;
    //年龄
    private String age;
    //出生日期
    private String birthDay;
    //身份证号码
    private String idc;
    //企业名称
    private String crptName;
    //发证类别
    private String tradeSort;
    //工作工种
    private String wrkNam;
    //体检日期
    private String bhkDate;
    //体检结论  1:合格发证，2：复检，3：不合格
    private String bhkRst;
    //主捡结论描述
    private String bhkAdv;

    private List<SynchronizeExamDetail> synchronizeExamDetailList;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBhkCode() {
        return bhkCode;
    }

    public void setBhkCode(String bhkCode) {
        this.bhkCode = bhkCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsxmrd() {
        return isxmrd;
    }

    public void setIsxmrd(String isxmrd) {
        this.isxmrd = isxmrd;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }

    public String getCrptName() {
        return crptName;
    }

    public void setCrptName(String crptName) {
        this.crptName = crptName;
    }

    public String getTradeSort() {
        return tradeSort;
    }

    public void setTradeSort(String tradeSort) {
        this.tradeSort = tradeSort;
    }

    public String getWrkNam() {
        return wrkNam;
    }

    public void setWrkNam(String wrkNam) {
        this.wrkNam = wrkNam;
    }

    public String getBhkDate() {
        return bhkDate;
    }

    public void setBhkDate(String bhkDate) {
        this.bhkDate = bhkDate;
    }

    public String getBhkRst() {
        return bhkRst;
    }

    public void setBhkRst(String bhkRst) {
        this.bhkRst = bhkRst;
    }

    public String getBhkAdv() {
        return bhkAdv;
    }

    public void setBhkAdv(String bhkAdv) {
        this.bhkAdv = bhkAdv;
    }

    public List<SynchronizeExamDetail> getSynchronizeExamDetailList() {
        return synchronizeExamDetailList;
    }

    public void setSynchronizeExamDetailList(List<SynchronizeExamDetail> synchronizeExamDetailList) {
        this.synchronizeExamDetailList = synchronizeExamDetailList;
    }
}
