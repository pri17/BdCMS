package com.bidanet.bdcms.plugin.wechat.config;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * Created by FirenzesEagle on 2016/6/1 0001.
 * Email:liumingbo2008@gmail.com
 */
public class MenuConfig {

    /**
     * 定义菜单结构
     *
     * @return
     */
    protected static WxMenu getMenu() {

        MainConfig mainConfig = new MainConfig();
        WxMpService wxMpService = mainConfig.wxMpService();
        //线上地址
        String url = "http://csexam.bidanet.com/";
//        //测试地址
//        String url = "http://2tkfc.free.758kongbao.com/";

        WxMenu menu = new WxMenu();
        WxMenu.WxMenuButton button1 = new WxMenu.WxMenuButton();
        button1.setType(WxConsts.BUTTON_VIEW);
        button1.setName("走进疾控");
        button1.setUrl(wxMpService.oauth2buildAuthorizationUrl("www.baidu.com", "snsapi_base", "z"));

        WxMenu.WxMenuButton button2 = new WxMenu.WxMenuButton();
        button2.setType(WxConsts.BUTTON_VIEW);
        button2.setName("健康资讯");
        button2.setUrl(wxMpService.oauth2buildAuthorizationUrl("http://www.baidu.com", "snsapi_base", "a"));

        WxMenu.WxMenuButton button3 = new WxMenu.WxMenuButton();
        button3.setName("健康证办理");

        WxMenu.WxMenuButton button31 = new WxMenu.WxMenuButton();
        button31.setType(WxConsts.BUTTON_VIEW);
        button31.setName("体检登记");
        button31.setUrl(wxMpService.oauth2buildAuthorizationUrl(url+"/wap/wapNotice.do", "snsapi_base", null));

//        WxMenu.WxMenuButton button32 = new WxMenu.WxMenuButton();
//        button32.setType(WxConsts.BUTTON_VIEW);
//        button32.setName("体检缴费");
//        button32.setUrl(wxMpService.oauth2buildAuthorizationUrl(url+"/wap/wapFee.do", "snsapi_base", null));

        WxMenu.WxMenuButton button33 = new WxMenu.WxMenuButton();
        button33.setType(WxConsts.BUTTON_VIEW);
        button33.setName("体检进程");
        button33.setUrl(wxMpService.oauth2buildAuthorizationUrl(url+"/wap/wapProgress.do", "snsapi_base", null));

        WxMenu.WxMenuButton button34 = new WxMenu.WxMenuButton();
        button34.setType(WxConsts.BUTTON_VIEW);
        button34.setName("电子健康证");
        button34.setUrl(wxMpService.oauth2buildAuthorizationUrl(url+"/wap/wapCard.do", "snsapi_base", null));

//        WxMenu.WxMenuButton button35 = new WxMenu.WxMenuButton();
//        button35.setType(WxConsts.BUTTON_VIEW);
//        button35.setName("常见问题");
//        button35.setUrl(wxMpService.oauth2buildAuthorizationUrl(url+"/wap/wapQuestion.do", "snsapi_base", null));

        button3.getSubButtons().add(button31);
//        button3.getSubButtons().add(button32);
        button3.getSubButtons().add(button33);
        button3.getSubButtons().add(button34);
//        button3.getSubButtons().add(button35);

        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);

        return menu;
    }

    /**
     * 运行此main函数即可生成公众号自定义菜单
     *
     * @param args
     */
    public static void main(String[] args) {
        MainConfig mainConfig = new MainConfig();
        WxMpService wxMpService = mainConfig.wxMpService();
        try {
            wxMpService.menuCreate(getMenu());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

}
