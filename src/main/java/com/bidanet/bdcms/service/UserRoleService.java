package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;

import java.util.List;

/**
*
*/
public interface UserRoleService extends Service<UserRole> {
    List<UserRole> getByUid(Long uid);

    boolean isAdmin(Long uid);
    void deleteT(Long id);

    /**
     * 根据roleId获取财务收费用户列表
     * @return
     */
    List<User> getUserListByRoleId();
}
