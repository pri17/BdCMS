package com.bidanet.bdcms.controller.wxWap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.common.ConfigInfo;
import com.bidanet.bdcms.common.ExamPackageProjectTool;
import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.BaseController;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.plugin.wechat.service.CoreService;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryService;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberService;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.fee.FeeService;
import com.bidanet.bdcms.service.systemSetting.ExamWxBQuestionService;
import com.bidanet.bdcms.service.systemSetting.IndexImageService;
import com.bidanet.bdcms.service.wap.ExamWxBindService;
import com.bidanet.bdcms.service.wap.WapService;
import com.bidanet.bdcms.util.XMLUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

//import com.bidanet.bdcms.controller.home.HomeBaseController;

/**
 * 微信接口.
 *
 * Copyright: Copyright (c) 2016-12-12 16:50:06
 * Company: 苏州必答网络科技有限公司
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
@Controller
@RequestMapping("/wap")
public class WeChatViewController extends BaseController{

    @Autowired
    private CoreService coreService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WapService wapService;
    @Autowired
    private ExamCategoryService examCategoryService;
    @Autowired
    private ExamPackageService examPackageService;
    @Autowired
    private ExamMemberService examMemberService;
    @Autowired
    private ExamMemberExamService examMemberExamService;
    @Autowired
    private ExamBlodIntestinalService examBlodIntestinalService;
    @Autowired
    private ExamWxBindService examWxBindService;
    @Autowired
    private ExamWxBQuestionService examWxBQuestionService;
    @Autowired
    private ExamEcardService examEcardService;
    @Autowired
    private ExamPayService examPayService;
    @Autowired
    private IndexImageService indexImageService;

    @Autowired
    private ConfigInfo configInfo;



    @RequestMapping("/disease")
    public void disease(Model model)throws WxErrorException,IOException{
        List<ExamIndexImage>examIndexImageList=indexImageService.getList();
        for(int i=0;i<examIndexImageList.size();i++){
            ExamIndexImage eII=examIndexImageList.get(i);
            if("体检登记".equals(eII.getTitle())){
                model.addAttribute("medicalRegistration",eII.getUrl());
            }
            if("体检缴费".equals(eII.getTitle())){
                model.addAttribute("medicalPayment",eII.getUrl());
            }
            if("复检通知".equals(eII.getTitle())){
                model.addAttribute("reExaminationNotice",eII.getUrl());
            }
            if("体检进程".equals(eII.getTitle())){
                model.addAttribute("physicalProcess",eII.getUrl());
            }
            if("电子健康".equals(eII.getTitle())){
                model.addAttribute("electronicHealth",eII.getUrl());
            }
            if("首页大图".equals(eII.getTitle())){
                model.addAttribute("indexBigImage",eII.getUrl());
            }
        }


    }

    @Autowired
    private FeeService feeService;

    //跳转从业人员须知页面
    @RequestMapping("/wapNotice")
    public void wapNotice(Model model) throws WxErrorException,IOException {
        //通过页面授权，获取code，然后获取accessToken,最后取到openId
//        String code = SpringWebTool.getRequest().getParameter("code");
//        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        //将openId传递到体检登录页面
        model.addAttribute("openId",getOpenId());
    }

    //跳转体检登记页面
    @RequestMapping("/wapAdd")
    public void wapAdd(Model model, String openId) throws WxErrorException,IOException {
        model.addAttribute("openId",getOpenId());
    }

    /**
     * 根据行业类别，查询套餐
     * @param pid
     * @return
     */
    @RequestMapping("/getCategoryByPid")
    @ResponseBody
    public Map<String, Object> getCategoryByPid(Long pid){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ExamCategory> categoryList = examCategoryService.getExamCategoryByPid(2,pid);
        List<ExamCategoryPackage> examCategoryPackageList = examCategoryService.getExamCategoryPackageByPid(pid);
        if (examCategoryPackageList.size()>0) {
            ExamCategoryPackage examCategoryPackage = examCategoryPackageList.get(0);
            map.put("examCategoryPackage",examCategoryPackage);
            if (examCategoryPackage.getPackageId()!=null) {
                ExamPackage examPackage = new ExamPackage();
                examPackage = examPackageService.get(examCategoryPackage.getPackageId());
                map.put("examPackage",examPackage);
//                examPackage.setId(examCategoryPackage.getPackageId());
//                List<ExamPackage> examPackages = examPackageService.findByExample(examPackage);
//                if (examPackages.size()>0) {
//                    map.put("examPackage",examPackages.get(0));
//                }
            }
        }
        map.put("categoryList",categoryList);
        return map;
    }

    /**
     * 保存体检登记信息
     * @param sex,birthday,idCardNum,mobile,workUnit
     * @return
     */
    @RequestMapping("/addExamMember")
    @ResponseBody
    public Map<String, Object> addExamMember(String openId, int sex,int age,  String idCardNum, String company
            ,String phoneNum,Long categoryId,Long packageId,BigDecimal packagePrice,String birthday){
        Map<String,Object> map = new HashMap<String, Object>();
        openId=getOpenId();
        // 先通过身份证号及openId判断，该微信用户的账号是否绑定，并且该身份证绑定的是否是这个微信号
        ExamMember member = new ExamMember();
        member.setIdCardNum(idCardNum);
        List<ExamMember> examMembers = examMemberService.findByExample(member);
        if (examMembers.size()>0) {
            ExamMember examMember = examMembers.get(0);
            ExamWxBind wxBind = new ExamWxBind();
            wxBind.setMemberId(examMember.getId());
            List<ExamWxBind> examWxBinds = examWxBindService.findByExample(wxBind);
            if (examWxBinds.size()>0) {
                ExamWxBind examWxBind = examWxBinds.get(0);
                if (!examWxBind.getWxOpenId().equals(openId)) {
                    map.put("error","该用户已绑定其它微信账号！");
                    return map;
                } else {
                    // 查询用户是否有健康证，如果有，再判断有效期是否超过三个月
                    ExamEcard examEcard = new ExamEcard();
                    examEcard.setMemberId(examMember.getId());
                    List<ExamEcard> examEcardList = examEcardService.findByExampleOrderDesc(examEcard,"createTime");
                    if (examEcardList.size()>0) {
                        ExamEcard ecard = examEcardList.get(0);
                        Date dNow = new Date();   //当前时间
                        Date dBefore = new Date();
                        Calendar calendar = Calendar.getInstance(); //得到日历
                        calendar.setTime(dNow);//把当前时间赋给日历
                        calendar.add(calendar.MONTH, 3);  //设置为前3月
                        dBefore = calendar.getTime();   //得到前3月的时间
                        if (ecard.getExpTime()>dBefore.getTime()) {
                            //有效期超过三个月，提醒
                            map.put("unOverDueError","你当前健康证有效期超过三个月，是否作废并生成新的体检单？");
                            return map;
                        } else {
                            //有效期小于三个月，判断是否存在未完成的体检项目
                            // 更新用户信息,同时查询用户是否拥有未完成的体检单，有的话提示
                            ExamMemberExam examMemberExam = new ExamMemberExam();
                            examMemberExam.setMemberId(examMember.getId());
                            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExampleOrderDesc(examMemberExam,"createTime");
                            if (checkFinishExam(map, examMemberExamList,sex,age,idCardNum,company ,phoneNum,categoryId,packageId,packagePrice,birthday)) return map;
                        }
                    } else {
                        //没有旧的健康证，，判断是否存在未完成的体检项目
                        // 更新用户信息,同时查询用户是否拥有未完成的体检单，有的话提示
                        ExamMemberExam examMemberExam = new ExamMemberExam();
                        examMemberExam.setMemberId(examMember.getId());
                        List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExampleOrderDesc(examMemberExam,"createTime");
                        if (checkFinishExam(map, examMemberExamList,sex,age,idCardNum,company ,phoneNum,categoryId,packageId,packagePrice,birthday)) return map;
                    }
                }
            } else {
                ExamWxBind wxBind1 = new ExamWxBind();
                wxBind1.setWxOpenId(openId);
                List<ExamWxBind> binds = examWxBindService.findByExampleExact(wxBind1);
                if (binds.size()>0) {
                    map.put("error","该微信账号已绑定其它用户！");
                    return map;
                } else {
                    // 将微信号与身份证绑定
                    ExamWxBind examWxBind = new ExamWxBind();
                    examWxBind.setWxOpenId(openId);
                    examWxBind.setMemberId(examMember.getId());
                    examWxBindService.saveWxBind(examWxBind);
                    //并新增体检单
                    wapService.addExamTS(sex,age,idCardNum,company ,phoneNum,categoryId,packageId,packagePrice,birthday);
                    map.put("success","成功");
                }
            }
        } else {
            //该身份证未进行过体检，保存用户信息并新增体检单
            wapService.addNewExamMemberTS(openId,sex,age,idCardNum,company ,phoneNum,categoryId,packageId,packagePrice,birthday);
            map.put("success","成功");
        }
        return map;
    }

    private boolean checkFinishExam(Map<String, Object> map, List<ExamMemberExam> examMemberExamList,
                                    int sex,int age,String idCardNum,String company ,String phoneNum,
                                    Long categoryId,Long packageId,BigDecimal packagePrice,String birthday) {
        if (examMemberExamList.size()>0) {
            ExamMemberExam exam = examMemberExamList.get(0);
            if (exam.getVerifyConclusion() == 1) {
                //没有未完成项目，可以直接新增体检信息
                wapService.addExamTS(sex,age,idCardNum,company ,phoneNum,categoryId,packageId,packagePrice,birthday);
                map.put("success","成功");
            } else {
                //存在未完成项目，并加以提示
                map.put("unFinishError","存在未完成体检单，是否作废并生成新的体检单？");
                return true;
            }
        } else {
            //没有体检纪录，可以直接新增
            wapService.addExamTS(sex,age,idCardNum,company ,phoneNum,categoryId,packageId,packagePrice,birthday);
            map.put("success","成功");
        }
        return false;
    }

    /**
     * 保存体检登记信息
     * @param name,sex,birthday,address,idCardNum,mobile,workUnit
     * @return
     */
    @RequestMapping("/addExam")
    @ResponseBody
    public Map<String, Object> addExam(String name, int sex,int age, String address,  String idCardNum, String company
            ,String phoneNum,Long categoryId,Long packageId,BigDecimal packagePrice,String birthday) {
        Map<String, Object> map = new HashMap<String, Object>();
        wapService.addExamTS(sex,age,idCardNum,company ,phoneNum,categoryId,packageId,packagePrice,birthday);
        map.put("success","成功");
        return map;
    }

    //微信登记保存成功页面
    @RequestMapping("/wapSaveSuccess")
    public void wapSaveSuccess(Model model) throws IOException {
    }

    //跳转体检项目及体检费用页面
    @RequestMapping("/wapFee")
    public void wapFee(Model model) throws IOException {
        //Gcx 标识是否需要缴费
        Boolean tag=true;
        ExamWxBind examWxBind = new ExamWxBind();
        examWxBind.setWxOpenId(getOpenId());
        List<ExamWxBind> examWxBinds = examWxBindService.findByExample(examWxBind);
        if (examWxBinds.size()>0){
            ExamWxBind wxBind = examWxBinds.get(0);
            Long memberId = wxBind.getMemberId();
            ExamMember examMember = examMemberService.get(memberId);
            ExamMemberExam examMemberExam1 = new ExamMemberExam();
            examMemberExam1.setIsNew(1);
            examMemberExam1.setMemberId(memberId);
            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExampleOrderDesc(examMemberExam1, "id");
            if (examMemberExamList.size()>0){
//获取支付
                ExamMemberExam examMemberExam = examMemberExamList.get(0);

                ExamPay examPay = examPayService.get(examMemberExam.getPayId());
                if (examPay.getPayState()==1){
                    ExamBlodIntestinal intestinal = new ExamBlodIntestinal();
                    intestinal.setExamNumber(examMemberExam.getExamNumber());
                    intestinal.setIsNew(1);
                    intestinal.setIsQualified(0);

                    List<ExamBlodIntestinal> list = examBlodIntestinalService.findByExample(intestinal);
                    List<String> projectList = ExamPackageProjectTool.buildProjectByBlod(list);
                    if(projectList.size()>0){
                        tag=false;
                    }
                    model.addAttribute("projectList",projectList);

                    model.addAttribute("price",examPay.getPayMoney());
                    HashMap<String, String> payMap = new HashMap<>();
                    payMap.put("out_trade_no","pay"+examPay.getId());
//                    payMap.put("total_fee","1");
                    payMap.put("total_fee",""+(int)(examPay.getPayMoney().floatValue()*100));
                    payMap.put("device_info","WEB");

                    payMap.put("notify_url",SpringWebTool.getWebRootUrl()+"/wap/payReturn.do");
//                    payMap.put("notify_url","http://xuejike.xicp.net/wap/payReturn.do");
                    payMap.put("trade_type","JSAPI");
                    payMap.put("openid",getOpenId());
                    payMap.put("body","健康证体检缴费");
                    payMap.put("spbill_create_ip",SpringWebTool.getClientIp());
                    try {
                        Map<String, String> payInfo=new HashMap<>();
                        if (examPay.getWxPayData()==""||examPay.getWxPayData()==null){
                            payInfo = wxMpService.getPayInfo(payMap);
                            examPay.setWxPayData(JSON.toJSONString(payInfo));
                            examPayService.updateBack(examPay);
                            model.addAttribute("payInfo",payInfo);
                        }else{
                            JSONObject jsonObject = JsonParseTool.parseObject(examPay.getWxPayData(), "数据异常");
                            model.addAttribute("payInfo",jsonObject);
                        }


                    } catch (WxErrorException e) {
                        e.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    model.addAttribute("no_pay",false);
                    return;
                }else{
                    model.addAttribute("msg","体检已缴费");
                }

            }else{
                //没有体检记录
                model.addAttribute("msg","暂无体检项目");
            }


        }
        model.addAttribute("no_pay",true);
        model.addAttribute("tag",tag);
    }

    @RequestMapping("/payReturn")
    @ResponseBody
    public String payReturn(){
        try {

            Map<String, String> kvm = XMLUtil.parseRequestXmlToMap(SpringWebTool.getRequest());
            HttpServletResponse response = SpringWebTool.getResponse();
//            WxMpPayRefundResult refundPay = wxMpService.refundPay(map);
            if (kvm.get("result_code").equals("SUCCESS")) {
                String outTradeNo = kvm.get("out_trade_no");
                if (outTradeNo!=""){
                    String substring = outTradeNo.substring(3);
                    feeService.batchProcessT(substring,1,1L,"微信支付",4);
                }
                //TODO(user) 微信服务器通知此回调接口支付成功后，通知给业务系统做处理
//                    logger.info("out_trade_no: " + kvm.get("out_trade_no") + " pay SUCCESS!");
                response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[ok]]></return_msg></xml>");
            } else {
//                    this.logger.error("out_trade_no: "
//                            + kvm.get("out_trade_no") + " result_code is FAIL");
                response.getWriter().write(
                        "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[result_code is FAIL]]></return_msg></xml>");
            }



        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据身份证号套餐
     * @param idCardNum
     * @return
     */
    @RequestMapping("/getCategoryByIdCardNum")
    @ResponseBody
    public Map<String, Object> getCategoryByIdCardNum(String idCardNum){
        Map<String,Object> map = new HashMap<String, Object>();
        ExamMember examMember = new ExamMember();
        examMember.setIdCardNum(idCardNum);
        List<ExamMember> members = examMemberService.findByExample(examMember);
        if (members.size()>0) {
            ExamMember member = members.get(0);
            ExamMemberExam examMemberExam = new ExamMemberExam();
            examMemberExam.setMemberId(member.getId());
            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExample(examMemberExam);
            if (examMemberExamList.size()>0) {
                ExamMemberExam exam = examMemberExamList.get(examMemberExamList.size()-1);
                //第一次体检，可以查询到套餐id;
                // 如果是复检，没有套餐，根据exam_pay查询价格，根据exam_member_exam的id连表查询复检项目
                if (exam.getPackageId()!=null) {
                    ExamPackage examPackage = new ExamPackage();
                    examPackage.setId(examMemberExamList.get(examMemberExamList.size()-1).getPackageId());
                    List<ExamPackage> examPackages = examPackageService.findByExample(examPackage);
                    if (examPackages.size()>0) {
                        ExamPackage examPackage1 = examPackages.get(0);
                        List<ExamProject> examProjectList = examPackageService.getExamPackageProjectList(examPackage1.getId());
                        map.put("examProjectList",examProjectList);
                        map.put("money",examPackage1.getMoney());
                        map.put("isRecheck",0);//是否为复检项目 0、否 1、是
                    }
                } else {
                    ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                    examBlodIntestinal.setMemberExamId(exam.getId());
                    List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalService.findByExample(examBlodIntestinal);
                    BigDecimal totalPrice = new BigDecimal(0);
                    for (int i = 0; i < examBlodIntestinalList.size(); i++) {
                        totalPrice = totalPrice.add(examBlodIntestinalList.get(i).getExamProject().getProjectPrice());
                        map.put("money",totalPrice);
                    }
                    map.put("examProjectList",examBlodIntestinalList);
                    map.put("isRecheck",1);//是否为复检项目 0、否 1、是
                }

            }
        }
        return map;
    }

    //跳转体检进程提示框信息页面
    @RequestMapping("/blankPage")
    public void blankPage(){

    }

    //跳转体检进程页面
    @RequestMapping("/wapProgress")
    public ModelAndView wapProgress() throws IOException, WxErrorException, UnirestException {
        //通过页面授权，获取code，然后获取accessToken,最后取到openId
//        String code = SpringWebTool.getRequest().getParameter("code");
//        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        String openId = getOpenId();
//        String openId = "oiqYWwUNtx8_LpUndExYfMqMicc8";

        ModelAndView mv = new ModelAndView();
        mv.addObject("openId",openId);

        //wanglu 添加后台逻辑判断  如果openid 等于 "" 或者 openid 体检记录查询不到返回空白页面，并提示信息框
        if (StringUtils.isBlank(openId)){
            mv.setViewName("/wap/blankPage");
        }else{
            //如果存在。查询是否有数据

            String url = "http://jkz.cscdc.com/wap/queryWapProgress.do";

            HttpResponse<JsonNode> jsonResponse = Unirest.get(url).queryString("openId",openId).asJson();
            org.json.JSONObject jsonObject = jsonResponse.getBody().getObject();

            if(jsonObject.has("error")){
                String errorInfo = (String) jsonObject.get("error");
                mv.addObject("error", errorInfo);
                mv.setViewName("/wap/blankPage");
            }

        }

        return mv;

    }

    /**
     * 根据openId查询体检进程
     * @param openId
     * @return
     */
    @RequestMapping("/queryWapProgress")
    @ResponseBody
    public Map<String, Object> queryWapProgress(String openId) {
//         openId = "oiqYWwUNtx8_LpUndExYfMqMicc8";
        Map<String, Object> map = new HashMap<String, Object>();
        ExamWxBind examWxBind = new ExamWxBind();
        examWxBind.setWxOpenId(getOpenId());
        List<ExamWxBind> examWxBindList = examWxBindService.findByExample(examWxBind);
        if (examWxBindList.size()>0) {
            examWxBind = examWxBindList.get(0);
            ExamMemberExam examMemberExam = new ExamMemberExam();
            examMemberExam.setMemberId(examWxBind.getMemberId());
            examMemberExam.setIsNew(1);
            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExample(examMemberExam);
            if (examMemberExamList.size()>0) {
                ExamMemberExam exam = examMemberExamList.get(0);
                ExamPay examPay = new ExamPay();
                examPay.setExamNumber(exam.getExamNumber());
                List<ExamPay> examPayList = examPayService.findByExample(examPay);
                map.put("examPayList",examPayList);
//                map.put("examMemberExamList",examMemberExamList);
                ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                examBlodIntestinal.setExamNumber(exam.getExamNumber());
                List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalService.findByExample(examBlodIntestinal);
                if (examBlodIntestinalList.size()>0) {
                    map.put("examBlodIntestinalList",examBlodIntestinalList);


                    ExamMemberExam exam1 = new ExamMemberExam();
                    exam1.setExamNumber(exam.getExamNumber());
                    List<ExamMemberExam> exams = examMemberExamService.findByExample(exam1);

                    boolean[] reCheck=new boolean[exams.size()];
                    for (int i = 0; i < reCheck.length; i++) {
                        reCheck[i]=true;
                    }
                    for (ExamBlodIntestinal intestinal : examBlodIntestinalList) {
                        if (intestinal.getIsQualified()>0){
//                            reCheck[intestinal.getRecheckTag()]= reCheck[intestinal.getRecheckTag()];
                        }else{
                            reCheck[intestinal.getRecheckTag()]= false;
                        }
                    }
                    map.put("recheck",reCheck);
                    map.put("exams",exams);
                } else {
                    map.put("error","暂无体检信息");
                }
            } else {
                map.put("error","暂无体检信息");
            }
        } else {
            map.put("error","该微信暂无体检纪录");
        }
        return map;
    }

    /**
     * 根据exam_id查询blod_intestinal
     * @param examId
     * @return
     */
    @RequestMapping("/queryBlodByExamId")
    @ResponseBody
    public Map<String, Object> queryBlodByExamId(Long examId) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ExamBlodIntestinal> examBlodIntestinalList = wapService.getBlodByExamId(examId);
        map.put("examBlodIntestinalList",examBlodIntestinalList);
        return map;
    }

    //跳转复检单页面提示框信息页面
    @RequestMapping("/wapReviewBlankPage")
    public void wapReviewBlankPage(){

    }

    //跳转复检通知单页面
    @RequestMapping("/wapReviewNotice")
    public ModelAndView wapReviewNotice(String openId) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        //Gcx  判断页面是否又信息
        Boolean  tag=true;
        List<ExamWxBind> examWxBindList = wapService.getExamWxBindByOpenId(getOpenId());
        if (examWxBindList.size()>0) {
            ExamWxBind examWxBind = examWxBindList.get(0);
            ExamMemberExam exam = new ExamMemberExam();
            exam.setMemberId(examWxBind.getMemberId());
            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExampleOrderDesc(exam,"createTime");
            if (examMemberExamList.size()>0) {
                ExamMemberExam examMemberExam = examMemberExamList.get(0);

                if(examMemberExam != null){

                    ExamMemberExam query = new ExamMemberExam();
                    query.setExamNumber(examMemberExam.getExamNumber());

                    //如果流转信息是复检
                    // 由于因为复检标识混乱，因此通过体检号去判断最新的是否是复检
                    if(examMemberExamService.findByExample(query).size() > 1){

                        //如果体检不合格
                        if(examMemberExam.getVerifyConclusion() != 1){

                            ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                            examBlodIntestinal.setMemberExamId(examMemberExam.getId());
                            List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalService.findByExample(examBlodIntestinal);
                            //gcx
                            for(int i=0;i<examBlodIntestinalList.size();i++){
                                ExamBlodIntestinal ebi=new ExamBlodIntestinal();
                                ebi.setExamNumber(examBlodIntestinalList.get(i).getExamNumber());
                                ebi.setType(examBlodIntestinalList.get(i).getType());
                                List<ExamBlodIntestinal>ebiList=examBlodIntestinalService.query(ebi);
                                if(ebiList.size()>1){
                                    examBlodIntestinalList.get(i).setExamResult(ebiList.get(ebiList.size()-2).getExamResult());
                                }
                            }
                            if(examBlodIntestinalList.size()>0){
                                tag=false;
                            }
                            //gcx
                            modelAndView.addObject("examMemberExam",examMemberExamList.get(0));
                            modelAndView.addObject("examBlodIntestinalList",examBlodIntestinalList);
                        }
                    }


                }

            } else {
                modelAndView.addObject("examMemberExam","");
            }
        }

        if(tag){
            modelAndView.setViewName("/wap/wapReviewBlankPage");
        }
        return modelAndView;

    }

    //跳转健康证页面
    @RequestMapping("/wapCard")
    public void wapCard(Model model) throws IOException, WxErrorException {
        //通过页面授权，获取code，然后获取accessToken,最后取到openId
//        String code = SpringWebTool.getRequest().getParameter("code");
//        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        String openId = getOpenId();

//        String openId = "oiqYWwUNtx8_LpUndExYfMqMicc8";
        List<ExamWxBind> examWxBindList = wapService.getExamWxBindByOpenId(openId);
        if (examWxBindList.size()>0) {
            ExamWxBind examWxBind = examWxBindList.get(0);
            ExamEcard examEcard = new ExamEcard();
            examEcard.setMemberId(examWxBind.getMemberId());
            List<ExamEcard> examEcardList = examEcardService.findByExampleOrderDesc(examEcard,"createTime");
            if (examEcardList.size()>0) {
                model.addAttribute("examEcard",examEcardList.get(0));
            } else {
                model.addAttribute("examEcard","");
            }
        } else {
            model.addAttribute("examEcard","");
        }
    }

    //跳转健康证页面
    @RequestMapping("/showECard")
    public void showECard(String openId,Model model) throws IOException {
        //Gcx  标识页面是否又数据
        Integer tag=1;
        List<ExamWxBind> examWxBindList = wapService.getExamWxBindByOpenId(getOpenId());
        if (examWxBindList.size()>0) {
            ExamWxBind examWxBind = examWxBindList.get(0);
            ExamEcard examEcard = new ExamEcard();
            examEcard.setMemberId(examWxBind.getMemberId());
            List<ExamEcard> examEcardList = examEcardService.findByExampleOrderDesc(examEcard,"createTime");
            if (examEcardList.size()>0) {

                ExamEcard showExamEcard = examEcardList.get(0);


                showExamEcard.replaceImg(configInfo.getFileDomainWei());

                model.addAttribute("examEcard",showExamEcard);

                tag=0;
            } else {
                ExamMemberExam eme=new ExamMemberExam();
                eme.setMemberId(examWxBind.getMemberId());
                if(examMemberExamService.query(eme).size()>0){
                    tag=2;
                }
                model.addAttribute("examEcard","");
            }
        } else {
            model.addAttribute("examEcard","");
        }
        model.addAttribute("tag",tag);
    }

    @RequestMapping("/showQrEcard")
    public void showQrEcard(String uuid,Model model) throws IOException {

        ExamEcard eEc = new ExamEcard();
        ExamEcard  examEcard=new ExamEcard();
        eEc.setUuid(uuid);
        List<ExamEcard> examEcards=examEcardService.query(eEc);
        if(examEcards.size()>0){
            examEcard =examEcards.get(0);


            examEcard.replaceImg(configInfo.getFileDomainWei());
        }
        
        model.addAttribute("examEcard",examEcard);
    }

    //跳转常见问题页面
    @RequestMapping("/wapQuestion")
    public void wapQuestion(Model model) throws IOException {
        List<ExamWxBQuestion> examWxBQuestionList = examWxBQuestionService.getList();
        model.addAttribute("examWxBQuestionList",examWxBQuestionList);
    }

    //跳转支付成功页面
    @RequestMapping("/paySuccess")
    public void paySuccess() throws IOException {
    }

    //跳转支付失败页面
    @RequestMapping("/payError")
    public void payError() throws IOException {
    }

//    @RequestMapping("/weChatLogin")
//    public void weChatLogin() throws IOException {
//
//
//        MainConfig mainConfig = new MainConfig();
//        WxMpService wxMpService = mainConfig.wxMpService();
//
//        String weChatUrl = wxMpService.oauth2buildAuthorizationUrl(Constants.ROOT_URL + "/weChat/weChatLoginSave.do", WxConsts.OAUTH2_SCOPE_USER_INFO , null);
//
//        SpringWebTool.getResponse().sendRedirect(weChatUrl);
//    }
//
//    @RequestMapping("/weChatLoginSave")
//    public void weChatLoginSave() throws WxErrorException, IOException {
//
//        String openId = coreService.getOpenIdByCode(SpringWebTool.getRequest().getParameter("code"));
//
//        if(openId != null && !openId.equals("")){
//            SpringWebTool.getSession().setAttribute("openId",openId);
//
//            String url = (String) SpringWebTool.getSession().getAttribute("preUrlAddress");
//            SpringWebTool.getResponse().sendRedirect(url);
//        }
//    }


    public String getOpenId(){
        String wx_openId=SpringWebTool.getRequest().getParameter("openId");
        if (wx_openId==null||"".equals(wx_openId)){

            wx_openId = (String) SpringWebTool.getSession().getAttribute("wx_openId");
            if (wx_openId==null||"".equals(wx_openId)){



                String code = SpringWebTool.getRequest().getParameter("code");
                if (code==null||"".equals(code)){
                    String url = wxMpService.oauth2buildAuthorizationUrl("http://" + configInfo.getQrCodeAddress() + SpringWebTool.getRequest().getRequestURI(), "snsapi_base","wx");
                    SpringWebTool.redirectUrl(url);
                    errorMsg("--");
                }
                try {
                    WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
                    wx_openId = wxMpOAuth2AccessToken.getOpenId();
                } catch (WxErrorException e) {
//                    e.printStackTrace();

                }
            }
        }
        SpringWebTool.getSession().setAttribute("wx_openId",wx_openId);
        return wx_openId;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String errorHander(){
        return "";
    }
}
