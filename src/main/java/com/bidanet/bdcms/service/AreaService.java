package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.Area;

import java.util.List;

/**
*
*/
public interface AreaService extends Service<Area> {

    List<Area> getRoot();

    void addAreaT(Area area);

    void updateAreaT(Area area);

    void deleteAreaT(Long id);

    List<Area> getOpenCityByPid(Long pid);

    List<Area> getArea(String name);

    Long getCityId(String name);

    List<Area> getAllNotParentCity();

}
