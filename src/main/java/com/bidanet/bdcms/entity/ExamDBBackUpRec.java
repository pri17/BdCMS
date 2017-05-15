package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * 数据库备份删除. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-11 14:30
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table (name = "exam_backup_database")
public class ExamDBBackUpRec {

    private Long id;

    private String creator;

    private Long createTime;

    private int isCurrent;

    private String path;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column (name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column (name = "is_current")
    public int getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(int isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
