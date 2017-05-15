package com.bidanet.bdcms.dao.examBusiness;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamNumber;

import java.util.List;

/**
 * Created by 张彬彬 on 2016-10-28 14:15:01.
 */
public interface ExamNumberDao extends Dao<ExamNumber> {

    List<ExamNumber> findListByCreateTime(Long todayStart,Integer areaCode);
}
