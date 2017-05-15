package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.MessageTpl;

/**
*消息模板
*/
public interface MessageTplService extends Service<MessageTpl> {

    void deleteT(long id);

    void addT(MessageTpl messageTpl);

    void setDisableT(long id);

    void setEnableT(long id);

    MessageTpl getByCode(String code);
}
