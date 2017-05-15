package com.bidanet.bdcms.service.wap.impl;

import com.bidanet.bdcms.bean.GenerateBarCode;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.UserRoleDao;
import com.bidanet.bdcms.dao.businessSetting.ExamCategoryDao;
import com.bidanet.bdcms.dao.businessSetting.ExamMemberPackageDao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.dao.businessSetting.ExamUnitDao;
import com.bidanet.bdcms.dao.examBusiness.*;
import com.bidanet.bdcms.dao.fee.ExamPayDao;
import com.bidanet.bdcms.dao.fee.FeeDao;
import com.bidanet.bdcms.dao.systemSetting.ExamAgenciesDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.dao.wap.ExamWxBindDao;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.businessSetting.ExamUnitService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.wap.WapService;
import org.apache.commons.lang.StringUtils;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
public class WapServiceImpl extends BaseServiceImpl<ExamMember> implements WapService {

    @Autowired
    private ExamNumberDao examNumberDao;

    @Autowired
    private ExamPayDao examPayDao;

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
    private ExamPackageProjectDao packageProjectDao;
    @Autowired
    private ExamUnitService examUnitService;
    @Autowired
    @Qualifier("generateBarCode")
    GenerateBarCode generateBarCode;

    public void addExamTS(int sex, int age, String idCardNum, String company
            , String phoneNum, Long categoryId, Long packageId, BigDecimal packagePrice, String birthday) {
        //保存之前先查询数据库是否存在该身份证号码，
        // 如果存在，修改内容；如果不存在，再新增
        ExamMember oldMember = new ExamMember();
        oldMember.setIdCardNum(idCardNum);
        List<ExamMember> memberList = examMemberDao.findByExample(oldMember);
        Date date = new Date();
        ExamMember member;
//        if (company!=null && company!="") {
//            //工作单位必须已“常熟市”三个字开头
//            if(company.length()>=3) {
//                String workBegin = company.substring(0, 3);
//                if (!workBegin.equals("常熟市")) {
//                    company = "常熟市" + company;
//                }
//            }else{
//                company = "常熟市" + company;
//            }
//        }
        if (memberList.size()>0) {
            member = memberList.get(0);
//            member.setName(name);
            member.setSex(sex);
            member.setBirthday(birthday);
//            member.setIdCardAddress(address);
            member.setIdCardNum(idCardNum);
            member.setMobile(phoneNum);
            member.setCreateTime(date.getTime());
            member.setWorkUnit(company);

            member =  examMemberDao.updateBack(member);
        } else {
            member =  new ExamMember();
//            member.setName(name);
            member.setSex(sex);
            member.setBirthday(birthday);
//            member.setIdCardAddress(address);
            member.setIdCardNum(idCardNum);
            member.setMobile(phoneNum);
            member.setCreateTime(date.getTime());
            member.setWorkUnit(company);
            member.setIsIdCardIcon(0);
            member = examMemberDao.saveBack(member);
        }
        // 先判断今年是否有体检单
        // 如果有，体检单号向后排；如果没有，流水码重新开始记;
        // 体检号生成规则：
        // 例：16 132 32262(其中16代表年份，132代表地区，32262为流水码，每自然年度结束后清零);
        ExamArea examArea = examAreaDao.get(SystemContent.JIKONGZHONGXINID);
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy");
        String yearNow = sdfo.format(date).toString();
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
        //将工作单位添加到表中
        ExamUnit examUnit = new ExamUnit();
        examUnit.setUnitName(company);
        examUnitService.addExamUnitByThirdT(examUnit);
        //取消  之前的 体检

        ExamMemberExam oldExam = new ExamMemberExam();
        oldExam.setMemberId(member.getId());
        List<ExamMemberExam> examMemberExamList = examMemberExamDao.findByExample(oldExam);
        if (examMemberExamList.size()>0) {

            for (int i = 0; i < examMemberExamList.size(); i++) {
                cancelMemberInfo(examMemberExamList.get(i).getId());
            }
        }
        //向体检表添加一条数据
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setCreateTime(date.getTime());
        examMemberExam.setMemberId(member.getId());
        examMemberExam.setMobile(phoneNum);
        examMemberExam.setExamTime(date.getTime());
        examMemberExam.setExamNumberId(examNumber.getId());
        examMemberExam.setExamNumber(examNumber.getNumber());
        examMemberExam.setWorkUnit(company);
        examMemberExam.setVerifyState(2);
        examMemberExam.setVerifyConclusion(0);
        examMemberExam.setAge(age);
        examMemberExam.setIsRecheck(0);
        examMemberExam.setExamNumberIcon(generateBarCode.generateFile(examNumber.getNumber()));
        examMemberExam.setIsNew(1);
        examMemberExam.setRecheckTag(0);
        examMemberExam.setIsRecheckPrint(0);
        examMemberExam.setEntryState(0);
        examMemberExam.setIsCancel(0);
        examMemberExam.setAreaId(SystemContent.JIKONGZHONGXINID);
        examMemberExam.setAgenciesId((long)2);
        examMemberExam.setCategoryId(categoryId);
        examMemberExam.setPackageId(packageId);
        examMemberExam.setIsShowRecheck(0);
        examMemberExam.setIsPhysicalPrint(0);
        examMemberExam.setChannel(2);
        examMemberExam.setIHA("阴性");
        examMemberExam.setDDIA("阴性");
        //2017-02-10 新增 将部分数据进行固化
        examMemberExam.setIdCardNum(idCardNum);
//        examMemberExam.setName(name);
        examMemberExam.setSex(sex);
        examMemberExam.setBirthday(birthday);
        examMemberExam.setAreaName(examAreaDao.get(SystemContent.JIKONGZHONGXINID).getAreaName());
        examMemberExam.setCategoryName(examCategoryDao.get(categoryId).getCategoryName());
        examMemberExam.setAgenciesName(examAgenciesDao.get((long)2).getAgenciesName());
        examMemberExam.setAgenciesCode(examAgenciesDao.get((long)2).getAgenciesCode());
        examMemberExam.setIsIdCardIcon(member.getIsIdCardIcon());
        if(StringUtils.isNotEmpty(member.getName())){
            examMemberExam.setName(member.getName());
        }

        if (StringUtils.isNotEmpty(member.getIcon())){
            examMemberExam.setIcon(member.getIcon());
        }


        examMemberExam = examMemberExamDao.saveBack(examMemberExam);

        //向收费表添加一条数据
        ExamPay examPay = new ExamPay();
        examPay.setExamMemberId(examMemberExam.getId());
        examPay.setPayMoney(packagePrice);
        //默认为已收费。其他为未收费
        examPay.setPayState(1);
        //未付款
        examPay.setPayType(9);
        examPay.setCreateTime(date.getTime());
        examPay.setIsPrint(0);
        examPay.setExamNumberId(examNumber.getId());
        examPay.setExamNumber(newExamNumber);
        examPay.setIsSpecial(0);
        examPay.setPayActualMoney(new BigDecimal(0));
        examPay.setIsNew(1);
        examPay.setRecheckTag(0);
//        examPay.setCreatorId(user.getUid());
        examPay.setIsCancel(0);

        //2017-02-10 新增 固化部分数据
        examPay.setWorkUnit(company);
        examPay.setIdCardNum(member.getIdCardNum());
        examPay.setAreaId(examMemberExam.getAreaId());
        examPay.setAreaName(examAreaDao.get(examMemberExam.getAreaId()).getAreaName());
//        examPay.setName(member.getName());
        if(StringUtils.isNotEmpty(member.getName())){
            examPay.setName(member.getName());
        }

        if (StringUtils.isNotEmpty(member.getIcon())){
            examPay.setIcon(member.getIcon());
        }

        feeDao.save(examPay);

        examMemberExam.setPayId(examPay.getId());
        examMemberExam.setPayState(1);
        examMemberExam.setPayType(9);
        examMemberExam.setPayMoney(examPay.getPayMoney());
        examMemberExam.setPayActualMoney(examPay.getPayActualMoney());
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
    }

    @Override
    public void addExamOfCompanyAddTS(int sex, int age, String idCardNum, String company, String phoneNum, Long categoryId, Long packageId, BigDecimal packagePrice, String birthday) {
        //保存之前先查询数据库是否存在该身份证号码，
        // 如果存在，修改内容；如果不存在，再新增
        ExamMember oldMember = new ExamMember();
        oldMember.setIdCardNum(idCardNum);
        List<ExamMember> memberList = examMemberDao.findByExample(oldMember);
        Date date = new Date();
        ExamMember member;
//        if (company!=null && company!="") {
//            //工作单位必须已“常熟市”三个字开头
//            if(company.length()>=3) {
//                String workBegin = company.substring(0, 3);
//                if (!workBegin.equals("常熟市")) {
//                    company = "常熟市" + company;
//                }
//            }else{
//                company = "常熟市" + company;
//            }
//        }
        if (memberList.size()>0) {
            member = memberList.get(0);
//            member.setName(name);
            member.setSex(sex);
            member.setBirthday(birthday);
//            member.setIdCardAddress(address);
            member.setIdCardNum(idCardNum);
            member.setMobile(phoneNum);
            member.setCreateTime(date.getTime());
            member.setWorkUnit(company);

            member =  examMemberDao.updateBack(member);
        } else {
            member =  new ExamMember();
//            member.setName(name);
            member.setSex(sex);
            member.setBirthday(birthday);
//            member.setIdCardAddress(address);
            member.setIdCardNum(idCardNum);
            member.setMobile(phoneNum);
            member.setCreateTime(date.getTime());
            member.setWorkUnit(company);
            member.setIsIdCardIcon(0);
            member = examMemberDao.saveBack(member);
        }
        // 先判断今年是否有体检单
        // 如果有，体检单号向后排；如果没有，流水码重新开始记;
        // 体检号生成规则：
        // 例：16 132 32262(其中16代表年份，132代表地区，32262为流水码，每自然年度结束后清零);
        ExamArea examArea = examAreaDao.get(SystemContent.JIKONGZHONGXINID);
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy");
        String yearNow = sdfo.format(date).toString();
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
        //将工作单位添加到表中
        ExamUnit examUnit = new ExamUnit();
        examUnit.setUnitName(company);
        examUnitService.addExamUnitByThirdT(examUnit);
        //取消  之前的 体检

        ExamMemberExam oldExam = new ExamMemberExam();
        oldExam.setMemberId(member.getId());
        List<ExamMemberExam> examMemberExamList = examMemberExamDao.findByExample(oldExam);
        if (examMemberExamList.size()>0) {

            for (int i = 0; i < examMemberExamList.size(); i++) {
                cancelMemberInfo(examMemberExamList.get(i).getId());
            }
        }
        //向体检表添加一条数据
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setCreateTime(date.getTime());
        examMemberExam.setMemberId(member.getId());
        examMemberExam.setMobile(phoneNum);
        examMemberExam.setExamTime(date.getTime());
        examMemberExam.setExamNumberId(examNumber.getId());
        examMemberExam.setExamNumber(examNumber.getNumber());
        examMemberExam.setWorkUnit(company);
        examMemberExam.setVerifyState(2);
        examMemberExam.setVerifyConclusion(0);
        examMemberExam.setAge(age);
        examMemberExam.setIsRecheck(0);
        examMemberExam.setExamNumberIcon(generateBarCode.generateFile(examNumber.getNumber()));
        examMemberExam.setIsNew(1);
        examMemberExam.setRecheckTag(0);
        examMemberExam.setIsRecheckPrint(0);
        examMemberExam.setEntryState(0);
        examMemberExam.setIsCancel(0);
        examMemberExam.setAreaId(SystemContent.JIKONGZHONGXINID);
        examMemberExam.setAgenciesId((long)2);
        examMemberExam.setCategoryId(categoryId);
        examMemberExam.setPackageId(packageId);
        examMemberExam.setIsShowRecheck(0);
        examMemberExam.setIsPhysicalPrint(0);
        examMemberExam.setChannel(4);
        examMemberExam.setIHA("阴性");
        examMemberExam.setDDIA("阴性");

        //2017-02-10 新增 将部分数据进行固化
        examMemberExam.setIdCardNum(idCardNum);
//        examMemberExam.setName(name);
        examMemberExam.setSex(sex);
        examMemberExam.setBirthday(birthday);
        examMemberExam.setAreaName(examAreaDao.get(SystemContent.JIKONGZHONGXINID).getAreaName());
        examMemberExam.setCategoryName(examCategoryDao.get(categoryId).getCategoryName());
        examMemberExam.setAgenciesName(examAgenciesDao.get((long)2).getAgenciesName());
        examMemberExam.setAgenciesCode(examAgenciesDao.get((long)2).getAgenciesCode());
        examMemberExam.setIsIdCardIcon(member.getIsIdCardIcon());
        if(StringUtils.isNotEmpty(member.getName())){
            examMemberExam.setName(member.getName());
        }

        if (StringUtils.isNotEmpty(member.getIcon())){
            examMemberExam.setIcon(member.getIcon());
        }


        examMemberExam = examMemberExamDao.saveBack(examMemberExam);

        //向收费表添加一条数据
        ExamPay examPay = new ExamPay();
        examPay.setExamMemberId(examMemberExam.getId());
        examPay.setPayMoney(packagePrice);
        //默认为已收费。其他为未收费
        examPay.setPayState(2);
        //未付款
        //examPay.setPayType(3);
        examPay.setPayType(2);
        examPay.setCreateTime(date.getTime());
        examPay.setIsPrint(0);
        examPay.setExamNumberId(examNumber.getId());
        examPay.setExamNumber(newExamNumber);
        examPay.setIsSpecial(0);
        examPay.setPayActualMoney(new BigDecimal(0));
        examPay.setIsNew(1);
        examPay.setRecheckTag(0);
//        examPay.setCreatorId(user.getUid());
        examPay.setIsCancel(0);

        //2017-02-10 新增 固化部分数据
        examPay.setWorkUnit(company);
        examPay.setIdCardNum(member.getIdCardNum());
        examPay.setAreaId(examMemberExam.getAreaId());
        examPay.setAreaName(examAreaDao.get(examMemberExam.getAreaId()).getAreaName());
//        examPay.setName(member.getName());
        if(StringUtils.isNotEmpty(member.getName())){
            examPay.setName(member.getName());
        }

        if (StringUtils.isNotEmpty(member.getIcon())){
            examPay.setIcon(member.getIcon());
        }

        feeDao.save(examPay);

        examMemberExam.setPayId(examPay.getId());
        examMemberExam.setPayState(2);
        examMemberExam.setPayType(2);
        examMemberExam.setPayMoney(examPay.getPayMoney());
        examMemberExam.setPayActualMoney(examPay.getPayActualMoney());
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

    public void addNewExamMemberTS(String openId, int sex, int age, String idCardNum, String company
            , String phoneNum, Long categoryId, Long packageId, BigDecimal packagePrice, String birthday) {
        Date date = new Date();
//        if (company!=null && company!="") {
//            //工作单位必须已“常熟市”三个字开头
//            if(company.length()>=3) {
//                String workBegin = company.substring(0, 3);
//                if (!workBegin.equals("常熟市")) {
//                    company = "常熟市" + company;
//                }
//            }else{
//                company = "常熟市" + company;
//            }
//        }
        ExamMember member =  new ExamMember();
//        member.setName(name);
        member.setSex(sex);
        member.setBirthday(birthday);
//        member.setIdCardAddress(address);
        member.setIdCardNum(idCardNum);
        member.setMobile(phoneNum);
        member.setCreateTime(date.getTime());
        member.setWorkUnit(company);
        member.setIsIdCardIcon(0);
        member = examMemberDao.saveBack(member);

        ExamWxBind examWxBind = new ExamWxBind();
        examWxBind.setMemberId(member.getId());
        examWxBind.setWxOpenId(openId);
        examWxBindDao.saveBack(examWxBind);
        // 先判断今年是否有体检单
        // 如果有，体检单号向后排；如果没有，流水码重新开始记;
        // 体检号生成规则：
        // 例：16 132 32262(其中16代表年份，132代表地区，32262为流水码，每自然年度结束后清零);
        ExamArea examArea = examAreaDao.get(SystemContent.JIKONGZHONGXINID);
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy");
        String yearNow = sdfo.format(date).toString();
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

        //向体检表添加一条数据
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setCreateTime(date.getTime());
        examMemberExam.setMemberId(member.getId());
        examMemberExam.setMobile(phoneNum);
        examMemberExam.setExamTime(date.getTime());
        examMemberExam.setExamNumberId(examNumber.getId());
        examMemberExam.setExamNumber(examNumber.getNumber());
        examMemberExam.setWorkUnit(company);
        examMemberExam.setVerifyState(2);
        examMemberExam.setVerifyConclusion(0);
        examMemberExam.setAge(age);
        examMemberExam.setIsRecheck(0);
        examMemberExam.setExamNumberIcon(generateBarCode.generateFile(examNumber.getNumber()));
        examMemberExam.setIsNew(1);
        examMemberExam.setRecheckTag(0);
        examMemberExam.setIsRecheckPrint(0);
        examMemberExam.setEntryState(0);
        examMemberExam.setIsCancel(0);
        examMemberExam.setAreaId(SystemContent.JIKONGZHONGXINID);
        examMemberExam.setAgenciesId((long)2);
        examMemberExam.setCategoryId(categoryId);
        examMemberExam.setPackageId(packageId);
        examMemberExam.setIsShowRecheck(0);
        examMemberExam.setIsPhysicalPrint(0);
        examMemberExam.setChannel(2);
        examMemberExam.setIHA("阴性");
        examMemberExam.setDDIA("阴性");

        //2017-02-10 新增 将部分数据进行固化
        examMemberExam.setIdCardNum(idCardNum);
//        examMemberExam.setName(name);
        examMemberExam.setSex(sex);
        examMemberExam.setBirthday(birthday);
        examMemberExam.setAreaName(examAreaDao.get(SystemContent.JIKONGZHONGXINID).getAreaName());
        examMemberExam.setCategoryName(examCategoryDao.get(categoryId).getCategoryName());
        examMemberExam.setAgenciesName(examAgenciesDao.get((long)2).getAgenciesName());
        examMemberExam.setAgenciesCode(examAgenciesDao.get((long)2).getAgenciesCode());
        examMemberExam.setIsIdCardIcon(0);

        examMemberExam = examMemberExamDao.saveBack(examMemberExam);

        //向收费表添加一条数据
        ExamPay examPay = new ExamPay();
        examPay.setExamMemberId(examMemberExam.getId());
        examPay.setPayMoney(packagePrice);
        //如果是乡镇卫生院用户登录，默认为已收费。其他为未收费
        examPay.setPayState(1);
        examPay.setPayType(9);
        examPay.setCreateTime(date.getTime());
        examPay.setIsPrint(0);
        examPay.setExamNumberId(examNumber.getId());
        examPay.setExamNumber(newExamNumber);
        examPay.setIsSpecial(0);
        examPay.setPayActualMoney(new BigDecimal(0));
        examPay.setIsNew(1);
        examPay.setRecheckTag(0);
//        examPay.setCreatorId(user.getUid());
        examPay.setIsCancel(0);

        //2017-02-10 新增 固化部分数据
        examPay.setWorkUnit(company);
        examPay.setIdCardNum(member.getIdCardNum());
        examPay.setAreaId(examMemberExam.getAreaId());
        examPay.setAreaName(examAreaDao.get(examMemberExam.getAreaId()).getAreaName());
//        examPay.setName(member.getName());

        feeDao.save(examPay);

        examMemberExam.setPayId(examPay.getId());
        examMemberExam.setPayState(1);
        examMemberExam.setPayType(9);
        examMemberExam.setPayMoney(examPay.getPayMoney());
        examMemberExam.setPayActualMoney(examPay.getPayActualMoney());
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
     * 保存内科，X线录入结果前的信息
     * @param examMemberExam
     */
    private void saveMedicalXResult(ExamMemberExam examMemberExam,Long projectId,Integer type) {
        ExamBlodIntestinal result = new ExamBlodIntestinal();
        result.setExamNumber(examMemberExam.getExamNumber());
        result.setMemberExamId(examMemberExam.getId());
        result.setProjectId(projectId);
        ExamProject project = projectDao.get(SystemContent.PROJECT_MEDICAL);
        result.setProjectName(project.getProjectName());
        result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN);
        result.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
        result.setType(type);
        result.setEntryState(0);
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
            decodeBase64ToImage(path,s);
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
    public List<ExamMember> getExamMemberByIdCardNum(String idCardNum) {
        return examAddDao.getExamMemberByIdCardNum(idCardNum);
    }

    @Override
    public List<ExamWxBind> getExamWxBindByOpenId(String openId) {
        return examWxBindDao.getListByOpenId(openId);
    }

    @Override
    public List<ExamBlodIntestinal> getBlodByExamId(Long examId) {
        return blodIntestinalDao.findByMemberExamId(examId);
    }

}
