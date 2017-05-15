package com.bidanet.bdcms.service.systemSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.systemSetting.WechatBindDao;
import com.bidanet.bdcms.entity.ExamWxBind;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.WechatUnbindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pri17 on 2017/5/9.
 */
@Service
public class WechatUnbindServiceImpl extends BaseServiceImpl<ExamWxBind> implements WechatUnbindService {

    @Autowired
    private WechatBindDao wechatBindDao;

    @Override
    public List<ExamWxBind> getAllwechatBindList() {
        List<ExamWxBind> wechatList = wechatBindDao.findAll();
        return wechatList;
    }

    @Override
    public void deleteWechatBind(Long id) {
        wechatBindDao.delete(id);
    }

    @Override
    public List<ExamWxBind> findByMemberId(Long id){
        return wechatBindDao.findByMemberId(id);
    }

    @Override
    protected Dao getDao() {
        return wechatBindDao;
    }
}
