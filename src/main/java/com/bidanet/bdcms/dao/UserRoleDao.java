package com.bidanet.bdcms.dao;

import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;

import java.util.List;

/**
* UserRole DAO
*/
public interface UserRoleDao extends Dao<UserRole> {

    User getProvinceAgentUser(long areaId);

    List<User> getCityAgentUser(long areaId);

    List<User> getUserByRoleCodeAndAreaId(String roleCode, long areaId);

    List<User> getUserByRoleCode(String roleCode);

    List<UserRole> getUserRoleByUid(Long uid);

    List<User> findFeeAllUserRole(String menuIds);
}
