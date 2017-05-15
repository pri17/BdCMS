package com.bidanet.bdcms.driver.ac;

import com.bidanet.bdcms.driver.ac.exception.NoLoginAcException;
import com.bidanet.bdcms.driver.cache.Cache;
import com.bidanet.bdcms.driver.uc.Uc;
import com.bidanet.bdcms.entity.Menu;
import com.bidanet.bdcms.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/8.
 */
@Service("ac.url")
public class UrlAc implements Ac {
    @Autowired
    Cache cache;
    @Resource(name = "uc.admin")
    Uc uc;
    @Autowired
    MenuService menuService;

    public static final String roleMenuTag="roleMenu.";
    public static final String menuTag="menu.";

    protected Map<String,Menu> menuMap=new HashMap<>();



    @PostConstruct
    public void load(){
        List<Menu> list = menuService.getList();
        for (Menu menu : list) {
            menuMap.put(menu.getUrl(),menu);
        }
    }
    public void reload(){
        menuMap=new HashMap<>();
        load();
    }
    protected Menu get(String url){
        Menu menu = cache.get(url,Menu.class);
        if (menu==null){
            menu = menuService.getByUrl(url);
            if (menu!=null){
                menuMap.put(url,menu);
            }

        }
        return menu;
    }


    protected boolean checkRole(Long role,Menu menu){
        cache.get(roleMenuTag+role);
        return true;
    }

    @Override
    public boolean checkAccess(String url) {
        Menu menu = get(url);
        /**
         * 不存在，或者
         */
        if (menu==null||!menu.getCheckAccess()){
            return true;
        }
        if (!uc.checkLogin()){
            throw new NoLoginAcException("用户未登录");
        }
        List<Long> userRoles = uc.getUserRoles();
        for (Long role : userRoles) {

        }


        return false;
    }
}
