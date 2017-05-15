package com.bidanet.bdcms.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 体检套餐. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-09 16:20
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name="exam_package")
public class ExamPackage {

    private Long id;

    /**
     * 套餐名称
     */
    private String name;
    /**
     * 体检套餐项目
     */
    private String projectId;

    /**
     * 体检套餐费用
     */
    private BigDecimal money;

    /**
     * 创建时间
     */
    private Long createTime;


    /**
     * 体检套餐项目
     */
    private String projectName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name="project_id")
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Column(name="create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name="project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    private String projectIdNameStr;

    @Transient
    public String getProjectIdNameStr() {
        return projectIdNameStr;
    }

    public void setProjectIdNameStr(String projectIdNameStr) {
        this.projectIdNameStr = projectIdNameStr;
    }
}
