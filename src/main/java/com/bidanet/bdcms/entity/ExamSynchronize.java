package com.bidanet.bdcms.entity;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 省平台、方正数据同步
 *
 * Copyright: Copyright (c) 2016-10-09 15:07:16
 * Company: 苏州必答网络科技有限公司
 * @author zhangbinbin
 * @version 1.0.0
 */
@Entity
@Table(name = "exam_synchronize")

public class ExamSynchronize {

    private Long id;

    private Long eCardId;

    private Long examMemberId;

    private Long memberId;

    private Integer isSuccess;

    private Long createTime;

    private Long updateTime;

    private String code;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column (name = "ecard_id")
    public Long geteCardId() {
        return eCardId;
    }

    public void seteCardId(Long eCardId) {
        this.eCardId = eCardId;
    }
    @Column (name = "exam_member_id")
    public Long getExamMemberId() {
        return examMemberId;
    }

    public void setExamMemberId(Long examMemberId) {
        this.examMemberId = examMemberId;
    }
    @Column (name = "member_id")
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Column (name = "is_success")
    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Column (name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column (name = "update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
