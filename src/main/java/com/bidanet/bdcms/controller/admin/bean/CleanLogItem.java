package com.bidanet.bdcms.controller.admin.bean;

/**
 * Created by Administrator on 2016/8/14.
 */
public class CleanLogItem {

    private String clearType;

    private Float price;

    private String createTime;

    private String orderName;

    private String userName;

    private String mobile;

    public String getClearType() {
        return clearType;
    }

    public void setClearType(String clearType) {
        this.clearType = clearType;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
