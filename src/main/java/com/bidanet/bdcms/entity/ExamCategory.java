package com.bidanet.bdcms.entity;


import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 行业分类
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_category")

public class ExamCategory {

    private Long id;
    /**
     * 行业分类名称
     */
    private String categoryName;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 父级id
     */
    private Long parentId;

    private Long code;

    @JSONField(serialize = false)
    private ExamCategory parentExamCategory;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(name = "parent_id")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id",insertable = false,updatable = false,foreignKey = @ForeignKey(name = "none"))
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamCategory getParentExamCategory() {
        return parentExamCategory;
    }

    public void setParentExamCategory(ExamCategory parentExamCategory) {
        this.parentExamCategory = parentExamCategory;
    }
}
