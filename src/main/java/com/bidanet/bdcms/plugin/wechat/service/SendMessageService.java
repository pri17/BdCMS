package com.bidanet.bdcms.plugin.wechat.service;

import com.bidanet.bdcms.plugin.wechat.entity.NewMessage;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.*;


import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public interface SendMessageService {

    WxMpXmlOutTextMessage autoReplyTextMessage(String toUser, String fromUser, String context);

    WxMpXmlOutImageMessage autoReplyImageMessage(String toUser, String fromUser, String mediaId);

    WxMpXmlOutVoiceMessage autoReplyVoiceMessage(String toUser, String fromUser, String mediaId);

    WxMpXmlOutVideoMessage autoReplyVideoMessage(String toUser, String fromUser, String mediaId, String title, String description);

    WxMpXmlOutMusicMessage autoReplyMusicMessage(String toUser, String fromUser, String title, String description, String hqMusicUrl, String musicUrl, String thumbMediaId);

    WxMpXmlOutNewsMessage autoReplyNewMessage(String toUser, String fromUser, String description, String picUrl, String title, String url);

    void sendTemplateMessage(String toUser, String templateId, String url, String topColor) throws WxErrorException;

    void sendTextMessage(String toUser, String context) throws WxErrorException;

    void sendImageMessage(String toUser, String mediaId) throws WxErrorException;

    void sendVoiceMessage(String toUser, String mediaId) throws WxErrorException;

    void sendVideoMessage(String toUser, String title, String mediaId, String thumbMediaId, String description) throws WxErrorException;

    void sendMusicMessage(String toUser, String title, String mediaId, String musicUrl, String description, String hqMusicUrl) throws WxErrorException;

    void sendNewMessage(NewMessage newMessage) throws WxErrorException;

}
