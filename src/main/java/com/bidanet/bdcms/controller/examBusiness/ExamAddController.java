package com.bidanet.bdcms.controller.examBusiness;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.ExamPackageProjectTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.driver.cache.Cache;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import com.bidanet.bdcms.service.UserRoleService;
import com.bidanet.bdcms.service.businessSetting.*;
import com.bidanet.bdcms.service.examBusiness.*;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.service.fee.FeeService;
import com.bidanet.bdcms.service.queryStatic.ExamTogetherQueryService;
import com.bidanet.bdcms.vo.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 体检业务-体检登记. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-20 14:31:28
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Controller("examAdd")
@RequestMapping("/admin/examBusiness/examAdd")
public class ExamAddController extends AdminBaseController {

    @Autowired
    Cache cache;
    @Autowired
    private ExamAddService examAddService;
    @Autowired
    private ExamCategoryService categoryService;
    @Autowired
    private ExamPackageService examPackageService;
    @Autowired
    private ExamMemberPackageService examMemberPackageService;
    @Autowired
    private ExamCategoryService examCategoryService;
    @Autowired
    private ExamAreaService examAreaService;
    @Autowired
    private ExamNumberService examNumberService;
    @Autowired
    private FeeService feeService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ExamMemberExamService examMemberExamService;
    @Autowired
    private ExamEcardService examEcardService;
    @Autowired
    private ExamTogetherQueryService examTogetherQueryService;
    @Autowired
    ExamMemberService examMemberService;
    @Autowired
    @Qualifier("localFileDrive")
    LocalFileDrive localFileDrive;

    @Autowired
    private ExamUnitService examUnitService;
    @Autowired
    private ExamPackageProjectService examPackageProjectService;
    @Autowired
    ExamAffectedService examAffectedService;

    /**
     * 进入页面，加载下拉列表的一些数据
     * @param model
     */
    @RequestMapping("/index")
    public void index(Model model){
        List<ExamCategory> examCategories = categoryService.getList();
        List<ExamPackage> examPackageList = examPackageService.getList();
        List<ExamCategory> examCategoryList = examCategoryService.getList();
        List<ExamArea> examAreaList = examAreaService.getList();
        User user = uc.getUser();
        model.addAttribute("loginUserAreaId",user.getAreaId());
        model.addAttribute("loginUserAreaCode",examAreaService.get(user.getAreaId()).getAreaCode());
        model.addAttribute("loginUserAgenciesId",user.getAgenciesId());
        model.addAttribute("examCategories",examCategories);
        model.addAttribute("examPackageList",examPackageList);
        model.addAttribute("examCategoryList",examCategoryList);
        model.addAttribute("examAreaList",examAreaList);

        //疾控打印
//        examNumberService.printTransferCpc();
    }

    @RequestMapping("/addJudgeExamMember")
    @ResponseBody
    public Map<String, Object> machineJudgeExamMember(String idCardNum,String judgeTag) {
        Map<String, Object> map = new HashMap<String, Object>();
        //先根据身份证号去判断
        map = examAddService.judgeExamMember(idCardNum,judgeTag);

        return map;
    }

    // TODO: 2017/4/18 体检号生成
    /**
     * 保存体检登记信息--保存信息成功后 如果是疾控则打印热敏纸 如果是乡镇则打印A4
     * @param name,sex,birthday,address,idCardNum,mobile,workUnit
     * @return
     */
    @RequestMapping("/addExamMember")
    @ResponseBody
    public AjaxCallBack addExamMember(String name,int sex,int age,String birthday,String address,String idCardNum,
                                      String mobile,String workUnit,Long areaId,Long packageId,BigDecimal packageMoney,String memberIcon,Long agenciesId,Long categoryId,Integer isCameraPhoto) throws UnsupportedEncodingException {
        User user = uc.getUser();

        String s = cache.get(idCardNum);
        if (s==null){
            cache.set(idCardNum,idCardNum,1);
        }else{
            return AjaxCallBack.error("操作过快，请稍后再试");
        }


        if(memberIcon.indexOf("base64,")!=-1){
            String[] iconStr = memberIcon.split("base64,");
            String icon = iconStr[1];
            memberIcon = localFileDrive.uploadBase64(icon, UUID.randomUUID().toString()+".bmp");
        }
        AjaxCallBack ajaxCallBack = new AjaxCallBack();
        List<ExamMember> examMembers = examAddService.getExamMemberByIdCardNum(idCardNum);

        ExamMemberExam examMemberExam = new ExamMemberExam();
        //先判断是否首次体检
        if (examMembers.size()>0) {
            ExamMember examMember = examMembers.get(0);
//            ExamMemberExam examMemberExam = new ExamMemberExam();
            examMemberExam.setMemberId(examMember.getId());
            examMemberExam.setIsNew(1);
            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExample(examMemberExam);
            //判断当前体检人员是否存在未完成的体检单：
            //如果有，将之前的体检单作废，生成新的体检单
            //如果没有，生成一张体检单
            if (examMemberExamList.size()>0) {
                //如果存在，判断最新的体检单是否完成
                ExamMemberExam exam = examMemberExamList.get(0);
                if (exam.getVerifyConclusion()==1) {
                    //如果已经完成，直接生成新的体检单
                    ExamMemberExam exam1 =  examNumberService.addExamTS( name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney,user,memberIcon,agenciesId,categoryId,isCameraPhoto);
                    ajaxCallBack.setStatusCode(200);
                    ajaxCallBack.setMessage(exam1.getId().toString());
                    ajaxCallBack.saveSuccess();
//               return      AjaxCallBack.success(examMemberExam.getId().toString());
                } else {
//                    //如果没有完成，将此体检单作废，并生成新的体检单
                    ExamMemberExam exam1 = examNumberService.cancelExamT(exam, name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney,user,memberIcon,agenciesId,categoryId,isCameraPhoto);
                    ajaxCallBack.setStatusCode(200);
                    ajaxCallBack.setMessage(exam1.getId().toString());
                    ajaxCallBack.saveSuccess();
//                    ajaxCallBack.setStatusCode(300);
//                    ajaxCallBack.setMessage("该用户还存在未完成项目！");
//               return      AjaxCallBack.error("该用户还存在未完成项目");
                }
            } else {
                //数据库无该数据，直接生成新的体检单
                ExamMemberExam exam =  examNumberService.addExamTS( name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney,user,memberIcon,agenciesId,categoryId,isCameraPhoto);
                ajaxCallBack.setStatusCode(200);
                ajaxCallBack.setMessage(exam.getId().toString());
                ajaxCallBack.saveSuccess();
//               return AjaxCallBack.success(examMemberExam.getId().toString());
//                ExamMemberExam exam = examMemberExamList.get(0);
//                examNumberService.cancelExamT(exam, name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaCode, packageId, packageMoney,user,memberIcon,agenciesId,categoryId);
//                ajaxCallBack.setStatusCode(200);
//                ajaxCallBack.setMessage("操作成功！");
//                ajaxCallBack.saveSuccess();
            }
        } else {
            ExamUnit examUnit=new ExamUnit();
            examUnit.setUnitName(workUnit);
            List<ExamUnit> examUnits=examUnitService.findByExample(examUnit);
            if(examUnits.size()<1){
                examUnit.setCreateTime(new Date().getTime());
                examUnitService.addExamUnitT(examUnit);
            }
            ExamMemberExam exam =   examNumberService.addExamTS( name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney,user,memberIcon,agenciesId,categoryId,isCameraPhoto);
            ajaxCallBack.setStatusCode(200);
            ajaxCallBack.setMessage(exam.getId().toString());
            ajaxCallBack.saveSuccess();
//            return  AjaxCallBack.success(examMemberExam.getId().toString());
        }
        return ajaxCallBack;
    }

    /**
     * 跳转打印体检单
     */
    @RequestMapping("/printViewExamForm")
    public void printViewExamForm(Model model,Long examId){
        ExamMemberExam examMemberExam = examMemberExamService.get(examId);
        model.addAttribute("exam",examMemberExam);
    }

    @RequestMapping("/jkPrintPhysicalExam")
    @ResponseBody
    public void jkPrintPhysicalExam(String memberExamId, Model model){
        User user = uc.getUser();
        ExamMemberExam examMemberExam = new ExamMemberExam();

        examMemberExam = examMemberExamService.get(Long.parseLong(memberExamId));

        model.addAttribute("examMemberExam",examMemberExam);
    }

    /**
     * 根据packageId获取套餐详情
     * @param packageId
     * @return
     */
    @RequestMapping("/getPackageById")
    @ResponseBody
    public Map<String, Object> getPackageById(String packageId){
        Map<String,Object> map = new HashMap<String, Object>();
        if (packageId!=null && packageId!="") {
            ExamPackage examPackage = examPackageService.get(Long.parseLong(packageId));
            if (examPackage!=null) {
                List<ExamProject> examProjectList = examPackageService.getExamPackageProjectList(Long.parseLong(packageId));
                map.put("examProjectList",examProjectList);
            }
            map.put("examPackage",examPackage);
        }
        return map;
    }

    /**
     * 根据身份证号码判断是否注册，若注册，返回体检历史信息列表
     * @param idCardNum
     * @return
     */
    @RequestMapping("/checkIdCardRegister")
    @ResponseBody
    public Map<String, Object> checkIdCardRegister(String idCardNum){
        Map<String,Object> map = new HashMap<String, Object>();
        ExamMember examMember = new ExamMember();
        examMember.setIdCardNum(idCardNum);
        List<ExamMember> memberList = examAddService.findByExampleExact(examMember);
        if (memberList.size()>0) {
            ExamMember member = memberList.get(0);
            ExamMemberPackage examMemberPackage = new ExamMemberPackage();
            examMemberPackage.setMemberId(member.getId());
            ExamMemberExam exam = new ExamMemberExam();
            exam.setMemberId(member.getId());
            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExample(exam);
//            List<ExamMemberPackage> examMemberPackageList = examMemberPackageService.getExamMemberPackageListByMemberId(examMemberPackage);
            map.put("examMemberExamList",examMemberExamList);
            map.put("examMemberList",memberList);

            //查询该用户是否有有效期大于90天的健康证
//            List<ExamEcard> examEcardList = examEcardService.getExamEcardByMemberId(member.getId());
            ExamEcard ecard = new ExamEcard();
            ecard.setMemberId(member.getId());
            List<ExamEcard> examEcardList = examEcardService.findByExampleOrderDesc(ecard,"createTime");
            if (examEcardList.size()>0) {
                ExamEcard examEcard = examEcardList.get(0);
                Date dNow = new Date();   //当前时间
                Date dBefore = new Date();
                Calendar calendar = Calendar.getInstance(); //得到日历
                calendar.setTime(dNow);//把当前时间赋给日历
                calendar.add(calendar.MONTH, 3);  //设置为前3月
                dBefore = calendar.getTime();   //得到前3月的时间
                if (examEcard.getExpTime()>dBefore.getTime()) {
                    //有效期大于三个月
                    map.put("hasEcard",1);
                } else {
                    map.put("hasEcard",0);
                }
            }

        }
        return map;
    }

    /**
     * 根据行业id获取行业
     * @param pid
     * @return
     */
    @RequestMapping("/getCategory")
    @ResponseBody
    public List<ValueLabel> getCategory(Long pid){
        List<ValueLabel> list = new ArrayList<ValueLabel>();
        if(pid!=null){
            List<ExamCategory> categoryList = examCategoryService.getExamCategoryByPid(1,pid);
            if(categoryList!=null&&categoryList.size()>0) {
                list.add(new ValueLabel("","请选择行业！"));
                for (ExamCategory examCategory : categoryList) {
                    list.add(new ValueLabel(String.valueOf(examCategory.getId()), examCategory.getCategoryName()));
                }
            }else{
                list.add(new ValueLabel("","暂无行业" ));
            }
        }else{
            list.add(new ValueLabel("","请选择行业" ));
        }
        return  list;
    }

    /**
     * 根据身份证号码判断是否注册，若注册，返回体检历史信息列表
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
            map.put("examCategoryPackage",examCategoryPackageList.get(0));
        }
        map.put("categoryList",categoryList);
        return map;
    }


    /**
     * 疾控跳转预览流转单
     */
    @RequestMapping("/jkPrintViewTransferForm")
    public void jkPrintViewTransferForm(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);

//        ExamCategory category = examMemberExam.getExamCategory();

        Long packageId = examMemberExam.getPackageId();
        ExamPackageProject pp = new ExamPackageProject();
        pp.setPackageId(packageId);
        List<ExamPackageProject> packageProjectList = examPackageProjectService.findByExample(pp);
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

        String categoryName =examMemberExam.getCategoryName();

        if(categoryName.length()>=5){
            categoryName.substring(0,5);
        }

        examMemberExam.setCategoryName(categoryName);

        String idCardHead5=examMemberService.get(examMemberExam.getMemberId()).getIdCardNum().substring(0,6);
        ExamAffected ea=new ExamAffected();
        ea.setAffectedCode(idCardHead5);
        Boolean tag=false;
        if(examAffectedService.query(ea).size()>0){
            tag=true;
        }


//        examMemberExam.setExamCategory(category);


        model.addAttribute("examMemberExam", examMemberExam);

        Integer payState = examMemberExam.getPayState();
        model.addAttribute("isPay",payState==2);
        /*标记是否是疫区*/
        model.addAttribute("tag",tag);
    }

    /**
     * 自助机打印完成后,更新打印的状态由0变成1
     * @param examMemberId
     */
    @RequestMapping("/examAddUpdatePhysicalPrint")
    @ResponseBody
    public AjaxCallBack examAddUpdatePhysicalPrint (String examMemberId){

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



    @RequestMapping("/addCompleteUnit")
    @ResponseBody
    public List<ExamUnit> queryUnitNameList(String q){
        ExamUnit unit = new ExamUnit();
        unit.setUnitName(q);
        return examUnitService.findByExample(unit);

    }

}
