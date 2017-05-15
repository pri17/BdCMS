package com.bidanet.bdcms.entity;

/**
 * Created by pri17 on 2017/5/12.
 */
public class WechatUnbind {


    private Long id;
    private String name;
    private String idCard;
    private String openID;
    private Long bindId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public Long getBindId() {
        return bindId;
    }

    public void setBindId(Long bindId) {
        this.bindId = bindId;
    }



}
