package com.bidanet.bdcms.dao.wap;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamWxBind;

import java.util.List;

/**
* Area DAO
*/
public interface ExamWxBindDao extends Dao<ExamWxBind> {
    List<ExamWxBind> getListByOpenId(String openId);
}
