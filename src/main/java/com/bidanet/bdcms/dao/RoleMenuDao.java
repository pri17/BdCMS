package com.bidanet.bdcms.dao;

import com.bidanet.bdcms.entity.RoleMenu;
import com.bidanet.bdcms.exception.DaoException;

import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public interface RoleMenuDao extends Dao<RoleMenu> {
    void deleteRoleId(Long roleId);

    List<RoleMenu> getMenusIds(Long roleId);
}
