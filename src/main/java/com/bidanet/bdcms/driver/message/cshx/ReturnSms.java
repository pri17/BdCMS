package com.bidanet.bdcms.driver.message.cshx;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Administrator on 2016/7/3.
 */
@XStreamAlias("returnsms")
public class ReturnSms{

    public static final String FAILD="Faild";
    public static final String SUCCESS="Success";
    String returnstatus;
    String message;
    Float remainpoint;
    String taskID;
    Integer successCounts;

    public String getReturnstatus() {
        return returnstatus;
    }

    public void setReturnstatus(String returnstatus) {
        this.returnstatus = returnstatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getRemainpoint() {
        return remainpoint;
    }

    public void setRemainpoint(Float remainpoint) {
        this.remainpoint = remainpoint;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public Integer getSuccessCounts() {
        return successCounts;
    }

    public void setSuccessCounts(Integer successCounts) {
        this.successCounts = successCounts;
    }


    public boolean isSuccess(){
        return SUCCESS.equals(getReturnstatus());
    }


}