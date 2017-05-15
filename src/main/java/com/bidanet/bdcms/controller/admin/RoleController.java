package com.bidanet.bdcms.controller.admin;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.entity.Role;
import com.bidanet.bdcms.service.RoleMenuService;
import com.bidanet.bdcms.service.RoleService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * RoleContreller
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController extends AdminBaseController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    @RequestMapping("/roleList")
    public void roleList(@ModelAttribute("query")Role query,
                         @ModelAttribute Page<Role> page){
        roleService.getPageByExample(query, page);
    }

    @RequestMapping("/toAddRole")
    public void toAddRole(Role role, Model model){
        model.addAttribute("vo",role);
    }

    @RequestMapping("/addRole")
    @ResponseBody
    public AjaxCallBack addRole(String description,String role,String menusList){
        Role roles = new Role();

        roles.setDescription(description);
        roles.setRole(role);

        Role saveRole= roleService.addRoleT(roles);

        roleService.addRoleMenuT(menusList,saveRole.getId());

        return AjaxCallBack.addSuccess();
    }
    @RequestMapping("/toUpdateRole")
    public void toUpdateRole(Long id,Model model){
        Role role=roleService.get(id);
        model.addAttribute("vo",role);
    }
    @RequestMapping("/updateRole")
    @ResponseBody
    public AjaxCallBack updateRole(Role role){
        roleService.updateRoleT(role);
        return AjaxCallBack.saveSuccess();
    }
    @RequestMapping("/deleteRole")
    public AjaxCallBack deleteRole(Long id){
        roleService.deleteRoleT(id);
        return AjaxCallBack.deleteSuccess();
    }

    /**
     *菜单
     * @param query
     * @param
     */
    @RequestMapping("/jurisdiction")
    public void jurisdiction(
            Long id,
            @ModelAttribute("query") Role query,
            Model model){
        List<Long> menuIdsByRoleId = roleMenuService.getMenuIds(id);
        if (menuIdsByRoleId.size()>0){
            model.addAttribute("checked", JsonParseTool.toJson(menuIdsByRoleId));
        }

        // roleService.getPageByExample(query, page);
    }

    @RequestMapping("/addRoleMenu")
    @ResponseBody
    public AjaxCallBack addRoleMenu(String menus, Long id){
        roleService.addRoleMenuT(menus,id);
        return AjaxCallBack.addSuccess();
    }


}
