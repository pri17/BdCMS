package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.MessageTplDao;
import com.bidanet.bdcms.entity.MessageTpl;
import com.bidanet.bdcms.entity.entityEnum.Status;
import com.bidanet.bdcms.service.MessageTplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
*   消息模板
*/
@Service
public class MessageTplServiceImpl extends BaseServiceImpl<MessageTpl> implements MessageTplService {
    @Autowired
    private MessageTplDao messageTplDao;
    @Override
    protected Dao getDao() {
        return messageTplDao;
    }

    /**
     *
     * 删除消息
     */
    @Override
    public void deleteT(long id) {
        messageTplDao.delete(id);

    }

    /**
     *
     * 增加消息
     */
    @Override
    public void addT(MessageTpl messageTpl) {
        checkString(messageTpl.getName(),"名字不能为空");
        checkString(messageTpl.getContent(),"消息内容不能为空");
        //消息模板新增富文本编辑带个“，”
        String contount=messageTpl.getContent();
        String cont=contount.replace(" ,","");
        messageTpl.setContent(cont);
        messageTpl.setStatus(Status.auditing);
        messageTplDao.save(messageTpl);

    }

    @Override
    /*
        把启用变成禁用
     */
    public void setDisableT(long id) {
        MessageTpl messageTpl1 = messageTplDao.get(id);
        checkNull(messageTpl1,"该消息模板不存在");
        messageTpl1.setStatus(Status.disable);
        messageTplDao.update(messageTpl1);

    }

    @Override
    /*
    把禁用变成启用
     */

    public void setEnableT(long id) {
        MessageTpl messageTpl1 = messageTplDao.get(id);
        checkNull(messageTpl1,"该消息模板不存在");
        messageTpl1.setStatus(Status.enable);
        messageTplDao.update(messageTpl1);

    }

    /**
     *
     * 更新消息
     */
    @Override
    public void updateT(MessageTpl messageTpl){
        checkString(messageTpl.getName(),"名字不能为空");
        checkString(messageTpl.getContent(),"内容不能为空");
        MessageTpl message = messageTplDao.get(messageTpl.getId());
        checkNull(messageTpl,"该消息不存在");
        message.setName(messageTpl.getName());
        message.setContent(messageTpl.getContent());
        messageTplDao.update(message);
    }




    @Override
    public MessageTpl getByCode(String code){
        MessageTpl tpl = new MessageTpl();
        tpl.setCode(code);
        List<MessageTpl> list = findByExampleExact(tpl);
        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
