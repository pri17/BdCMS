package com.bidanet.bdcms.plugin.shiro;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by Administrator on 2016/9/23.
 */
public class UserRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String) principalCollection.getPrimaryPrincipal();

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        UserService userService = getUserService();

        return null;
    }
    protected UserService getUserService(){
        UserService userService = SpringWebTool.getBean(UserService.class);
        return userService;
    }
}
