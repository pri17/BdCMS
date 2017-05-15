package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/7/13.
 */
@Entity
@Table(name = "sys_role_menu")
public class RoleMenu {
    private long id;
    private Long roleId;
    private Long menuId;
    private Role role;
    private Menu menu;


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
    @Column(name = "menu_id")
    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
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
    @JoinColumn(name = "menu_id",insertable = false,updatable = false)
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
