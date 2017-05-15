package com.bidanet.bdcms.dao.impl;

import com.bidanet.bdcms.dao.UserDao;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.UserRole;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuejike on 2016-05-24.
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    @Override
    public List<UserRole> findAdminList(String realName, Long agenciesId, Long roleId, String username, String mobile, int pageCurrent, int pageSize) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder(" from UserRole as ur where 1=1");
//        map.put("code","%"+RoleCode.EMOLOYEE+"%");

        if(StringUtils.isNotBlank(realName)){
            hql.append(" and ur.user.realName like:realName");
            map.put("realName","%"+realName+"%");
        }
        if(agenciesId!=null){
            hql.append(" and ur.user.agenciesId =:agenciesId");
            map.put("agenciesId",agenciesId);
        }
        if(roleId!=null){
            hql.append(" and ur.roleId =:roleId");
            map.put("roleId",roleId);
        }
        if(StringUtils.isNotBlank(username)){
            hql.append(" and ur.user.username =:username");
            map.put("username",username);
        }
        if(StringUtils.isNotBlank(mobile)){
            hql.append(" and ur.user.mobile =:mobile");
            map.put("mobile",mobile);
        }
        hql.append(" order by ur.user.createTime DESC");

        Query query = getSession().createQuery(hql.toString());
        query.setFirstResult((pageCurrent-1) * pageSize);
        query.setMaxResults(pageSize);

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return query.list();
    }

    @Override
    public Long findCountAdminList(String realName, Long agenciesId, Long roleId, String username, String mobile) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder("select count(ur.uid) from UserRole as ur where 1=1");
//        map.put("code","%"+RoleCode.EMOLOYEE+"%");
        if(StringUtils.isNotBlank(realName)){
            hql.append(" and ur.user.realName =:realName");
            map.put("realName",realName);
        }
        if(agenciesId!=null){
            hql.append(" and ur.user.agenciesId =:agenciesId");
            map.put("agenciesId",agenciesId);
        }
        if(roleId!=null){
            hql.append(" and ur.roleId =:roleId");
            map.put("roleId",roleId);
        }
        if(StringUtils.isNotBlank(username)){
            hql.append(" and ur.user.username =:username");
            map.put("username",username);
        }
        if(StringUtils.isNotBlank(mobile)){
            hql.append(" and ur.user.mobile =:mobile");
            map.put("mobile",mobile);
        }
        Query query = getSession().createQuery(hql.toString());

        for (String key :
                map.keySet()) {
            query.setParameter(key, map.get(key));
        }

        return (long)query.uniqueResult();
    }
}
