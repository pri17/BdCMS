package com.bidanet.bdcms.controller.system;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.examBusiness.ExamMemberService;
import com.bidanet.bdcms.service.systemSetting.WechatUnbindService;
import com.bidanet.bdcms.service.wap.ExamWxBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pri17 on 2017/5/9.
 * 微信解绑
 */
@Controller("wechatUnbind")
@RequestMapping("/admin/systemSetting/wechatUnbind")
public class WechatUnbindController extends AdminBaseController {

    @Autowired
    private WechatUnbindService wechatUnbindService;
    @Autowired
    private ExamMemberService examMemberService;

    private String tableId = "";

    /**
     * 点击菜单进入页面
     * @param model
     */
    @RequestMapping("/index")
    public void index(@ModelAttribute("query") WechatUnbind query,
            Model model, String tabid) {

        List<ExamWxBind> lists = wechatUnbindService.getAllwechatBindList();
        List<WechatUnbind> resultList = new ArrayList<>();
        if ((query.getName() == "" || query.getName() == null) && (query.getIdCard() == "" || query.getIdCard() == null)) {
            for (int i = 0; i < lists.size(); i++) {
                // ExamMember mem1 = new ExamMember();
                ExamMember mem2 = new ExamMember();
                // mem1.setId(lists.get(i).getMemberId());
                mem2 = examMemberService.findById(lists.get(i).getMemberId()).get(0);
                WechatUnbind unbind = new WechatUnbind();
                unbind.setId(lists.get(i).getMemberId());
                unbind.setName(mem2.getName());
                unbind.setIdCard(mem2.getIdCardNum());
                unbind.setOpenID(lists.get(i).getWxOpenId());
                unbind.setBindId(lists.get(i).getId());
                resultList.add(unbind);
            }
        model.addAttribute("wechatList", resultList);
    }else{
            List<ExamMember> memberList = examMemberService.findBynameId(query.getName(),query.getIdCard());
            System.out.println("memberList长度" + memberList.size());
           // for (int i = 0; i < lists.size(); i++) {
                for(int j=0;j<memberList.size();j++){
                    ExamWxBind temp = new ExamWxBind();
//                    temp.setMemberId(memberList.get(j).getId());
                    List<ExamWxBind> examWxBindList = wechatUnbindService.findByMemberId(memberList.get(j).getId());
                    if(examWxBindList.size()>0) {
                        temp = examWxBindList.get(0);
                        WechatUnbind wechatUnbind = new WechatUnbind();
                        wechatUnbind.setName(memberList.get(j).getName());
                        wechatUnbind.setIdCard(memberList.get(j).getIdCardNum());
                        wechatUnbind.setId(temp.getMemberId());
                        wechatUnbind.setOpenID(temp.getWxOpenId());
                        wechatUnbind.setBindId(temp.getId());
                        resultList.add(wechatUnbind);
                    }
                //}
            }
            model.addAttribute("wechatList", resultList);
        }
        tableId = tabid;

    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxCallBack delete(Long id){
        wechatUnbindService.deleteByIdT(id);
       // wechatUnbindService.deleteWechatBind(id);
        return AjaxCallBack.unbindSuccess();

    }
}
