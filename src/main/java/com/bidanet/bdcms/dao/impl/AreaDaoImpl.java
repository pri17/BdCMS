package com.bidanet.bdcms.dao.impl;

import com.bidanet.bdcms.dao.AreaDao;
import com.bidanet.bdcms.entity.Area;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
*
*/
@Repository
public class AreaDaoImpl extends BaseDaoImpl<Area> implements AreaDao {

    @Override
    public List<Area> getList(String name) {
        List list=getSession().createQuery("from Area where name like?"). setString(0, "%"+name+"%").list();
        return list;
    }

    @Override
    public Area getCityId(String name) {
        Area area=(Area) getSession().createQuery("from Area where name like ?").setString(0,name).uniqueResult();
        return area;
    }

    @Override
    public List<Area> getAllNotParentCity() {
        List list = getSession().createQuery("from Area where parentId <> ?").setLong(0, 0).list();
        return list;
    }
}
