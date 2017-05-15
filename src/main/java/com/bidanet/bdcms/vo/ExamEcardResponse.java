package com.bidanet.bdcms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by gao on 2017/2/21.
 */
public class ExamEcardResponse {
        @JsonProperty("desc")
        private String desc;
        @JsonProperty("rtnCode")
        private String rtnCode;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }
}
