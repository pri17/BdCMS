package com.bidanet.bdcms.dao.impl;

import com.bidanet.bdcms.common.RoleCode;
import com.bidanet.bdcms.dao.UserRoleDao;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*
*/
@Repository
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole> implements UserRoleDao {

    public void changeRole(long fromId,long toId){
//        getSession().createQuery("update UserRole set roleId=:toid where roleId=:fromId")
    }


    @Override
    public User getProvinceAgentUser(long areaId){
        List<User> list = getUserByRoleCodeAndAreaId(RoleCode.PROVINCE_AGENT, areaId);
        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }
    @Override
    public List<User> getCityAgentUser(long areaId){
        List<User> list = getUserByRoleCodeAndAreaId(RoleCode.CITY_AGENT,areaId);
        return list;

    }

    @Override
    public List<User> getUserByRoleCodeAndAreaId(String roleCode, long areaId) {
        return getSession()
                .createQuery("select ur.user from UserRole as ur where ur.role.code = ? and ur.user.areaId = ? ")
                .setString(0, roleCode)
                .setLong(1, areaId).list();
    }
    @Override
    public List<User> getUserByRoleCode(String roleCode){
        return getSession().createQuery("select ur.user from UserRole as ur where ur.role.code = ?")
                .setString(0,roleCode).list();
    }

    @Override
    public List<UserRole> getUserRoleByUid(Long uid){
        return getSession().createQuery("from UserRole as ur where ur.uid = ?")
                .setLong(0,uid).list();
    }

    @Override
    public List<User> findFeeAllUserRole(String menuIds) {
//        select distinct(t.uid) from sys_user_role t where t.role_id in (select distinct(t.role_id) from sys_role_menu t where t.menu_id in (27,41));

        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("from User as t where t.uid in (select distinct(t.uid) from UserRole sur where sur.roleId in (select distinct(rm.roleId) from RoleMenu rm where rm.menuId in ("+menuIds+")) ) ");

        Query query = getSession().createQuery(hql.toString());

        for (String key : map.keySet()) {
            query.setParameter(key, map.get(key));
        }
        return query.list();
    }
}
