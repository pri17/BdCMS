package com.bidanet.bdcms.entity;

import com.bidanet.bdcms.entity.entityEnum.Status;

import javax.persistence.*;

/**
 * 消息模板
 */
@Entity
@Table(name = "sys_message_tpl")
public class MessageTpl {
    private long id;
    /**
     * 发布人
     */
    private String name;
    /**
     * 版本内容
     */
    private String content;

    private Status status;
    /**
     * 验证码
     */
    private String code;
    private String type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "content",columnDefinition = "text")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Enumerated(value = EnumType.ORDINAL)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
