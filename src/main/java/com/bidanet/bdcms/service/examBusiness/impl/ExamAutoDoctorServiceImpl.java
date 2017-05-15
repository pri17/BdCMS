package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamAutoDoctorDao;
import com.bidanet.bdcms.entity.ExamAutoDoctor;
import com.bidanet.bdcms.service.examBusiness.ExamAutoDoctorService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
*
*/
@Service
public class ExamAutoDoctorServiceImpl extends BaseServiceImpl<ExamAutoDoctor> implements ExamAutoDoctorService {
    @Autowired
    private ExamAutoDoctorDao dao;
    @Override
    protected Dao getDao() {
        return dao;
    }


}
