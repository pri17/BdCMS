package com.bidanet.bdcms.controller.examBusiness;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryService;
import com.bidanet.bdcms.service.businessSetting.ExamMemberPackageService;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.service.businessSetting.ExamUnitService;
import com.bidanet.bdcms.service.examBusiness.*;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.vo.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 体检业务-手工录入. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-07 09:53:26
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Controller("menualEntry")
@RequestMapping("/admin/examBusiness/munualEntry")
public class MenualEntryController extends AdminBaseController {

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
    private ExamMemberExamService examMemberExamService;
    @Autowired
    private ExamMemberService examMemberService;
    @Autowired
    private ExamEcardService examEcardService;
    @Autowired
    private ExamUnitService examUnitService;
    @Autowired
    private ExamAffectedService examAffectedService;

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
        model.addAttribute("meLoginUserAreaId",user.getAreaId());
        model.addAttribute("loginUserAreaCode",examAreaService.get(user.getAreaId()).getAreaCode());
        model.addAttribute("loginUserAgenciesId",user.getAgenciesId());
        model.addAttribute("examCategories",examCategories);
        model.addAttribute("examPackageList",examPackageList);
        model.addAttribute("examCategoryList",examCategoryList);
        model.addAttribute("examAreaList",examAreaList);
    }

    @RequestMapping("/addJudgeExamMember")
    @ResponseBody
    public Map<String, Object> machineJudgeExamMember(String idCardNum,String judgeTag) {
        Map<String, Object> map = new HashMap<String, Object>();
        //先根据身份证号去判断
        map = examAddService.judgeExamMember(idCardNum,judgeTag);

        return map;
    }

    /**
     * 保存体检登记信息
     * @param name,sex,birthday,address,idCardNum,mobile,workUnit
     * @return
     */
    @RequestMapping("/addExamMember")
    @ResponseBody
    public AjaxCallBack addExamMember(String name, int sex, int age, String birthday, String address, String idCardNum,
                                      String mobile, String workUnit, Long areaId, Long packageId, BigDecimal packageMoney, String memberIcon, Long agenciesId,Long categoryId,Integer isCameraPhoto) throws UnsupportedEncodingException {
        User user = uc.getUser();
        AjaxCallBack ajaxCallBack = new AjaxCallBack();
        List<ExamMember> examMembers = examAddService.getExamMemberByIdCardNum(idCardNum);
        if (examMembers.size()>0) {
            ExamMember examMember = examMembers.get(0);
            ExamMemberExam examMemberExam = new ExamMemberExam();
            examMemberExam.setMemberId(examMember.getId());
            examMemberExam.setIsNew(1);
            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExample(examMemberExam);
            //判断该用户是否存在体检单
            if (examMemberExamList.size()>0) {
                //如果存在，判断最新的体检单是否完成
                ExamMemberExam exam = examMemberExamList.get(0);
                if (exam.getVerifyConclusion()==1) {
                    //如果已经完成，直接生成新的体检单
                    ExamMemberExam exam1 =  examNumberService.addExamTS( name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney,user,memberIcon,agenciesId,categoryId,isCameraPhoto);
                    ajaxCallBack.setStatusCode(200);
                    ajaxCallBack.setMessage(exam1.getId().toString());
                    ajaxCallBack.saveSuccess();
                } else {
                    //如果没有完成，将此体检单作废，并生成新的体检单
                    ExamMemberExam exam1 =  examNumberService.cancelExamT(exam, name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney,user,memberIcon,agenciesId,categoryId,isCameraPhoto);
                    ajaxCallBack.setStatusCode(200);
                    ajaxCallBack.setMessage(exam1.getId().toString());
                    ajaxCallBack.saveSuccess();
//                    ajaxCallBack.setStatusCode(300);
//                    ajaxCallBack.setMessage("该用户还存在未完成项目！");
                }
            } else {
                //如果不存在，直接生成新的体检单
                ExamMemberExam exam =  examNumberService.addExamTS( name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney,user,memberIcon,agenciesId,categoryId,isCameraPhoto);
                ajaxCallBack.setStatusCode(200);
                ajaxCallBack.setMessage(exam.getId().toString());
                ajaxCallBack.saveSuccess();
            }
        } else {
            ExamMemberExam examMemberExam = examNumberService.addExamTS( name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney,user,memberIcon,agenciesId,categoryId,isCameraPhoto);
            ajaxCallBack.setStatusCode(200);
            ajaxCallBack.setMessage(examMemberExam.getId().toString());
            ajaxCallBack.saveSuccess();
        }
        return ajaxCallBack;
    }

    /**
     * 疾控跳转预览流转单
     */
    @RequestMapping("/jkPrintViewTransferFormMe")
    public void jkPrintViewTransferFormMe(Model model,Long id){
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
     * 根据packageId获取套餐详情
     * @param packageId
     * @return
     */
    @RequestMapping("/getPackageById")
    @ResponseBody
    public Map<String, Object> getPackageById(String packageId){
        Map<String,Object> map = new HashMap<String, Object>();
        ExamPackage examPackage = examPackageService.get(Long.parseLong(packageId));
        if (examPackage!=null) {
            List<ExamProject> examProjectList = examPackageService.getExamPackageProjectList(Long.parseLong(packageId));
            map.put("examProjectList",examProjectList);
        }
        map.put("examPackage",examPackage);
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
        List<ExamMember> memberList = examAddService.getExamMemberByIdCardNum(idCardNum);
        map.put("memberList",memberList);
        if (memberList.size()>0) {
            ExamMember member = memberList.get(0);
            map.put("examMember",member);
            ExamMemberPackage examMemberPackage = new ExamMemberPackage();
            examMemberPackage.setMemberId(member.getId());
            ExamMemberExam exam = new ExamMemberExam();
            exam.setMemberId(member.getId());
            List<ExamMemberExam> examMemberExamList = examMemberExamService.findByExample(exam);
//            List<ExamMemberPackage> examMemberPackageList = examMemberPackageService.getExamMemberPackageListByMemberId(examMemberPackage);
            map.put("examMemberExamList",examMemberExamList);

            //查询该用户是否有有效期大于90天的健康证
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
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        currCal.clear();
        currCal.set(Calendar.YEAR, currentYear);
        //将小时至0
        currCal.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        currCal.set(Calendar.MINUTE, 0);
        //将秒至0
        currCal.set(Calendar.SECOND,0);
        //将毫秒至0
        currCal.set(Calendar.MILLISECOND, 0);
        return currCal.getTime();
    }

    /**
     * 上传图片
     * @param path
     * @return
     */
    @RequestMapping("/uploadPic")
    @ResponseBody
    public AjaxCallBack uploadPic(String path, Model model) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tempPath = "/upload/img/" + simpleDateFormat.format(new Date());
            String realPath = SpringWebTool.getRealPath(tempPath);
            File directory = new File(realPath);
            if (!directory.exists()){
                directory.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            String s = directory + "/" +uuid.toString()+ ".jpg";
            File file = new File(s);
                file.createNewFile();

            if (!file.isFile()) {
                return AjaxCallBack.error("");
            }
            decodeBase64ToImage(path,s);
            model.addAttribute("s",s);
            AjaxCallBack ajaxCallBack = new AjaxCallBack(200 , s ,true);
            return ajaxCallBack;
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxCallBack.error("图片上传失败!");
        }
    }

    /**
     * 将Base64位编码的图片进行解码，并保存到指定目录
     *
     * @param base64
     *			base64编码的图片信息
     * @return
     */
    public static void decodeBase64ToImage(String base64, String path) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            FileOutputStream write = new FileOutputStream(path);
            byte[] decoderBytes = decoder.decodeBuffer(base64);
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Integer payState = examMemberExam.getPayState();
        model.addAttribute("isPay",payState==2);

        String idCardHead5=examMemberService.get(examMemberExam.getMemberId()).getIdCardNum().substring(0,6);
        ExamAffected ea=new ExamAffected();
        ea.setAffectedCode(idCardHead5);
        Boolean tag=false;
        if(examAffectedService.query(ea).size()>0){
            tag=true;
        }
         /*标记是否是疫区*/
        model.addAttribute("tag",tag);
    }

    /**
     * 跳转打印体检单
     */
    @RequestMapping("/printViewExamFormMe")
    public void printViewExamForm(Model model,Long examId){
        ExamMemberExam examMemberExam = examMemberExamService.get(examId);
        model.addAttribute("exam",examMemberExam);
    }


    /**
     * 自助机打印完成后,更新打印的状态由0变成1
     * @param examMemberId
     */
    @RequestMapping("/examMenualUpdatePhysicalPrint")
    @ResponseBody
    public AjaxCallBack examMenualUpdatePhysicalPrint (String examMemberId){

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


    @RequestMapping("/menualCompleteUnit")
    @ResponseBody
    public List<ExamUnit> queryUnitNameList(String q){
        ExamUnit unit = new ExamUnit();
        unit.setUnitName(q);
        return examUnitService.findByExample(unit);

    }

}
