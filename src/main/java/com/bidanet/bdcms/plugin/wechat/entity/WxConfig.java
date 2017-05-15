package com.bidanet.bdcms.plugin.wechat.entity;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * Created by Administrator on 2016/9/24.
 */
public class WxConfig {

    private WxMpInMemoryConfigStorage config;
    private WxMpService wxMpService;
    private WxMpMessageRouter wxMpMessageRouter;

    public WxMpInMemoryConfigStorage getConfig() {
        return config;
    }

    public void setConfig(WxMpInMemoryConfigStorage config) {
        this.config = config;
    }

    public WxMpService getWxMpService() {
        return wxMpService;
    }

    public void setWxMpService(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    public WxMpMessageRouter getWxMpMessageRouter() {
        return wxMpMessageRouter;
    }

    public void setWxMpMessageRouter(WxMpMessageRouter wxMpMessageRouter) {
        this.wxMpMessageRouter = wxMpMessageRouter;
    }
}
