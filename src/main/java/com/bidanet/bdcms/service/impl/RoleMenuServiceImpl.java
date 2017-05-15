package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.MenuDao;
import com.bidanet.bdcms.dao.RoleDao;
import com.bidanet.bdcms.dao.RoleMenuDao;
import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.entity.Role;
import com.bidanet.bdcms.entity.RoleMenu;
import com.bidanet.bdcms.service.RoleMenuService;
import com.bidanet.bdcms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenu> implements RoleMenuService{
    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleDao roleDao;
    @Override
    protected Dao getDao() {
        return roleMenuDao;
    }
    @Override
    public List<Long> getMenuIds(long id){
       // Role role = roleDao.get(id);
        List<RoleMenu> roleMenuList=roleMenuDao.getMenusIds(id);
        ArrayList<Long> list = new ArrayList<>();
       /* role.getMenus().forEach(m -> list.add(m.getId()));*/
        long menusid;
        for (RoleMenu roleMenu:roleMenuList){
            menusid=roleMenu.getMenuId();
            list.add(menusid);
        }
        return list;
    }

    @Override
    public void deleteT(Long id) {
        RoleMenu roleMenu=roleMenuDao.get(id);
        roleDao.delete(roleMenu.getRoleId());
        menuDao.delete(roleMenu.getMenuId());
        roleMenuDao.deleteRoleId(id);
    }

    @Override
    public void deleteByIdT(Long id) {
        RoleMenu roleMenu=new RoleMenu();
        roleMenu.setMenuId(id);
        List<RoleMenu> roleMenus=roleMenuDao.findByExample(roleMenu);
        RoleMenu rm=null;
        if (roleMenus.size()>0){
             rm=roleMenus.get(0);
        }
        roleMenuDao.delete(rm.getId());
        menuDao.delete(id);
    }

}
