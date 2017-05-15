package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.RolePermissionDao;
import com.bidanet.bdcms.entity.RolePermission;
import com.bidanet.bdcms.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
*
*/
@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermission> implements RolePermissionService {
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Override
    protected Dao getDao() {
        return rolePermissionDao;
    }


}
