package com.bidanet.bdcms.dao.wap.impl;

import com.bidanet.bdcms.dao.impl.BaseDaoImpl;
import com.bidanet.bdcms.dao.wap.ExamWxBindDao;
import com.bidanet.bdcms.entity.ExamWxBind;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
*
*/
@Repository
public class ExamWxBindDaoImpl extends BaseDaoImpl<ExamWxBind> implements ExamWxBindDao {

    @Override
    public List<ExamWxBind> getListByOpenId(String openId) {
        List list=getSession().createQuery("from ExamWxBind where wxOpenId like?"). setString(0, openId).list();
        return list;
    }

}
