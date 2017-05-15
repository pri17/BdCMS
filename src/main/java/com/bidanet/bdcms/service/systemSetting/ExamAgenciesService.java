package com.bidanet.bdcms.service.systemSetting;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

import java.util.List;

/**
 * Created by xuejike on 2016-05-24.
 */
public interface ExamAgenciesService extends Service<ExamAgencies> {

    void getExamAgenciesList(ExamAgencies examAgencies, Page<ExamAgencies> page);

//    void toStartStopT(Long uid);
//
//    void changePwdT(String oldPwd, String newPwd, User loginUser);
//
    /**
     * 修改体检机构
     * @param examAgencies
     */
    void updateAgenciesT(ExamAgencies examAgencies);

    ExamAgencies addAgenciesT(ExamAgencies examAgencies);

    JSONObject checkAgenciesCode(String agenciesCode);

    JSONObject checkAgenciesName(String agenciesName);

    /**
     * 获取所有的体检机构用于体检选取
     * @return
     */
    List<ExamAgencies> getAllAgencies();

    ExamAgencies findAgenciesById(Long id);

    /**
     * 添加体检机构时添加体检医生
     */
    void addAgenciesDoctor(ExamAgencies examAgencies);

}
