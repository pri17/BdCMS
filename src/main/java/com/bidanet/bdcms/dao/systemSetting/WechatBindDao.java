package com.bidanet.bdcms.dao.systemSetting;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamWxBind;

import java.util.List;

/**
 * Created by pri17 on 2017/5/9.
 */
public interface WechatBindDao extends Dao<ExamWxBind> {
    public List<ExamWxBind> findByMemberId(Long id);
}
