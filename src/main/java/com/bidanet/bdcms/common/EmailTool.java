package com.bidanet.bdcms.common;

import com.bidanet.bdcms.driver.message.Message;
import com.bidanet.bdcms.driver.message.email.EmailMessageImpl;
import com.bidanet.bdcms.entity.MessageTpl;
import com.bidanet.bdcms.service.ConfigService;
import com.bidanet.bdcms.service.MessageTplService;
import org.apache.commons.mail.*;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/8/2.
 */
@Service
public class EmailTool {

    public static final String sms_registerEmail = "sms.registerEmail";

    public static final String sms_addEmail = "sms.addEmail";

    public static final String sms_changeEmail = "sms.changeEmail";

    @Autowired
    ConfigService configService;

    @Autowired
    MessageTplService messageTplService;




/*    public boolean send(String msg,String ...email){
        String config = configService.getConfig(smsDriver);

        Message bean = SpringWebTool.getBean(config, Message.class);
        boolean send = bean.send(msg, email);
        if (send){
            logInfoService.addInfoLog("sms",email+" -> "+msg);
        }else{
            logInfoService.addErrorLog("sms",email+" -> "+msg);
        }
        return send;
    }*/

/*    public boolean sendTpl(String email,String tplCode,String ...param){
        MessageTpl tpl = messageTplService.getByCode(tplCode);
        if (tpl==null){
            logInfoService.addErrorLog("sms","不存在模板代码:"+tplCode);
            return false;
        }
        String content = tpl.getContent();
        if (param!=null){
            for (int i = 0; i < param.length; i++) {
                content=content.replaceAll("\\{"+i+"\\}",param[i]);
            }
        }
        return send(content,email);
    }*/


    public boolean send(String msg, String subject, String ...email){
        boolean flag = false;

        //html 代码
/*        String htmlEmailTemplate = "你最近还好吗？.... <img src=\"http://www.apache.org/images/feather.gif\"> ....";*/
        ImageHtmlEmail imageHtmlEmail = new ImageHtmlEmail();
        imageHtmlEmail.setHostName("smtp.exmail.qq.com");
        imageHtmlEmail.setAuthentication("service@yingegou.com", "Abc123456");
        try {
            // define you base URL to resolve relative resource locations
            URL url = new URL("http://www.apache.org");
            //设置编码格式
            imageHtmlEmail.setCharset("UTF-8");
            imageHtmlEmail.setDataSourceResolver(new DataSourceUrlResolver(url));
            imageHtmlEmail.setFrom("service@yingegou.com");
            imageHtmlEmail.setSubject(subject);
            imageHtmlEmail.setHtmlMsg(msg);
            imageHtmlEmail.addTo(email);
            imageHtmlEmail.send();
            flag = true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (EmailException e1){
            e1.printStackTrace();
        }
        return  flag;
    }


    public boolean sendTpl(String email,String tplCode,String ...param) {
        MessageTpl tpl = messageTplService.getByCode(tplCode);
        if (tpl==null){
//            logInfoService.addErrorLog("sms","不存在模板代码:"+tplCode);
            return false;
        }
        String content = tpl.getContent();
        String subject = tpl.getName();
        if (param!=null){
            for (int i = 0; i < param.length; i++) {
                content=content.replaceAll("\\{"+i+"\\}",param[i]);
            }
        }

        return send(content,subject,email);

    }




}
