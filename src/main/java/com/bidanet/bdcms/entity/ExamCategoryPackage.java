package com.bidanet.bdcms.entity;


import javax.persistence.*;

/**
 * 行业体检项目表
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_category_package")

public class ExamCategoryPackage {

    private Long id;
    /**
     * 行业分类名称
     */
    private Long categoryId;
    /**
     * 父级id
     */
    private Long packageId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "category_id")
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name = "package_id")
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
}
