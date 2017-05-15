package com.bidanet.bdcms.entity;/**
 * Created by jizhaoqun on 16/10/9.
 */

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 体检项目(价格). <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-09 16:45
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name="exam_project")
public class ExamProject {

    private long id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目价格
     */
    private BigDecimal projectPrice;

    private Long createTime;

    private Long updateTime;

    private Integer topLimit;

    private Integer lowerLimit;

    private String unit;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "project_price")
    public BigDecimal getProjectPrice() {
        return projectPrice;
    }

    public void setProjectPrice(BigDecimal projectPrice) {
        this.projectPrice = projectPrice;
    }

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "top_limit")
    public Integer getTopLimit() {
        return topLimit;
    }

    public void setTopLimit(Integer topLimit) {
        this.topLimit = topLimit;
    }

    @Column(name = "lower_limit")
    public Integer getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Integer lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
