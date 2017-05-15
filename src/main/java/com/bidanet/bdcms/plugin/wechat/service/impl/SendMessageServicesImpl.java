package com.bidanet.bdcms.plugin.wechat.service.impl;

import com.bidanet.bdcms.plugin.wechat.entity.NewMessage;
import com.bidanet.bdcms.plugin.wechat.entity.NewsContext;
import com.bidanet.bdcms.plugin.wechat.service.SendMessageService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/9/26.
 */
@Service
public class SendMessageServicesImpl implements SendMessageService {

    @Autowired
    private WxMpService wxMpService;

    /**
     * 发送模板消息
     *
     * @param toUser
     * @param templateId
     * @param url
     * @param topColor
     * @throws WxErrorException
     */
    @Override
    public void sendTemplateMessage(String toUser, String templateId, String url, String topColor) throws WxErrorException {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setToUser(toUser);
        templateMessage.setTemplateId(templateId);
        templateMessage.setUrl(url);
        templateMessage.setTopColor(topColor);
        templateMessage.getData().add(new WxMpTemplateData("name", "哈哈哈", topColor));
        templateMessage.getData().add(new WxMpTemplateData("value", "这是一个内容", topColor));

        wxMpService.templateSend(templateMessage);
    }

    /**
     * 自动回复文本信息
     *
     * @param toUser
     * @param fromUser
     * @param context
     * @return
     */
    @Override
    public WxMpXmlOutTextMessage autoReplyTextMessage(String toUser, String fromUser, String context) {
        return WxMpXmlOutMessage.TEXT()
                .content(context)
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }

    /**
     * 自动回复图片文本信息
     *
     * @param toUser
     * @param fromUser
     * @param mediaId
     * @return
     */
    @Override
    public WxMpXmlOutImageMessage autoReplyImageMessage(String toUser, String fromUser, String mediaId) {
        return WxMpXmlOutMessage.IMAGE()
                .mediaId(mediaId)
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }

    /**
     * 自动回复语音消息
     *
     * @param toUser
     * @param fromUser
     * @param mediaId
     * @return
     */
    @Override
    public WxMpXmlOutVoiceMessage autoReplyVoiceMessage(String toUser, String fromUser, String mediaId) {
        return WxMpXmlOutMessage.VOICE()
                .mediaId(mediaId)
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }

    /**
     * 自动回复视频消息
     *
     * @param toUser
     * @param fromUser
     * @param mediaId
     * @param title
     * @param description 描述
     * @return
     */
    @Override
    public WxMpXmlOutVideoMessage autoReplyVideoMessage(String toUser, String fromUser, String mediaId, String title, String description) {
        return WxMpXmlOutMessage.VIDEO()
                .mediaId(mediaId)
                .fromUser(fromUser)
                .toUser(toUser)
                .title(title)
                .description(description)
                .build();
    }

    /**
     * 自动回复音乐文件
     *
     * @param toUser
     * @param fromUser
     * @param title
     * @param description
     * @param hqMusicUrl
     * @param musicUrl
     * @param thumbMediaId
     * @return
     */
    @Override
    public WxMpXmlOutMusicMessage autoReplyMusicMessage(String toUser, String fromUser, String title, String description, String hqMusicUrl, String musicUrl, String thumbMediaId) {
        return WxMpXmlOutMessage.MUSIC()
                .fromUser(fromUser)
                .toUser(toUser)
                .title(title)
                .description(description)
                .hqMusicUrl(hqMusicUrl)
                .musicUrl(musicUrl)
                .thumbMediaId(thumbMediaId)
                .build();
    }

    /**
     * 自动回复 图文消息
     *
     * @param toUser
     * @param fromUser
     * @param description
     * @param picUrl
     * @param title
     * @param url
     * @return
     */
    @Override
    public WxMpXmlOutNewsMessage autoReplyNewMessage(String toUser, String fromUser, String description, String picUrl, String title, String url) {
        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setDescription(description);
        item.setPicUrl(picUrl);
        item.setTitle(title);
        item.setUrl(url);

        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
                .fromUser(fromUser)
                .toUser(toUser)
                .addArticle(item)
                .build();

        return m;
    }

    /**
     * 主动发送文本消息
     *
     * @param toUser
     * @param context
     */
    @Override
    public void sendTextMessage(String toUser, String context)  {
        WxMpCustomMessage message = WxMpCustomMessage
                .TEXT()
                .toUser(toUser)
                .content(context)
                .build();

        try {
            wxMpService.customMessageSend(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主动发送 图片消息
     * @param toUser
     * @param mediaId
     * @throws WxErrorException
     */
    @Override
    public void sendImageMessage(String toUser, String mediaId)  {
        WxMpCustomMessage wxMpCustomMessage = WxMpCustomMessage
                .IMAGE()
                .toUser(toUser)
                .mediaId(mediaId)
                .build();

        try {
            wxMpService.customMessageSend(wxMpCustomMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主动发送语音消息
     * @param toUser
     * @param mediaId
     * @throws WxErrorException
     */
    @Override
    public void sendVoiceMessage(String toUser, String mediaId)  {
        WxMpCustomMessage message = WxMpCustomMessage.VOICE()
                .toUser("OPENID")
                .mediaId("MEDIA_ID")
                .build();

        try {
            wxMpService.customMessageSend(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主动发送视频消息
     * @param toUser
     * @param title
     * @param mediaId
     * @param thumbMediaId
     * @param description
     * @throws WxErrorException
     */
    @Override
    public void sendVideoMessage(String toUser, String title, String mediaId, String thumbMediaId, String description) throws WxErrorException {
        WxMpCustomMessage message = WxMpCustomMessage.VIDEO()
                .toUser(toUser)
                .title(title)
                .mediaId(mediaId)
                .thumbMediaId(thumbMediaId)
                .description(description)
                .build();

        wxMpService.customMessageSend(message);
    }

    /**
     * 主动发送音乐消息
     * @param toUser
     * @param title
     * @param mediaId
     * @param musicUrl
     * @param description
     * @param hqMusicUrl
     * @throws WxErrorException
     */
    @Override
    public void sendMusicMessage(String toUser, String title, String mediaId, String musicUrl, String description, String hqMusicUrl) throws WxErrorException {
        WxMpCustomMessage message = WxMpCustomMessage.MUSIC()
                .toUser("OPENID")
                .title("TITLE")
                .thumbMediaId("MEDIA_ID")
                .description("DESCRIPTION")
                .musicUrl("MUSIC_URL")
                .hqMusicUrl("HQ_MUSIC_URL")
                .build();

        wxMpService.customMessageSend(message);
    }

    /**
     * 主动发送音乐消息
     * @param newMessage
     */
    @Override
    public void sendNewMessage(NewMessage newMessage) throws WxErrorException {
        WxMpCustomMessage message = WxMpCustomMessage.NEWS()
                .toUser(newMessage.getToUser())
                .build();

        for (NewsContext newsContext: newMessage.getNewsContext()){
            WxMpCustomMessage.WxArticle article1 = new WxMpCustomMessage.WxArticle();
            article1.setUrl(newsContext.getUrl());
            article1.setPicUrl(newsContext.getPicUrl());
            article1.setDescription(newsContext.getDescription());
            article1.setTitle(newsContext.getTitle());
            message.getArticles().add(article1);
        }

        wxMpService.customMessageSend(message);

    }


}
