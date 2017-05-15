package com.bidanet.bdcms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.common.RoleCode;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.RoleDao;
import com.bidanet.bdcms.dao.RoleMenuDao;
import com.bidanet.bdcms.dao.UserRoleDao;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.entity.entityEnum.Status;
import com.bidanet.bdcms.service.RoleService;

import com.bidanet.bdcms.vo.ValueLabel;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
*
*/
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Override
    protected Dao getDao() {
        return roleDao;
    }


    @Override
    public Role addRoleT(Role role) {
        checkString(role.getRole(),"角色名不能为空！");
        Role checkRole = new Role();
        checkRole.setRole(role.getRole());
        List<Role> roles = roleDao.findByExample(checkRole);
        if(roles!=null&&roles.size()>0){
            errorMsg("此角色已存在！");
        }
//        Role checkCodeRole = new Role();
//        checkCodeRole.setCode(role.getCode());
//        List<Role> codeRoles = roleDao.findByExample(checkCodeRole);
//        if(codeRoles!=null&&codeRoles.size()>0){
//            errorMsg("此角色代码已存在！");
//        }
        role.setStatus(Status.enable);
       Role newRole =  roleDao.saveBack(role);

        return newRole;
    }

    @Override
    public void addRoleMenuT(String meuns,Long id) {

        List<Integer> menuIds = JsonParseTool.parseArray(meuns, Integer.class, "菜单数据不正确");
        Role role=roleDao.get(id);
        if (role!=null){
            roleMenuDao.deleteRoleId(id);
        }
        HashSet<Integer> set = new HashSet<>();
        set.addAll(menuIds);
        // TODO: 2016/7/13 删除RoleMenu 的 RoleId =id 所有实体
        // TODO: 2016/7/13  RoleMenuDao.save()
        for (long menuid: set) {
            RoleMenu menu = new RoleMenu();
            menu.setMenuId(menuid);
            menu.setRoleId(id);
            roleMenuDao.save(menu);
        }
    }

  /*  @Override
    public void getAlreaday(Long id,Role query) {
        List<Role> list= roleDao.getAlready(id,query);
    }*/
    @Override
    public List<Long> getMenuIdsByRoleId(long id){
        Role role = roleDao.get(id);
        ArrayList<Long> list = new ArrayList<>();
        role.getMenus().forEach(m -> list.add(m.getId()));
        return list;
    }



    @Override
    public void updateRoleT(Role role) {
        checkString(role.getRole(),"角色名不能为空！");
        Role upRole = roleDao.get(role.getId());
        upRole.setRole(role.getRole());
        upRole.setDescription(role.getDescription());
        roleDao.update(upRole);
    }

    @Override
    public void deleteRoleT(Long id) {
        UserRole ur = new UserRole();
        ur.setRoleId(id);
        List<UserRole> urs = userRoleDao.findByExample(ur);
        if(urs!=null&&urs.size()>0){
            errorMsg("该角色下存在用户，无法删除！");
        }
        roleDao.delete(id);
    }

    @Override
    public void toStartOrStopT(Long id) {
        Role role = roleDao.get(id);
        checkNull(role,"没有找到此角色！");
        if(role.getStatus()==Status.enable) {
            role.setStatus(Status.disable);
        }else{
            role.setStatus(Status.enable);
        }
        roleDao.update(role);

    }

    @Override
    public void transformUsersT(Long fromId,Long toId) {
        //当前角色
        UserRole nowUserRole = new UserRole();
        nowUserRole.setRoleId(fromId);
        List<UserRole> nowUserRoles = userRoleDao.findByExample(nowUserRole);
        for(UserRole ur:nowUserRoles){
            ur.setRoleId(toId);
            userRoleDao.update(ur);
        }
        //待转角色
//        UserRole transformUserRole = new UserRole();
//        transformUserRole.setRoleId(toId);
//        List<UserRole> transformUserRoles = userRoleDao.findByExample(transformUserRole);
//
//        transformUserRoles.addAll(nowUserRoles);
    }

    @Override
    public List <Role> getOtherRole(Long id){
        Role role = roleDao.get(id);
        List <Role> roles = roleDao.findAll();
        roles.remove(role);
        return  roles;
    }

    @Override
    public JSONObject checkRoleName(String role) {
        JSONObject json =new JSONObject();
        Role findRole = new Role();
        findRole.setRole(role);
        List<Role> roles = roleDao.findByExample(findRole);
        if(roles!=null&&roles.size()>0){
            json.put("error","命名重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return  json;
    }

    @Override
    public JSONObject checkRoleCode(String code) {
        JSONObject json =new JSONObject();
        Role findRole = new Role();
        findRole.setCode(code);
        List<Role> roles = roleDao.findByExample(findRole, MatchMode.EXACT);
        if(roles!=null&&roles.size()>0){
            json.put("error","命名重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return  json;
    }


    @Override
    public User getProvinceAgentUser(long areaId){

        return userRoleDao.getProvinceAgentUser(areaId);
    }

    @Override
    public User getCompanyAngent(){
        List<User> list = userRoleDao.getUserByRoleCode(RoleCode.COUNTRY_AGENT);
        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }


    @Override
    public UserRole addUserT(long uid, String roleCode) {
        Role role = getByCode(roleCode);
        UserRole userRole = new UserRole();
        if (role!=null){

            userRole.setUid(uid);
            userRole.setRoleId(role.getId());
            userRoleDao.save(userRole);
            return userRole;
        }
        errorMsg("该角色不存在:"+roleCode);
        return userRole;
    }

    /**
     * 删除role下面的menu
     * @param roleId
     */
    @Override
    public void deleteRolesById(Long roleId) {

        roleMenuDao.deleteRoleId(roleId);

    }

    public Role getByCode(String code){
        Role role = new Role();
        role.setCode(code);
        List<Role> list = findByExampleExact(role);
        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }


    @Override
    public List<Role> getAllRole() {
        Role role = new Role();
//        role.setCode(RoleCode.EMOLOYEE);
//        List<Role> roleList = roleDao.findByExample(role);
        List<Role> roleList = roleDao.findAll();
        return  roleList;
    }
}
