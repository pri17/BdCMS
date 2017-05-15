package com.bidanet.bdcms.controller.examBusiness;/**
 * Created by CF on 2016/11/21.
 */

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamIntestinal;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamResultDescriptionService;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.List;

/**
 * 肠检录入.
 * 类详细说明.
 * Copyright: Copyright (c) 16-11-21 00:45
 * Company: 苏州必答网络科技有限公司
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("examIntestinalResult")
@RequestMapping("/admin/examBusiness/examIntestinalResult")
public class ExamIntestinalResultController extends AdminBaseController {

    @Autowired
    private ExamIntestinalService intestinalService;
    @Autowired
    private ExamBlodIntestinalService blodIntestinalService;
    @Autowired
    private ExamResultDescriptionService examResultDescriptionService;

    /**
     * 肠检结果录入页面
     * @param page
     * @param model
     */
    @RequestMapping("/index")
    public void index(
            String beginTime,String endTime,Integer entryState,
            Long areaId,Long packageId,String examNumber,
            @ModelAttribute Page<ExamIntestinal> page,
            Model model
    ) throws ParseException {

        intestinalService.findIntestinalMember(page,beginTime,endTime,entryState,areaId,packageId,examNumber);

        // 获取列表页面首个肠检信息的肠检结果信息
        if(page.getList()!=null&&page.getList().size()>0){
            List<ExamBlodIntestinal> blodIntestinalList =
                    findSelectedIntestinalDetail(page.getList().get(0).getId());
            model.addAttribute("blodIntestinalList",blodIntestinalList);
        }

        model.addAttribute("beginTime",beginTime);
        model.addAttribute("endTime",endTime);
        model.addAttribute("entryState",entryState);
        model.addAttribute("areaId",areaId);
        model.addAttribute("packageId",packageId);
        model.addAttribute("examNumber",examNumber);
        model.addAttribute("type", SystemContent.PROJECT_TYPE_CHANGDAO);

    }

    /**
     * 获取选定的肠检信息的肠检结果信息
     * @param intestinalId
     * @return
     */
    @RequestMapping("/findSelectedIntestinalDetail")
    @ResponseBody
    public List<ExamBlodIntestinal> findSelectedIntestinalDetail(Long intestinalId){
        List<ExamBlodIntestinal> blodIntestinalList =
                blodIntestinalService.findSelectedIntestinalDetail(intestinalId);
        return blodIntestinalList;
    }

    /**
     * 跳转至录入肠检结果信息页面
     * @param id
     */
    @RequestMapping("/toEntryIntestinalResult")
    public void toEntryIntestinalResult(Long id,Model model){
        ExamIntestinal intestinal = intestinalService.get(id);
        model.addAttribute("intestinalId",id);
        model.addAttribute("intestinal",intestinal);
        List<ExamBlodIntestinal> blodIntestinalList =
                findSelectedIntestinalDetail(id);
        model.addAttribute("blodIntestinalList",blodIntestinalList);
    }

//    @RequestMapping("/entryIntestinalResult")
//    @ResponseBody
//    public AjaxCallBack entryIntestinalResult(Long intestinalId, String isQualified6,String intestinalResult6,String isRecheck6){
//        User user = uc.getUser();
//        blodIntestinalService.entryIntestinalResultT(intestinalId,isQualified6,intestinalResult6,isRecheck6,user);
//        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
//        return ajaxCallBack;
//    }


    /**
     * 录入肠检结果信息
     * @param intestinalId
     * @param isQualified6
     * @param intestinalResult6
     * @return
     */
    @RequestMapping("/entryIntestinalResult")
    @ResponseBody
    public AjaxCallBack entryIntestinalResult(Long intestinalId, String isQualified6,String intestinalResult6){
        User user = uc.getUser();
        blodIntestinalService.entryIntestinalResultSecondT(intestinalId,isQualified6,intestinalResult6,user);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     * 设置选中的肠检信息对应的肠检结果都设为合格（原录入的不合格项保留）
     * @param intestinalIds
     * @return
     */
    @RequestMapping("/defaultSelectedResult")
    @ResponseBody
    public AjaxCallBack defaultSelectedResult(String intestinalIds){
        if(StringUtils.isBlank(intestinalIds)){
            errorMsg("请至少选择一项后再操作！");
        }
        String [] ids = intestinalIds.split(",");
        blodIntestinalService.defaultSelectedResultT(ids);
        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
        return ajaxCallBack;
    }

    @RequestMapping("/toEntryIntestinalResultRight")
    public void toEntryIntestinalResultRight(Long id,Model model){
        //体检结果表id
        ExamBlodIntestinal result = blodIntestinalService.get(id);
        model.addAttribute("id",id);
        model.addAttribute("result",result);
    }

//    @RequestMapping("/findIntestinalDescription")
//    @ResponseBody
//    public List<ValueLabel> findIntestinalDescription(String projectId){
//        List<ValueLabel> list = new ArrayList<ValueLabel>();
//        ExamResultDescription resultDescription = new ExamResultDescription();
//        //TODO ProjectId定义常量
//        resultDescription.setProjectId(Long.valueOf(projectId));
//        List<ExamResultDescription> resultDescriptionList = examResultDescriptionService.findByExample(resultDescription);
//        if(resultDescriptionList!=null&&resultDescriptionList.size()>0){
//            for(ExamResultDescription rd:resultDescriptionList){
//                ValueLabel vl = new ValueLabel(String.valueOf(rd.getId()),rd.getDescription());
//                list.add(vl);
//            }
//        }else{
//            return null;
//        }
//        return list;
//    }



    @RequestMapping("/toJoinIntestinal")
    public void toJoinIntestinal(String description,String descriptionId,String projectId,Model model) throws UnsupportedEncodingException {
        description = URLDecoder.decode(description, "UTF-8");
        model.addAttribute("description", description);
        model.addAttribute("descriptionId", descriptionId);
        model.addAttribute("projectId", projectId);
    }

    /**
     * 新增肠检项目描述用语
     * @param description
     * @param descriptionId
     * @param projectId
     * @return
     */
    @RequestMapping("/joinIntestinal")
    @ResponseBody
    public AjaxCallBack joinIntestinal(String description,String descriptionId,String projectId){
        examResultDescriptionService.joinBlodT(description,descriptionId,projectId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    @RequestMapping("/toUpIntestinal")
    public void toUpIntestinal(String projectId,Model model){
        model.addAttribute("projectId",Long.valueOf(projectId));
    }

    /**
     * 修改肠检项目描述用语
     * @param updescriptionId
     * @param updescription
     * @return
     */
    @RequestMapping("/upIntestinal")
    @ResponseBody
    public AjaxCallBack upIntestinal(String updescriptionId,String updescription){
        examResultDescriptionService.upMedicalT(updescriptionId,updescription);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     * 右侧体检结果描述修改页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpExamResult")
    public void toUpExamResult(Long id,Model model){
        //当前选择的体检项目结果ID
        ExamBlodIntestinal result = blodIntestinalService.get(id);
        model.addAttribute("id",id);
        model.addAttribute("result",result);
    }

    /**
     * 修改体检信息结果内容
     * @param upId  待修改的单个体检项目结果ID
     * @param updescription  待修改的判断结果
     * @return
     */
    @RequestMapping("/upExamResult")
    @ResponseBody
    public AjaxCallBack upExamResult(Long upId,String upIdLength,String updescription){
        User loginUser = uc.getUser();
        examResultDescriptionService.upExamResultT(upIdLength,updescription,upId, loginUser);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

}
