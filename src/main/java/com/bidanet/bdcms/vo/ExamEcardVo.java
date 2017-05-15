package com.bidanet.bdcms.vo;

import com.bidanet.bdcms.entity.ExamEcard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gao on 2017/2/21.
 */
public class ExamEcardVo {
    private Long idx;

    /**
     * 健康证编号
     */
    private String jkzcod;
    /*
    人员名称
     */
    private String empname;
    /**
     * 企业名称
     */
    private String crpname;

    /**
     * 有效期
     */
    private String inddate;

    /**
     * categoryId
     * 发证类别编码
     */
    private String tradeCode;
    /**
     * 工种
     */
    private String wrkname;
    /**
     * 地区名称
     */
    private String areaname;
    /**
     * 培训日期
     */
    private String trddat;
    /**
     * 发证日期 issueTime
     */
    private String scrdat;
    /**
     * 发证机关 eCardAgency
     */
    private String scrorg;
    /**
     * 体检编号 examNumber
     */
    private String bhkCode;


    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getJkzcod() {
        return jkzcod;
    }

    public void setJkzcod(String jkzcod) {
        this.jkzcod = jkzcod;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getCrpname() {
        return crpname;
    }

    public void setCrpname(String crpname) {
        this.crpname = crpname;
    }

    public String getInddate() {
        return inddate;
    }

    public void setInddate(String inddate) {
        this.inddate = inddate;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getWrkname() {
        return wrkname;
    }

    public void setWrkname(String wrkname) {
        this.wrkname = wrkname;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getTrddat() {
        return trddat;
    }

    public void setTrddat(String trddat) {
        this.trddat = trddat;
    }

    public String getScrdat() {
        return scrdat;
    }

    public void setScrdat(String scrdat) {
        this.scrdat = scrdat;
    }

    public String getScrorg() {
        return scrorg;
    }

    public void setScrorg(String scrorg) {
        this.scrorg = scrorg;
    }

    public String getBhkCode() {
        return bhkCode;
    }

    public void setBhkCode(String bhkCode) {
        this.bhkCode = bhkCode;
    }

    public void assign(ExamEcard examEcard){
        this.setIdx(examEcard.getId());
        this.setJkzcod(examEcard.geteCardNumber());
        this.setEmpname(examEcard.getName());
        this.setCrpname(examEcard.getWorkUnit());
        SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
        Date expDate=new Date(examEcard.getExpTime());
        this.setInddate(sdf.format(expDate));
        List<Long> categoryIds=new ArrayList<Long>();
        categoryIds.add(89L);
        categoryIds.add(88L);
        categoryIds.add(90L);
        categoryIds.add(91L);
        categoryIds.add(5L);
        categoryIds.add(76L);
        categoryIds.add(77L);
        categoryIds.add(78L);
        categoryIds.add(79L);
        categoryIds.add(81L);
        categoryIds.add(82L);
        categoryIds.add(83L);
        categoryIds.add(84L);
        categoryIds.add(94L);
        if(categoryIds.contains(examEcard.getCategoryId())){
            this.setTradeCode("1002");
        }else{
            this.setTradeCode("1001");
        }

        this.setWrkname("");
        this.setAreaname(examEcard.getAreaName());
        this.setTrddat("");
        Date scrdate=new Date(examEcard.getIssueTime());
        this.setScrdat(sdf.format(scrdate));
        this.setScrorg(examEcard.geteCardAgency());
        this.setBhkCode(examEcard.getExamNumber());
    }
}
