package com.bidanet.bdcms.dao;

import com.bidanet.bdcms.entity.Config;

import java.util.List;

/**
* Config DAO
*/
public interface ConfigDao extends Dao<Config> {
    List<Object> getGroup();
}
