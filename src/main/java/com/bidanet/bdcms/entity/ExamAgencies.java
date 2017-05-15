package com.bidanet.bdcms.entity;


import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 体检机构表
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_agencies")

public class ExamAgencies {

    private long id;
    /**
     * 机构名称
     */
    private String agenciesName;
    /**
     * 机构代码
     */
    private Integer agenciesCode;
    /**
     * 地段区域
     */
    private Long areaId;

    private String areaName;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "agencies_name")
    public String getAgenciesName() {
        return agenciesName;
    }

    public void setAgenciesName(String agenciesName) {
        this.agenciesName = agenciesName;
    }

    @Column(name = "agencies_code")
    public Integer getAgenciesCode() {
        return agenciesCode;
    }

    public void setAgenciesCode(Integer agenciesCode) {
        this.agenciesCode = agenciesCode;
    }

//    @Column(name = "lot_area")
//    public String getLotArea() {
//        return lotArea;
//    }
//
//    public void setLotArea(String lotArea) {
//        this.lotArea = lotArea;
//    }

    @Column(name = "area_id")
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Transient
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
