package com.bidanet.bdcms.controller.examManage;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.examBusiness.*;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.vo.Page;
import com.bidanet.bdcms.vo.ValueLabel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 结果判断. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-02 09:27:37
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Controller("examResult")
@RequestMapping ("/admin/examManage/examResult")
public class ExamResultController extends AdminBaseController{
    @Autowired
    private ExamMemberExamService memberExamService;
    @Autowired
    private ExamBlodIntestinalService blodIntestinalService;
    @Autowired
    private ExamMemberService memberService;
    @Autowired
    private ExamResultDescriptionService resultDescriptionService;
    @Autowired
    private ExamAgenciesService agenciesService;

    /**
     * 体检结果判断页面
     * @param page
     * @param model
     */
    @RequestMapping("/index")
    public void index(
            String examNumberScan,
            String memberName,String idNum,
            String startTime,String endTime,Integer payState,
            String areaId,Integer isRecheck,Integer verifyConclusion,
            String examNumber,Long isErQuery,Model model,
            @ModelAttribute Page<ExamMemberExam> page
            ) throws ParseException {
            if(StringUtils.isNotBlank(examNumberScan)){
              examNumber = examNumberScan;
            }
//        if (isErQuery!=null) {
            User user = uc.getUser();

            if(areaId == null){
                areaId = user.getAreaId().toString();
            }
            ExamAgencies agencies = agenciesService.get(user.getAgenciesId());
//            memberExamService.findExamMemberResultDecide(page,memberName,idNum,startTime,endTime,payState,areaId,
//                    isRecheck,verifyConclusion,examNumber,agencies);
            if(StringUtils.isNotEmpty(examNumberScan)||StringUtils.isNotEmpty(idNum)||StringUtils.isNotEmpty(startTime)||StringUtils.isNotEmpty(endTime)||
                    payState!=null||isRecheck!=null||verifyConclusion!=null||StringUtils.isNotEmpty(examNumber)){
                        memberExamService.findExamMemberResultDecide(page,memberName,idNum,startTime,endTime,payState,areaId,
                        isRecheck,verifyConclusion,examNumber,agencies);
                        if(page.getList()!=null&&page.getList().size()>0){
                            List<ExamBlodIntestinal> resultList =
                                    findSelectedResultDetail(page.getList().get(0).getExamNumber());
                                model.addAttribute("resultList",resultList);
                }
            }else{//打开页面时默认筛选当天的日期。
                Date d = new Date();
                //System.out.println(d);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNowStr = sdf.format(d);
                //startTime = "2017-04-22";
                startTime = dateNowStr;
                endTime = dateNowStr;
                memberExamService.findExamMemberResultDecide(page,memberName,idNum,startTime,endTime,payState,areaId,
                        isRecheck,verifyConclusion,examNumber,agencies);
            }

            if(agencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)){
                model.addAttribute("loginUserRole",user);
            }

            model.addAttribute("currentAreaId",areaId);
            model.addAttribute("currentAgenciesCode",user.getAgenciesCode());
            model.addAttribute("memberName",memberName);
            model.addAttribute("idNum",idNum);
            model.addAttribute("startTime",startTime);
            model.addAttribute("endTime",endTime);
            model.addAttribute("payState",payState);
            model.addAttribute("areaId",areaId);
            model.addAttribute("isRecheck",isRecheck);
            model.addAttribute("verifyConclusion",verifyConclusion);
            model.addAttribute("examNumber",examNumber);
            model.addAttribute("examNumberScan",examNumberScan);
//        }
    }

//    /**
//     * 判断是否只有肠检未判断
//     */
//    private boolean isOnlyIntestinal (ExamBlodIntestinal list){
//        long id = list.getMemberId();
//        List<ExamBlodIntestinal> list1 = memberExamService.findEveryBlodIn(id);//6项
//        ExamBlodIntestinal temp = new ExamBlodIntestinal();
//        for(int i=0;i<list1.size();i++){
//            temp = list1.get(i);
//            if(temp.getProjectId()==6){
//                continue;
//            }else if(temp.getIsQualified()==0){
//                return false;
//            }
//
//        }
//        return true;
//   }
//    /**
//     * 移除只有肠检未判断的
//     */
//    private List<ExamBlodIntestinal> removeOnly(List <ExamBlodIntestinal> resultlist){
//        List<ExamBlodIntestinal> temp = new ArrayList<ExamBlodIntestinal>();
//        for(int i=0;i<resultlist.size();i++){
//            if(!isOnlyIntestinal(resultlist.get(i))){
//                temp.add(resultlist.get(i));
//            }
//        }
//        return temp;
//
//    }


    /**
     * 查找选定的体检用户体检结果详情
     * @param examNumber
     * @return
     */
    @RequestMapping("/findSelectedResultDetail")
    @ResponseBody
    public List<ExamBlodIntestinal> findSelectedResultDetail(String examNumber){
        List<ExamBlodIntestinal> resultList =
                blodIntestinalService.findSelectedResultDetail(examNumber);
        return resultList;
    }

    /**
     * 查找选定的体检用户体检结果详情(返回map保存登录用户)
     * @param examNumber
     * @return
     */
    @RequestMapping("/findSelectedResultDetailBackMap")
    @ResponseBody
    public Map<String,Object> findSelectedResultDetailBackMap(String examNumber){
        Map<String,Object> map = new HashMap<String,Object>();
        User user = uc.getUser();
        ExamAgencies agencies = agenciesService.get(user.getAgenciesId());
        if(agencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)){
            map.put("loginUserRole",user);
        }
        List<ExamBlodIntestinal> resultList =
                blodIntestinalService.findSelectedResultDetailBackMap(examNumber,map);
        return map;
    }

    /**
     * 跳转到结果判断页面左侧弹框页
     * @param id
     * @param model
     */
    @RequestMapping("/toEntryResult")
    public void toEntryResult(Long id,Model model){
        ExamMemberExam memberExam = memberExamService.get(id);
        ExamMember member = memberService.get(memberExam.getMemberId());
        model.addAttribute("memberExam",memberExam);
        model.addAttribute("member",member);
    }

    /**
     * 左侧弹框页点击不合格进入录入页面
     * @param model
     */
    @RequestMapping("/toEntryResultDecide")
    public void toEntryResultDecide(String examNumber,Model model){
        List<ExamBlodIntestinal> resultList = findSelectedResultDetail(examNumber);
        model.addAttribute("resultList",resultList);
    }

    /**
     * 录入体检结果数据
     * @param results
     * @return
     */
    @RequestMapping("/entryResultDecide")
    @ResponseBody
    public AjaxCallBack entryResultDecide(String results){
 //       List<Object> blodIntestinalList = JsonParseTool.parseArray(results,"转换失败！");
        org.json.JSONObject jsonObject = new org.json.JSONObject(results);
        blodIntestinalService.entryResultDecideT(jsonObject,uc.getUser().getRealName());
        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
        return ajaxCallBack;
    }

    /**
     * 右侧体检描述用语页面
     * @param id
     * @param model
     */
    @RequestMapping("/toEntryResultRight")
    public void toEntryResultRight(String id,Model model){
        ExamBlodIntestinal result = blodIntestinalService.get(Long.valueOf(id));
        model.addAttribute("result",result);
    }

    @RequestMapping("/toJoinResult")
    public void toJoinResult(String description,String resultDescriptionId,String projectId,Model model) throws UnsupportedEncodingException {
        description= URLDecoder.decode(description,"UTF-8");
        model.addAttribute("description",description);
        model.addAttribute("resultDescriptionId",resultDescriptionId);
        model.addAttribute("projectId",projectId);
    }

    @RequestMapping("/joinResult")
    @ResponseBody
    public AjaxCallBack joinResult(String description,String resultDescriptionId,String projectId){
        resultDescriptionService.joinBlodT(description,resultDescriptionId,projectId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    @RequestMapping("/toUpResult")
    public void toUpResult(String projectId,Model model){
        model.addAttribute("projectId",Long.valueOf(projectId));
    }

    @RequestMapping("/upResult")
    @ResponseBody
    public AjaxCallBack upResult(String updescriptionId,String updescription){
        resultDescriptionService.upMedicalT(updescriptionId,updescription);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     * 查找当前项目对应的体检描述用语
     * @param id
     * @return
     */
    @RequestMapping("/findResultDescription")
    @ResponseBody
    public List<ValueLabel> findResultDescription(String id){
        ExamBlodIntestinal result = blodIntestinalService.get(Long.valueOf(id));
        List<ValueLabel> list = new ArrayList<ValueLabel>();
        ExamResultDescription resultDescription = new ExamResultDescription();
        //TODO ProjectId没有定义常量
        resultDescription.setProjectId(result.getProjectId());
        if(result.getIsQualified()==1L){
            resultDescription.setType(2);
        }else if(result.getIsQualified()==2L){
            resultDescription.setType(1);
        }
        List<ExamResultDescription> resultDescriptionList = resultDescriptionService.findByExample(resultDescription);
        if(resultDescriptionList!=null&&resultDescriptionList.size()>0){
            for(ExamResultDescription rd:resultDescriptionList){
                list.add(new ValueLabel(String.valueOf(rd.getId()),rd.getDescription()));
            }
        }else{
            return null;
        }
        return list;
    }

    /**
     * 批量审核
     * @param resultIds
     * @return
     */
   @RequestMapping("/defaultSelectedCheckBoxResultDecide")
    @ResponseBody
    public AjaxCallBack defaultSelectedCheckBoxResultDecide(String resultIds){
       User loginUser=uc.getUser();
        if(StringUtils.isBlank(resultIds)){
            errorMsg("请至少选择一项再操作");
        }
        String [] ids = resultIds.split(",");
        blodIntestinalService.defaultSelectedResultDecide(ids,loginUser);
        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
        return ajaxCallBack;
    }

    /**
     * 设置筛选范围内的体检结果都设为合格
     * @param memberName
     * @param idNum
     * @param startTime
     * @param endTime
     * @param payState
     * @param areaId
     * @param isRecheck
     * @param verifyConclusion
     * @param examNumber
     * @return
     */
    @RequestMapping("/defaultSelectedResultDecide")
    @ResponseBody
    public AjaxCallBack defaultSelectedResultDecide(String memberName,String idNum,
                                                    String startTime,String endTime,Integer payState,
                                                    String areaId,Integer isRecheck,Integer verifyConclusion,
                                                    String examNumber) throws ParseException {
        User loginUser = uc.getUser();
        blodIntestinalService.defaultSelectedResultDecideT(memberName,idNum,startTime,endTime,payState,areaId,isRecheck,verifyConclusion,examNumber,loginUser);
        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
        return ajaxCallBack;
    }

    /**
     * 录入右侧体检结果数据(乡镇结果判断用)
     * @param results
     * @return
     */
    @RequestMapping("/saveRightResultDecide")
    @ResponseBody
    public AjaxCallBack saveRightResultDecide(String results,String examMemberId){
//        ExamBlodIntestinal blodIntestinalResult = new ExamBlodIntestinal();
//        blodIntestinalResult.setMemberExamId(Long.valueOf(examMemberId));
//        List<ExamBlodIntestinal> blodIntestinalResultList = blodIntestinalService.findByExample(blodIntestinalResult);
//        boolean f = false;
//        if(blodIntestinalResultList!=null&&blodIntestinalResultList.size()>0){
//            for(ExamBlodIntestinal result:blodIntestinalResultList){
//                if(result.getProjectId().equals(SystemContent.PROJECT_HEV)||result.getProjectId().equals(SystemContent.PROJECT_ALT)||result.getProjectId().equals(SystemContent.PROJECT_HAV)){
//                    f=true;
//                }
//
//                if(f){
//                    ExamBlod blod = new ExamBlod();
//                    blod.setMemberExamId(Long.valueOf(examMemberId));
//                    List<ExamBlod> blodList = blodService.findByExample(blod);
//                    if (blodList.size() <= 0) {
//                        errorMsg("血检还未采集！");
//                    }
//                }
//            }
//        }else{
//            errorMsg("没有任何项目需要保存！");
//        }
        if(StringUtils.isBlank(examMemberId)){
            errorMsg("没有要保存的体检信息！");
        }
        org.json.JSONObject jsonObject = new org.json.JSONObject(results);
        blodIntestinalService.saveRightResultDecideT(jsonObject,uc.getUser().getRealName());
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        return ajaxCallBack;
    }

    /**
     * 结果判断导出
     * @param memberName
     * @param idNum
     * @param startTime
     * @param endTime
     * @param payState
     * @param areaId
     * @param isRecheck
     * @param verifyConclusion
     * @param examNumber
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/exportDecideExcel")
    @ResponseBody
    public FileOutput exportDecideExcel(int pageCurrent, int pageSize,String memberName,String idNum,
                                        String startTime,String endTime,Integer payState,
                                        String areaId,Integer isRecheck,Integer verifyConclusion,
                                        String examNumber) throws IOException, ParseException {
        User user = uc.getUser();
        ExamAgencies agencies = agenciesService.get(user.getAgenciesId());
        HSSFWorkbook hw = memberExamService.exportDecideExcel(pageCurrent,pageSize,memberName,idNum,
                startTime,endTime,payState,areaId,isRecheck,verifyConclusion,examNumber,agencies);
        String path = SpringWebTool.getRealPath("/temp/");
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String  downloadTime= DateTool.timeToStrYmd(new Date().getTime());
        String filename = downloadTime + "-resultDecide.xls";
        File file = new File(path + "/" + filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream stream = new FileOutputStream(file);

        hw.write(stream);
        stream.flush();
        stream.close();
        return new FileOutput(file, filename);
    }

    /**
     * 所有项目判断为合格(除肠检外)
     * @param examMemberId
     * @return
     */
    @RequestMapping(value="/toDecideQualified")
    @ResponseBody
    public AjaxCallBack toDecideQualified(Long examMemberId){
        User loginUser = uc.getUser();
        blodIntestinalService.decideQualifiedT(examMemberId,loginUser);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        return ajaxCallBack;
    }

}
