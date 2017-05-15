package com.bidanet.bdcms.entity;


import javax.persistence.*;

/**
 * 疫区表-即用户身份证前6位代表的区域-可新增删除e
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_affected")

public class ExamAffected {

    private Long id;
    /**
     * 疫区代码
     */
    private String affectedCode;
    /**
     * 疫区代码对应的区域
     */
    private String affectedName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "affected_code")
    public String getAffectedCode() {
        return affectedCode;
    }

    public void setAffectedCode(String affectedCode) {
        this.affectedCode = affectedCode;
    }

    @Column(name = "affected_name")
    public String getAffectedName() {
        return affectedName;
    }

    public void setAffectedName(String affectedName) {
        this.affectedName = affectedName;
    }
}
