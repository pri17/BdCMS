package com.bidanet.bdcms.service.systemSetting;

import com.bidanet.bdcms.entity.SysLog;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

/**
 * Created by xuejike on 2016-05-24.
 */
public interface SysLogService extends Service<SysLog> {

    void getSysLogList(SysLog sysLog, Page<SysLog> page);

    /**
     * 修改系统日志
     * @param sysLog
     */
    void updateSysLogT(SysLog sysLog);

    void addSysLogT(SysLog sysLog);

}
