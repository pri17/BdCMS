package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * 角色权限
 */
@Entity
@Table(name = "sys_role_permission")
public class RolePermission {
    private long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 权限ID
     */
    private Long permissionId;

    private Role role;
    private Permission permission;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "role_id")
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    @Column(name = "permission_id")
    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    @ManyToOne
    @JoinColumn(name = "role_id",insertable = false,updatable = false)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @ManyToOne
    @JoinColumn(name = "permission_id",insertable = false,updatable = false)
    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
