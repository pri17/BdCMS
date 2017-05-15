package com.bidanet.bdcms.bean;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by asus on 2017/3/7.
 */
public class CompanyPeopleInform {
    @Excel(name="编号")
    private String id;
    @Excel(name = "身份证号")
    private String idCardNum;
    @Excel(name = "工作单位")
    private String company;
    @Excel(name = "联系电话")
    private String phoneNum;
    @Excel(name = "行业类别")
    private String category;
    @Excel(name = "行业")
    private String industry;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
