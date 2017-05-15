package com.bidanet.bdcms.driver.uc;

import com.bidanet.bdcms.entity.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/4.
 */
public interface Uc {

    /**
     * 设置登录状态，默认保留1小时
     * @param uid  用户UID
     * @return 用户标识Token
     */
    String setLogin(Long uid);

    /**
     * 设置登录状态
     * @param uid 用户UID
     * @param hour 状态保留时间
     * @return 用户标识Token
     */
    String setLogin(Long uid,int hour);



    /**
     * 检测request里的token是否登录
     * @return
     */
    boolean checkLogin();




    Long getLoginUid();

    /**
     *
     * @param key
     * @param obj
     */
    void setUserCache(String key,Object obj);

    /**
     * 用户缓存
     * @param key 键
     * @param obj 值
     * @param second 有效期
     */
    void setUserCache(String key,Object obj,int second);

    <T> T getUserCache(String key, Class<T> tClass);


    List<Long> getUserRoles();

    void updateLogin(int hour);

    void updateLogin();

    void logout();

    User getUser();
}
