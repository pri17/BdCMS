package com.bidanet.bdcms.entity;

import com.bidanet.bdcms.entity.entityEnum.Status;

import javax.persistence.*;

/**
 * 权限
 */
@Entity
@Table(name = "sys_permission")
public class Permission {
    private long id;
    private String permission;
    private String description;
    private Status status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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
}
