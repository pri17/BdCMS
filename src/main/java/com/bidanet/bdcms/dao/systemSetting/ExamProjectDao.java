package com.bidanet.bdcms.dao.systemSetting;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamProject;
import net.sf.ehcache.transaction.xa.EhcacheXAException;

import java.util.List;

/**
 * 体检项目dao. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-10 15:36
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public interface ExamProjectDao extends Dao<ExamProject>{
    List<ExamProject> getExamPackageProjectList(long packageId);
}
