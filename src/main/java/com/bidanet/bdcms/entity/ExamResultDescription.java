package com.bidanet.bdcms.entity;


import javax.persistence.*;

/**
 * x线内科血检肠检描述表. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-10 11:25
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_result_description")
public class ExamResultDescription {

    private long id;

    private long projectId;

    private String description;

    private Long createTime;

    private Integer Type; //合格与否对应不同结果描述 1 合格 2 不合格

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "project_id")
    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column (name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }
}
