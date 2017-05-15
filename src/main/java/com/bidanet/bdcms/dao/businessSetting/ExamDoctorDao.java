package com.bidanet.bdcms.dao.businessSetting;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamDoctor;

import java.util.List;


/**
 * 体检医生dao. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-18 14:30:05
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
public interface ExamDoctorDao extends Dao<ExamDoctor>{

    List<ExamDoctor> findDoctorByExample(String doctorName,String projectName,Long areaId,Long agenciesId, int pageCurrent,int pageSize);


    List<ExamDoctor> getAllDoctor(Long agenciesId);
}
