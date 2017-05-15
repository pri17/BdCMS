package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.RoleMenu;

import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public interface RoleMenuService extends Service<RoleMenu>{
    List<Long> getMenuIds(long id);
    void deleteT(Long id);
    void deleteByIdT(Long id);
}

