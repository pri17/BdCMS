package com.bidanet.bdcms.service.systemSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.systemSetting.ExamPayItemDao;
import com.bidanet.bdcms.dao.systemSetting.SysLogDao;
import com.bidanet.bdcms.entity.ExamPayItem;
import com.bidanet.bdcms.entity.SysLog;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamPayItemService;
import com.bidanet.bdcms.service.systemSetting.SysLogService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbinbin on 2016-10-11 11:35:59.
 */
@Service
public class ExamPayItemServiceImpl extends BaseServiceImpl<ExamPayItem> implements ExamPayItemService {
    @Autowired
    private ExamPayItemDao examPayItemDao;
    @Override
    protected Dao getDao() {
        return examPayItemDao;
    }

    @Override
    public void getExamPayItemList(ExamPayItem examPayItem, Page<ExamPayItem> page){
        List<ExamPayItem> list = examPayItemDao.findByExample(examPayItem, page.getPageCurrent(), page.getPageSize());
        long count = examPayItemDao.countByExample(examPayItem);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void updateExamPayItemT(ExamPayItem examPayItem) {
        checkString(examPayItem.getName(),"请填写项目名称！");
        checkString(examPayItem.getType(),"请填写扫码方式！");
        checkString(examPayItem.getScan(),"请填写收费方式！");
        ExamPayItem examPayItem1 = examPayItemDao.get(examPayItem.getId());
        checkNull(examPayItem1,"没有找到此项目！");
        examPayItem1.setName(examPayItem.getName());
        examPayItem1.setScan(examPayItem.getScan());
        examPayItem1.setType(examPayItem.getType());
        examPayItemDao.update(examPayItem1);
    }

    @Override
    public void addExamPayItemT(ExamPayItem examPayItem) {
        checkString(examPayItem.getName(),"请填写项目名称！");
        checkString(examPayItem.getType(),"请填写扫码方式！");
        checkString(examPayItem.getScan(),"请填写收费方式！");
        examPayItemDao.save(examPayItem);
    }

}
