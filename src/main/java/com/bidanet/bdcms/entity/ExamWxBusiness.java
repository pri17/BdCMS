package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * 微信端业务. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-09 17:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table (name = "exam_wx_business")
public class ExamWxBusiness {

    private long id;

    /**
     * 业务内容
     */
    private String content;

    private Long createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column (name = "create_time")
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
