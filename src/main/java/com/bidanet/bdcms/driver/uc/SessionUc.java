package com.bidanet.bdcms.driver.uc;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;
import com.bidanet.bdcms.exception.NoLoginException;
import com.bidanet.bdcms.service.UserRoleService;
import com.bidanet.bdcms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/29.
 */
public class SessionUc implements Uc {
    protected String loginTag="login";
    protected String roleTag=".role";

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @Override
    public String setLogin(Long uid) {

        return setLogin(uid,0);
    }

    @Override
    public String setLogin(Long uid, int hour) {
        SpringWebTool.getSession().setAttribute(loginTag,uid);

        List<UserRole> list = userRoleService.getByUid(uid);
        ArrayList<Long> roles = new ArrayList<>();
        list.forEach(r->roles.add(r.getRoleId()));
        setUserCache(loginTag+roleTag,roles);


        User user = userService.get(uid);

        setUserCache("login_user_info",user);

        return loginTag;
    }

    @Override
    public boolean checkLogin() {
        Object attribute = SpringWebTool.getSession().getAttribute(loginTag);

        return attribute!=null;
    }

    @Override
    public Long getLoginUid() {
        if (!checkLogin()){
            throw new NoLoginException();
        }
        return (Long) SpringWebTool.getSession().getAttribute(loginTag);
    }

    @Override
    public void setUserCache(String key, Object obj) {
        setUserCache(key, obj,0);
    }

    @Override
    public void setUserCache(String key, Object obj, int second) {
        SpringWebTool.getSession().setAttribute(key, obj);
    }

    @Override
    public <T> T getUserCache(String key, Class<T> tClass) {
        Object attribute = SpringWebTool.getSession().getAttribute(key);
        return (T) attribute;
    }

    @Override
    public List<Long> getUserRoles() {
        List list = getUserCache(loginTag + roleTag, List.class);
        return list;
    }

    @Override
    public void updateLogin(int hour) {
//        setLogin()
    }

    @Override
    public void updateLogin() {

    }

    @Override
    public void logout() {
        SpringWebTool.getSession().setAttribute(loginTag,null);
    }

    @Override
    public User getUser() {
        User user = getUserCache("login_user_info", User.class);
        if (user==null){

            throw new NoLoginException();
        }
        return user;
    }
}
