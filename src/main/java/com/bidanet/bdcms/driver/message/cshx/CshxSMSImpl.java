package com.bidanet.bdcms.driver.message.cshx;

import com.bidanet.bdcms.common.XmlTool;

import com.bidanet.bdcms.service.ConfigService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创世华信短信接口
 */
@Service("message.cshxsms")
public class CshxSMSImpl implements com.bidanet.bdcms.driver.message.Message {

    @Autowired
    ConfigService configService;

    @Autowired
    XmlTool xmlTool;


    protected static final String userIdKey="sms_cshx_user_id";
    protected static final String userNameKey="sms_cshx_username";
    protected static final String pwdKey="sms_cshx_pwd";



    @Override
    public boolean send(String msg, String... mobile){
        try {
            HttpResponse<String> response = Unirest.post("http://sh2.ipyy.com/sms.aspx")
                    .field("userid", configService.getConfig(userIdKey))
                    .field("account", configService.getConfig(userNameKey))
                    .field("password", configService.getConfig(pwdKey))
                    .field("mobile",getMobile(mobile))
                    .field("content",msg)
                    .field("action", "send").asString();
            String body = response.getBody();

            ReturnSms returnSms = ((ReturnSms) xmlTool.fromXml(body,ReturnSms.class));
            // TODO: 2016/7/4 记录日志
// TODO: 2016/7/4 记录日志
            return returnSms != null && returnSms.isSuccess();


        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }
    protected String getMobile(String[] mobile){
        if (mobile==null||mobile.length==0){
            return "";
        }
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < mobile.length - 1; i++) {
            sb.append(mobile[i]).append(",");
        }
        sb.append(mobile[mobile.length-1]);
        return sb.toString();
    }



}
