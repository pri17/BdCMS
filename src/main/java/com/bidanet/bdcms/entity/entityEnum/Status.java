package com.bidanet.bdcms.entity.entityEnum;

/**
 * Created by xuejike on 2016-04-19.
 */
public enum Status {


    /**
     * 禁用=0,下架,已经使用优惠券
     */
    disable,
    /**
     * 启用=1，上架,未使用优惠券
     */
    enable,

    /**
     * 待审核=2
     */
    auditing,
    /**
     * 已经过期优惠券
     */
    timeout,

    /**
     * 驳回
     */
    rejected,



    ;


    public String cn(){
        switch (this){
            case disable:
                return "禁用";
            case enable:
                return "启用";
            case auditing:
                return "待审核";
            case rejected:
                return "驳回";
            default:
                return "非法状态";
        }
    }


}
