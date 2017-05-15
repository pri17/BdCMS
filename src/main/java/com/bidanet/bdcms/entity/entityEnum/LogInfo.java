package com.bidanet.bdcms.entity.entityEnum;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Administrator on 2016/7/3.
 */
@Document(collection = "log")
public class LogInfo<T> {
    public static final int INFO=0;
    public static final int WARN=1;
    public static final int ERROR=2;

    private String id;
    private Integer level;
    private String tag;
    private String msg;
    private T ext;
    private String stackInfo;
    private Long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStackInfo() {
        return stackInfo;
    }

    public void setStackInfo(String stackInfo) {
        this.stackInfo = stackInfo;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public T getExt() {
        return ext;
    }

    public void setExt(T ext) {
        this.ext = ext;
    }
}
