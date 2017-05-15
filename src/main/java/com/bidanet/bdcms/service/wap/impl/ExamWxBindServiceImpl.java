package com.bidanet.bdcms.service.wap.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.wap.ExamWxBindDao;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.wap.ExamWxBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 体检登记. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-12-13 16:24:40
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
@Service
public class ExamWxBindServiceImpl extends BaseServiceImpl<ExamWxBind> implements ExamWxBindService {

    @Autowired
    private ExamWxBindDao examWxBindDao;

    @Override
    protected Dao getDao() {
        return examWxBindDao;
    }

    @Override
    public List<ExamWxBind> getExamWxBindByOpenId(String openId) {
        return examWxBindDao.getListByOpenId(openId);
    }

    @Override
    public void saveWxBind(ExamWxBind examWxBind) {
        examWxBindDao.save(examWxBind);
    }

}
