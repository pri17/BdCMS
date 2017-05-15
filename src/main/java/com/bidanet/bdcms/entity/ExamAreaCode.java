package com.bidanet.bdcms.entity;


import javax.persistence.*;

/**
 * 行政区划代码-身份证前6位对应
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_area_code")

public class ExamAreaCode {

    private Long id;
    private Long pid;
    /**
     * 对应的行政区域名称
     */
    private String name;
    /**
     * 类型：0-省，1-市，2-县
     */
    private Integer type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
