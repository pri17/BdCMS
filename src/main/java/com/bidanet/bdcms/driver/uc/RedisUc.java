package com.bidanet.bdcms.driver.uc;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.driver.cache.Cache;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;
import com.bidanet.bdcms.exception.NoLoginException;
import com.bidanet.bdcms.service.UserRoleService;
import com.bidanet.bdcms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/7/4.
 */
@Service("uc.redis")
public class RedisUc implements Uc {

    public static final String tag="uc.";
    public static final String token="token";
    public static final String userRole="user_role.";


    @Autowired
    Cache cache;
    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;




    @Override
    public String setLogin(Long uid) {
        return setLogin(uid,1);
    }

    @Override
    public String setLogin(Long uid, int hour) {
        String s = UUID.randomUUID().toString();

        String token=tag+s;

        List<UserRole> list = userRoleService.getByUid(uid);
        ArrayList<Long> roles = new ArrayList<>();
        list.forEach(r->roles.add(r.getRoleId()));

        cache.set(userRole+uid,roles);
        cache.set(token,uid,7*24*60*60*hour); //hour是秒

        User user = userService.get(uid);

        SpringWebTool.getSession().setAttribute(RedisUc.token,token);
        setUserCache("user_login_info",user);


        SpringWebTool.getResponse().addCookie(new Cookie(RedisUc.token,token));
        SpringWebTool.getSession().setAttribute(RedisUc.token,token);
        return token;
    }
    /**
     * 测试该Token是否登录
     * @param token
     * @return
     */

    public boolean checkLogin(String token) {
        String s = cache.get(token);
        return s != null;
    }
    @Override
    public boolean checkLogin(){

        return checkLogin(getToken());
    }

    public Long getLoginUid(String token){

        Long uid = cache.get(token, Long.class);
        if (uid==null){
            throw new NoLoginException();
        }
        return uid;
    }
    @Override
    public Long getLoginUid(){

        return getLoginUid(getToken());
    }

    @Override
    public void setUserCache(String key, Object obj) {
        setUserCache(key, obj,60*60*24*10);
    }

    @Override
    public void setUserCache(String key, Object obj, int second) {
        String token = getToken();
        cache.set("usercache."+token+"."+key, obj,second);

    }

    @Override
    public <T> T getUserCache(String key, Class<T> tClass) {
        return cache.get("usercache."+getToken()+"."+key,tClass);
    }


    @Override
    public List<Long> getUserRoles(){
        List<Long> list = cache.getArray(userRole + getLoginUid(), Long.class);
        return list;
    }
    @Override
    public void updateLogin(int hour) {
        updateLogin(getToken(),hour);
    }

    @Override
    public void updateLogin() {
        updateLogin(getToken());
    }

    @Override
    public void logout() {
        cache.delete(getToken());
    }

    @Override
    public User getUser() {
        User user = getUserCache("user_login_info", User.class);
        if(user==null){
            throw new NoLoginException();
        }
        return user;
    }


    public void updateLogin(String token, int hour) {
        cache.expire(token,60*60*hour);
    }


    public void updateLogin(String token) {
        cache.expire(token,60*60*1);
    }
    protected String getToken(){
        Object attribute = SpringWebTool.getSession().getAttribute(token);
        if (attribute!=null){
            return (String) attribute;
        }
        Cookie[] cookies = SpringWebTool.getRequest().getCookies();
        if(cookies!=null){
            for (Cookie c :
                    cookies) {
                if (token.equals(c.getName())&&c.getValue()!=null){
                    return c.getValue();
                }
            }
        }

        String parameter = SpringWebTool.getRequest().getParameter(token);

        return parameter;
    }
}
