package com.bidanet.bdcms.dao.impl;

import com.bidanet.bdcms.common.RoleCode;
import com.bidanet.bdcms.entity.Menu;
import com.bidanet.bdcms.entity.Role;

import  com.bidanet.bdcms.dao.RoleDao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
*
*/
@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {

  /*  @Override
    public List<Role> getAlready(Long id,Role query) {
        List list= getSession().createQuery("from Role where id=?").setLong(0,id).list();
        return  list;
    }*/





}
