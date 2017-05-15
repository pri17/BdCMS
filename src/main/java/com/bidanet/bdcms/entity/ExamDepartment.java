package com.bidanet.bdcms.entity;


import javax.persistence.*;

/**
 * 科室表-仅对疾控中心有效.
 *
 * Copyright: Copyright (c) 2016-12-14 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_department")

public class ExamDepartment {

    private long id;
    /**
     * 科室名称
     */
    private String departmentName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "department_name")
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
