package com.bidanet.bdcms.service.systemSetting;

import com.bidanet.bdcms.entity.ExamWxBind;
import com.bidanet.bdcms.service.Service;

import java.util.List;

/**
 * Created by pri17 on 2017/5/9.
 * 微信解绑服务
 */
public interface WechatUnbindService extends Service<ExamWxBind> {
    /**
     * 获取所有的微信绑定记录
     * @return
     */
    List<ExamWxBind> getAllwechatBindList();

    List<ExamWxBind> findByMemberId(Long id);

    /**
     * 解绑微信(在Wxbind表中删除数据)
     */
    void deleteWechatBind(Long id);

    /**
     * 通过姓名查找
     * @return
     */
    /**
     * 通过身份证查找
     * @return
     */


}
