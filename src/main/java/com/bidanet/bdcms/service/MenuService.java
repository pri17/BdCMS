package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.Menu;

import java.util.Collection;
import java.util.List;

/**
*
*/
public interface MenuService extends Service<Menu> {
    void updateMenuT(Menu menu);

    void addMenuT(Menu menu);

    void deleteT(Long id);

    List<Menu> getTree();

    List<Menu> findAdminParents(List<Long> ids);

    List<Menu> buildMenuTree(Collection<Menu> srouce, String code);

    List<Menu> buildMenuTree(Collection<Menu> srouce);
    Menu getByUrl(String url);
}
