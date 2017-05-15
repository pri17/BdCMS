package com.bidanet.bdcms.dao;

import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;

import java.util.List;

/**
 * Created by xuejike on 2016-05-24.
 */
public interface UserDao extends Dao<User> {
    List<UserRole> findAdminList(String realName, Long agenciesId, Long roleId, String username, String mobile, int pageCurrent, int pageSize);

    Long findCountAdminList(String realName, Long agenciesId, Long roleId, String username, String mobile);
}
