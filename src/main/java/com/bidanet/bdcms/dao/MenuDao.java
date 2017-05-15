package com.bidanet.bdcms.dao;

import com.bidanet.bdcms.entity.Menu;

import java.util.List;

/**
* Menu DAO
*/
public interface MenuDao extends Dao<Menu> {

    List<Menu> getTree();

    List<Menu> findAdminParents(List<Long> ids);
}
