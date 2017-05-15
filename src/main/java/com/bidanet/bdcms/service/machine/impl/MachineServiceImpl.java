package com.bidanet.bdcms.service.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.GenerateBarCode;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.UserRoleDao;
import com.bidanet.bdcms.dao.businessSetting.ExamCategoryDao;
import com.bidanet.bdcms.dao.businessSetting.ExamMemberPackageDao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.dao.examBusiness.*;
import com.bidanet.bdcms.dao.examManage.ExamEcardDao;
import com.bidanet.bdcms.dao.fee.ExamPayDao;
import com.bidanet.bdcms.dao.fee.FeeDao;
import com.bidanet.bdcms.dao.systemSetting.ExamAgenciesDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.dao.wap.ExamWxBindDao;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import com.bidanet.bdcms.service.businessSetting.ExamUnitService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.machine.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 体检登记. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-12-13 16:24:40
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
@Service
public class MachineServiceImpl extends BaseServiceImpl<ExamMember> implements MachineService {

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
    private ExamAddDao examAddDao;
    @Autowired
    private ExamWxBindDao examWxBindDao;
    @Autowired
    private ExamAgenciesDao examAgenciesDao;

    @Autowired
    private ExamCategoryDao examCategoryDao;

    @Autowired
    private ExamPayDao examPayDao;
    @Autowired
    private ExamPackageProjectDao packageProjectDao;

    @Autowired
    private ExamEcardDao examEcardDao;

    @Autowired
    private ExamUnitService examUnitService;

    @Autowired
    @Qualifier("localFileDrive")
    private LocalFileDrive localFileDrive;
    @Autowired
    @Qualifier("generateBarCode")
    GenerateBarCode generateBarCode;

    public Map<String,Object> addExamTS(String name, String sex, String address, String idCardNum, String workUnit
            , String mobile, Long categoryId, Long packageId, String birthday, String imageBase64, String packagePrice) {
        Map<String,Object> map = new HashMap<String, Object>();


            //先根据身份证号码获取该用户 如果没有则新增
            // 如果有则查询该用户是不是有未检查的
            //如果没有 去查询健康证号 是否在三个月有效期内 如果在 提示 如果不在
            //如果有 则提示他是否需要作废

            String birthdayStr = birthday.replace("年", "-");
            birthdayStr = birthdayStr.replace("月", "-");
            birthdayStr = birthdayStr.replace("日", "");

            //保存之前先查询数据库是否存在该身份证号码，
            // 如果存在，修改内容；如果不存在，再新增
            ExamMember oldMember = new ExamMember();
            oldMember.setIdCardNum(idCardNum);
            List<ExamMember> memberList = examMemberDao.findByExample(oldMember);
            Date date = new Date();
            ExamMember member = new ExamMember();

//        if (workUnit!=null && workUnit!="") {
//            //工作单位必须已“常熟市”三个字开头
//            if(workUnit.length()>=3) {
//                String workBegin = workUnit.substring(0, 3);
//                if (!workBegin.equals("常熟市")) {
//                    workUnit = "常熟市" + workUnit;
//                }
//            }else{
//                workUnit = "常熟市" + workUnit;
//            }
//        }

            if (memberList.size() > 0) {
                member = memberList.get(0);
                member.setName(name);
                if (sex.equals("男")) {
                    member.setSex(1);
                } else {
                    member.setSex(0);
                }

                member.setBirthday(birthdayStr);
                member.setIdCardAddress(address);
                member.setIdCardNum(idCardNum);
                member.setMobile(mobile);
                member.setCreateTime(date.getTime());
                member.setWorkUnit(workUnit);

                String iconPath = localFileDrive.uploadBase64(imageBase64, UUID.randomUUID().toString() + "icon.jpg");

                member.setIcon(iconPath);

                member.setIsIdCardIcon(1);


                member =   examMemberDao.updateBack(member);
            } else {
                member = new ExamMember();
                member.setName(name);
                if (sex.equals("男")) {
                    member.setSex(1);
                } else {
                    member.setSex(0);
                }
                member.setBirthday(birthdayStr);
                member.setIdCardAddress(address);
                member.setIdCardNum(idCardNum);
                member.setMobile(mobile);
                member.setCreateTime(date.getTime());
                member.setWorkUnit(workUnit);
                member.setIsIdCardIcon(1);

                //头像图片
                String iconPath = localFileDrive.uploadBase64(imageBase64, UUID.randomUUID().toString() + ".jpg");

                member.setIcon(iconPath);

                member = examMemberDao.saveBack(member);
            }

            //判断用户输入的工作单位是否存在 如果不存在则新增
            ExamUnit examUnit = new ExamUnit();
            examUnit.setUnitName(workUnit);
            examUnitService.addExamUnitByThirdT(examUnit);


            //如果该用户数据库有未审核的项目 则进行作废
            ExamMemberExam newstMemberExam = new ExamMemberExam();

            newstMemberExam.setMemberId(member.getId());
            newstMemberExam.setIsNew(1);

            List<ExamMemberExam> newsMemberExamList = examMemberExamDao.findByExample(newstMemberExam);

            if (newsMemberExamList.size() > 0) {

                ExamMemberExam listMemberExam = new ExamMemberExam();

                listMemberExam = newsMemberExamList.get(0);

                //如果该用户的ectryState=0 未完成 作废

                if (listMemberExam.getVerifyConclusion()==0){

                    //作废
                   int isCancelSuccess =  cancelMemberInfo(listMemberExam.getId());

                    if (isCancelSuccess == 1){

                        //保存新数据 --耗时较长
                        map = saveMemberInfo(member, birthdayStr, workUnit, mobile, categoryId, packageId, imageBase64, packagePrice);


                    }else{

                        map.put("errorMsg","作废失败,请重试");

                    }



                }else{

                    //保存新数据
                    map = saveMemberInfo(member, birthdayStr, workUnit, mobile, categoryId, packageId, imageBase64, packagePrice);

                }


            }else{
                map = saveMemberInfo(member, birthdayStr, workUnit, mobile, categoryId, packageId, imageBase64, packagePrice);
            }


        return map;
    }

    /**
     * exam_member_exam iscancel isnew
     * exam_blod_intestinal iscancel isnew
     * exam_pay iscancel  isnew
     * @return
     */
    public int cancelMemberInfo(Long memberExamId){

        int isCancelSuccess = 0;

        ExamMemberExam examMemberExam = examMemberExamDao.get(memberExamId);

        ExamPay examPay = examPayDao.get(examMemberExam.getPayId());

        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();

        examBlodIntestinal.setIsNew(1);
        examBlodIntestinal.setMemberExamId(examMemberExam.getId());

        List<ExamBlodIntestinal> blodIntestinalList = blodIntestinalDao.findByExample(examBlodIntestinal);


        // 修改exam_blod_intestinal表is_new 状态
        if (blodIntestinalList.size()>0) {
            for (int j = 0; j < blodIntestinalList.size(); j++) {
                ExamBlodIntestinal examBlodIntestinal1 = blodIntestinalList.get(j);
                examBlodIntestinal1.setIsNew(0);
                examBlodIntestinal1.setIsCancel(1);
                blodIntestinalDao.update(examBlodIntestinal1);
            }
        }

        //examPay
        examPay.setIsCancel(1);
        examPay.setIsNew(0);

        examPay = examPayDao.updateBack(examPay);

        if (examPay.getId()!=null){

            //exammemebrexam
            examMemberExam.setIsNew(0);
            examMemberExam.setIsCancel(1);

            examMemberExam =  examMemberExamDao.updateBack(examMemberExam);

            if (examMemberExam.getId()!=null){

                isCancelSuccess = 1;

            }
        }


        return isCancelSuccess;

    }

    /**
     * 新增用户体检数据
     * @param member
     * @param birthdayStr
     * @param workUnit
     * @param mobile
     * @param categoryId
     * @param packageId
     * @param imageBase64
     * @param packagePrice
     * @return
     */
    public Map<String, Object> saveMemberInfo(ExamMember member, String birthdayStr, String workUnit
            , String mobile, Long categoryId, Long packageId, String imageBase64, String packagePrice) {

        Map<String, Object> map = new HashMap<String, Object>();

        Date date = new Date();

        // 先判断今年是否有体检单
        // 如果有，体检单号向后排；如果没有，流水码重新开始记;
        // 体检号生成规则：
        // 例：16 132 32262(其中16代表年份，132代表地区，32262为流水码，每自然年度结束后清零);


        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy");
        String yearNow = sdfo.format(date).toString();
        String newExamNumber = yearNow.substring(yearNow.length()-2,yearNow.length());
        newExamNumber += SystemContent.JIKONGZHONGXIN;
        ExamNumber examNumber = new ExamNumber();
        examNumber.setMemberId(member.getId());
        examNumber.setAreaCode(SystemContent.JIKONGZHONGXIN);
        List<ExamNumber> numberList = examNumberDao.findListByCreateTime(getCurrYearFirst().getTime(),SystemContent.JIKONGZHONGXIN);
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
        map.put("examNumber", examNumber);

        //向体检表添加一条数据--新插入记录
        ExamMemberExam examMemberExam = new ExamMemberExam();

        //数据保存成功后，将之前的一条体检数据值is_new置为0
        ExamMemberExam oldMemberExam = new ExamMemberExam();

        oldMemberExam.setIsNew(1);
        oldMemberExam.setMemberId(member.getId());

        List<ExamMemberExam> oldMemberExamList = examMemberExamDao.findByExample(oldMemberExam);

        if (oldMemberExamList.size() > 0) {//该人员记录不为空 则需要修改

            oldMemberExam = oldMemberExamList.get(0);

        }

        examMemberExam.setCreateTime(date.getTime());
        examMemberExam.setMemberId(member.getId());
        examMemberExam.setCategoryId(categoryId);
        examMemberExam.setMobile(mobile);

        ExamAgencies examAgencies = new ExamAgencies();
        examAgencies.setAgenciesCode(SystemContent.JIKONGZHONGXIN);
        List<ExamAgencies> examAgenciesList = examAgenciesDao.findByExample(examAgencies);
        if (examAgenciesList.size() > 0) {
            ExamAgencies examAgencie = examAgenciesList.get(0);
            examMemberExam.setAgenciesId(examAgencie.getId());
        }
        examMemberExam.setExamTime(date.getTime());
        examMemberExam.setExamNumberId(examNumber.getId());
        examMemberExam.setExamNumber(examNumber.getNumber());


        examMemberExam.setWorkUnit(workUnit);
        examMemberExam.setVerifyState(2);
        examMemberExam.setVerifyConclusion(0);
        examMemberExam.setEntryState(0);
        examMemberExam.setIsCancel(0);
        examMemberExam.setIsPhysicalPrint(0);
        examMemberExam.setPackageId(packageId);
        examMemberExam.setIHA("阴性");
        examMemberExam.setDDIA("阴性");
        //自助机用户
        examMemberExam.setChannel(3);
        //是否是三星用户
        //通过生日计算年龄
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dbDate = null;
        try {
            dbDate = dateFormat.parse(birthdayStr);
            int age = getAge(dbDate);
            examMemberExam.setAge(age);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        examMemberExam.setIsRecheck(0);
        examMemberExam.setPayMoney(new BigDecimal(packagePrice));
        examMemberExam.setIsNew(1);
        examMemberExam.setExamNumberIcon(generateBarCode.generateFile(examNumber.getNumber()));


        ExamArea examArea = new ExamArea();
        examArea.setAreaCode(SystemContent.JIKONGZHONGXIN);
        List<ExamArea> areaList = examAreaDao.findByExample(examArea);
        if (areaList.size() > 0) {
            examMemberExam.setAreaId(areaList.get(0).getId());
        }
//        examMemberExam.setAgenciesId(user.getAgenciesId());
        examMemberExam.setIsRecheckPrint(0);
        examMemberExam.setIsShowRecheck(0);
        examMemberExam.setRecheckTag(0);


        //2017-02-10 新增 将部分数据进行固化
        examMemberExam.setIcon(member.getIcon());
        examMemberExam.setIdCardNum(member.getIdCardNum());
        examMemberExam.setName(member.getName());
        examMemberExam.setSex(member.getSex());
        examMemberExam.setBirthday(member.getBirthday());
        examMemberExam.setAreaName(examAreaDao.get(examMemberExam.getAreaId()).getAreaName());
        examMemberExam.setCategoryName(examCategoryDao.get(categoryId).getCategoryName());
        examMemberExam.setAgenciesName(examAgenciesDao.get(examMemberExam.getAgenciesId()).getAgenciesName());
        examMemberExam.setAgenciesCode(examAgenciesDao.get(examMemberExam.getAgenciesId()).getAgenciesCode());
        examMemberExam.setIsIdCardIcon(member.getIsIdCardIcon());

        examMemberExam = examMemberExamDao.saveBack(examMemberExam);
        map.put("examMemberExam", examMemberExam);

        ExamPay oldExamPay = new ExamPay();

        if (examMemberExam.getId() != null) {

            if (oldMemberExam.getId() != null) {

                oldMemberExam.setIsNew(0);

                oldMemberExam = examMemberExamDao.updateBack(oldMemberExam);

                if (oldMemberExam.getId() != 0) {

                    oldExamPay = examPayDao.get(oldMemberExam.getPayId());
                }

            }

        }


        //向收费表添加一条数据
        ExamPay examPay = new ExamPay();
        examPay.setExamMemberId(examMemberExam.getId());
        examPay.setPayMoney(new BigDecimal(packagePrice));
        examPay.setPayState(1);
        examPay.setPayType(9);
        examPay.setCreateTime(date.getTime());
        examPay.setIsPrint(0);
        examPay.setIsSpecial(0);
        examPay.setIsNew(1);
        examPay.setRecheckTag(0);
        examPay.setIsCancel(0);
        examPay.setPayActualMoney(new BigDecimal(packagePrice));
        examPay.setExamNumberId(examNumber.getId());
        examPay.setExamNumber(newExamNumber);
        examPay.setExamMemberId(examMemberExam.getId());

        //2017-02-10 新增 固化部分数据
        examPay.setIcon(member.getIcon());
        examPay.setWorkUnit(workUnit);
        examPay.setIdCardNum(member.getIdCardNum());
        examPay.setAreaId(examMemberExam.getAreaId());
        examPay.setAreaName(examAreaDao.get(examMemberExam.getAreaId()).getAreaName());
        examPay.setName(member.getName());

        examPay = feeDao.saveBack(examPay);

        if (examPay.getId() != null) {

            if (oldExamPay.getId() != null) {
                //更新旧数据
                oldExamPay.setIsNew(0);

                examPayDao.update(oldExamPay);
            }

        }

        //pay保存成功后进行回调填值
        examMemberExam.setPayId(examPay.getId());
        examMemberExam.setPayState(1);
        examMemberExam.setPayType(9);
        examMemberExamDao.update(examMemberExam);

        // 保存内科，X线录入结果前的信息
//        saveMedicalXResult(examMemberExam, oldMemberExam, SystemContent.PROJECT_MEDICAL, SystemContent.PROJECT_TYPE_MEDICAL);
//        saveMedicalXResult(examMemberExam, oldMemberExam, SystemContent.PROJECT_X, SystemContent.PROJECT_TYPE_X);

        //保存各项目录入结果前的信息
        ExamPackageProject pp = new ExamPackageProject();

        pp.setPackageId(packageId);

        List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);

        if(packageProjectList!=null&&packageProjectList.size()>0){

            for(ExamPackageProject p:packageProjectList){
                if(p.getProjectId()==SystemContent.PROJECT_MEDICAL){
                    savePackageProjectResult(examMemberExam,oldMemberExam,SystemContent.PROJECT_MEDICAL,SystemContent.PROJECT_TYPE_MEDICAL);
                }
                if(p.getProjectId()==SystemContent.PROJECT_X){
                    savePackageProjectResult(examMemberExam,oldMemberExam,SystemContent.PROJECT_X,SystemContent.PROJECT_TYPE_X);
                }
                if(p.getProjectId()==SystemContent.PROJECT_ALT){
                    savePackageProjectResult(examMemberExam,oldMemberExam,SystemContent.PROJECT_ALT,SystemContent.PROJECT_TYPE_ALT);
                }
                if(p.getProjectId()==SystemContent.PROJECT_HAV){
                    savePackageProjectResult(examMemberExam,oldMemberExam,SystemContent.PROJECT_HAV,SystemContent.PROJECT_TYPE_HAV);
                }
                if(p.getProjectId()==SystemContent.PROJECT_HEV){
                    savePackageProjectResult(examMemberExam,oldMemberExam,SystemContent.PROJECT_HEV,SystemContent.PROJECT_TYPE_HEV);
                }
                if(p.getProjectId()==SystemContent.PROJECT_CHANGDAO){
                    savePackageProjectResult(examMemberExam,oldMemberExam,SystemContent.PROJECT_CHANGDAO,SystemContent.PROJECT_TYPE_CHANGDAO);
                }
            }
        }

        return map;
    }

    public static int getAge(Date birthday) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (birthday != null) {
            now.setTime(new Date());
            born.setTime(birthday);
            if (born.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }
        }
        return age;
    }

    /**
     * 保存各项目录入结果前的信息
     * @param examMemberExam
     */
    private void savePackageProjectResult(ExamMemberExam examMemberExam,ExamMemberExam oldMemberExam,Long projectId,Integer type) {
        //获取该用户最新的数据
        List<ExamBlodIntestinal> oldBlodIntestinalList = new ArrayList<ExamBlodIntestinal>();
        ExamBlodIntestinal oldBlodIntestinal = new ExamBlodIntestinal();
        if (oldMemberExam.getId()!=null){
            oldBlodIntestinal.setIsNew(1);
            oldBlodIntestinal.setMemberExamId(oldMemberExam.getId());

            oldBlodIntestinalList = blodIntestinalDao.findByExample(oldBlodIntestinal);
        }




        Boolean isHaveOldList = false;

        if (oldBlodIntestinalList.size() > 0) {//有数据

            //先保存新的数据 新数据保存成功后将老数据修改
            isHaveOldList = true;

        }


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
        result.setIsShowRefresh(0);
        result.setRecheckTag(0);
        result.setIsCancel(0);
        result.setCreateTime(new Date().getTime());
        result = blodIntestinalDao.saveBack(result);


        if (result.getId() != 0 && isHaveOldList) {

            //修改
            for (ExamBlodIntestinal blodIntestinal : oldBlodIntestinalList) {

                blodIntestinal.setIsNew(0);

                blodIntestinalDao.save(blodIntestinal);
            }

        }
    }


//    /**
//     * 保存内科，X线录入结果前的信息
//     *
//     * @param examMemberExam
//     */
//    private void saveMedicalXResult(ExamMemberExam examMemberExam, ExamMemberExam oldMemberExam, Long projectId, Integer type) {
//        //获取该用户最新的数据
//        List<ExamBlodIntestinal> oldBlodIntestinalList = new ArrayList<ExamBlodIntestinal>();
//
//        ExamBlodIntestinal oldBlodIntestinal = new ExamBlodIntestinal();
//
//        oldBlodIntestinal.setIsNew(1);
//        oldBlodIntestinal.setMemberExamId(oldMemberExam.getId());
//
//        oldBlodIntestinalList = blodIntestinalDao.findByExample(oldBlodIntestinal);
//
//        Boolean isHaveOldList = false;
//
//        if (oldBlodIntestinalList.size() > 0) {//有数据
//
//            //先保存新的数据 新数据保存成功后将老数据修改
//            isHaveOldList = true;
//
//        }
//
//
//        ExamBlodIntestinal result = new ExamBlodIntestinal();
//        result.setExamNumber(examMemberExam.getExamNumber());
//        result.setMemberExamId(examMemberExam.getId());
//        result.setProjectId(projectId);
//        ExamProject project = projectDao.get(SystemContent.PROJECT_MEDICAL);
//        result.setProjectName(project.getProjectName());
//        result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN);
//        result.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
//        result.setType(type);
//        result.setEntryState(0);
//        result.setIsNew(1);
//        result.setIsShowRefresh(0);
//        result = blodIntestinalDao.saveBack(result);
//
//
//        if (result.getId() != 0 && isHaveOldList) {
//
//            //修改
//            for (ExamBlodIntestinal blodIntestinal : oldBlodIntestinalList) {
//
//                blodIntestinal.setIsNew(0);
//
//                blodIntestinalDao.save(blodIntestinal);
//            }
//
//        }
//
//
//    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
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
     *
     * @param path
     * @return
     */
    public String uploadPic(String path) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tempPath = "/upload/img/" + simpleDateFormat.format(new Date());
            String realPath = SpringWebTool.getRealPath(tempPath);
            File directory = new File(realPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            String s = directory + "/" + uuid.toString() + ".jpg";
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
     * @param base64 base64编码的图片信息
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
    public List<ExamMember> getExamMemberByIdCardNum(String idCardNum) {
        return examAddDao.getExamMemberByIdCardNum(idCardNum);
    }

    @Override
    public List<ExamWxBind> getExamWxBindByOpenId(String openId) {
        return examWxBindDao.getListByOpenId(openId);
    }

    /**
     * 判断是否3个月 是否需要作废
     * @param idCardNum
     * @return
     * map  isnew先处理
     * map  isThirty后处理
     * map isCancel最后处理
     */
    @Override
    public JSONObject judgeExamMember(String idCardNum,String judgeTag) {


        JSONObject json = new JSONObject();

        ExamMember examMember = new ExamMember();

        examMember.setIdCardNum(idCardNum);

        List<ExamMember> examMemberList = examMemberDao.findByExample(examMember);


        if(examMemberList.size()>0){//有该用户

            json.put("isNew",1);

            //是否3个月
            if (Integer.parseInt(judgeTag)==1){

                examMember=examMemberList.get(0);
                //获取健康证
                ExamEcard examEcard = new ExamEcard();
                examEcard.setMemberId(examMember.getId());

                List<ExamEcard> examEcardList = examEcardDao.findByExampleDESC(examEcard,"id");

                if(examEcardList.size()>0){

                    ExamEcard newExamEcard = examEcardList.get(0);

                    Long currentTime = new Date().getTime();
                    //健康证过期时间-现在的时间 如果大于3个月 提示 如果不大于三个月不提示
                    Long cauculateTime = newExamEcard.getExpTime()-currentTime;

                    if (cauculateTime>0){

                        if (cauculateTime/(1000 * 60 * 60 *24)>90){

                            //超过3个月
                            json.put("isThirty",1);

                            //判断是否有未完成的项目
                            int isCancel = judgeCancel(examMember.getId());

                            if(isCancel == 1){
                                json.put("isCancel",1);
                            }

                        }else{

                            json.put("isThirty",0);

                            //不超过3个月 直接判断是否作废
                            int isCancel = judgeCancel(examMember.getId());

                            if(isCancel == 1){
                                json.put("isCancel",1);
                            }

                        }

                    }else{

                        //不超过3个月 直接判断是否作废
                        int isCancel = judgeCancel(examMember.getId());

                        if(isCancel == 1){
                            json.put("isCancel",1);
                        }
                    }





                }else{
                    //无健康证 直接判断是否作废
                    int isCancel = judgeCancel(examMember.getId());

                    if(isCancel == 1){
                        json.put("isCancel",1);
                    }

                }

            }else if(Integer.parseInt(judgeTag)==2){//是否有复检

                int isCancel = judgeCancel(examMember.getId());

                if(isCancel == 1){
                    json.put("isCancel",1);
                }else{
                    json.put("isCancel",0);
                }


            }




        }else{//无该用户，新增处理

            json.put("isNew",0);


        }

        return json;
    }

    /**
     * 根据用户id获取是否
     * @param memberId
     * @return
     */
    public int judgeCancel(Long memberId){

        int isCancel = 0;

        ExamMemberExam examMemberExam = new ExamMemberExam();

        examMemberExam.setMemberId(memberId);

        examMemberExam.setIsNew(1);

        List<ExamMemberExam> examMemberExamList = examMemberExamDao.findByExampleDESC(examMemberExam,"id");

        if(examMemberExamList.size()>0){

            ExamMemberExam newMemberExam = examMemberExamList.get(0);

            if(newMemberExam.getVerifyConclusion()==0){
                //有未完成项目
                isCancel = 1;

            }

        }

        return isCancel;
    }

}


