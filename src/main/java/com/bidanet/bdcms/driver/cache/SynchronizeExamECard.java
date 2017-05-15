package com.bidanet.bdcms.driver.cache;

import java.io.Serializable;

/**
 * 省平台健康证信息上传. <br>
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
public class SynchronizeExamECard implements Serializable {

    //业务主键
    private String idx;
    //健康证编码
    private String jkzcod;
    //人员姓名
    private String empname;
    //企业名称
    private String crpname;
    //失效日期
    private String inddate;
    //发证类别编码  1001 食品  1002 其他
    private String tradeCode;
    //工种
    private String wrkname;
    //地区名称
    private String areaname;
    //培训日期
    private String trddat;
    //发证日期
    private String scrdat;
    //发证机关
    private String scrorg;
    //体检编号
    private String bhkCode;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
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
}
