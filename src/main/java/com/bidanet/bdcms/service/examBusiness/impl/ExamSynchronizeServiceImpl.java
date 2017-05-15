package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamSynchronizeDao;
import com.bidanet.bdcms.entity.ExamSynchronize;
import com.bidanet.bdcms.service.examBusiness.ExamSynchronizeService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
*
*/
@Service
public class ExamSynchronizeServiceImpl extends BaseServiceImpl<ExamSynchronize> implements ExamSynchronizeService {
    @Autowired
    private ExamSynchronizeDao dao;
    @Override
    protected Dao getDao() {
        return dao;
    }


    @Override
    public Boolean batchSynchronizeData(ExamSynchronize examSynchronize) {
        //组装数据上传省平台
        //组装数据上传方正平台


        return true;
    }

    /**
     * 同步省平台
     * @param examSynchronize
     * @return
     */
    public Boolean synchronizeDateToProvince(ExamSynchronize examSynchronize){

        return true;
    }


    /**
     * 同步方正平台
     * @param examSynchronize
     * @return
     */
    public Boolean synchronizeDateToFangzheng(ExamSynchronize examSynchronize){

        return true;
    }
}
