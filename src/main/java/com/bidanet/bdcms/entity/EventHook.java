package com.bidanet.bdcms.entity;

import com.bidanet.bdcms.entity.entityEnum.Status;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/8/22.
 */
@Entity
@Table(name = "_event_hook")
public class EventHook {
    private long id;
    /**
     * 事件标识
     */
    private String eventTag;
    /**
     * 事件代码
     */
    private String eventCode;
    /**
     * 事件参数
     */
    private String param;
    /**
     * 状态码
     */
    private Status status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "event_tag")
    public String getEventTag() {
        return eventTag;
    }

    public void setEventTag(String eventTag) {
        this.eventTag = eventTag;
    }

    @Column(name = "event_code")
    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    @Column(columnDefinition = "text")
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Enumerated(EnumType.ORDINAL)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
