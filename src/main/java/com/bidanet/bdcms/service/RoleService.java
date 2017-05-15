package com.bidanet.bdcms.service;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.entity.Role;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;

import java.util.List;
import java.util.Set;

/**
*
*/
public interface RoleService extends Service<Role>{

    Role addRoleT(Role role);

    void addRoleMenuT(String menus,Long id);

  /*  void getAlreaday(Long id,Role query);*/

    List<Long> getMenuIdsByRoleId(long id);

    void updateRoleT(Role role);

    void deleteRoleT(Long id);

    void toStartOrStopT(Long id);

    void transformUsersT(Long fromId,Long toId);

    List<Role> getOtherRole(Long id);

    JSONObject checkRoleName(String role);

    JSONObject checkRoleCode(String code);

    List<Role> getAllRole();

    User getProvinceAgentUser(long areaId);

    User getCompanyAngent();


    UserRole addUserT(long uid, String roleCode);

    void deleteRolesById(Long roleId);

}
