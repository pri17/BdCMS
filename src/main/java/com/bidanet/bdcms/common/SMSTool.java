package com.bidanet.bdcms.common;

import com.bidanet.bdcms.driver.message.Message;
import com.bidanet.bdcms.entity.MessageTpl;
import com.bidanet.bdcms.service.ConfigService;
import com.bidanet.bdcms.service.MessageTplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SMSTool {

    public static final String sms_code="sms.code";
    public static final String sms_shop_code="sms.shopStoreCode";

    public static final String sms_tuan_code="sms.tuancode";



    protected static final String smsDriver="sms_driver";
    @Autowired
    ConfigService configService;

    @Autowired
    MessageTplService messageTplService;


    public boolean send(String msg,String ...mobile){
        String config = configService.getConfig(smsDriver);

        Message bean = SpringWebTool.getBean(config, Message.class);
        boolean send = bean.send(msg, mobile);

        return send;
//        return true;
    }

    public boolean sendTpl(String mobile,String tplCode,String ...param){
        MessageTpl tpl = messageTplService.getByCode(tplCode);
        if (tpl==null){
            return false;
        }
        String content = tpl.getContent();
        if (param!=null){
            for (int i = 0; i < param.length; i++) {
                content=content.replaceAll("\\{"+i+"\\}",param[i]);
            }
        }
        return send(content,mobile);
    }
}
