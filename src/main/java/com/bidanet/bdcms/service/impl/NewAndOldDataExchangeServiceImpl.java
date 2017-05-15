package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.NewAndOldDataExchangeDao;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.NewAndOldDataExchange;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.NewAndOldDataExchangeService;
import com.bidanet.bdcms.service.examBusiness.ExamNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by xuejike-pc on 2017/2/20.
 */
@Service
public class NewAndOldDataExchangeServiceImpl extends BaseServiceImpl<NewAndOldDataExchange>implements NewAndOldDataExchangeService {
    @Autowired
    NewAndOldDataExchangeDao newAndOldDataExchangeDao;
    @Autowired
    ExamNumberService examNumberService;
    @Override
    protected Dao getDao() {
        return newAndOldDataExchangeDao;
    }

    @Override
    public ExamMemberExam insertDataT(String name, Integer sex, Integer age, String birthday,
                                      String address , String idCardNum, String mobile, String workUnit,
                                      Long areaId, Long packageId, BigDecimal packageMoney, User user,
                                      String memberIcon, Long agenciesId, Long categoryId, Integer isCameraPhoto,
                                      Long oldId, String physicalNumberOld){
        ExamMemberExam  exam = examNumberService.addExamTOldToNewNoCreateMemberNumberT
                (name, sex, age, birthday, address, idCardNum, mobile, workUnit, areaId,
                        packageId, packageMoney, user, memberIcon, agenciesId,
                        categoryId, isCameraPhoto, physicalNumberOld);

        NewAndOldDataExchange nade=new NewAndOldDataExchange();
        nade.setOldId(oldId);
        nade.setNewId(exam.getId());
        nade.setNewToOldTag(false);
        nade.setOldToNewTage(false);
        nade.setPhysicalResult(false);
        nade.setExchangeTime(System.currentTimeMillis());
        nade.setPhysicalNumberNew(exam.getExamNumber());
        nade.setPhysicalNumberOld(physicalNumberOld);
        newAndOldDataExchangeDao.save(nade);
        return exam;
    }

}
