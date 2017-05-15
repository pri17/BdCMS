package com.bidanet.bdcms.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.bidanet.bdcms.common.TreeNode;
import com.bidanet.bdcms.entity.entityEnum.Status;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

/**
 * parentId=0 是省，
 */
@Entity
@Table(name = "_area")

public class Area implements TreeNode<Long,Area> {

    private long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 拼音
     */
    private String pinyin;
    /**
     * 编号
     */
    private String code;
    /**
     * 父节点
     */
    private Long parentId;


    private Status status;

    @JSONField(serialize = false)
    private Area parentArea;

    private List<Area> children;

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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "parent_id")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Enumerated(value = EnumType.ORDINAL)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id",insertable = false,updatable = false,foreignKey = @ForeignKey(name = "none"))
    @NotFound(action = NotFoundAction.IGNORE)
    public Area getParentArea() {
        return parentArea;
    }

    public void setParentArea(Area parentArea) {
        this.parentArea = parentArea;
    }

    @Transient
    @Override
    public Long getTreeId() {
        return getId();
    }
    @Transient
    @Override
    public Long getTreeParentId() {
        return getParentId();
    }
    @Transient
    @Override
    public void setChildren(List<Area> list) {
        this.children=list;
    }
    @Transient
    @Override
    public List<Area> getChildren() {
        return children;
    }
    @Transient
    @Override
    public boolean isRoot() {

        return getParentId()==0;
    }
}
