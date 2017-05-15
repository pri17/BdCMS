package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.NewAndOldDataExchange;
import com.bidanet.bdcms.entity.User;

import java.math.BigDecimal;

/**
 * Created by xuejike-pc on 2017/2/20.
 */
public interface NewAndOldDataExchangeService extends Service<NewAndOldDataExchange> {
    ExamMemberExam insertDataT(String name, Integer sex, Integer age, String birthday,
                               String address , String idCardNum, String mobile, String workUnit,
                               Long areaId, Long packageId, BigDecimal packageMoney, User user,
                               String memberIcon, Long agenciesId, Long categoryId, Integer isCameraPhoto,
                               Long oldId, String physicalNumberOld);
}
