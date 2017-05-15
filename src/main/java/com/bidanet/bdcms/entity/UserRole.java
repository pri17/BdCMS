package com.bidanet.bdcms.entity;


import javax.persistence.*;

/**
 * 用户角色
 */
@Entity
@Table(name = "sys_user_role")
public class UserRole {
    private long id;
    /**
     * 用户UID
     */
    private Long uid;
    /**
     * 用户的角色ID
     */
    private Long roleId;

    private User user;
    private Role role;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Column(name = "role_id")
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid",insertable = false,updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id",insertable = false,updatable = false)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
