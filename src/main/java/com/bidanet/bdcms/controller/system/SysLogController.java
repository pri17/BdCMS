package com.bidanet.bdcms.controller.system;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.SysLog;
import com.bidanet.bdcms.service.systemSetting.SysLogService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 体检机构管理Controller
 */
@Controller("sysLog")
@RequestMapping("/admin/systemSetting/sysLog")
public class SysLogController extends AdminBaseController {

    @Autowired
    private SysLogService sysLogService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") SysLog query,
            @ModelAttribute Page<SysLog> page,String tabid){
        sysLogService.getSysLogList(query,page);

        tableId = tabid;
    }

    /**
     * 跳转添加系统日志页面
     */
    @RequestMapping("/toAddSysLog")
    public void toAddSysLog(){
    }

    /**
     * 添加系统日志
     * @param sysLog
     * @return
     */
    @RequestMapping("/addSysLog")
    @ResponseBody
    public AjaxCallBack addSysLog(SysLog sysLog){
//        sysLog.setCreator(uc.getUser().getUsername());
//        sysLog.setUpdater(uc.getUser().getUsername());
        Date date = new Date();
        sysLog.setCreateTime(date.getTime());
        sysLog.setUpdateTime(date.getTime());
        sysLogService.addSysLogT(sysLog);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 跳转修改体检机构页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateSysLog")
    public void toUpdateSysLog(Long id,Model model){
        SysLog sysLog = sysLogService.get(id);
        model.addAttribute("sysLog",sysLog);
    }

    /**
     * 修改体检机构
     * @param sysLog
     * @return
     */
    @RequestMapping("/updateSysLog")
    @ResponseBody
    public AjaxCallBack updateSysLog(SysLog sysLog){
//        sysLog.setUpdater(uc.getUser().getUsername());
        Date date = new Date();
        sysLog.setUpdateTime(date.getTime());
        sysLogService.updateSysLogT(sysLog);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除系统日志
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxCallBack delete(Long id){
        sysLogService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }

}
