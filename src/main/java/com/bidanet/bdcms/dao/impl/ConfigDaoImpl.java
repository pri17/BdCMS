package com.bidanet.bdcms.dao.impl;

import com.bidanet.bdcms.entity.Config;

import  com.bidanet.bdcms.dao.ConfigDao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
*
*/
@Repository
public class ConfigDaoImpl extends BaseDaoImpl<Config> implements ConfigDao {

    @Override
    public List<Object> getGroup() {
        List list = getSession().createSQLQuery("select DISTINCT _group from _config").list();
        return list;
    }
}
