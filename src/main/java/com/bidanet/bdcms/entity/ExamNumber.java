package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * 用户体检号. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-09 16:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name="exam_number")
public class ExamNumber {

    private Long  id;

    private String number;

    private Long waterNumber;

    private Long memberId;

    private Long createTime;

    private Integer areaCode;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "water_number")
    public Long getWaterNumber() {
        return waterNumber;
    }

    public void setWaterNumber(Long waterNumber) {
        this.waterNumber = waterNumber;
    }

    @Column(name="member_id")
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Column(name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name="area_code")
    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }
}
