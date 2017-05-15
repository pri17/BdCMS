package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.common.RoleCode;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.MenuDao;
import com.bidanet.bdcms.dao.UserRoleDao;
import com.bidanet.bdcms.entity.Menu;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;
import com.bidanet.bdcms.service.UserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
*
*/
@Service("userRoleService")
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {
    @Autowired
    private UserRoleDao dao;
    @Override
    protected Dao getDao() {
        return dao;
    }

    @Autowired
    private MenuDao menuDao;


    @Override
    public List<UserRole> getByUid(Long uid) {
        UserRole userRole=new UserRole();
        userRole.setUid(uid);
        List<UserRole> list = dao.findByExample(userRole);

        return list;
    }
    @Override
    public boolean isAdmin(Long uid){
        List<UserRole> roleList = getByUid(uid);
        for (UserRole userRole : roleList) {
            if (userRole.getRole().getCode().startsWith(RoleCode.EMOLOYEE)){
                return true;
            }
        }
        return false;

    }

    @Override
    public void deleteT(Long id) {
        dao.delete(id);
    }

    /**
     * 收费
     * @return
     */
    @Override
    public List<User> getUserListByRoleId() {

        Menu menu = new Menu();
        menu.setName("收 费");

        List<Menu> menuList = menuDao.findByExample(menu);

        String menuIds = "";

        for (Menu menu1 : menuList){
            if(menu1.getName().length()<4){
                menuIds += menu1.getId()+","+menu1.getParentId();
            }

        }


        List<User> list = dao.findFeeAllUserRole(menuIds);

        List<User> newList = new ArrayList<>();

        for (User user : list){
            System.out.println(user.getAgenciesCode());
            if (user.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)){
                newList.add(user);
            }
        }

        return newList;
    }
}
