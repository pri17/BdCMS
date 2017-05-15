package com.bidanet.bdcms.common;

import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.RoleMenuService;
import com.bidanet.bdcms.service.RoleService;
import com.bidanet.bdcms.service.UserRoleService;
import com.bidanet.bdcms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户登录工具，获取登录用户信息工具
 */
@Service
public class UserCenter {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleMenuService roleMenuService;

    public static final String loginTag="loginTag";
    public static final String urlListTag="urlList";
    public static final String loginUser="loginUser";
    public static final String roleMenus="roleMenus";

    /**
     * 设置登录状态
     * @param uid  用户UID
     * @return
     */
    public boolean setLogin(Long uid){
        HttpSession session = SpringWebTool.getSession();
        User user = userService.get(uid);
        if (user!=null){
            session.setAttribute(loginTag,true);
            session.setAttribute(loginUser,user);
            // TODO: 2016/6/21 加载用户角色信息
            loadRole(uid);
            return true;
        }else{
            return  false;
        }

    }
    public User getLogin(){
        HttpSession session = SpringWebTool.getSession();
        return (User) session.getAttribute(loginUser);
    }

    /**
     * 加载角色
     * @param uid
     */
    protected void loadRole(Long uid){
        // TODO: 2016/6/25 获取用户角色

        UserRole userRole = new UserRole();
        userRole.setUid(uid);
        List<UserRole> userRoles = userRoleService.findByExampleExact(userRole);
        if(userRoles!=null&&userRoles.size()>0){
           Long roleId = userRoles.get(0).getRoleId();
            // TODO: 2016/6/25 获取角色的Menu
            loadUrl(roleId);
        }

    }

    /**
     * 根据角色ID获取菜单
     * @param roleId
     */
    protected void loadUrl(Long roleId){
//        getUrlList().add("");

        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        List<RoleMenu> roleMenuList = roleMenuService.findByExampleExact(roleMenu);
        if(roleMenuList!=null&&roleMenuList.size()>0){
            List<Menu> list = new ArrayList<Menu>();
            roleMenuList.forEach(r->list.add(r.getMenu()));
            SpringWebTool.getSession().setAttribute("roleMenus",list);
        }

//        Role role = roleService.get(roleId);
//        if(role!=null){
////            Set<Menu> set=role.getMenus();
// //           List<Menu> list=new ArrayList<Menu>(set);
////            SpringWebTool.getSession().setAttribute("roleMenus",set);
////            System.out.println("loadUrl---------"+set);
//        }
    }

    /**
     * 获取当前用户的菜单集合
     * @return
     */
    protected List<String> getUrlList(){
        List<String> attribute = ((List<String>) SpringWebTool.getSession().getAttribute(urlListTag));
        if (attribute==null){
            attribute=new ArrayList<String>();
        }
        return  attribute;
    }


    /**
     * 检测用户是否登录
     * @return
     */
    public boolean checkLogin(){
        Object attribute = SpringWebTool.getSession().getAttribute(loginTag);
        return attribute != null && ((Boolean) attribute);
    }

    /**
     * 退出
     */
    public void logout(){
        SpringWebTool.getSession().setAttribute(loginTag,null);
    }

    /**
     * 判断是否可以被访问
     * @param url
     * @return
     */
    public boolean access(String url){
        getUrlList().indexOf("---");
        return true;
    }

}
