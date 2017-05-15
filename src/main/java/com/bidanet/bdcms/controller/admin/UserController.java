package com.bidanet.bdcms.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.driver.uc.TestIn;
import com.bidanet.bdcms.entity.Role;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;
import com.bidanet.bdcms.service.UserService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 用户管理Controller
 */
@Controller
@RequestMapping("/admin/user")
public class UserController extends AdminBaseController {

    @Autowired
    private UserService userService;

    public String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") User query,
            @ModelAttribute Page<User> page,String tabid){
        userService.getUserList(query,page);

        tableId = tabid;
    }

    @RequestMapping("/toUpdateAdminPassWord")
    public void toUpdateAdminPassWord( Model model){
        Long uid=uc.getLoginUid();
        User user=userService.get(uid);
        model.addAttribute("vo",user);

    }

    @RequestMapping("/updateAdminPassWord")
    @ResponseBody
    public AjaxCallBack UpdateAdminPassWord(String oldPw,String newPw){
        Long uid=uc.getLoginUid();
        User user=userService.get(uid);
        userService.changePwdT(oldPw,newPw,user);

        return AjaxCallBack.saveSuccess();

    }

    /**
     * 检测用户名唯一性
     * @param username
     * @return
     */
    @RequestMapping("/checkUserName")
    @ResponseBody
    public JSONObject checkUserName(String username){

        JSONObject json = userService.checkUserName(username);
        return  json;
    }

    /**
     * 检测号码唯一性
     * @param mobile
     * @return
     */
    @RequestMapping("/checkUserMobile")
    @ResponseBody
    public JSONObject checkUserMobile(String mobile){
        JSONObject json = userService.checkUserMobile(mobile);
        return  json;
    }

    /**
     * 检测邮箱唯一性
     * @param email
     * @return
     */
    @RequestMapping("/checkUserEmail")
    @ResponseBody
    public JSONObject checkUserEmail(String email){
        JSONObject json = userService.checkUserEmial(email);
        return  json;
    }

    /**
     * 跳转添加用户页面
     */
    @RequestMapping("/toAddUser")
    public void toAddUser(){
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("/addUser")
    @ResponseBody
    public AjaxCallBack addUser(User user){
        userService.addUserT(user);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }


    /**
     * 跳转去重置密码页面
     */
    @RequestMapping("/toUpdateUserPwd")
    public void toUpdateUserPwd(Long uid,Model model){
        User user = userService.get(uid);
        model.addAttribute("user",user);
    }

    /**
     * 重置密码
     * @param user
     * @param newPwd
     * @param submitPwd
     * @return
     */
    @RequestMapping("/updateUserPwd")
    @ResponseBody
    public AjaxCallBack updateUserPwd(User user,String newPwd,String submitPwd){
        userService.updateUserPwdT(user,newPwd,submitPwd);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }


    /**
     * 跳转修改用户页面
     * @param uid
     * @param model
     */
    @RequestMapping("/toUpdateUser")
    public void toUpdateUser(Long uid,Model model){
        User user = userService.get(uid);
        model.addAttribute("user",user);
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @RequestMapping("/updateUser")
    @ResponseBody
    public AjaxCallBack updateUser(User user){
        userService.updateUserT(user);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }


    /**
     * 启用禁用
     * @param uid
     * @return
     */
    @RequestMapping("/toStartStop")
    @ResponseBody
    public AjaxCallBack toStartStop(Long uid){
        if (uc.getLoginUid()==uid) {
            return AjaxCallBack.error("不可禁用自己的账号！");
        } else {
            userService.toStartStopT(uid);
            return AjaxCallBack.handleSuccess();
        }
    }

    @Autowired
    private TestIn testIn;

    /**
     * 查询管理员
     * @param username
     * @param mobile
     * @param page
     * @param model
     */
    @RequestMapping("/adminIndex")
    public void adminIndex(String realName,Long agenciesId,Long roleId,String username,String mobile,
                           @ModelAttribute Page<UserRole>  page,
                           Model model){

//        testIn.testInAction();
        model.addAttribute("realName",realName);
        model.addAttribute("username",username);
        model.addAttribute("mobile",mobile);
        model.addAttribute("roleId",roleId);
        model.addAttribute("agenciesId",agenciesId);
        userService.findAdminList(realName,agenciesId,roleId,username,mobile,page);
    }

    /**
     * 查询员工角色
     */
    @RequestMapping("/adminTypeIndex")
    public void adminTypeIndex(Model model){
        List<Role> emoloyeeRoles = userService.findAdminType();
        model.addAttribute("emoloyeeRoles",emoloyeeRoles);
    }

    @RequestMapping("/toAddAdminUser")
    public void toAddAdminUser(Long roleId,Model model){
        model.addAttribute("roleId",roleId);
    }

    @RequestMapping("/addAdminUser")
    @ResponseBody
    public AjaxCallBack addAdminUser(User user,Long roleId){
        userService.addAdminUserT(user,roleId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    @RequestMapping("/toUpAdminUser")
    public void toUpAdminUser(Long uid,Long roleId,Model model){
        User user = userService.get(uid);

        model.addAttribute("user",user);
        model.addAttribute("roleId",roleId);
    }

    @RequestMapping("/upAdminUser")
    @ResponseBody
    public AjaxCallBack upAdminUser(User user,Long roleId){
        userService.upAdminUserT(user,roleId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

}
