package com.bidanet.bdcms.dao.impl;

import com.bidanet.bdcms.entity.Menu;

import  com.bidanet.bdcms.dao.MenuDao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
*
*/
@Repository
public class MenuDaoImpl extends BaseDaoImpl<Menu> implements MenuDao {


    @Override
    public List<Menu> getTree() {
        return getSession().createQuery("from Menu  where parentId is null order by id asc").list();
//        return null;
    }

    @Override
    public List<Menu> findAdminParents(List<Long> ids) {
        List<Menu> menus = getSession().createQuery("from Menu as m  where m.parentId in (:ids) ").setParameterList("ids",ids).list();
        return menus;
    }
}
