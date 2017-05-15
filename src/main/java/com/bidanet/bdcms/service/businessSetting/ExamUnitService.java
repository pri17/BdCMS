package com.bidanet.bdcms.service.businessSetting;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.entity.ExamUnit;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

/**
 * Created by zhangbinbin on 2016-10-11 11:35:59.
 */
public interface ExamUnitService extends Service<ExamUnit> {

    void getExamUnitList(ExamUnit examUnit, Page<ExamUnit> page);

    void updateExamUnitT(ExamUnit examUnit);

    void addExamUnitT(ExamUnit examUnit);

    void addExamUnitByThirdT(ExamUnit examUnit);

    /**
     * 新增工作单位
     * @param unitName
     * @return
     */
    JSONObject checkUnitName(String unitName);

}
