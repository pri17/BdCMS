package com.bidanet.bdcms.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.entity.Role;
import com.bidanet.bdcms.entity.UserRole;
import com.bidanet.bdcms.service.RoleMenuService;
import com.bidanet.bdcms.service.RoleService;
import com.bidanet.bdcms.service.UserRoleService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by CF on 2016/5/31.
 */

@Controller
@RequestMapping("admin/userRole")
public class UserRoleController extends AdminBaseController{
    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    private  String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") Role query,
            @ModelAttribute Page<Role> page,String tabid){
        roleService.getPageByExample(query,page);

        SpringWebTool.getSession().setAttribute("roleTableId",tabid);
    }

    @RequestMapping("/toAddRole")
    public void toAddRole(){

    }

    @RequestMapping("/addRole")
    @ResponseBody
    public AjaxCallBack addRole(String description,String role,String menusList){

        //默认返回成功
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();

        //前台可能传过那边这个字符串值
        if(menusList.equals("") || menusList.equals("[]")){
            ajaxCallBack = AjaxCallBack.error("权限菜单不能为空");
        }else{
            Role roles = new Role();

            roles.setDescription(description);
            roles.setRole(role);
            Role saveRole= roleService.addRoleT(roles);
            roleService.addRoleMenuT(menusList,saveRole.getId());
        }


        String tableid = (String) SpringWebTool.getSession().getAttribute("roleTableId");
        ajaxCallBack.setTabid(tableid);
        return ajaxCallBack;
    }

    @RequestMapping("/toUpdateRole")
    public void toUpdateRole(Long id, Model model){

        Role role=roleService.get(id);

        List<Long> menuIdsByRoleId = roleMenuService.getMenuIds(role.getId());
        if (menuIdsByRoleId.size()>0){
            model.addAttribute("checked", JsonParseTool.toJson(menuIdsByRoleId));
        }

        model.addAttribute("role",role);
    }

    /**
     * 更新菜单
     * @param role
     * @return
     */
    @RequestMapping("/updateRole")
    @ResponseBody
    public AjaxCallBack updateRole(String id,String description,String role,String menusList){
        //首先获取数据 然后再更新
        Role oldRole = roleService.get(Long.parseLong(id));

        oldRole.setDescription(description);

        oldRole.setRole(role);

        roleService.updateT(oldRole);


        //将所有的菜单删除 然后再重新保存
        roleService.deleteRolesById(oldRole.getId());

        roleService.addRoleMenuT(menusList,oldRole.getId());

        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        //ajaxCallBack.setTabid("roleIndex");


        String tableid = (String) SpringWebTool.getSession().getAttribute("roleTableId");
        ajaxCallBack.setTabid(tableid);

        return ajaxCallBack;
    }

    @RequestMapping("/deleteRole")
    @ResponseBody
    public AjaxCallBack deleteRole(Long id){
        roleService.deleteRoleT(id);
        return AjaxCallBack.deleteSuccess();
    }

    @RequestMapping("/toStartOrStop")
    @ResponseBody
    public AjaxCallBack toStartOrStop(Long id){
        roleService.toStartOrStopT(id);
        return AjaxCallBack.handleSuccess();
    }

//    @RequestMapping("/transformUsers")
//    @ResponseBody
//    public AjaxCallBack transformUsers(Long fromId, Long toId){
//        roleService.transformUsersT(fromId,toId);
//        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
//        ajaxCallBack.setTabid("roleIndex");
//        return ajaxCallBack;
//    }
//
//    /**
//     * 查询待转移的角色，排除自己
//     * @param id
//     * @param model
//     */
//    @RequestMapping("/toTransformUsers")
//    public void toTransformUsers(Long id,Model model){
//        List<Role> roles =  roleService.getOtherRole(id);
//        Role role = roleService.get(id);
//        model.addAttribute("role",role);
//    }

    @RequestMapping("/checkRoleName")
    @ResponseBody
    public JSONObject checkRoleName(String role){
        JSONObject json = roleService.checkRoleName(role);
        return json;
    }

    @RequestMapping("/checkRoleCode")
    @ResponseBody
    public JSONObject checkRoleCode(String code){
        JSONObject json = roleService.checkRoleCode(code);
        return json;
    }

    /**
     * 角色查找带回(多选)
     * @param query
     * @param page
     */
    @RequestMapping("/lookBackRoleMultiple")
    public void lookBackRoleMultiple(
            @ModelAttribute("query") Role query,
            @ModelAttribute Page<Role> page
    ){
        roleService.getPageByExample(query,page);
    }

//    /**
//     * 根据角色获取用户列表
//     * @param model
//     */
//    @RequestMapping("/getRoleUserList")
//    public void getRoleUserList(Long roleId,Model model){
//
//        List<UserRole> userRoleList = userRoleService.getUserListByRoleId(roleId);
//
//        model.addAttribute("userRoleList",userRoleList);
//    }

}
