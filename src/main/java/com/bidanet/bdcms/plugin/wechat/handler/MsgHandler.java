package com.bidanet.bdcms.plugin.wechat.handler;

import com.bidanet.bdcms.plugin.wechat.entity.NewMessage;
import com.bidanet.bdcms.plugin.wechat.entity.NewsContext;
import com.bidanet.bdcms.plugin.wechat.service.SendMessageService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 转发客户消息Handler
 *
 * Created by FirenzesEagle on 2016/7/27 0027.
 * Email:liumingbo2008@gmail.com
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Autowired
    private SendMessageService sendMessageService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
//        if (wxMessage.getMsgType().equals("text")){
////            sendMessageService.sendTemplateMessage(wxMessage.getFromUserName() , "AENxUuXJ7pW5UfXsxdHzy16e38An-kIuDbNdydgShy8" , "http://www.baidu.com" , "#00ff00");
////            sendMessageService.sendTextMessage(wxMessage.getFromUserName() , "这是测试消息");
//            List<NewsContext> contextList = new ArrayList<>();
//
//            NewsContext newsContext = new NewsContext();
//            newsContext.setUrl("http://www.baidu.com");
//            newsContext.setDescription("这是描述1");
//            newsContext.setPicUrl("http://g.hiphotos.baidu.com/image/h%3D360/sign=0d515a9bd9c451dae9f60aed86fc52a5/dbb44aed2e738bd4a59870f4a58b87d6267ff9be.jpg");
//            newsContext.setTitle("这是标题1");
//
//            NewsContext newsContext1 = new NewsContext();
//            newsContext1.setUrl("http://www.jd.com");
//            newsContext1.setDescription("这是描述2");
//            newsContext1.setPicUrl("http://e.hiphotos.baidu.com/image/h%3D360/sign=ea96ce4c0e7b020813c939e752d8f25f/14ce36d3d539b600be63e95eed50352ac75cb7ae.jpg");
//            newsContext1.setTitle("这是标题2");
//
//            NewsContext newsContext2 = new NewsContext();
//            newsContext2.setUrl("http://www.taobao.com");
//            newsContext2.setDescription("这是描述3");
//            newsContext2.setPicUrl("http://f.hiphotos.baidu.com/image/h%3D360/sign=0ec20d71f01fbe09035ec5125b600c30/00e93901213fb80e0ee553d034d12f2eb9389484.jpg");
//            newsContext2.setTitle("这是标题3");
//
//            NewsContext newsContext3 = new NewsContext();
//            newsContext3.setUrl("http://www.mi.com");
//            newsContext3.setDescription("这是描述4");
//            newsContext3.setPicUrl("http://f.hiphotos.baidu.com/image/h%3D360/sign=88b61a3d1238534393cf8127a312b01f/e1fe9925bc315c60893482be8fb1cb134954773a.jpg");
//            newsContext3.setTitle("这是标题4");
//
//            contextList.add(newsContext);
//            contextList.add(newsContext1);
//            contextList.add(newsContext2);
//            contextList.add(newsContext3);
//
//            NewMessage newMessage = new NewMessage();
//            newMessage.setNewsContext(contextList);
//            newMessage.setToUser(wxMessage.getFromUserName());
//
//            sendMessageService.sendNewMessage(newMessage);
//        }

        return sendMessageService.autoReplyTextMessage(wxMessage.getFromUserName(),wxMessage.getToUserName(),"感谢关注常熟疾控中心！");
//        return sendMessageService.autoReplyImageMessage(wxMessage.getFromUserName(),wxMessage.getToUserName(),"ktMkGOSHIfBAkvRdt2EKsAA6hVTV_fu-c_oZk74dEj0bYQhoeAXsfOGgkjpaW3XR");

    }

}
