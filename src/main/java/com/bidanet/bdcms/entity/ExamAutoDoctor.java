package com.bidanet.bdcms.entity;


import javax.persistence.*;

/**
 * 体检区域表.
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_auto_doctor")
public class ExamAutoDoctor {

    private Long id;
    /**
     * 区域名称
     */
    private String name;

    private Long agenciesId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "agencies_id")
    public Long getAgenciesId() {
        return agenciesId;
    }

    public void setAgenciesId(Long agenciesId) {
        this.agenciesId = agenciesId;
    }
}
