package com.bidanet.bdcms.dao;

import com.bidanet.bdcms.entity.Area;

import java.util.List;

/**
* Area DAO
*/
public interface AreaDao extends Dao<Area> {
    List<Area> getList(String name);
    Area getCityId(String name);
    List<Area> getAllNotParentCity();

}
