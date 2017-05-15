package com.bidanet.bdcms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.common.PasswordTool;
import com.bidanet.bdcms.common.SMSTool;
import com.bidanet.bdcms.common.URLTool;
import com.bidanet.bdcms.dao.*;
import com.bidanet.bdcms.dao.examBusiness.ExamAreaDao;
import com.bidanet.bdcms.dao.systemSetting.ExamAgenciesDao;
import com.bidanet.bdcms.dao.systemSetting.ExamDepartmentDao;
import com.bidanet.bdcms.driver.cache.Cache;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.entity.entityEnum.UserLoginStatus;
import com.bidanet.bdcms.entity.entityEnum.UserStatus;
import com.bidanet.bdcms.service.UserService;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuejike on 2016-05-24.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    protected Dao getDao() {
        return userDao;
    }
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private Cache cache;

    @Autowired
    private ExamAreaDao examAreaDao;

    @Autowired
    private URLTool urlTool;

    @Autowired
    private SMSTool smsTool;

    @Autowired
    private ExamDepartmentDao examDepartmentDao;

    @Autowired
    private ExamAgenciesDao examAgenciesDao;

    @Override
    public void getUserList(User user, Page<User> page){
        List<User> list = userDao.findByExample(user, page.getPageCurrent(), page.getPageSize());
        long count = userDao.countByExample(user);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void toStartStopT(Long uid) {
        User user = userDao.get(uid);
        if(user.getStatus()== UserStatus.enable){
            user.setStatus(UserStatus.disable);
        }else{
            user.setStatus(UserStatus.enable);
        }
        userDao.update(user);
    }

    /**
     * 修改密码接口
     * @param oldPwd
     * @param newPwd
     * @param loginUser
     * @return
     */
    @Override
    public void changePwdT(String oldPwd, String newPwd, User loginUser) {
        String old = PasswordTool.getPwdEncode(oldPwd,loginUser.getSalt());
        String newP = loginUser.getPassword();
        if(loginUser==null){
            errorMsg("没有找到用户！");
        }
        if(StringUtils.isBlank(oldPwd)){
            errorMsg("请输入原密码！");
        }
        if(StringUtils.isBlank(newPwd)){
            errorMsg("请输入新密码！");

        }
        if(!(PasswordTool.getPwdEncode(oldPwd,loginUser.getSalt()).equals(loginUser.getPassword()))){
            errorMsg("原密码错误！");
        }
        loginUser.setPassword(PasswordTool.getPwdEncode(newPwd,loginUser.getSalt()));
        userDao.update(loginUser);
    }


    @Override
    public void updateUserT(User user) {
        checkString(user.getUsername(),"请填写用户名！");
        User updateUser = userDao.get(user.getUid());
        checkNull(updateUser,"没有找到此用户！");
        updateUser.setUsername(user.getUsername());
        user.setSalt(PasswordTool.getSale());
        user.setPassword(PasswordTool.getPwdEncode(user.getPassword(),user.getSalt()));
        userDao.update(updateUser);
    }

    @Override
    public void updateUserPwdT(User user, String newPwd, String submitPwd) {
        checkString(newPwd,"新密码不能为空！");
        checkString(submitPwd,"确认密码不能为空！");
        User checkUser = userDao.get(user.getUid());
        checkNull(checkUser,"没有找到此用户！");
//        String old = PasswordTool.getPwdEncode(oldPwd,checkUser.getSalt());
//        String n = checkUser.getPassword();
//        if(!(PasswordTool.getPwdEncode(oldPwd,checkUser.getSalt()).equals(checkUser.getPassword()))){
//            errorMsg("原密码错误！");
//        }
        if(!newPwd.equals(submitPwd)){
            errorMsg("新密码两次输入不一致！");
        }
        checkUser.setPassword(PasswordTool.getPwdEncode(newPwd,checkUser.getSalt()));
        userDao.update(checkUser);
    }

    @Override
    public void addUserT(User user) {
        checkString(user.getUsername(),"请填写用户名！");
        checkString(user.getPassword(),"请填写用户密码！");
        checkString(user.getMobile(),"请填写手机号！");
        user.setSalt(PasswordTool.getSale());
        user.setPassword(PasswordTool.getPwdEncode(user.getPassword(),user.getSalt()));
        user.setStatus(UserStatus.enable);
        userDao.save(user);
    }


    protected void checkEmail(String email,UserLoginStatus loginStatus){
        if (email!=null&&!email.isEmpty()){
            User user = new User();
            if (checkUserHas(user)){
                errorMsg("该邮箱已经被使用");
            }
        }
    }
    protected void checkTel(String tel,UserLoginStatus loginStatus){
        if (tel!=null&&!tel.isEmpty()){
            User user = new User();
            user.setMobile(tel);
            if (checkUserHas(user)){
                errorMsg("该手机号已经被使用");
            }
        }
    }
    protected void checkUserName(String username,UserLoginStatus loginStatus){
        if (username!=null&&!username.isEmpty()){
            User user = new User();
            user.setUsername(username);
            if (checkUserHas(user)){
                errorMsg("该用户名已经被使用");
            }
        }
    }

    protected boolean checkUserHas(User user){
        List<User> list = findByExampleExact(user);
        return list.size()>0;
    }






    /**
     * 检测用户名是否已使用
     * @param username
     * @return
     */
    @Override
    public JSONObject checkUserName(String username) {
        JSONObject json =new JSONObject();
        User user = new User();
        user.setUsername(username);

        List<User> users = userDao.findByExample(user,MatchMode.EXACT);
        if(users!=null&&users.size()>0){
            json.put("error","用户名重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }

    @Override
    public JSONObject checkUserName(UserLoginStatus loginStatus, String username) {
        JSONObject json =new JSONObject();
        User user = new User();
        user.setUsername(username);
        List<User> users = userDao.findByExample(user,MatchMode.EXACT);
        if(users!=null&&users.size()>0){
            json.put("error","用户名重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }

    /**
     * 检测手机号是否已使用
     * @param mobile
     * @return
     */
    @Override
    public JSONObject checkUserMobile(String mobile) {
        JSONObject json =new JSONObject();
        User user = new User();
        user.setMobile(mobile);
        List<User> users = userDao.findByExample(user,MatchMode.EXACT);
        if(users!=null&&users.size()>0){
            json.put("error","手机号重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }



    /**
     * 检测邮箱是否已使用
     * @param email
     * @return
     */
    @Override
    public JSONObject checkUserEmial(String email) {
        JSONObject json =new JSONObject();
        User user = new User();
        List<User> users = userDao.findByExample(user,MatchMode.EXACT);
        if(users!=null&&users.size()>0){
            json.put("error","邮箱重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }



    /**
     * 用户登录接口
     *
     * @param picCode
     * @param name
     * @param pwd
     * @return
     */
    @Override
    public User loginApi(String name, String pwd,String picCode,String checkPicCode) {
        if(StringUtils.isBlank(name)||StringUtils.isBlank(pwd)){
            errorMsg("用户名和密码不能为空！");
        }
        if(StringUtils.isBlank(picCode)){
            errorMsg("图片验证码不能为空！");
        }
//        if(isPhone(name)==false&&isMail(name)==false) {
//            errorMsg("请输入手机号或者邮箱！");
//        }

        User user = userLogin(name, pwd,UserLoginStatus.customer);
        if(!picCode.equalsIgnoreCase(checkPicCode)){
            errorMsg("图片验证码错误！");
        }

        return user;
    }

    @Override
    public User userLogin(String name, String pwd, UserLoginStatus userLoginStatus) {
        User user = new User();
        if (userLoginStatus==UserLoginStatus.customer){
            if(isPhone(name)){
                user.setMobile(name);
            }else if(isMail(name)){
            }else{
                user.setUsername(name);
            }
        }else{
            user.setUsername(name);
        }

        List<User> users = userDao.findByExample(user, MatchMode.EXACT);
        if(users!=null&&users.size()>0){
            user = users.get(0);
        }else{
            errorMsg("无此用户！");
        }


        if(!(PasswordTool.getPwdEncode(pwd,user.getSalt()).equals(user.getPassword()))){
            String pa=PasswordTool.getPwdEncode(pwd,user.getSalt());
            errorMsg("密码错误！");
        }
        return user;
    }

    /**
     * 验证手机号
     * @param str
     * @return
     */
    public boolean isPhone(String str)
    {
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        Matcher phoneCheck = pattern.matcher(str);
        return phoneCheck.matches();
    }

    /**
     * 验证邮箱
     * @param str
     * @return
     */
    public boolean isMail(String str)
    {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
        Matcher mailCheck = pattern.matcher(str);
        return mailCheck.matches();
    }

    /**
     * 验证是否为数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }


    @Override
    public String getGenerationNickname() {
        int num = (int)((Math.random()*9+1)*1000000);
        return new String("用户8" + String.valueOf(num));
    }


    @Override
    public User getUserByUsername(String username) {

        return null;
    }

    @Override
    public void findAdminList(String realName, Long agenciesId, Long roleId, String username, String mobile, Page<UserRole> page) {
        List<UserRole> list = userDao.findAdminList(realName,agenciesId,roleId,username,mobile,page.getPageCurrent(),page.getPageSize());

        for (UserRole userRole:list){

            User user = userRole.getUser();
            //获取区域信息
            ExamArea area = examAreaDao.get(user.getAreaId());
            if(area!=null) {
                user.setAreaName(area.getAreaName());
            }

            //获取科室信息
            if (user.getDepartmentId()!= null ){

                ExamDepartment examDepartment = examDepartmentDao.get(user.getDepartmentId());


                if(examDepartment != null){
                    user.setDepartmentName(examDepartment.getDepartmentName());
                }

            }


        }





        Long total = userDao.findCountAdminList(realName,agenciesId,roleId,username,mobile);
        page.setList(list);
        page.setTotal(total);
    }

    /**
     * 查询员工角色
     * @return
     */
    @Override
    public List<Role> findAdminType() {
//        Role role = new Role();
//        role.setCode(RoleCode.EMOLOYEE);
//        List<Role> emoloyeeRoles = roleDao.findByExample(role);
        List<Role> emoloyeeRoles = roleDao.findAll();
        if(emoloyeeRoles!=null&&emoloyeeRoles.size()>0){
            return emoloyeeRoles;
        }else{
            return new ArrayList<Role>();
        }

    }

    @Override
    public void addAdminUserT(User user, Long roleId) {
        checkString(user.getRealName(),"请填写姓名！");
        checkString(user.getUsername(),"请填写登录名！");
        checkString(user.getPassword(),"请填写用户密码！");
        checkString(user.getMobile(),"请填写手机号！");
        if(user.getAgenciesId()==null){
            errorMsg("请选择所在单位！");
        }
        if(roleId==null){
            errorMsg("请选择角色！");
        }
        if(user.getAreaId()==null){
            errorMsg("请选择地区！");
        }

        //2017-02-09
        ExamAgencies examAgencies = examAgenciesDao.get(user.getAgenciesId());
        user.setAgenciesName(examAgencies.getAgenciesName());
        user.setAgenciesCode(examAgencies.getAgenciesCode());

        // 创建用户
        User adminUser = saveUserT(user);

        //保存用户角色关系
        UserRole userRole =new UserRole();
        userRole.setUid(adminUser.getUid());
        userRole.setRoleId(roleId);
        userRoleDao.save(userRole);
    }

    @Override
    public void upAdminUserT(User user,Long roleId) {
        checkString(user.getRealName(),"请填写姓名！");
        checkString(user.getUsername(),"请填写登录名！");
        checkString(user.getMobile(),"请填写手机号！");
        if(user.getAgenciesId()==null){
            errorMsg("请选择所在单位！");
        }
        if(roleId==null){
            errorMsg("请选择角色！");
        }

        if(user.getAreaId()==null){
            errorMsg("请选择地区！");
        }


        User updateUser = userDao.get(user.getUid());
        checkNull(updateUser,"没有找到此用户！");

        updateUser.setRealName(user.getRealName());
        updateUser.setUsername(user.getUsername());
        updateUser.setAreaId(user.getAreaId());
        updateUser.setMobile(user.getMobile());
        updateUser.setAgenciesId(user.getAgenciesId());

        //2017-02-09
        ExamAgencies examAgencies = examAgenciesDao.get(user.getAgenciesId());
        updateUser.setAgenciesName(examAgencies.getAgenciesName());
        updateUser.setAgenciesCode(examAgencies.getAgenciesCode());


        userDao.update(updateUser);
        /**
         * 更新用户角色
         */
        UserRole userRole = new UserRole();
        userRole.setUid(user.getUid());
        List<UserRole> userRoles = userRoleDao.findByExample(userRole);
        if(userRoles!=null&&userRoles.size()>0){
            UserRole   upUserRole = userRoles.get(0);
            upUserRole.setRoleId(roleId);
            userRoleDao.update(upUserRole);
        }

    }

    /**
     * 建用户账户
     * @param user
     */
    public User saveUserT(User user) {
        user.setSalt(PasswordTool.getSale());
        user.setPassword(PasswordTool.getPwdEncode(user.getPassword(),user.getSalt()));
        user.setCreateTime(new Date().getTime());
        user.setStatus(UserStatus.enable);
        userDao.save(user);
        return  user;
    }
}
