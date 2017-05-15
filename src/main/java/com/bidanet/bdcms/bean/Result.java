package com.bidanet.bdcms.bean;

/**
 * Created by Administrator on 2016/6/28.
 */
public class Result {
    public static final int STATUS_OK=200;
    public static final int STATUS_ERROR=300;
    public static final int STATUS_TIMEOUT=301;

    protected int statusCode;
    protected String message;
    protected String param;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Result(int statusCode, String message,String param) {
        this.statusCode = statusCode;
        this.message = message;
        this.param = param;
    }



    public Result() {
    }


}
