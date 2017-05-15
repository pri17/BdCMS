package com.bidanet.bdcms.plugin.wechat.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by FirenzesEagle on 2016/5/30 0030.
 * Email:liumingbo2008@gmail.com
 */
@Configuration
public class MainConfig {

    //TODO 填写公众号开发信息
    //测试公众号 APP_ID
    protected static final String APP_ID = "wxfc5b93ec59c607fe";
    //测试公众号 APP_SECRET
    protected static final String APP_SECRET = "59de80cddb0ece6f9242165ab5d313d9";
    //测试公众号 TOKEN
    protected static final String TOKEN = "WeChatToken";
    //测试公众号 AES_KEY
    protected static final String AES_KEY = "";
//    //正式公众号 APP_ID
//    protected static final String APP_ID = "wxfc5b93ec59c607fe";
//    //正式公众号 APP_SECRET
//    protected static final String APP_SECRET = "59de80cddb0ece6f9242165ab5d313d9";
//    //正式公众号 TOKEN
//    protected static final String TOKEN = "WeChatToken";
//    //正式公众号 AES_KEY
//    protected static final String AES_KEY = "";

    //微信支付商户号
    protected static final String PARTNER_ID = "1371963602";
    //微信支付平台商户API密钥(https://pay.weixin.qq.com/index.php/core/account/api_cert)
    protected static final String PARTNER_KEY = "csjkwxzfcsjkwxzfcsjkwxzfcsjkwxzf";

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(MainConfig.APP_ID);
        configStorage.setSecret(MainConfig.APP_SECRET);
        configStorage.setToken(MainConfig.TOKEN);
        configStorage.setAesKey(MainConfig.AES_KEY);
        configStorage.setPartnerId(MainConfig.PARTNER_ID);
        configStorage.setPartnerKey(MainConfig.PARTNER_KEY);

        return configStorage;
    }

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

}
