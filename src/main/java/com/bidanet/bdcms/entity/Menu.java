package com.bidanet.bdcms.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 菜单
 */
@Entity
@Table(name = "sys_menu")
public class Menu {
    private long id;
    private String name;
    private String code;

    private String url;
    private Long parentId;
    /**
     * 是否在后台显示
     */
    private Boolean show;
    /**
     * 是否检查 权限
     */
    private Boolean checkAccess;

    private List<Menu> childrend;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Column(name = "parent_id")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Column(name = "_show")
    public Boolean getShow() {return show;}

    public void setShow(Boolean show) {this.show = show;}

    @OneToMany(mappedBy = "parentId")
//    @JoinColumn(name = "parent_id",foreignKey = @ForeignKey(name = "none"),insertable = false,updatable = false)
    public List<Menu> getChildrend() {
        return childrend;
    }

    public void setChildrend(List<Menu> childrend) {
        this.childrend = childrend;
    }

    @Column(name = "check_access")
    public Boolean getCheckAccess() {
        return checkAccess;
    }

    public void setCheckAccess(Boolean checkAccess) {
        this.checkAccess = checkAccess;
    }
}

