package com.bidanet.bdcms.service.systemSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.systemSetting.SysLogDao;
import com.bidanet.bdcms.entity.SysLog;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.SysLogService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuejike on 2016-05-24.
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLog> implements SysLogService {
    @Autowired
    private SysLogDao sysLogDao;
    @Override
    protected Dao getDao() {
        return sysLogDao;
    }

    @Override
    public void getSysLogList(SysLog sysLog, Page<SysLog> page){
        List<SysLog> list = sysLogDao.findByExample(sysLog, page.getPageCurrent(), page.getPageSize());
        long count = sysLogDao.countByExample(sysLog);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void updateSysLogT(SysLog sysLog) {
        checkString(sysLog.getTitle(),"请填写版本标题！");
        checkString(sysLog.getContent(),"请填写版本内容！");
        SysLog sysLog1 = sysLogDao.get(sysLog.getId());
        checkNull(sysLog1,"没有找到此日志！");
        sysLog1.setTitle(sysLog.getTitle());
        sysLog1.setContent(sysLog.getContent());
        sysLogDao.update(sysLog1);
    }

    @Override
    public void addSysLogT(SysLog sysLog) {
        checkString(sysLog.getTitle(),"请填写版本标题！");
        checkString(sysLog.getContent(),"请填写版本内容！");
        sysLogDao.save(sysLog);
    }

}
