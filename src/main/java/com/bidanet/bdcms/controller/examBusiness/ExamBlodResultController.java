package com.bidanet.bdcms.controller.examBusiness;/**
 * Created by CF on 2016/11/10.
 */

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamBlod;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamResultDescription;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamBlodService;
import com.bidanet.bdcms.service.examBusiness.ExamResultDescriptionService;
import com.bidanet.bdcms.vo.Page;
import com.bidanet.bdcms.vo.ValueLabel;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 血检结果录入.
 * 类详细说明.
 * Copyright: Copyright (c) 16-11-10 15:17
 * Company: 苏州必答网络科技有限公司
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("examBlodResult")
@RequestMapping("/admin/examBusiness/examBlodResult")
public class ExamBlodResultController extends AdminBaseController{

    @Autowired
    private ExamBlodService blodService;
    @Autowired
    private ExamBlodIntestinalService blodIntestinalService;
    @Autowired
    private ExamResultDescriptionService examResultDescriptionService;

    /**
     * 血检结果录入页面
     * @param page
     * @param model
     */
    @RequestMapping("/index")
    public void index(
            String beginTime,String endTime,Integer entryState,
            Long areaId,Long packageId,String examNumber,
            @ModelAttribute Page<ExamBlod> page,
            Model model
        ) throws ParseException {
        blodService.findBlodMember(page,beginTime,endTime,entryState,areaId,packageId,examNumber);

        // 获取列表页面首个血检信息的血检结果信息
        if(page.getList()!=null&&page.getList().size()>0){
            List<ExamBlodIntestinal> blodIntestinalList =
                    findSelectedBlodDetail(page.getList().get(0).getId());
            model.addAttribute("blodIntestinalList",blodIntestinalList);
        }

        model.addAttribute("beginTime",beginTime);
        model.addAttribute("endTime",endTime);
        model.addAttribute("entryState",entryState);
        model.addAttribute("areaId",areaId);
        model.addAttribute("packageId",packageId);
        model.addAttribute("examNumber",examNumber);
    }

    /**
     * 获取选定的血检信息的血检结果信息
     * @param blodId
     * @return
     */
    @RequestMapping("/findSelectedBlodDetail")
    @ResponseBody
    public List<ExamBlodIntestinal> findSelectedBlodDetail(Long blodId){
        List<ExamBlodIntestinal> blodIntestinalList =
                blodIntestinalService.findSelectedBlodDetail(blodId);
        return blodIntestinalList;
    }

    /**
     * 跳转至录入血检结果信息页面
     * @param id
     */
    @RequestMapping("/toEntryblodResult")
    public void toEntryblodResult(Long id,Model model){
        ExamBlod blod = blodService.get(id);
        model.addAttribute("blodId",id);
        model.addAttribute("blod",blod);
        List<ExamBlodIntestinal> blodIntestinalList =
                findSelectedBlodDetail(id);
        model.addAttribute("blodIntestinalList",blodIntestinalList);
    }

//    /**
//     * 录入血检结果信息
//     * @return
//     */
//    @RequestMapping("/entryBlodResult")
//    @ResponseBody
//    public AjaxCallBack entryBlodResult(Long blodId,String isQualified5,String isQualified7,String isQualified8,
//                                        String blodResult5,String blodResult7,String blodResult8,
//                                        String isRecheck5,String isRecheck7,String isRecheck8,
//                                        String descriptionVal){
//        User user = uc.getUser();
//        blodIntestinalService.entryBlodResultT(blodId,isQualified5,isQualified7,
//                isQualified8,blodResult5,blodResult7,blodResult8,isRecheck5,
//                isRecheck7,isRecheck8,descriptionVal,user.getRealName());
//        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
//        return ajaxCallBack;
//    }

    /**
     * 录入血检结果信息
     * @return
     */
    @RequestMapping("/entryBlodResult")
    @ResponseBody
    public AjaxCallBack entryBlodResult(Long blodId,String isQualified5,String isQualified7,String isQualified8,
                                        String blodResult5,String blodResult7,String blodResult8){
        User user = uc.getUser();
        blodIntestinalService.entryBlodResultSecondT(blodId,isQualified5,isQualified7,
                isQualified8,blodResult5,blodResult7,blodResult8,user.getRealName());
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

//    /**
//     * 设置选中的血检信息对应的血检结果都设为合格（原录入的不合格项保留）
//     * @param blodIds
//     * @return
//     */
//    @RequestMapping("/defaultSelectedResult")
//    @ResponseBody
//    public AjaxCallBack defaultSelectedResult(String blodIds){
////        if(StringUtils.isBlank(blodIds)){
////            errorMsg("请至少选择一项再操作！");
////        }
//        String [] ids = blodIds.split(",");
//        blodIntestinalService.defaultSelectedResultT(ids);
//        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
//        return ajaxCallBack;
//    }

    /**
     * 设置筛选范围内的血检信息对应的血检结果都设为合格（原录入的不合格项保留）
     * @return
     */
    @RequestMapping("/defaultSelectedResult")
    @ResponseBody
    public AjaxCallBack defaultSelectedResult(String beginTime,String endTime,Integer entryState,
                                              Long areaId,Long packageId,String examNumber,String type) throws ParseException {
        User loginUser = uc.getUser();
        blodIntestinalService.defaultSelectedResultByConditionT(beginTime,endTime,entryState,areaId,packageId,examNumber,type,loginUser);
        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
        return ajaxCallBack;
    }

    /**
     * 进入右侧体检结果描述页面
     * @param id
     * @param model
     */
    @RequestMapping("/toEntryBlodResultRight")
    public void toEntryBlodResultRight(Long id,Model model){
        //体检结果表id
        ExamBlodIntestinal result = blodIntestinalService.get(id);
        model.addAttribute("id",id);
        model.addAttribute("result",result);
    }

    /**
     * 自动完成根据点击当前行的合格状态返回的对应的体检结果描述用语
     * @param id
     * @return
     */
    @RequestMapping("/findBlodDescription")
    @ResponseBody
    public List<ValueLabel> findBlodDescription(Long id){
        List<ValueLabel> list = new ArrayList<ValueLabel>();
        ExamBlodIntestinal result = blodIntestinalService.get(id);
        ExamResultDescription resultDescription = new ExamResultDescription();
        resultDescription.setType(result.getIsQualified());
        resultDescription.setProjectId(result.getProjectId());
        List<ExamResultDescription> resultDescriptionList = examResultDescriptionService.findByExample(resultDescription);
        if(resultDescriptionList!=null&&resultDescriptionList.size()>0){
            for(ExamResultDescription rd:resultDescriptionList){
                list.add(new ValueLabel(String.valueOf(rd.getId()),rd.getDescription()));
            }
        }else{
            return null;
        }
        return list;
    }

    @RequestMapping("/toJoinBlod")
    public void toJoinBlod(String description,String descriptionId,String projectId,Model model) throws UnsupportedEncodingException {
        description= URLDecoder.decode(description,"UTF-8");
        model.addAttribute("descriptionId",descriptionId);
        model.addAttribute("description",description);
        model.addAttribute("projectId",projectId);
    }

    @RequestMapping("/joinBlod")
    @ResponseBody
    public AjaxCallBack joinBlod(String description,String descriptionId,String projectId){
        // TODO 新增要根据isQualified修改
        examResultDescriptionService.joinBlodT(description,descriptionId,projectId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    @RequestMapping("/toUpBlod")
    public void toUpBlod(String projectId,Model model){
       model.addAttribute("projectId",Long.valueOf(projectId));
    }

    @RequestMapping("/upBlod")
    @ResponseBody
    public AjaxCallBack upBlod(String updescriptionId,String updescription){
        examResultDescriptionService.upMedicalT(updescriptionId,updescription);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     * 设置选中的体检信息对应的结果都设为合格(体检结果判断用，以最后一次操作为准)
     * @param resultIds
     * @return
     */
    @RequestMapping("/defaultSelectedResultForResultDecide")
    @ResponseBody
    public AjaxCallBack defaultSelectedResultForResultDecide(String resultIds){
        if(StringUtils.isBlank(resultIds)){
            errorMsg("请至少选择一项再操作！");
        }
        String [] ids = resultIds.split(",");
        blodIntestinalService.defaultSelectedResultForResultDecideT(ids);
        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
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
        examResultDescriptionService.upExamResultT(upIdLength,updescription,upId,loginUser);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     *
     * 检测血检肠检是否已采集过
     * @return
     */
    @RequestMapping("/checkIsCollect")
    @ResponseBody
    public Map<String,Object> checkIsCollect(String examNumber,String type){
        Map<String,Object> map = new HashMap<String,Object>();
        blodIntestinalService.checkIsCollectT(examNumber,type,map);
        return map;
    }

}
