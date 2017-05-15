package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.bean.CompanyPeopleInform;
import com.bidanet.bdcms.bean.GenerateBarCode;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.UserRoleDao;
import com.bidanet.bdcms.dao.businessSetting.ExamCategoryDao;
import com.bidanet.bdcms.dao.businessSetting.ExamMemberPackageDao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.dao.examBusiness.*;
import com.bidanet.bdcms.dao.fee.FeeDao;
import com.bidanet.bdcms.dao.systemSetting.ExamAgenciesDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryPackageService;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryService;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.service.examBusiness.ExamNumberService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.service.wap.WapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 体检登记. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-28 14:17:38
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
@Service
public class ExamNumberServiceImpl extends BaseServiceImpl<ExamNumber> implements ExamNumberService {

    @Autowired
    private ExamNumberDao examNumberDao;

    @Override
    protected Dao getDao() {
        return examNumberDao;
    }

    @Autowired
    private ExamMemberDao examMemberDao;
    @Autowired
    private ExamAreaDao examAreaDao;
    @Autowired
    private ExamMemberExamDao examMemberExamDao;
    @Autowired
    private ExamMemberPackageDao examMemberPackageDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private FeeDao feeDao;
    @Autowired
    private ExamBlodIntestinalDao blodIntestinalDao;
    @Autowired
    private ExamProjectDao projectDao;
    @Autowired
    private ExamPackageProjectDao packageProjectDao;
    @Autowired
    private ExamAgenciesService examAgenciesService;

    @Autowired
    private ExamCategoryDao examCategoryDao;

    @Autowired
    private ExamAgenciesDao examAgenciesDao;

    @Autowired
    @Qualifier("localFileDrive")
    LocalFileDrive localFileDrive;

    @Autowired
    WapService wapService;
    @Autowired
    ExamCategoryService examCategoryService;
    @Autowired
    ExamPackageService examPackageService;
    @Autowired
    ExamCategoryPackageService examCategoryPackageService;

    @Autowired
    @Qualifier("generateBarCode")
    GenerateBarCode generateBarCode;

    public List<ExamNumber> getNumberListByCreateTime(Long todayStart,Integer areaCode) {
        return examNumberDao.findListByCreateTime(todayStart,areaCode);
    }

    public void addExamNumberT(ExamNumber examNumber) {
        Date date =  new Date();
        examNumber.setCreateTime(date.getTime());
        examNumberDao.save(examNumber);
    }

    public ExamMemberExam cancelExamT(ExamMemberExam oldExam,String name, int sex, int age, String birthday, String address, String idCardNum,
                           String mobile, String workUnit, Long areaId, Long packageId, BigDecimal packageMoney, User user, String memberIcon, Long agenciesId, Long categoryId,Integer isCameraPhoto) {
        //将最后一张体检单作废
        //并将blod_intestinal及exam_pay表中数据作废
        oldExam.setIsCancel(1);
        examMemberExamDao.update(oldExam);
        ExamPay examPay = feeDao.get(oldExam.getPayId());
        examPay.setIsNew(0);
        examPay.setIsCancel(1);
        feeDao.update(examPay);
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
        examBlodIntestinal.setMemberExamId(oldExam.getId());
        List<ExamBlodIntestinal> blodIntestinals = blodIntestinalDao.findByExample(examBlodIntestinal);
        if (blodIntestinals.size()>0) {
            for (int i = 0; i < blodIntestinals.size(); i++) {
                ExamBlodIntestinal blod = blodIntestinals.get(i);
                blod.setIsNew(0);
                blod.setIsCancel(1);
                blodIntestinalDao.update(blod);
            }
        }
        return addExamTS(name, sex, age, birthday, address, idCardNum,mobile, workUnit, areaId, packageId, packageMoney, user, memberIcon, agenciesId, categoryId,isCameraPhoto);
    }

    public ExamMemberExam addExamTS(String name, int sex, int age, String birthday, String address, String idCardNum,
                                    String mobile, String workUnit, Long areaId, Long packageId, BigDecimal packageMoney, User user, String memberIcon, Long agenciesId, Long categoryId, Integer isCameraPhoto) {
        //保存之前先查询数据库是否存在该身份证号码，
        // 如果存在，修改内容；如果不存在，再新增
        ExamMember oldMember = new ExamMember();
        oldMember.setIdCardNum(idCardNum);
        List<ExamMember> memberList = examMemberDao.findByExample(oldMember);
        Date date = new Date();
        if (memberIcon!=null){
            if(memberIcon.indexOf("base64,")!=-1){
                String[] iconStr = memberIcon.split("base64,");
                String icon = iconStr[1];
                memberIcon = localFileDrive.uploadBase64(icon, UUID.randomUUID().toString()+".bmp");
            }

        }
        ExamMember member;
//        if (workUnit!=null && workUnit!="") {
//            //工作单位必须已“常熟市”三个字开头
//            if(workUnit.length()>=3) {
//                String workBegin = workUnit.substring(0, 3);
//                if (!workBegin.equals("常熟市")) {
//                    workUnit = "常熟市" + workUnit;
//                }
//            }else{
//                   workUnit = "常熟市" + workUnit;
//            }
//        }
        if (memberList.size()>0) {
            member = memberList.get(0);
            member.setName(name);
            member.setSex(sex);
            member.setBirthday(birthday);
            member.setIdCardAddress(address);
            member.setIdCardNum(idCardNum);
            member.setMobile(mobile);
            member.setCreateTime(date.getTime());
            member.setIcon(memberIcon);
            member.setWorkUnit(workUnit);

            member =  examMemberDao.updateBack(member);
        } else {
            member =  new ExamMember();
            member.setName(name);
            member.setSex(sex);
            member.setBirthday(birthday);
            member.setIdCardAddress(address);
            member.setIdCardNum(idCardNum);
            member.setMobile(mobile);
            member.setCreateTime(date.getTime());
            member.setIcon(memberIcon);
            member.setWorkUnit(workUnit);
            if (isCameraPhoto == 1){
                member.setIsIdCardIcon(0);
            }else {
                member.setIsIdCardIcon(1);
            }
            member = examMemberDao.saveBack(member);
        }
        // 先判断今年是否有体检单
        // 如果有，体检单号向后排；如果没有，流水码重新开始记;
        // 体检号生成规则：
        // 例：16 132 32262(其中16代表年份，132代表地区，32262为流水码，每自然年度结束后清零);
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy");
        String yearNow = sdfo.format(date).toString();

        ExamArea  examArea = examAreaDao.get(areaId);

        String newExamNumber = yearNow.substring(yearNow.length()-2,yearNow.length());
        newExamNumber += examArea.getAreaCode();
        ExamNumber examNumber = new ExamNumber();
        examNumber.setMemberId(member.getId());
        examNumber.setAreaCode(examArea.getAreaCode());
        List<ExamNumber> numberList = examNumberDao.findListByCreateTime(getCurrYearFirst().getTime(),examArea.getAreaCode());
        if (numberList.size() > 0) {
            Long waterNumber = numberList.get(0).getWaterNumber();
            DecimalFormat df = new DecimalFormat("00000");
            newExamNumber += df.format(waterNumber + 1);
            examNumber.setWaterNumber(waterNumber+1);
        } else {
            newExamNumber += "00001";
            examNumber.setWaterNumber(1L);
        }
        examNumber.setCreateTime(new Date().getTime());
        examNumber.setNumber(newExamNumber);
        examNumber = examNumberDao.saveBack(examNumber);

        ExamMemberExam oldExam = new ExamMemberExam();
        oldExam.setMemberId(member.getId());
        List<ExamMemberExam> examMemberExamList = examMemberExamDao.findByExample(oldExam);
        if (examMemberExamList.size()>0) {
            for (int i = 0; i < examMemberExamList.size(); i++) {
                ExamMemberExam oldE = examMemberExamList.get(i);
                oldE.setIsNew(0);
                examMemberExamDao.update(oldE);
            }
        }
        //向体检表添加一条数据
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setCreateTime(date.getTime());
        examMemberExam.setMemberId(member.getId());
        examMemberExam.setMobile(mobile);
        examMemberExam.setAgenciesId(agenciesId);
        examMemberExam.setExamTime(date.getTime());
        examMemberExam.setExamNumberId(examNumber.getId());
        examMemberExam.setExamNumber(examNumber.getNumber());
        examMemberExam.setWorkUnit(workUnit);
        examMemberExam.setVerifyState(2);
        examMemberExam.setVerifyConclusion(0);
        examMemberExam.setAge(age);
        examMemberExam.setIsRecheck(0);
//        examMemberExam.setExamNumberIcon(generateBarCode.generateFile(examNumber.getNumber()));
        examMemberExam.setIsNew(1);
        examMemberExam.setRecheckTag(0);
        examMemberExam.setIsRecheckPrint(0);
        examMemberExam.setEntryState(0);
        examMemberExam.setIsCancel(0);
        examMemberExam.setPayMoney(packageMoney);
        examMemberExam.setIHA("阴性");
        examMemberExam.setDDIA("阴性");
//        ExamArea examArea = new ExamArea();
//        examArea.setAreaCode(areaCode);
//        List<ExamArea> areaList = examAreaDao.findByExample(examArea);
//        if (areaList.size()>0) {
//            examMemberExam.setAreaId(areaList.get(0).getId());
//        }
        examMemberExam.setAreaId(areaId);
        examMemberExam.setAgenciesId(user.getAgenciesId());
        examMemberExam.setCategoryId(categoryId);
        examMemberExam.setPackageId(packageId);
        examMemberExam.setIsShowRecheck(0);
        examMemberExam.setIsPhysicalPrint(0);
        examMemberExam.setChannel(1);
        //是否是三星用户
        examMemberExam = examMemberExamDao.saveBack(examMemberExam);

        //2017-02-10 新增 将部分数据进行固化
        examMemberExam.setIcon(memberIcon);
        examMemberExam.setIdCardNum(idCardNum);
        examMemberExam.setName(name);
        examMemberExam.setSex(sex);
        examMemberExam.setBirthday(birthday);
        examMemberExam.setAreaName(examAreaDao.get(areaId).getAreaName());
        examMemberExam.setCategoryName(examCategoryDao.get(categoryId).getCategoryName());
        examMemberExam.setAgenciesName(examAgenciesDao.get(agenciesId).getAgenciesName());
        examMemberExam.setAgenciesCode(examAgenciesDao.get(agenciesId).getAgenciesCode());
        if (isCameraPhoto == 1){
            examMemberExam.setIsIdCardIcon(0);
        }else {
            examMemberExam.setIsIdCardIcon(1);
        }

//        //向从业人员体检表插入一条数据
//        ExamMemberPackage examMemberPackage = new ExamMemberPackage();
//        examMemberPackage.setMemberId(member.getId());
//        examMemberPackage.setPackageId(packageId);
//        examMemberPackage.setExamTime(date.getTime());
//        examMemberPackage.setExamState(1);
//        examMemberPackageDao.save(examMemberPackage);
        //向收费表添加一条数据
        ExamPay examPay = new ExamPay();
        examPay.setExamMemberId(examMemberExam.getId());
        examPay.setPayMoney(packageMoney);
        //如果是乡镇卫生院用户登录，默认为已收费。其他为未收费
        ExamAgencies examAgencies = examAgenciesService.get(user.getAgenciesId());
        if (examAgencies!=null) {
            if (examAgencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)) {
                examPay.setPayState(1);
                examPay.setPayType(9);
            } else {
                //乡镇收费处
                examPay.setTollCollectorId(user.getUid());
                examPay.setTollCollectorName(user.getRealName());
                examPay.setPayTime(date.getTime());
                examPay.setPayState(2);
                //2017-01-20 修改为记账方式
                examPay.setPayType(3);
            }
        }
        examPay.setCreateTime(date.getTime());
        examPay.setIsPrint(0);
        examPay.setExamNumberId(examNumber.getId());
        examPay.setExamNumber(newExamNumber);
        examPay.setIsSpecial(0);
        examPay.setPayActualMoney(new BigDecimal(0));
        examPay.setIsNew(1);
        examPay.setRecheckTag(0);
        examPay.setCreatorId(user.getUid());
        examPay.setIsCancel(0);

        //2017-02-10 新增 固话部分数据
        examPay.setWorkUnit(workUnit);
        examPay.setIdCardNum(idCardNum);
        examPay.setAreaId(areaId);
        examPay.setAreaName(examAreaDao.get(areaId).getAreaName());
        examPay.setName(name);
        examPay.setIcon(memberIcon);

        feeDao.save(examPay);

        examMemberExam.setPayId(examPay.getId());
        ExamAgencies examAgenciesUp = examAgenciesService.get(user.getAgenciesId());
        if (examAgenciesUp!=null) {
            if (examAgenciesUp.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)) {
                examMemberExam.setPayState(1);
                examMemberExam.setPayType(9);
            } else {
                //乡镇收费处
                examMemberExam.setPayState(2);
                //2017-01-20 修改为记账方式
                examMemberExam.setPayType(3);
                examMemberExam.setPayMoney(examPay.getPayMoney());
                examMemberExam.setPayActualMoney(examPay.getPayActualMoney());
            }
        }
        examMemberExam = examMemberExamDao.updateBack(examMemberExam);

        if (examMemberExam.getId()!=0){

            // 保存各项目录入结果前的信息
            ExamPackageProject pp = new ExamPackageProject();

            pp.setPackageId(packageId);

            List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);

            if(packageProjectList!=null&&packageProjectList.size()>0){

                for(ExamPackageProject p:packageProjectList){
                    if(p.getProjectId()==SystemContent.PROJECT_MEDICAL){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_MEDICAL,SystemContent.PROJECT_TYPE_MEDICAL);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_X){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_X,SystemContent.PROJECT_TYPE_X);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_ALT){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_ALT,SystemContent.PROJECT_TYPE_ALT);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_HAV){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_HAV,SystemContent.PROJECT_TYPE_HAV);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_HEV){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_HEV,SystemContent.PROJECT_TYPE_HEV);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_CHANGDAO){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_CHANGDAO,SystemContent.PROJECT_TYPE_CHANGDAO);
                    }
                }
            }

        }else{

            errorMsg("");
        }

        return examMemberExam;


    }


    @Override
    public void printTransferTown() {

    }

    /**
     * 保存内科，X线录入结果前的信息
     * @param examMemberExam
     */
    private void savePackageProjectResult(ExamMemberExam examMemberExam,Long projectId,Integer type) {
        ExamBlodIntestinal result = new ExamBlodIntestinal();
        result.setExamNumber(examMemberExam.getExamNumber());
        result.setMemberExamId(examMemberExam.getId());
        result.setMemberId(examMemberExam.getMemberId());
        result.setProjectId(projectId);
        ExamProject project = projectDao.get(projectId);
        result.setProjectName(project.getProjectName());
        result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN);
        result.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
        result.setType(type);
        result.setEntryState(0);
        result.setIsNew(1);
        result.setRecheckTag(0);
        result.setIsShowRefresh(0);
        result.setIsCancel(0);
        result.setCreateTime(new Date().getTime());
        blodIntestinalDao.save(result);
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
        currCal.set(Calendar.SECOND, 0);
        //将毫秒至0
        currCal.set(Calendar.MILLISECOND, 0);
        return currCal.getTime();
    }

    /**
     * 上传图片
     * @param path
     * @return
     */
    public String uploadPic(String path) {
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
                return "";
            }
            decodeBase64ToImage(path, s);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
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


    @Override
    public void addExamForCompanyT(List<CompanyPeopleInform> list) {
        checkError(list);

        for(int i=0;i<list.size();i++){
            CompanyPeopleInform cpl=list.get(i);
            if(cpl.getIdCardNum().length()<18||cpl.getIdCardNum().length()>18){
                errorMsg("编号为:"+cpl.getId()+"的用户的身份证号格式错误");
            }
            Map<String,String>map=getSexAndAgeAndBirthday(cpl.getIdCardNum());
            ExamCategory am=new ExamCategory();
            am.setCategoryName(cpl.getIndustry());
            List<ExamCategory>eCList=examCategoryService.query(am);
            if(eCList.size()<=0){
                errorMsg("编号为:"+cpl.getId()+"的用户的行业不存在");
            }

            ExamCategory ams=new ExamCategory();
            ams.setCategoryName(cpl.getCategory());
            List<ExamCategory>eCLists=examCategoryService.query(ams);
            if(eCLists.size()<=0){
                errorMsg("编号为:"+cpl.getId()+"的用户的行业类别不存在");
            }
            ExamCategoryPackage ecp=new ExamCategoryPackage();
            ecp.setCategoryId(eCLists.get(0).getId());
            List<ExamCategoryPackage>eCPList=examCategoryPackageService.query(ecp);
            if(eCPList.size()<=0){
                errorMsg("编号为:"+cpl.getId()+"的用户对应的体检套餐不存在");
            }
            ExamPackage  ep=examPackageService.get(eCPList.get(0).getPackageId());

            wapService.addExamOfCompanyAddTS(Integer.parseInt(map.get("sex")),
                    Integer.parseInt(map.get("age")),
                    cpl.getIdCardNum(),
                    cpl.getCompany(),
                    cpl.getPhoneNum(),
                    eCList.get(0).getId(),
                    ep.getId(),
                    ep.getMoney(),
                    map.get("birthday")
            );
        }
    }

    private Map<String,String> getSexAndAgeAndBirthday(String idCardNumber){
        String birthday = idCardNumber.substring(6,14);
        birthday = birthday.substring(0,4)+"-"+birthday.substring(4,6)+"-"+birthday.substring(6,8);
        Integer sex =Integer.parseInt(idCardNumber.substring(16, 17));
        if(sex%2 == 0){
            sex = 0;
        }else{
            sex = 1;
        }

        int se = Integer.valueOf(idCardNumber.substring(idCardNumber.length() - 1)) % 2;
        String  dates = idCardNumber.substring(6, 10);
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year=df.format(new Date());
        Integer age=Integer.parseInt(year)-Integer.parseInt(dates);
        Map<String,String> map=new HashMap<>();
        map.put("sex",sex.toString());
        map.put("age",age.toString());
        map.put("birthday",birthday);
        return  map;
    }


    private void checkError(List<CompanyPeopleInform> list){
        for(int i=0;i<list.size();i++){
            CompanyPeopleInform cpl=list.get(i);
            if(cpl.getIdCardNum().length()<18||cpl.getIdCardNum().length()>18){
                errorMsg("编号为:"+cpl.getId()+"的用户的身份证号格式错误");
            }
            if(cpl.getPhoneNum().length()<11||cpl.getPhoneNum().length()>11){
                errorMsg("编号为:"+cpl.getId()+"的用户对应的手机号格式异常");
            }
            Map<String,String>map=getSexAndAgeAndBirthday(cpl.getIdCardNum());
            ExamCategory am=new ExamCategory();
            am.setCategoryName(cpl.getCategory());
            List<ExamCategory>eCList=examCategoryService.query(am);
            if(eCList.size()<=0){
                errorMsg("编号为:"+cpl.getId()+"的用户的行业类别不存在");
            }
            ExamCategoryPackage ecp=new ExamCategoryPackage();
            ecp.setCategoryId(eCList.get(0).getId());
            List<ExamCategoryPackage>eCPList=examCategoryPackageService.query(ecp);
            if(eCPList.size()<=0){
                errorMsg("编号为:"+cpl.getId()+"的用户对应的体检套餐不存在");
            }



        }

    }


    @Override
    public ExamMemberExam addExamTOldToNewNoCreateMemberNumberT(String name, int sex, int age, String birthday, String address, String idCardNum, String mobile, String workUnit, Long areaId, Long packageId, BigDecimal packageMoney, User user, String memberIcon, Long agenciesId, Long categoryId, Integer isCameraPhoto, String memberNumber) {
        //保存之前先查询数据库是否存在该身份证号码，
        // 如果存在，修改内容；如果不存在，再新增
        ExamMember oldMember = new ExamMember();
        oldMember.setIdCardNum(idCardNum);
        List<ExamMember> memberList = examMemberDao.findByExample(oldMember);
        Date date = new Date();
        if (memberIcon!=null){
            if(memberIcon.indexOf("base64,")!=-1){
                String[] iconStr = memberIcon.split("base64,");
                String icon = iconStr[1];
                memberIcon = localFileDrive.uploadBase64(icon, UUID.randomUUID().toString()+".bmp");
            }

        }
        ExamMember member;
//        if (workUnit!=null && workUnit!="") {
//            //工作单位必须已“常熟市”三个字开头
//            if(workUnit.length()>=3) {
//                String workBegin = workUnit.substring(0, 3);
//                if (!workBegin.equals("常熟市")) {
//                    workUnit = "常熟市" + workUnit;
//                }
//            }else{
//                   workUnit = "常熟市" + workUnit;
//            }
//        }
        if (memberList.size()>0) {
            member = memberList.get(0);
            member.setName(name);
            member.setSex(sex);
            member.setBirthday(birthday);
            member.setIdCardAddress(address);
            member.setIdCardNum(idCardNum);
            member.setMobile(mobile);
            member.setCreateTime(date.getTime());
            member.setIcon(memberIcon);
            member.setWorkUnit(workUnit);

            member =  examMemberDao.updateBack(member);
        } else {
            member =  new ExamMember();
            member.setName(name);
            member.setSex(sex);
            member.setBirthday(birthday);
            member.setIdCardAddress(address);
            member.setIdCardNum(idCardNum);
            member.setMobile(mobile);
            member.setCreateTime(date.getTime());
            member.setIcon(memberIcon);
            member.setWorkUnit(workUnit);
            if (isCameraPhoto == 1){
                member.setIsIdCardIcon(0);
            }else {
                member.setIsIdCardIcon(1);
            }
            member = examMemberDao.saveBack(member);
        }
        // 先判断今年是否有体检单
        // 如果有，体检单号向后排；如果没有，流水码重新开始记;
        // 体检号生成规则：
        // 例：16 132 32262(其中16代表年份，132代表地区，32262为流水码，每自然年度结束后清零);
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy");
        /*String yearNow = sdfo.format(date).toString();*/

        ExamArea  examArea = examAreaDao.get(areaId);

       /* String newExamNumber = yearNow.substring(yearNow.length()-2,yearNow.length());*/
        /*newExamNumber += examArea.getAreaCode();*/
        ExamNumber examNumber = new ExamNumber();
        examNumber.setMemberId(member.getId());
        examNumber.setAreaCode(examArea.getAreaCode());
        List<ExamNumber> numberList = examNumberDao.findListByCreateTime(getCurrYearFirst().getTime(),examArea.getAreaCode());
        if (numberList.size() > 0) {
            Long waterNumber = numberList.get(0).getWaterNumber();
           /* DecimalFormat df = new DecimalFormat("00000");*/
           /* newExamNumber += df.format(waterNumber + 1);*/
            examNumber.setWaterNumber(waterNumber+1);
        } else {
            /*newExamNumber += "00001";*/
            examNumber.setWaterNumber(1L);
        }
        examNumber.setCreateTime(new Date().getTime());
        examNumber.setNumber(memberNumber);
        examNumber = examNumberDao.saveBack(examNumber);

        ExamMemberExam oldExam = new ExamMemberExam();
        oldExam.setMemberId(member.getId());
        List<ExamMemberExam> examMemberExamList = examMemberExamDao.findByExample(oldExam);
        if (examMemberExamList.size()>0) {
            for (int i = 0; i < examMemberExamList.size(); i++) {
                ExamMemberExam oldE = examMemberExamList.get(i);
                oldE.setIsNew(0);
                examMemberExamDao.update(oldE);
            }
        }
        //向体检表添加一条数据
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setCreateTime(date.getTime());
        examMemberExam.setMemberId(member.getId());
        examMemberExam.setMobile(mobile);
        examMemberExam.setAgenciesId(agenciesId);
        examMemberExam.setExamTime(date.getTime());
        examMemberExam.setExamNumberId(examNumber.getId());
        examMemberExam.setExamNumber(examNumber.getNumber());
        examMemberExam.setWorkUnit(workUnit);
        examMemberExam.setVerifyState(2);
        examMemberExam.setVerifyConclusion(0);
        examMemberExam.setAge(age);
        examMemberExam.setIsRecheck(0);
//        examMemberExam.setExamNumberIcon(generateBarCode.generateFile(examNumber.getNumber()));
        examMemberExam.setIsNew(1);
        examMemberExam.setRecheckTag(0);
        examMemberExam.setIsRecheckPrint(0);
        examMemberExam.setEntryState(0);
        examMemberExam.setIsCancel(0);
        examMemberExam.setPayMoney(packageMoney);
//        ExamArea examArea = new ExamArea();
//        examArea.setAreaCode(areaCode);
//        List<ExamArea> areaList = examAreaDao.findByExample(examArea);
//        if (areaList.size()>0) {
//            examMemberExam.setAreaId(areaList.get(0).getId());
//        }
        examMemberExam.setAreaId(areaId);
        examMemberExam.setAgenciesId(user.getAgenciesId());
        examMemberExam.setCategoryId(categoryId);
        examMemberExam.setPackageId(packageId);
        examMemberExam.setIsShowRecheck(0);
        examMemberExam.setIsPhysicalPrint(0);
        examMemberExam.setChannel(1);
        //是否是三星用户
        examMemberExam = examMemberExamDao.saveBack(examMemberExam);

        //2017-02-10 新增 将部分数据进行固化
        examMemberExam.setIcon(memberIcon);
        examMemberExam.setIdCardNum(idCardNum);
        examMemberExam.setName(name);
        examMemberExam.setSex(sex);
        examMemberExam.setBirthday(birthday);
        examMemberExam.setAreaName(examAreaDao.get(areaId).getAreaName());
        examMemberExam.setCategoryName(examCategoryDao.get(categoryId).getCategoryName());
        examMemberExam.setAgenciesName(examAgenciesDao.get(agenciesId).getAgenciesName());
        examMemberExam.setAgenciesCode(examAgenciesDao.get(agenciesId).getAgenciesCode());
        if (isCameraPhoto == 1){
            examMemberExam.setIsIdCardIcon(0);
        }else {
            examMemberExam.setIsIdCardIcon(1);
        }

//        //向从业人员体检表插入一条数据
//        ExamMemberPackage examMemberPackage = new ExamMemberPackage();
//        examMemberPackage.setMemberId(member.getId());
//        examMemberPackage.setPackageId(packageId);
//        examMemberPackage.setExamTime(date.getTime());
//        examMemberPackage.setExamState(1);
//        examMemberPackageDao.save(examMemberPackage);
        //向收费表添加一条数据
        ExamPay examPay = new ExamPay();
        examPay.setExamMemberId(examMemberExam.getId());
        examPay.setPayMoney(packageMoney);
        //如果是乡镇卫生院用户登录，默认为已收费。其他为未收费
        ExamAgencies examAgencies = examAgenciesService.get(user.getAgenciesId());
        if (examAgencies!=null) {
            if (examAgencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)) {
                examPay.setPayState(1);
                examPay.setPayType(9);
            } else {
                //乡镇收费处
                examPay.setTollCollectorId(user.getUid());
                examPay.setTollCollectorName(user.getRealName());
                examPay.setPayTime(date.getTime());
                examPay.setPayState(2);
                //2017-01-20 修改为记账方式
                examPay.setPayType(3);
            }
        }
        examPay.setCreateTime(date.getTime());
        examPay.setIsPrint(0);
        examPay.setExamNumberId(examNumber.getId());
        examPay.setExamNumber(examNumber.getNumber());
        examPay.setIsSpecial(0);
        examPay.setPayActualMoney(new BigDecimal(0));
        examPay.setIsNew(1);
        examPay.setRecheckTag(0);
        examPay.setCreatorId(user.getUid());
        examPay.setIsCancel(0);

        //2017-02-10 新增 固话部分数据
        examPay.setWorkUnit(workUnit);
        examPay.setIdCardNum(idCardNum);
        examPay.setAreaId(areaId);
        examPay.setAreaName(examAreaDao.get(areaId).getAreaName());
        examPay.setName(name);
        examPay.setIcon(memberIcon);

        feeDao.save(examPay);

        examMemberExam.setPayId(examPay.getId());
        ExamAgencies examAgenciesUp = examAgenciesService.get(user.getAgenciesId());
        if (examAgenciesUp!=null) {
            if (examAgenciesUp.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)) {
                examMemberExam.setPayState(1);
                examMemberExam.setPayType(9);
            } else {
                //乡镇收费处
                examMemberExam.setPayState(2);
                //2017-01-20 修改为记账方式
                examMemberExam.setPayType(3);
                examMemberExam.setPayMoney(examPay.getPayMoney());
                examMemberExam.setPayActualMoney(examPay.getPayActualMoney());
            }
        }
        examMemberExam = examMemberExamDao.updateBack(examMemberExam);

        if (examMemberExam.getId()!=0){

            // 保存各项目录入结果前的信息
            ExamPackageProject pp = new ExamPackageProject();

            pp.setPackageId(packageId);

            List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);

            if(packageProjectList!=null&&packageProjectList.size()>0){

                for(ExamPackageProject p:packageProjectList){
                    if(p.getProjectId()==SystemContent.PROJECT_MEDICAL){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_MEDICAL,SystemContent.PROJECT_TYPE_MEDICAL);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_X){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_X,SystemContent.PROJECT_TYPE_X);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_ALT){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_ALT,SystemContent.PROJECT_TYPE_ALT);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_HAV){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_HAV,SystemContent.PROJECT_TYPE_HAV);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_HEV){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_HEV,SystemContent.PROJECT_TYPE_HEV);
                    }
                    if(p.getProjectId()==SystemContent.PROJECT_CHANGDAO){
                        savePackageProjectResult(examMemberExam,SystemContent.PROJECT_CHANGDAO,SystemContent.PROJECT_TYPE_CHANGDAO);
                    }
                }
            }

        }else{

            errorMsg("");
        }

        return examMemberExam;

    }
}
