package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.MenuDao;
import com.bidanet.bdcms.entity.Menu;
import com.bidanet.bdcms.service.MenuService;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.bidanet.bdcms.common.JsonParseTool.toJson;


/**
*
*/
@Service("menuService")
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {
    @Autowired
    private MenuDao menuDao;
    @Override
    protected Dao getDao() {
        return menuDao;
    }

    /**
     * 更新菜单
     * @param menu
     */
    @Override
    public void updateMenuT(Menu menu) {
        checkNull(menu.getName(),"菜单名称不能为空");
        checkNull(menu.getUrl(),"地址不能为空");
        checkNull(menu.getShow(),"不能为空");
        Menu updateMenu=menuDao.get(menu.getParentId());
        checkNull(updateMenu,"该菜单不存在");
        updateMenu.setName(menu.getName());
        updateMenu.setUrl(menu.getUrl());
        updateMenu.setShow(menu.getShow());
        menuDao.update(updateMenu);
    }

    /**
     * 增加菜单
     * @param menu
     */
    @Override
    public void addMenuT(Menu menu) {
        checkNull(menu.getName(),"菜单名称不能为空");
        Long id=menu.getParentId();
        menu.setParentId(id);
        checkString(menu.getUrl(),"url不能为空");
        menu.setCheckAccess(true);
        menu.setShow(true);
        menuDao.save(menu);
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void deleteT(Long id) {
        menuDao.delete(id);
    }

    @Override
    public List<Menu> getTree() {
        return menuDao.getTree() ;
    }

    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public List<Menu> getList() {
      List<Menu>  menuList=menuDao.findAll();
        return menuList;
    }

    /**
     * 查询admin菜单下的所有子菜单
     * @param ids
     * @return
     */
    @Override
    public List<Menu> findAdminParents(List<Long> ids) {
        List<Menu> allRoleMenus = new ArrayList<>();
        List<Menu> menus = menuDao.findAdminParents(ids);
        return menus;
    }

    @Override
    public List<Menu> buildMenuTree(Collection<Menu> srouce, String code){

        ArrayList<Menu> tree = new ArrayList<>();
        Menu other = new Menu();
        other.setId(Long.MAX_VALUE);
        other.setName("其他");
        other.setChildrend(new ArrayList<>());
        HashMap<Long, Menu> map = new HashMap<>();

        if(srouce!=null) {
            for (Menu menu:srouce) {
                menu.setChildrend(new ArrayList<>());
                map.put(menu.getId(),menu);
            }
            for (Menu m : srouce) {
                checkBuildTree(code, tree, other, map, m);
            }
        }
        tree.add(other);
            return tree;
    }

    private void checkBuildTree(String code, ArrayList<Menu> tree, Menu other, HashMap<Long, Menu> map, Menu m) {
        if (m.getParentId() == null &&
                (code == null || code.equals(m.getCode()))) {
            tree.add(m);


        } else {
            Menu menu = map.get(m.getParentId());
            if (menu != null) {
                menu.getChildrend().add(m);
            } else {
                Menu pmenu = menuDao.get(m.getParentId());
                if (pmenu==null){
                    other.getChildrend().add(m);
                }else{
                    pmenu.setChildrend(new ArrayList<>());
                    map.put(pmenu.getId(),pmenu);
                    pmenu.getChildrend().add(m);

                    checkBuildTree(code, tree, other, map, pmenu);
                }

            }

        }
    }

    @Override
    public List<Menu> buildMenuTree(Collection<Menu> srouce){
        return buildMenuTree(srouce,null);
    }

    @Override
    public Menu getByUrl(String url) {
        Menu menu = new Menu();
        menu.setUrl(url);
        List<Menu> list = menuDao.findByExample(menu, MatchMode.EXACT);
        if (list.size()>0){
            return list.get(0);
        }
        return null;

    }


}
