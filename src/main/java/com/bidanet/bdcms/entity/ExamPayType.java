package com.bidanet.bdcms.entity;/**
 * Created by jizhaoqun on 16/10/9.
 */

import javax.persistence.*;

/**
 * 体检收费项目设置. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-09 16:45
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name="exam_pay_type")
public class ExamPayType {

    private Long id;
    //名称
    private String name;



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


}

