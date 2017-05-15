package com.bidanet.bdcms.entity;

import com.bidanet.bdcms.entity.entityEnum.Status;

/**
 * Created by xuejike on 2016-06-17.
 */
public class HookEntity {
    private long id;
    /**
     * 钩子名称
     */
    private String name;
    /**
     * 钩子介绍
     */
    private String info;
    /**
     * 钩子标签
     */
    private String tag;
    /**
     * 状态
     */
    private Status status;

}
