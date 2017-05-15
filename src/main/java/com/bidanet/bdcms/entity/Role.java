package com.bidanet.bdcms.entity;

import com.bidanet.bdcms.entity.entityEnum.Status;

import javax.persistence.*;
import java.util.Set;

/**
 * 角色
 */
@Entity
@Table(name = "sys_role",uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Role {

    private long id;
    /**
     * 角色名称
     */
    private String role;
    /**
     * 角色代码
     */
    private String code;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 角色状态
     */
    private Status status;
    /**
     * 角色初始化url
     */
    private  String indexUrl;

    private String indexTabid;

    private String indexName;
    private Set<RoleMenu> menus;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Enumerated(EnumType.ORDINAL)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @OneToMany
    @JoinColumn(name = "role_id")
    public Set<RoleMenu> getMenus() {
        return menus;
    }

    public void setMenus(Set<RoleMenu> menus) {
        this.menus = menus;
    }

    @Column(name = "index_url",columnDefinition = "text")
    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }
    @Column(name = "index_tabid")
    public String getIndexTabid() {
        return indexTabid;
    }

    public void setIndexTabid(String indexTabid) {
        this.indexTabid = indexTabid;
    }
    @Column(name = "index_name")
    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
}
