package com.bidanet.bdcms.controller.queryStatic;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.driver.cache.RecheckEntity;
import com.bidanet.bdcms.driver.cache.TogetherQueryEntity;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.RoleService;
import com.bidanet.bdcms.service.UserRoleService;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryService;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.service.examBusiness.*;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.queryStatic.ExamTogetherQueryService;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;

/**
 * 体检综合查询. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-03 11:05:23
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Controller("examTogether")
@RequestMapping ("/admin/queryStatic/examTogetherQuery")
public class ExamTogetherQueryController extends AdminBaseController{

    @Autowired
    private ExamTogetherQueryService examTogetherQueryService;

    @Autowired
    private ExamMemberExamService examMemberExamService;

    @Autowired
    private ExamPayService examPayService;

    @Autowired
    private ExamEcardService examEcardService;

    @Autowired
    private ExamReviewService examReviewService;

    @Autowired
    private ExamBlodService examBlodService;

    @Autowired
    private ExamIntestinalService examIntestinalService;

    @Autowired
    private ExamBlodIntestinalService examBlodIntestinalService;

    @Autowired
    private ExamMemberService examMemberService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ExamAreaService examAreaService;

    @Autowired
    private ExamCategoryService examCategoryService;



    private String tableId = "";


    /**
     * 初始查询
     * @param name
     * @param areaId
     * @param idCard
     * @param eCardNumber
     * @param workUnit
     * @param examNumber
     * @param startTime
     * @param endTime
     * @param payState
     * @param payType
     * @param isRecheck
     * @param isQualified
     * @param page
     * @param model
     * @param tabid
     * @param togetherCategoryLevelTwo
     * @param isEtQuery
     * @param sortByTime
     * @param channel
     * @param parentId
     * @throws ParseException
     */
    @RequestMapping("/index")
    public void index(String name, String areaId, String idCard, String eCardNumber,String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck,
                      String isQualified, @ModelAttribute Page<TogetherQueryEntity> page, Model model,String tabid,Long togetherCategoryLevelTwo,Long isEtQuery,String sortByTime,String channel,
                      String parentId, String insState) throws ParseException {
//        if (isEtQuery!=null) {

        User user = uc.getUser();

        if(areaId == null){
            areaId = user.getAreaId().toString();
        }
            examTogetherQueryService.queryExamTogetherList(page,user.getUid(), name,eCardNumber, togetherCategoryLevelTwo, areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified,sortByTime,channel,insState);

            List<UserRole> userRoles = userRoleService.getByUid(user.getUid());
            if (userRoles.size()>0) {
                UserRole userRole = userRoles.get(0);
                Role role = roleService.get(userRole.getRoleId());
                model.addAttribute("currentAreaId",areaId);
                model.addAttribute("currentAgenciesCode",user.getAgenciesCode());
                model.addAttribute("roleCode",role.getCode());
                model.addAttribute("userAgenciesCode",user.getAgenciesCode());
                model.addAttribute("name", name);
                model.addAttribute("areaId", areaId);
                model.addAttribute("idCard", idCard);
                model.addAttribute("workUnit", workUnit);
                model.addAttribute("examNumber", examNumber);
                model.addAttribute("eCardNumber", eCardNumber);
                model.addAttribute("payState", payState);
                model.addAttribute("startTime", startTime);
                model.addAttribute("payType", payType);
                model.addAttribute("startTime", startTime);
                model.addAttribute("endTime", endTime);
                //model.addAttribute("togetherQAdd", togetherQAdd);
                model.addAttribute("isRecheck", isRecheck);
                model.addAttribute("sortByTime",sortByTime);
                model.addAttribute("togetherCategoryLevelTwoId", togetherCategoryLevelTwo);
                model.addAttribute("categoryParentId", parentId);
                model.addAttribute("channel",channel);
                model.addAttribute("insState",insState);
            }
//        }
        //tableId = tabid;

        SpringWebTool.getSession().setAttribute("togetherQueryTableId",tabid);
    }

    @RequestMapping("/toChangeMember")
    public void toChangeMember(String memberExamId,Model model) {

        //根据examNumber查询该用户

        ExamMemberExam memberExam = examMemberExamService.get(Long.parseLong(memberExamId));


        //获得主参数对象
        ExamMember examMember = examMemberService.get(memberExam.getMemberId());

        ExamCategory examCategory= examCategoryService.get(memberExam.getCategoryId());

        ExamPay examPay = examPayService.get(memberExam.getPayId());

        model.addAttribute("memberExam", memberExam);
        model.addAttribute("examMember",examMember);
        model.addAttribute("examCategory",examCategory);
        model.addAttribute("examPay",examPay);
    }


    /**
     * wanglu 修改体检人员信息接口 取消：当isRemark为1时,修改用户付费为0元，收费状态为已收费
     * 只修改体检人员基本信息，同步相关表的数据
     * @param name
     * @param sex
     * @param idCardNum
     * @param birthday
     * @param age
     * @param address
     * @param mobile
     * @return
     */
    @RequestMapping("/changeMember")
    @ResponseBody
    public AjaxCallBack changeMember(Long memberExamId,String name, int sex, String idCardNum,String birthday,
                                     int age,String address,String mobile,Long categoryId,String workUnit) {

        boolean flag = false;

        flag = examMemberExamService.updateExamMemberExamAndExamMeberUserInfoT(memberExamId,
                name,sex,idCardNum,birthday,age,address,mobile,categoryId,workUnit);


        if (flag){
            AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
            String tableId = (String) SpringWebTool.getSession().getAttribute("togetherQueryTableId");
            ajaxCallBack.setTabid(tableId);
            return ajaxCallBack;
        }else{
            AjaxCallBack ajaxCallBack = AjaxCallBack.error("修改失败!");
            return ajaxCallBack;
        }

    }

/*    *//**
     * 当isRemark为1时,修改用户付费为0元，收费状态为已收费
     * @param name
     * @param sex
     * @param birthday
     * @param address
     * @param idCardNum
     * @param mobile
     * @param workUnit
     * @param age
     * @param areaId
     * @param packageId
     * @param packageMoney
     * @param isRemark
     * @param memberExamId
     * @param categoryId
     * @return
     *//*
    @RequestMapping("/changeMember")
    @ResponseBody
    public AjaxCallBack changeMember(String name,String sex,String birthday,String address,String idCardNum,String mobile,String workUnit,Integer age,
                                     String areaId,String agenciesId,String packageId,String packageMoney,String isRemark,String memberExamId,String categoryId) {

        AjaxCallBack ajaxCallBack =new AjaxCallBack();
       ExamMemberExam memberExam =  examMemberExamService.changeEtqMember( name, sex, birthday, address, idCardNum, mobile, workUnit, age,
               areaId,agenciesId, packageId, packageMoney, isRemark, memberExamId,categoryId);

        if (memberExam.getId()!=null){
            ajaxCallBack.saveSuccess();
        }else{
            ajaxCallBack.error("修改失败!");
        }

        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;

    }*/

    /**
     * 判断审核
     * 做物理删除 根据id获取相关数据进行删除
     * @param
     * @return
     */
    @RequestMapping("/batchDeletes")
    @ResponseBody
    public AjaxCallBack batchDeletes(String memberExamIds){

        List<String> memberExamIdList = Arrays.asList(memberExamIds.split(","));

        //批量删除 信息
        //1、exam_ecard
        //1、exam_blod_intestinal
        //2、exam_blod
        //3、exam_intestinal
        //4、exam_member_exam

        Boolean isDelete = false;

        for (int i = 0; i < memberExamIdList.size(); i++) {

            ExamMemberExam memberExam = examMemberExamService.get(Long.parseLong(memberExamIdList.get(i)));

             isDelete =  examMemberExamService.etBatchDelete(memberExam);


            if (!isDelete){
                break;
            }
        }


        if (!isDelete){
            return AjaxCallBack.error("删除失败");
        }else{
            return AjaxCallBack.success("删除成功");
        }



    }

//    /**
//     * 打印导出word
//     *
//     */
//    @RequestMapping("/viewExaminationForm")
//    public void viewExaminationForm(){
//
//        try{
//
//            VelocityContext veContext = new VelocityContext();
//
//            veContext.put("startTime","2015-10-01");
//
//            String templetePath = "viewExaminationForm.vm";
//
//            Properties ps = new Properties();
//            ps.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "/WEB-INF/templates/admin/queryStatic/examTogetherQuery/");// 这是模板所在路径
//            VelocityEngine ve = new VelocityEngine();
//            ve.init(ps);
//            ve.init();
//            Template template = ve.getTemplate(templetePath, "utf-8");
//
//            File srcFile = new File("/Users/zangli/Documents/abc.doc");//输出路径
//            FileOutputStream fos = new FileOutputStream(srcFile);
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
//            template.merge(veContext, writer);
//            writer.flush();
//            writer.close();
//            fos.close();
//
//        }catch (Exception e){
//
//        }
//
//    }
    /**
     * 跳转预览肠检单
     */
    @RequestMapping("/viewIntestinalForm")
    public void viewIntestinalForm(Model model,String ids){
        if (StringUtils.isNotBlank(ids)) {
            ids = ids.substring(0,ids.length()-1);
            List<ExamMemberExam> examMemberExams = examTogetherQueryService.getExamMemberExams(ids);
            model.addAttribute("examMemberExams",examMemberExams);
        }
        //获取登陆的用户
        User user = uc.getUser();

        ExamArea examArea = examAreaService.get(user.getAreaId());
        user.setAreaName(examArea.getAreaName());
        user.setAreaCode(examArea.getAreaCode());
        model.addAttribute("user",user);
        model.addAttribute("printUser",user.getRealName());
    }

    /**
     * 跳转打印肠检单
     */
    @RequestMapping("/printViewIntestinalForm")
    public void printViewIntestinalForm(Model model,String ids){
        if (StringUtils.isNotBlank(ids)) {
//            User userInfo = (User) SpringWebTool.getSession().getAttribute("login_user_info");

            model.addAttribute("printUser",  uc.getUser().getRealName());
            ids = ids.substring(0,ids.length()-1);
            List<ExamMemberExam> examMemberExams = examTogetherQueryService.getExamMemberExams(ids);
            int pageSize=16;
            int size = examMemberExams.size();

            int pageNum=size/pageSize;
            if (size%pageSize>0){
                pageNum++;
            }

            ArrayList<List> pageList = new ArrayList<>();
            for (int i = 0; i < pageNum; i++) {
                ArrayList<ExamMemberExam> pageData = new ArrayList<>();
                for (int j = 0; j < pageSize; j++) {
                    int dataIndex = pageSize * i + j;
                    if (dataIndex<examMemberExams.size()){
                        ExamMemberExam examMemberExam = examMemberExams.get(dataIndex);
                        examMemberExam.setInsState(1);//肠检单打印字段设置为已打印
                        examMemberExamService.updateT(examMemberExam);
                        pageData.add(examMemberExam);
                    }else{
                        ExamMemberExam examMemberExam = new ExamMemberExam();
                        pageData.add(examMemberExam);
                    }
                }
                pageList.add(pageData);
            }
            model.addAttribute("pageData",pageList);
            model.addAttribute("pageNum",pageNum);

            model.addAttribute("examMemberExams",examMemberExams);
        }

        //获取登陆的用户
        User user = uc.getUser();

        ExamArea examArea = examAreaService.get(user.getAreaId());
        user.setAreaName(examArea.getAreaName());
        user.setAreaCode(examArea.getAreaCode());
        model.addAttribute("user",user);
    }

    /**
     * 跳转预览体检单
     */
    @RequestMapping("/viewExaminationForm")
    public void viewExaminationForm(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);

//        ExamCategory category = examMemberExam.getExamCategory();

        String categoryName =examMemberExam.getCategoryName();

        if(categoryName.length()>=5){
            categoryName.substring(0,5);
        }

        examMemberExam.setCategoryName(categoryName);

//        examMemberExam.setExamCategory(category);

        model.addAttribute("examMemberExam",examMemberExam);
    }



    /**
     * 跳转打印体检单      todo// wanglu 打印去除，没有生成健康证号的bean
     */
    @RequestMapping("/printViewExaminationForm")
    public void printViewExaminationForm(Model model,String ids){

        ids = ids.substring(0,ids.length()-1);
        List<ExamMemberExam> examMemberExamList = examTogetherQueryService.getExamMemberExams(ids);

        Iterator<ExamMemberExam> it = examMemberExamList.iterator();
        while(it.hasNext()){
            ExamMemberExam temp = it.next();

            //如果健康证号 为null  就移除打印集合
            if(StringUtils.isBlank(temp.geteCardNumber()) ){
                it.remove();
            }
        }

      for(int i=0;i<examMemberExamList.size();i++){
          String examNumber=examMemberExamList.get(i).getExamNumber();
          ExamBlodIntestinal ebI=new ExamBlodIntestinal();
          ebI.setExamNumber(examNumber);
          /*内科*/
          ebI.setProjectId(9L);
          List<ExamBlodIntestinal>ebIN=examBlodIntestinalService.query(ebI);
          int num;
          if(ebIN.size()>0){
              num=ebIN.size()-1;
              ExamBlodIntestinal xx=ebIN.get(num);
              String name=xx.getDoctorName();
              examMemberExamList.get(i).getProjectData().put("n",ebIN.get(num).getDoctorName());
          }
          /* 胸部X线检查*/
          ebI.setProjectId(10L);
          List<ExamBlodIntestinal>ebIX=examBlodIntestinalService.query(ebI);
          if(ebIX.size()>0){
              num=ebIX.size()-1;
              examMemberExamList.get(i).getProjectData().put("x",ebIX.get(num).getDoctorName());
          }
          /*甲肝*/
          ebI.setProjectId(8L);
          List<ExamBlodIntestinal>ebIJ=examBlodIntestinalService.query(ebI);
          if(ebIJ.size()>0){
              num=ebIJ.size()-1;
              examMemberExamList.get(i).getProjectData().put("j",ebIJ.get(num).getDoctorName());
          }
          /*戊肝*/
          ebI.setProjectId(5L);
          List<ExamBlodIntestinal>ebIW=examBlodIntestinalService.query(ebI);
          if(ebIW.size()>0){
              num=ebIW.size()-1;
              examMemberExamList.get(i).getProjectData().put("w",ebIW.get(num).getDoctorName());
          }
          /*谷丙转氨酶*/
          ebI.setProjectId(7L);
          List<ExamBlodIntestinal>ebIB=examBlodIntestinalService.query(ebI);
          if(ebIB.size()>0){
              num=ebIB.size()-1;
              examMemberExamList.get(i).getProjectData().put("b",ebIB.get(num).getDoctorName());
          }
          /*肠道带菌检查*/
          ebI.setProjectId(6L);
          List<ExamBlodIntestinal>ebIC=examBlodIntestinalService.query(ebI);
          if(ebIC.size()>0){
              num=ebIC.size()-1;
              examMemberExamList.get(i).getProjectData().put("c",ebIC.get(num).getDoctorName());
          }
      }



        model.addAttribute("examMemberExamList",examMemberExamList);
    }



    /**
     * 跳转预览流转单
     */
    @RequestMapping("/viewTransferForm")
    public void viewTransferForm(Model model,String id){
//        ExamMemberExam examMemberExam = examMemberExamService.get(id);
//        model.addAttribute("examMemberExam",examMemberExam);
        List<ExamMemberExam> examMemberExamList = examTogetherQueryService.getExamMemberExams(id);
        if (examMemberExamList.size()>0) {
            ExamMemberExam exam = examMemberExamList.get(0);
            model.addAttribute("exam",exam);
        } else {
            model.addAttribute("exam",null);
        }
    }

    /**
     * 疾控跳转预览流转单
     */
    @RequestMapping("/jkViewTransferForm")
    public void jkViewTransferForm(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);

//        ExamCategory category = examMemberExam.getExamCategory();

        String categoryName =examMemberExam.getCategoryName();

        if(categoryName.length()>=5){
            categoryName.substring(0,5);
        }

        examMemberExam.setCategoryName(categoryName);

//        examMemberExam.setExamCategory(category);

        model.addAttribute("examMemberExam",examMemberExam);
    }

    /**
     * 疾控跳转预览流转单
     */
    @RequestMapping("/jkMachinePrintViewTransferForm")
    public void jkMachinePrintViewTransferForm(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);

//        ExamCategory category = examMemberExam.getExamCategory();

        String categoryName =examMemberExam.getCategoryName();

        if(categoryName.length()>=5){
            categoryName.substring(0,5);
        }

        examMemberExam.setCategoryName(categoryName);

//        examMemberExam.setExamCategory(category);

        model.addAttribute("examMemberExam",examMemberExam);
    }

    /**
     * 疾控跳转预览流转单
     */
    @RequestMapping("/jkPrintViewTransferForm")
    public void jkPrintViewTransferForm(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);

//        ExamCategory category = examMemberExam.getExamCategory();

        String categoryName =examMemberExam.getCategoryName();

        if(categoryName.length()>=5){
            categoryName.substring(0,5);
        }

        examMemberExam.setCategoryName(categoryName);

//        examMemberExam.setExamCategory(category);

        model.addAttribute("examMemberExam",examMemberExam);
    }

    /**
     * 疾控跳转预览流转单
     */
    @RequestMapping("/jkRecheckViewTransferForm")
    public void jkRecheckViewTransferForm(Model model,Long id){
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
            String temp = examBlodIntestinal1.getExamResult() ;

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

           // recheckProjectName += tempProjectName + ",";

        }

        //返回复检结果
        //model.addAttribute("resultStr",recheckProjectName.substring(0,recheckProjectName.length()-1));

        List<RecheckEntity> calculateRecheckEntityList = getSingleList(recheckEntityList);

        List<RecheckEntity> resultRecheckEntityList = new ArrayList<>();

        for (RecheckEntity recheckEntity : calculateRecheckEntityList){
            resultRecheckEntityList.add(recheckEntity);
        }


        if (calculateRecheckEntityList.size()<4){
            int size = 4-calculateRecheckEntityList.size();

            for(int i=0;i<size;i++){

                RecheckEntity recheckEntity = new RecheckEntity();
                recheckEntity.setName("");
                resultRecheckEntityList.add(recheckEntity);
            }
        }

        //拼接生成的属于
        String temp = recheckProjectName.substring(0,recheckProjectName.length()-1);
        String resultString = stitchingString(temp);
        model.addAttribute("resultString",resultString);
        //String resultString = stitchingString(recheckProjectName);

        int resultLenght = resultString.length() / SystemContent.PROJECT_NUMBER;

        for(int j=0;j<(resultLenght+1);j++){

            String str= "";
            if (j==3){
                 str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,resultString.length());
            }else{
                int end=(j+1)*SystemContent.PROJECT_NUMBER;
                if(end>(resultString.length()-1)){
                    end=resultString.length()-1;
                }
                str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,end);
            }

            resultStringList.add(str);
        }


        //循环返回条数
        model.addAttribute("resultList",resultRecheckEntityList);

        model.addAttribute("resultStrList",resultStringList);

        model.addAttribute("examMemberExam", examMemberExam);
    }

    /**
     * 疾控跳转预览流转单
     */
    @RequestMapping("/jkRecheckPrintViewTransferForm")
    public void jkPrintRecheckViewTransferForm(Model model,Long id){

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

            recheckProjectName += tempProjectName + ",";

        }

        //返回复检结果
        model.addAttribute("resultStr",recheckProjectName.substring(0,recheckProjectName.length()-1));


        List<RecheckEntity> calculateRecheckEntityList = getSingleList(recheckEntityList);

        List<RecheckEntity> resultRecheckEntityList = new ArrayList<>();

        for (RecheckEntity recheckEntity : calculateRecheckEntityList){
            resultRecheckEntityList.add(recheckEntity);
        }


        if (calculateRecheckEntityList.size()<4){
            int size = 4-calculateRecheckEntityList.size();

            for(int i=0;i<size;i++){

                RecheckEntity recheckEntity = new RecheckEntity();
                recheckEntity.setName("");
                resultRecheckEntityList.add(recheckEntity);
            }
        }

        //拼接生成的属于
        String temp = recheckProjectName.substring(0,recheckProjectName.length()-1);
        String resultString = stitchingString(temp);
        model.addAttribute("resultString",resultString);
//        String resultString = stitchingString(recheckProjectName);

        int resultLenght = resultString.length() / SystemContent.PROJECT_NUMBER;

        for(int j=0;j<(resultLenght+1);j++){

            String str= "";
            if (j==3){
                str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,resultString.length());
            }else{
                int end=(j+1)*SystemContent.PROJECT_NUMBER;
                if(end>(resultString.length()-1)){
                    end=resultString.length()-1;
                }
                str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,end);
                str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,end);
            }

            resultStringList.add(str);
        }


        //循环返回条数
        model.addAttribute("resultList",resultRecheckEntityList);

        model.addAttribute("resultStrList",resultStringList);

        model.addAttribute("examMemberExam", examMemberExam);
        model.addAttribute("resultString",resultString);
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
    public List<RecheckEntity> getSingleList(List<RecheckEntity> recheckEntityList){

//        List<RecheckEntity> list = new ArrayList<RecheckEntity>();

        for (int i = 0; i < recheckEntityList.size()-1; i++) {
            for (int j = recheckEntityList.size()-1; j > i; j--) {
                if (recheckEntityList.get(j).getName().equals(recheckEntityList.get(i).getName())) {
                    recheckEntityList.remove(j);
                }
            }
        }

        return recheckEntityList;

    }

    /**
     * 疾控跳转预览流转单
     */
    @RequestMapping("/jkMachineRecheckPrintViewTransferForm")
    public void jkMachineRecheckPrintViewTransferForm(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);

        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();

        examBlodIntestinal.setMemberExamId(examMemberExam.getId());

        //体检不合格 需要重新体检 同时打印复检单
        List<ExamBlodIntestinal> blodIntestinalList = examBlodIntestinalService.findByExample(examBlodIntestinal);

        model.addAttribute("resultMap",blodIntestinalList.toString());


        model.addAttribute("examMemberExam",JsonParseTool.toJson(examMemberExam));
    }

    /**
     * 跳转打印流转单
     */
    @RequestMapping("/printViewTransferForm")
    public void printViewTransferForm(Model model,String ids){
        if (StringUtils.isNotBlank(ids)) {
            ids = ids.substring(0,ids.length()-1);
            List<ExamMemberExam> examMemberExamList = examTogetherQueryService.getExamMemberExams(ids);
            model.addAttribute("examMemberExamList",examMemberExamList);
        }
    }

//    /**
//     * 疾控跳转打印流转单
//     */
//    @RequestMapping("/jkPrintViewTransferForm")
//    public void jkPrintViewTransferForm(Model model,String ids){
//        if (StringUtils.isNotBlank(ids)) {
//            ids = ids.substring(0,ids.length()-1);
//            List<ExamMemberExam> examMemberExamList = examTogetherQueryService.getExamMemberExams(ids);
//            model.addAttribute("examMemberExamList",examMemberExamList);
//        }
//    }

    /**
     * 跳转健康检查表
     */
    @RequestMapping("/examCheckForm")
    public void examCheckForm(Model model,String id){
        List<ExamMemberExam> examMemberExamList = examTogetherQueryService.getExamMemberExams(id);
        if (examMemberExamList.size()>0) {
            ExamMemberExam exam = examMemberExamList.get(0);
            model.addAttribute("exam",exam);
        } else {
            model.addAttribute("exam",null);
        }
    }

    /**
     * 跳转不合格告知书
     */
    @RequestMapping("/unqualifiedNotice")
    public void unqualifiedNotice(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
        examBlodIntestinal.setMemberExamId(examMemberExam.getId());
        List<ExamBlodIntestinal> examBlodIntestinals = examBlodIntestinalService.findByExample(examBlodIntestinal);
        model.addAttribute("examBlodIntestinals",examBlodIntestinals);
        model.addAttribute("examMemberExam",examMemberExam);
    }

    /**
     * 跳转打印不合格告知书
     */
    @RequestMapping("/printUnqualifiedNotice")
    public void printUnqualifiedNotice(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
        examBlodIntestinal.setMemberExamId(examMemberExam.getId());
        List<ExamBlodIntestinal> examBlodIntestinals = examBlodIntestinalService.findByExample(examBlodIntestinal);
        model.addAttribute("examBlodIntestinals",examBlodIntestinals);
        model.addAttribute("examMemberExam",examMemberExam);
//        if (StringUtils.isNotBlank(ids)) {
//            ids = ids.substring(0,ids.length()-1);
//            List<ExamMemberExam> examMemberExams = examTogetherQueryService.getExamMemberExams(ids);
//            model.addAttribute("examMemberExams",examMemberExams);
//        }
    }

    /**
     * 判断审核
     * @param ids
     * @return
     */
    @RequestMapping("/getMemberExamListByIds")
    @ResponseBody
    public Map<String, Object> getMemberExamListByIds(String ids){
        Map<String,Object> map = new HashMap<String, Object>();
        ids = ids.substring(0,ids.length()-1);
        List<ExamMemberExam> examMemberExamList = examTogetherQueryService.getExamMemberExams(ids);
        map.put("examMemberExamList",examMemberExamList);
        return map;
    }

    /**
     * 导出列表
     * @param pageCurrent
     * @param pageSize
     * @param name
     * @param areaId
     * @param idCard
     * @param workUnit
     * @param examNumber
     * @param startTime
     * @param endTime
     * @param payState
     * @param payType
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/exportTogetherExcel")
    @ResponseBody
    public FileOutput exportTogetherExcel(int pageCurrent, int pageSize,Long togetherCategoryLevelTwoId,String name,String areaId, String idCard, String workUnit,
                                          String examNumber,String startTime, String endTime, String payState, String payType, String channel,
                                          String eCardNumber) throws IOException, ParseException {
        HSSFWorkbook hw = examTogetherQueryService.exportTogetherExcel(pageCurrent,pageSize,URLDecoder.decode(name,"UTF-8"),areaId,idCard, URLDecoder.decode(workUnit,"UTF-8"),examNumber,
                startTime,endTime,payState,payType,togetherCategoryLevelTwoId,channel,eCardNumber);
        String path = SpringWebTool.getRealPath("/temp/");
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String  downloadTime= DateTool.timeToStrYmd(new Date().getTime());
        String filename = downloadTime + "-togetherQuery.xls";
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


    public static void main(String[] args){
            List<RecheckEntity> list  =   new  ArrayList<RecheckEntity>();

            RecheckEntity recheckEntity0 = new RecheckEntity();
        recheckEntity0.setName("血检");
        RecheckEntity recheckEntity1 = new RecheckEntity();
        recheckEntity1.setName("肠检");
        RecheckEntity recheckEntity2 = new RecheckEntity();
        recheckEntity2.setName("X先");
        RecheckEntity recheckEntity3 = new RecheckEntity();
        recheckEntity3.setName("血检");
        RecheckEntity recheckEntity4 = new RecheckEntity();
        recheckEntity4.setName("内科");
            list.add(recheckEntity0);
        list.add(recheckEntity1);
        list.add(recheckEntity2);
        list.add(recheckEntity3);
        list.add(recheckEntity4);


        for (int i = 0; i < list.size()-1; i++) {
            for (int j = list.size()-1; j > i; j--) {
                if (list.get(j).getName().equals(list.get(i).getName())) {
                    list.remove(j);
                }
            }
        }


//        Set<RecheckEntity> set = new  HashSet<RecheckEntity>(list);

//        list.clear();
//
//       list.addAll(set);

//        //过滤重复的Point对象
//        List<RecheckEntity> listss = new ArrayList<RecheckEntity>();
//        for (RecheckEntity o:list)
//        {
//            System.out.println( "==== " + o.getName());
//
//            if (!list.contains(o))
//            {
//                listss.add((RecheckEntity)o);
//            }
//        }

        System.out.println( "去重后的集合： " + list.size());
    }


    /**
     * 自助机打印完成后,更新打印的状态由0变成1
     * @param examMemberId
     */
    @RequestMapping("/EtqUpdatePhysicalPrint")
    @ResponseBody
    public AjaxCallBack EtqUpdatePhysicalPrint (String examMemberId){

        ExamMemberExam examMemberExam = examMemberExamService.get(Long.parseLong(examMemberId));

        examMemberExam.setIsRecheckPrint(1);

        examMemberExam =  examMemberExamService.updateBack(examMemberExam);

        AjaxCallBack ajaxCallBack = new AjaxCallBack();

        if (examMemberExam.getId()!=null){
            ajaxCallBack.success("");
        }else{
            ajaxCallBack.error("");
        }

        return ajaxCallBack;
    }

}
