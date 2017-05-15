package com.bidanet.bdcms.entity;


import javax.persistence.*;

/**
 * 角色
 */
@Entity
@Table(name = "sys_log")
public class SysLog {

    private long id;
    /**
     * 发布人
     */
    private String creator;
    /**
     * 版本标题
     */
    private String title;
    /**
     * 版本内容
     */
    private String content;
    /**
     * 发布时间
     */
    private Long createTime;
    /**
     * 修改人
     */
    private String updater;
    /**
     * 修改时间
     */
    private Long updateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    @Column(name = "update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
