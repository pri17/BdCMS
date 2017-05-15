package com.bidanet.bdcms.entity.entityEnum;

/**
 * Created by Administrator on 2016/7/28.
 */
public enum  UserLoginStatus {
    /**
     * 员工
     */
    employee,
    /**
     * 商户
     */
    shop,
    /**
     * 用户
     */
    customer
    ;

    public String cn (){
        switch (this){
            case employee:
             return "员工";
            case shop:
              return "商户";
            case customer:
               return "用户";
            default:
                return "非法登录";
        }
    }
}
