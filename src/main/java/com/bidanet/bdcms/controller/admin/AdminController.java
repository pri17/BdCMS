package com.bidanet.bdcms.controller.admin;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.VerifyCodeUtils;
import com.bidanet.bdcms.entity.Menu;
import com.bidanet.bdcms.entity.Role;
import com.bidanet.bdcms.entity.RoleMenu;
import com.bidanet.bdcms.service.MenuService;
import com.bidanet.bdcms.service.RoleMenuService;
import com.bidanet.bdcms.service.RoleService;
import com.bidanet.bdcms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuejike on 2016-04-15.
 */
@Controller
@RequestMapping("/admin/")
public class AdminController extends AdminBaseController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    RoleService roleService;


    @RequestMapping("/index")
    public void index(Model model){

        try{
            Long roleId = null;
            if(uc.checkLogin()) {
                if (uc.getUserRoles() != null&&uc.getUserRoles().size()>0) {
                    roleId = uc.getUserRoles().get(0);
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    List<RoleMenu> roleMenuList = roleMenuService.findByExampleExact(roleMenu);
                    List<Menu> list = new ArrayList<Menu>();
                    if(roleMenuList!=null&&roleMenuList.size()>0){
                        roleMenuList.forEach(r->list.add(r.getMenu()));
                    }
                    Role role = roleService.get(roleId);
                    model.addAttribute("role",role);

//        Set<Menu> menus = (Set<Menu>) SpringWebTool.getSession().getAttribute(UserCenter.roleMenus);
                    List<Menu> tree = menuService.buildMenuTree(list);
                    if(tree!=null&&tree.size()>0) {

                        model.addAttribute("menuTree", tree.get(0));
                    }

                }
                model.addAttribute("login_user",userService.get(uc.getLoginUid()));
                Role role=roleService.get(uc.getUserRoles().get(0));
                model.addAttribute("index_url",role.getIndexUrl());
                model.addAttribute("index_tabid",role.getIndexTabid());
                model.addAttribute("index_name",role.getIndexName());
            }else{
                SpringWebTool.redirect("/admin/public/login");
            }
        }catch (Exception e){
            SpringWebTool.redirect("/admin/public/login");
        }





    }
    @RequestMapping("info")
    public void  info(){

    }

    @RequestMapping("/testCode")
    public void testCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int w = 100, h = 40;
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        VerifyCodeUtils.outputImage(w,h,response.getOutputStream(),verifyCode);
    }
}
