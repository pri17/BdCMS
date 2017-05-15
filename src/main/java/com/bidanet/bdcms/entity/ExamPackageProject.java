package com.bidanet.bdcms.entity;/**
 * Created by jizhaoqun on 16/10/9.
 */

import org.hibernate.annotations.CollectionId;

import javax.persistence.*;

/**
 * 套餐跟项目关联表. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-09 16:29
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name="exam_package_project")
public class ExamPackageProject {

    private long id;

    /**
     * 套餐id
     */
    private Long packageId;

    /**
     * 项目id
     */
    private Long projectId;

    private Long createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="package_id")
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    @Column(name="project_id")
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Column(name="create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
