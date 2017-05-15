package com.bidanet.bdcms.service;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.entity.Role;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;
import com.bidanet.bdcms.entity.entityEnum.UserLoginStatus;
import com.bidanet.bdcms.vo.Page;

import java.util.List;

/**
 * Created by xuejike on 2016-05-24.
 */
public interface UserService extends Service<User> {

    void getUserList(User user, Page<User> page);

    void toStartStopT(Long uid);

    void changePwdT(String oldPwd, String newPwd, User loginUser);

    /**
     * 修改用户
     * @param user
     */
    void updateUserT(User user);

    void updateUserPwdT(User user, String newPwd, String submitPwd);

    void addUserT(User user);

    JSONObject checkUserName(String username);

    JSONObject checkUserName(UserLoginStatus loginStatus,String username);

    JSONObject checkUserMobile(String mobile);

    JSONObject checkUserEmial(String email);

    User loginApi(String name, String pwd,String picCode,String checkPicCode);

    User userLogin(String name, String pwd, UserLoginStatus userLoginStatus);

    //获得随机产生用户昵称
    String getGenerationNickname();


    User getUserByUsername(String username);

    void findAdminList(String realName, Long agenciesId, Long roleId, String username, String mobile, Page<UserRole> page);

    List<Role> findAdminType();

    void addAdminUserT(User user, Long roleId);

    void upAdminUserT(User user, Long roleId);

}
