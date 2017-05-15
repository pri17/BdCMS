package com.bidanet.bdcms.service.fee.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.fee.ExamPayTypeDao;
import com.bidanet.bdcms.dao.systemSetting.ExamPayItemDao;
import com.bidanet.bdcms.entity.ExamPayItem;
import com.bidanet.bdcms.entity.ExamPayType;
import com.bidanet.bdcms.service.fee.ExamPayTypeService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbinbin on 2016-10-11 11:35:59.
 */
@Service("examPayTypeService")
public class ExamPayTypeServiceImpl extends BaseServiceImpl<ExamPayType> implements ExamPayTypeService {
    @Autowired
    private ExamPayTypeDao examPayTypeDao;

    @Override
    protected Dao getDao() {
        return examPayTypeDao;
    }

    @Override
    public List<ExamPayType> getAllPayType() {

        return  examPayTypeDao.findAll();
    }
}
