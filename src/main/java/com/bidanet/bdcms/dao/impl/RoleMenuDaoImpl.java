package com.bidanet.bdcms.dao.impl;

import com.bidanet.bdcms.dao.RoleMenuDao;
import com.bidanet.bdcms.entity.RoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
@Repository
public class RoleMenuDaoImpl extends BaseDaoImpl<RoleMenu> implements RoleMenuDao {
    @Override
    public void deleteRoleId(Long roleId) {
        getSession().createQuery("delete from RoleMenu as rm  where rm.roleId=?").setLong(0,roleId).executeUpdate();
    }

    @Override
    public List<RoleMenu> getMenusIds(Long roleId) {
        List list=getSession().createQuery("from RoleMenu as rm where rm.roleId=?").setLong(0,roleId).list();
        return list;
    }
}
