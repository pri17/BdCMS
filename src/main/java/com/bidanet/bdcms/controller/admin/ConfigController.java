package com.bidanet.bdcms.controller.admin;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.entity.Config;
import com.bidanet.bdcms.service.ConfigService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 配置管理Controller
 */
@Controller
@RequestMapping("/admin/config")
public class ConfigController extends AdminBaseController {
    @Autowired
    private ConfigService configService;

    /**
     *获取配置列表
     */
    @RequestMapping("/configList")
    public void messageList(@ModelAttribute("query") Config query,
                            @ModelAttribute Page<Config> page){
                configService.getPageByExample(query,page);
    }

   /* @RequestMapping("/getConfigList")
    @ResponseBody
    public Page getConfigList(Config config, DataTablePage dataTablePage){
        Page<Config>configPage=dataTablePage.getPage();
        return configPage;
    }*/

    /**
     *根据id删除配置
     */
    @RequestMapping("deleteCo")
    @ResponseBody
    public AjaxCallBack deleteCo(long id){
        configService.deleteT(id);
        return AjaxCallBack.deleteSuccess();
    }

    /**
     *新增配置
     */
    @RequestMapping("/toAddCo")
    public void toAddCo(Config config, Model model){
        model.addAttribute("vo",config);
    }

    @RequestMapping("/addConfig")
    @ResponseBody
    public AjaxCallBack addConfig(Config config){
        configService.addConfigT(config);
        return AjaxCallBack.addSuccess();
    }


    /**
     *更新配置
     */
    @RequestMapping("/toUpdateCo")
    public void toUpdateCo(long id,Model model){
        Config config=configService.get(id);
        model.addAttribute("vo",config);
    }
    @RequestMapping("/update")
    @ResponseBody
    public AjaxCallBack update(Config config){
        configService.updateT(config);
        return AjaxCallBack.saveSuccess();
    }



}
