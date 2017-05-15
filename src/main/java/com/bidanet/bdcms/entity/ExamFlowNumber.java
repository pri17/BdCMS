package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * . <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c)
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name="exam_flow_number")
public class ExamFlowNumber {

    private Long id;

    private Long flowNumber;

    private Long createTime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "flow_number")
    public Long getFlowNumber() {
        return flowNumber;
    }

    public void setFlowNumber(Long flowNumber) {
        this.flowNumber = flowNumber;
    }

    @Column (name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
