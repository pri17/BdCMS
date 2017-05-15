package com.bidanet.bdcms.controller.machine;/**
 * Created by admin on 2016/12/12.
 */

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.ExamPackageProjectTool;
import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.driver.cache.RecheckEntity;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryService;
import com.bidanet.bdcms.service.businessSetting.ExamPackageProjectService;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.service.examBusiness.*;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.machine.MachineService;
import com.bidanet.bdcms.service.systemSetting.ExamProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

/**
 * 自助机接口.
 * <p>
 * Copyright: Copyright (c) 2016-12-12 16:50:06
 * Company: 苏州必答网络科技有限公司
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
@Controller
@RequestMapping("/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private ExamMemberExamService examMemberExamService;

    @Autowired
    private ExamPayService examPayService;

    @Autowired
    private ExamMemberService examMemberService;

    @Autowired
    private ExamEcardService examEcardService;

    @Autowired
    private ExamBlodIntestinalService examBlodIntestinalService;

    @Autowired
    private ExamProjectService examProjectService;

    @Autowired
    private ExamCategoryService examCategoryService;

    @Autowired
    private ExamPackageService examPackageService;
    @Autowired
    ExamPackageProjectService examPackageProjectService;

    @Autowired
    private ExamReadCardSetService examReadCardSetService;
    @Autowired
    private ExamAffectedService examAffectedService;

    //自助机主页
    @RequestMapping("/index")
    public void index(Model model) throws IOException {

       ExamReadCardSet examReadCardSet =  examReadCardSetService.get(1L);

        model.addAttribute("readCardSetTag",examReadCardSet.getTag());


    }

    //登记页面
    @RequestMapping("/updateReadCardSet")
    public void updateReadCardSet() {

        ExamReadCardSet examReadCardSet =  examReadCardSetService.get(1L);

        examReadCardSet.setTag(1);

        examReadCardSetService.updateBack(examReadCardSet);

    }


    //登记页面
    @RequestMapping("/registerExam")
    public void registerExam() {

    }

    /**
     * 保存体检登记信息
     *1、需要判断作废
     * 2、需要判断是否在90天有效期内
     * @param idCardNum,mobile,workUnit
     * @return
     * name: name,
     * sex: sex,
     * address: address,
     * idCardNum: idCardNum,
     * workUnit: workUnit,
     * phoneNum: phoneNum,
     * packageId: packageId,
     * birthday: birthday,
     * imageBase64: icon
     */
    @RequestMapping("/machineJudgeExamMember")
    @ResponseBody
    public Map<String, Object> machineJudgeExamMember(String idCardNum,String judgeTag) {
        Map<String, Object> map = new HashMap<String, Object>();
        //先根据身份证号去判断
        map = machineService.judgeExamMember(idCardNum,judgeTag);

        return map;
    }

    /**
     * 对已有的体检数据进行作废处理
     * @param name
     * @param sex
     * @param address
     * @param idCardNum
     * @param workUnit
     * @param mobile
     * @param categoryId
     * @param packageId
     * @param birthday
     * @param imageBase64
     * @param packagePrice
     * @return
     */
    @RequestMapping("/machineAddExamMember")
    @ResponseBody
    public Map<String, Object> addMachineExamMember(String name, String sex, String address, String idCardNum, String workUnit
            , String mobile, Long categoryId, Long packageId, String birthday, String imageBase64, String packagePrice) {
        Map<String, Object> map = new HashMap<String, Object>();
        //地区默认-疾控
        long d1=new Date().getTime();
        map = machineService.addExamTS(name, sex, address, idCardNum, workUnit, mobile, categoryId, packageId, birthday, imageBase64, packagePrice);
        System.out.println("-->"+(new Date().getTime()-d1));
        return map;
    }



    @RequestMapping("/printExamView")
    public void printExamView() {

    }


    ///////////////////////////体检单 复检单打印/////////////////////////////////////


    @RequestMapping("/printExam")
    public void printExam() {

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
//                ExamPackage examPackage = new ExamPackage();
//                examPackage.setId(examCategoryPackage.getPackageId());
                ExamPackage examPackage = examPackageService.get(examCategoryPackage.getPackageId());
                map.put("examPackage",examPackage);

            }
        }
        map.put("categoryList",categoryList);
        return map;
    }

    //直接根据身份证号码获取用户信息
    @RequestMapping ("/getMemberInfoByIdCardNumber")
    @ResponseBody
    public Map<String,String> getMemberInfoByIdCardNumber(String idCardNumber){

        Map<String, String> resultMap = new HashMap<>();

        ExamMember examMember = new ExamMember();

        if(StringUtils.isEmpty(idCardNumber)){

            resultMap.put("examMember","");

        }else{

            examMember.setIdCardNum(idCardNumber);

            List<ExamMember> memberList = examMemberService.findByExample(examMember);

            if (memberList.size()>0){

                examMember = memberList.get(0);

                resultMap.put("examMember",JsonParseTool.toJson(examMember));


            }else{
                resultMap.put("examMember","");
            }


        }

        return resultMap;

    }

    //根据身份证号码数据查找体检单 复检单
    @RequestMapping("/getExamPhysicalByIdCardNumber")
    @ResponseBody
    public Map<String, String> getExamPhysicalByIdCardNumber(String idCardNumber) {

        Map<String, String> resultMap = new HashMap<>();

        ExamMemberExam examMemberExam = new ExamMemberExam();

        if (StringUtils.isEmpty(idCardNumber)) {

            resultMap.put("errorMsg", "身份证信息获取失败,请重试");

        } else {

            //根据用户身份证获取最新的可打印的ecard数据
            ExamMember examMember = new ExamMember();

            examMember.setIdCardNum(idCardNumber);

            List<ExamMember> memberList = examMemberService.findByExample(examMember);
            if (memberList.size()>0){
                examMember = memberList.get(0);

                examMemberExam.setMemberId(examMember.getId());

                examMemberExam.setIsNew(1);

                List<ExamMemberExam> examMemberExamsList = examMemberExamService.findByExampleOrderDesc(examMemberExam, "id");

                //这条数据应该只有一条
                if (examMemberExamsList.size() > 0) {

                    ExamMemberExam examMemberExam1 = examMemberExamsList.get(0);

                    if (examMemberExam1.getVerifyConclusion() == 0) {//未判断

                        if (examMemberExam1.getIsRecheckPrint() == 1) {

                            //暂无可打印的健康证
                            resultMap.put("errorMsg", "您已打印过体检单,如需再次打印,请联系工作人员！");

                        } else {

                            resultMap.put("examMemberExam", JsonParseTool.toJson(examMemberExam1));


                        }

//                    //暂无可打印的健康证
//                    resultMap.put("errorMsg", "暂无体检(复检)单可打印");


                    } else if (examMemberExam1.getVerifyConclusion() == 1) {//合格

                        //暂无可打印的健康证
                        resultMap.put("errorMsg", "检查合格,无体检(复检)单打印！");


                    } else if (examMemberExam1.getVerifyConclusion() == 2) {//不合格

                        if (examMemberExam1.getIsRecheckPrint()== 1) {

                            //暂无可打印的健康证
                            resultMap.put("errorMsg", "您已打印过复检单,如需再次打印,请联系工作人员！");

                        } else {


                            resultMap.put("examMemberExam", JsonParseTool.toJson(examMemberExam1));

                        }

                    }

                } else {

                    //暂无可打印的健康证
                    resultMap.put("errorMsg", "暂无体检(复检)单可打印");

                }
            }else{
                resultMap.put("errorMsg","该用户未登记体检");
            }


        }

        return resultMap;

    }

//    @RequestMapping("/updateMemberPhoto")
//    public void updateMemberPhoto(Long memberId,String memberPhoto){
//
//        examMemberService.updateMemberIcon(memberId,memberPhoto);
//
//    }

    @RequestMapping("/updateMemberInfo")
    @ResponseBody
    public Map<String, String> updateMemberInfo(Long id,Long memberId,String memberPhoto,String memberName,String memberAddress,String memberSex,int tag){

       int i= 0;
       i = examMemberService.updateMemberInfo(id,memberId,memberPhoto,memberName,memberAddress,memberSex,tag);

        Map<String, String> resultMap = new HashMap<>();

        if (i==1){
            resultMap.put("statusCode", "200");
//            ajaxCallBack.success("");
        }else{
            resultMap.put("statusCode", "300");
//            ajaxCallBack.error("");
        }

        return resultMap;
    }




    @RequestMapping("/printExamResult")
    @ResponseBody
    public Map<String, String> printExamResult(String idCardNum) {

        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("errorMsg", "cuowu");

        return resultMap;


    }


    @RequestMapping("/printExamSuccess")
    public void printExamSuccess() {

        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("errorMsg", "cuowu");

    }

    @RequestMapping("/jkMachinePrintViewTransferForm")
    public void jkMachinePrintViewTransferForm(Model model, Long id) {




        ExamMemberExam examMemberExam = examMemberExamService.get(id);


        Long packageId = examMemberExam.getPackageId();//57
        ExamPackageProject pp = new ExamPackageProject();//package project一一对应
        pp.setPackageId(packageId);//57
        List<ExamPackageProject> packageProjectList = examPackageProjectService.findByExample(pp);//project_id:5,6,7,8,9,10
        ArrayList<String> projectList = new ArrayList<>();

        List<String> buildProject = ExamPackageProjectTool.buildProject(packageProjectList);
        for (int i=0;i<4;i++){
            if (i<buildProject.size()){
                projectList.add(i+2+"."+buildProject.get(i));
            }else{
                projectList.add("");
            }
        }
        model.addAttribute("examProject",projectList);


//        ExamCategory category = examMemberExam.getExamCategory();

        String categoryName = examMemberExam.getCategoryName();

        if (categoryName.length() >= 5) {
            categoryName.substring(0, 5);
        }

        examMemberExam.setCategoryName(categoryName);


        //支付

//        examPay.setExamNumber(examMemberExam.getExamNumber());
        Integer payState = examMemberExam.getPayState();
        model.addAttribute("isPay",payState==2);
//        examMemberExam.setExamCategory(category);

        model.addAttribute("examMemberExam", examMemberExam);

        //gcx  疫区标识
        String idCardHead5=examMemberService.get(examMemberExam.getMemberId()).getIdCardNum().substring(0,6);
        ExamAffected ea=new ExamAffected();
        ea.setAffectedCode(idCardHead5);
        Boolean tag=false;
        if(examAffectedService.query(ea).size()>0){
            tag=true;
        }
        model.addAttribute("tag",tag);
    }


    @RequestMapping("/jkMachineRecheckPrintViewTransferForm")
    public void jkMachineRecheckPrintViewTransferForm(Model model, Long id) {

        ExamMemberExam examMemberExam = examMemberExamService.get(id);

        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();

         examBlodIntestinal.setMemberExamId(examMemberExam.getId());

        //体检不合格 需要重新体检 同时打印复检单
        List<ExamBlodIntestinal> blodIntestinalList = examBlodIntestinalService.findByExample(examBlodIntestinal);

        List<String> resultStringList = new ArrayList<>();

        String recheckProjectName = "";

        List<RecheckEntity> recheckEntityList = new ArrayList<>();

        for (ExamBlodIntestinal examBlodIntestinal1 : blodIntestinalList){

            String tempProjectName = examBlodIntestinal1.getProjectName();

            Long tempId = examBlodIntestinal1.getProjectId();

            //5、戊肝 6、肠道检查 7、谷丙转氨酶 8、甲肝 9、内科检查 10、胸部x线

            RecheckEntity recheckEntity = new RecheckEntity();

            if (tempId == 6){//肠道

                recheckEntity.setName(SystemContent.PROJECT_INTESTINAL);

            }else if (tempId == 9){//内科

                recheckEntity.setName(SystemContent.PROJECT_NK);

            }else if (tempId == 10){//胸部

                recheckEntity.setName(SystemContent.PROJECT_XXIAN);

            }else{//血检

                recheckEntity.setName(SystemContent.PROJECT_BLOD);

            }

            recheckEntityList.add(recheckEntity);


            String result="";
            ExamBlodIntestinal ebiM=new ExamBlodIntestinal();
            ebiM.setExamNumber(examBlodIntestinal1.getExamNumber());
            ebiM.setProjectId(examBlodIntestinal1.getProjectId());
            List<ExamBlodIntestinal> list=examBlodIntestinalService.query(ebiM);
            if(list.size()>1){
                result=list.get(list.size()-2).getExamResult();
            }

            recheckProjectName += tempProjectName +"("+result+")"+ ",";

            //recheckProjectName += tempProjectName + ",";

        }
        Integer payState = examMemberExam.getPayState();
        model.addAttribute("isPay",payState==2);
        //返回复检结果
        model.addAttribute("resultStr",recheckProjectName.substring(0,recheckProjectName.length()-1));

        List<RecheckEntity> calculateRecheckEntityList = getSingleList(recheckEntityList);

        List<RecheckEntity> resultRecheckEntityList = new ArrayList<>();


        for (int i=0;i<4;i++){
            if (i<calculateRecheckEntityList.size()){
                RecheckEntity recheckEntity = calculateRecheckEntityList.get(i);
                recheckEntity.setName((i+2)+"."+recheckEntity.getName());
                resultRecheckEntityList.add(recheckEntity);
            }else{
                RecheckEntity recheckEntity = new RecheckEntity();
                recheckEntity.setName("");
                resultRecheckEntityList.add(recheckEntity);
            }
        }




        //拼接生成的
        String temp = recheckProjectName.substring(0,recheckProjectName.length()-1);
        String resultString = stitchingString(temp);
        model.addAttribute("resultString",resultString);
//        model.addAttribute("headInfo",resultString);
        System.out.println(resultString.length());
        int pnum=26;
        int resultLenght = resultString.length() / pnum;
        String headText="";
        for(int j=0;j<=resultLenght;j++){
            String str= "";
            if (j==resultLenght){
                headText +=  resultString.substring(j*pnum,resultString.length());
            }else{
                headText +=   resultString.substring(j*pnum,(j+1)*pnum) +"<br/>";
            }
//            resultStringList.add(str);
        }
        model.addAttribute("headTxt",headText);


        //循环返回条数
        model.addAttribute("resultList",resultRecheckEntityList);

        model.addAttribute("resultStrList",resultStringList);

        model.addAttribute("examMemberExam", examMemberExam);

        String idCardHead5=examMemberService.get(examMemberExam.getMemberId()).getIdCardNum().substring(0,6);
        ExamAffected ea=new ExamAffected();
        ea.setAffectedCode(idCardHead5);
        Boolean tag=false;
        if(examAffectedService.query(ea).size()>0){
            tag=true;
        }
        model.addAttribute("tag",tag);
    }


    /**
     * 拼接字符串
     * @param content
     * @return
     */
    public String stitchingString(String content){

        StringBuffer buffer = new StringBuffer();

        buffer.append(SystemContent.PROJECT_BEFORE_WORD);
        buffer.append(content);
        buffer.append(SystemContent.PROJECT_AFTER_WORD);

        return  buffer.toString();

    }

    /**
     * 去重
     * @param recheckEntityList
     * @return
     */
    public List<RecheckEntity> getSingleList (List < RecheckEntity > recheckEntityList) {

        for (int i = 0; i < recheckEntityList.size() - 1; i++) {
            for (int j = recheckEntityList.size() - 1; j > i; j--) {
                if (recheckEntityList.get(j).getName().equals(recheckEntityList.get(i).getName())) {
                    recheckEntityList.remove(j);
                }
            }
        }

        return recheckEntityList;

    }
    /**
     * 自助机打印完成后,更新打印的状态由0变成1
     * @param examMemberId
     */
    @RequestMapping("/updatePhysicalPrint")
    @ResponseBody
    public AjaxCallBack updatePhysicalPrint (String examMemberId){


        examMemberExamService.updatePrintInfoT(Long.parseLong(examMemberId));
        AjaxCallBack ajaxCallBack = new AjaxCallBack();

        if (examMemberId!=null){
            ajaxCallBack.success("");
        }else{
            ajaxCallBack.error("");
        }

        return ajaxCallBack;
    }


    //////////////////////健康证打印////////////////////////
    //打印健康证
    @RequestMapping("/printECard")
    public void printECard () {

    }

    @RequestMapping("/printECardSuccess")
    public void printECardSuccess () {

    }

    @RequestMapping("/printECardSuccessAfter")
    public void printECardSuccessAfter () {

    }

    @RequestMapping("/getECardByIdCard")
    @ResponseBody
    public Map<String, String> getMmeberInfoByIdCard (String idCardNum,String headData){

        Map<String, String> resultMap = new HashMap<>();

        ExamMemberExam examMemberExam = new ExamMemberExam();

        if (StringUtils.isEmpty(idCardNum)) {

            resultMap.put("errorMsg", "身份证信息获取失败,请重试");

        } else {

            //根据用户身份证获取最新的可打印的ecard数据
            ExamMember examMember = new ExamMember();

            examMember.setIdCardNum(idCardNum);

            //按理只有一条记录
            List<ExamMember> examMemberList = examMemberService.findByExampleOrderDesc(examMember,"id");

            if (examMemberList.size() > 0) {

                ExamMember newExamMember = examMemberList.get(0);

                //根据该用户id 获取ecard信息 按照时间排序
                ExamEcard examEcard = new ExamEcard();
                examEcard.setMemberId(newExamMember.getId());

                List<ExamEcard> examEcardList = examEcardService.findByExampleOrderDesc(examEcard, "id");

                if (examEcardList.size() > 0) {

                    //取第一条数据进行判断 如果isprint = 0 则继续打印 如果isprint=1 则提示用户已打印过 去窗口找人工打印
                    ExamEcard firstECard = examEcardList.get(0);

                    if (firstECard.getIsPrint() == 0) {

                        resultMap.put("result", JsonParseTool.toJson(firstECard));


                    } else {

                        resultMap.put("errorMsg", "您已打印过健康证,如需补打请联系工作人员！");
                    }


                } else {
                    resultMap.put("errorMsg", "该人员无相关健康证信息");
                }


            } else {

                //未查询到该用户
                resultMap.put("errorMsg", "无该人员记录信息");
            }


        }


        return resultMap;

    }


    /**
     * 修改该处的打印标识 置为已打印
     * @param eCardId
     */
    @RequestMapping("/updateECard")
    @ResponseBody
    public AjaxCallBack updateECard (Long eCardId){

        ExamEcard examEcard = examEcardService.get(eCardId);

        examEcard.setIsPrint(1);

        examEcard = examEcardService.updateBack(examEcard);

        AjaxCallBack ajaxCallBack = new AjaxCallBack();

        if (examEcard.getId()!=null){
            ajaxCallBack.success("");
        }else{
            ajaxCallBack.error("");
        }

        return ajaxCallBack;

    }



}
