package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.bean.PhysicalItemDetails;
import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamDoctorDao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.dao.examBusiness.ExamBlodIntestinalDao;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberExamDao;
import com.bidanet.bdcms.dao.fee.ExamPayDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.*;

import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
* cf
*/
@Service
public class ExamBlodIntestinalServiceImpl extends BaseServiceImpl<ExamBlodIntestinal> implements ExamBlodIntestinalService {
    @Autowired
    private ExamBlodIntestinalDao blodIntestinalDao;
    @Override
    protected Dao getDao() {
        return blodIntestinalDao;
    }
    @Autowired
    private ExamProjectDao projectDao;
    @Autowired
    private ExamMemberExamDao memberExamDao;
    @Autowired
    private ExamPayDao payDao;
    @Autowired
    private ExamPackageProjectDao packageProjectDao;
    @Autowired
    private ExamMemberExamDao examMemberExamDao;
    @Autowired
    private ExamDoctorDao examDoctorDao;

    /**
     * 获取列表页面首个血检信息的血检结果信息
     * @param id
     */
    @Override
    public List<ExamBlodIntestinal> findSelectedBlodDetail(long id) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamBlodId(id);
        //未作废
        blodIntestinal.setIsCancel(0);
        List<ExamBlodIntestinal> examBlodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal);
        if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0){
            return examBlodIntestinalList;
        }else{
            return null;
        }
    }

    //保存左侧弹出框血检录入信息
    @Override
    public void entryBlodResultT(Long blodId, String isQualified5, String isQualified7, String isQualified8,
                                 String blodResult5, String blodResult7, String blodResult8,
                                 String isRecheck5, String isRecheck7, String isRecheck8, String descriptionVal, String realName) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamBlodId(blodId);
        List<ExamBlodIntestinal> blodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal);
        if(blodIntestinalList!=null&&blodIntestinalList.size()>0){
           for (ExamBlodIntestinal bi:blodIntestinalList){
               if(bi.getProjectId()== SystemContent.PROJECT_HEV){
                   if("阳性".equals(blodResult5)){
                      bi.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                       saveEntryResult(blodResult5, isRecheck5, realName, bi);
                   }
               }else if(bi.getProjectId()==SystemContent.PROJECT_ALT){
                   if(StringUtils.isNotBlank(descriptionVal)) {
                       if (">90".equals(descriptionVal) || Long.valueOf(descriptionVal) > 90) {
                           bi.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                           saveEntryResult(descriptionVal, isRecheck7, realName, bi);
                       }
                   }

               }else if(bi.getProjectId()==SystemContent.PROJECT_HAV){
                   if("阳性".equals(blodResult8)){
                       bi.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                       saveEntryResult(blodResult8, isRecheck8, realName, bi);
                   }
               }

           }
        }
    }

    /**
     * 保存录入结果通用信息
     * @param blodResult
     * @param isRecheck
     * @param realName
     * @param blodIntestinal
     */
    private void saveEntryResult(String blodResult, String isRecheck, String realName, ExamBlodIntestinal blodIntestinal) {
        blodIntestinal.setIsRecheck(blodIntestinalRecheckTrans(isRecheck));
        blodIntestinal.setExamResult(blodResult);
        blodIntestinal.setDoctorName(realName);
        blodIntestinal.setEntryState(Integer.valueOf(1));
        blodIntestinal.setJudgeTime(new Date().getTime());
        blodIntestinal.setRemark(SystemContent.PROJECT_REMARK_DEFAULT);
        blodIntestinalDao.update(blodIntestinal);
    }

    @Override
    public List<ExamBlodIntestinal> findSelectedBlodIntestinalDetail(String memberExamId) {

        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();

        blodIntestinal.setMemberExamId(Long.parseLong(memberExamId));

        blodIntestinal.setIsCancel(0);

        List<ExamBlodIntestinal> examBlodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal,"id");

        if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0){
            return examBlodIntestinalList;
        }else{
            return null;
        }
    }

    /**
     * 设置选中的血检信息对应的血检结果都设为合格（原录入的不合格项保留）
     * @param ids
     */
    @Override
    public void defaultSelectedResultT(String[] ids) {
            for(int i=0;i<ids.length;i++){
                ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
                blodIntestinal.setExamBlodId(Long.valueOf(ids[i]));
                List<ExamBlodIntestinal> blodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal);
                // TODO 复检判断是否放这里呢
                for(ExamBlodIntestinal bi:blodIntestinalList) {
                    if (bi.getIsQualified() == SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN) {
                        bi.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                        bi.setJudgeTime(new Date().getTime());
                        blodIntestinalDao.update(bi);
                    }
                }
        }
    }

    /**
     * 获取选择的列表页面肠检信息的肠检结果信息
     * @param intestinalId
     */
    @Override
    public List<ExamBlodIntestinal> findSelectedIntestinalDetail(Long intestinalId) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamIntestinalId(intestinalId);
        //未作废
        blodIntestinal.setIsCancel(0);
        List<ExamBlodIntestinal> examBlodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal);
        if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0){
            return examBlodIntestinalList;
        }else{
            return null;
        }
    }

    //保存左侧弹出框肠检录入信息
    @Override
    public void entryIntestinalResultT(Long intestinalId, String isQualified6, String blodResult6, String isRecheck6, User user) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamIntestinalId(intestinalId);
        List<ExamBlodIntestinal> blodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal);
        if(blodIntestinalList!=null&&blodIntestinalList.size()>0){
            for (ExamBlodIntestinal bi:blodIntestinalList){
                if(bi.getProjectId()==SystemContent.PROJECT_CHANGDAO){
                    if("痢疾杆菌阳性".equals(blodResult6)){
                        bi.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                        bi.setIsRecheck(blodIntestinalRecheckTrans(isRecheck6));
                        bi.setExamResult(blodResult6);
                        bi.setDoctorName(user.getRealName());
                        bi.setJudgeTime(new Date().getTime());
                        bi.setEntryState(Integer.valueOf(1));
                        blodIntestinalDao.update(bi);
                    }
                }
            }
        }
    }

    /**
     * 查找选定的体检用户体检结果详情
     * @param examNumber
     * @return
     */
    @Override
    public List<ExamBlodIntestinal> findSelectedResultDetail(String examNumber) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamNumber(examNumber);
        blodIntestinal.setIsShowRefresh(0);
        //未作废
        blodIntestinal.setIsCancel(0);
        List<ExamBlodIntestinal> examBlodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal, MatchMode.EXACT);
        if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0){
            return examBlodIntestinalList;
        }else{
            return null;
        }
    }

    /**
     * 批量审批
     * @param ids
     * @param loginUser
     */
    @Override
    public void defaultSelectedResultDecide(String[] ids,User loginUser) {
        for(int i=0;i<ids.length;i++){
            //ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
//            blodIntestinal.setMemberExamId(Long.valueOf(ids[i]));
//            List<ExamBlodIntestinal> blodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal);
//            for(ExamBlodIntestinal bi:blodIntestinalList) {
//                if (bi.getIsQualified() == Integer.valueOf(0)) {
//                    bi.setIsRecheck(0);// 不复检
//                    bi.setIsQualified(Integer.valueOf(1)); //合格
//                    blodIntestinalDao.update(bi);
//                }
//            }


          decideQualifiedT(Long.valueOf(ids[i]),loginUser);
        }
    }

    /**
     * 通过memberExamId删除相关信息
     * @param memberExamId
     */
    @Override
    public void deleteByMemberExamIdT(Long memberExamId) {

        List<ExamBlodIntestinal> list = blodIntestinalDao.findByMemberExamId(memberExamId);

        for(ExamBlodIntestinal bi:list){

            //删除操作
            blodIntestinalDao.delete(bi.getId());
        }
    }

    /**
     * 保存内科或者X线体检结果
     * @param user
     * @param isQualified
     * @param descriptionVal
     * @param descriptionId
     * @param examNumber
     * @param projectId
     */
    @Override
    public void saveMedicalResultT(User user, Integer isQualified, String descriptionVal, Long descriptionId, ExamMemberExam examNumber, Long projectId) {
        checkString(descriptionVal,"请输入体检描述用语！");
        // 此处也可保存结果的时候直接保存之前未录入过的体检描述用语
        checkString(String.valueOf(descriptionId),"请先添加此体检描述用语后再尝试此操作！");
        List<ExamBlodIntestinal> blodIntestinals = blodIntestinalDao.findByMemberExamId(examNumber.getId());
        if(blodIntestinals!=null&&blodIntestinals.size()>0) {
            ExamBlodIntestinal blodIntestinal = blodIntestinals.get(0);
//            ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
            blodIntestinal.setMemberExamId(examNumber.getId());
            blodIntestinal.setMemberId(examNumber.getMemberId());
            blodIntestinal.setExamNumber(examNumber.getExamNumber());
            blodIntestinal.setExamResult(descriptionVal);
            blodIntestinal.setExamResultId(descriptionId);
            blodIntestinal.setProjectId(projectId);
            ExamProject project = projectDao.get(projectId);
            blodIntestinal.setProjectName(project.getProjectName());
            blodIntestinal.setIsQualified(isQualified);
            if (isQualified == SystemContent.PROJECT_ISQUALIFIED_YES) { // 合格
                blodIntestinal.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO); // 是否复检 否
            } else if (isQualified == SystemContent.PROJECT_ISQUALIFIED_NO) {
                blodIntestinal.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
            }
            blodIntestinal.setDoctorName(user.getRealName());
            blodIntestinal.setJudgeTime(new Date().getTime());
            blodIntestinalDao.update(blodIntestinal);
        }
    }

    /**
     * 设置选中的体检信息对应的结果都设为合格(体检结果判断用，以最后一次操作为准)
     * @param ids
     */
    @Override
    public void defaultSelectedResultForResultDecideT(String[] ids) {
            ExamBlodIntestinal result = new ExamBlodIntestinal();
            for(int i=0;i<ids.length;i++){
            result.setMemberExamId(Long.valueOf(ids[i]));
            List<ExamBlodIntestinal> blodIntestinalList = blodIntestinalDao.findByExample(result);
            for(ExamBlodIntestinal bi:blodIntestinalList) {
                    bi.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);// 不复检
                    bi.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES); //合格
                    // TODO 默认合格体检结果
                    blodIntestinalDao.update(bi);
            }
        }
    }

    /**
     * @param user
     * @param isQualified
     * @param description
     * @param memberExam
     * @param projectId
     */
    @Override
    public void saveMedicalResultT(User user, Integer isQualified, String description, ExamMemberExam memberExam, Long projectId) {
            ExamBlodIntestinal blodIntestinal = findResultByProIdMeId(memberExam.getId(),projectId);
//            ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
            blodIntestinal.setMemberExamId(memberExam.getId());
            blodIntestinal.setMemberId(memberExam.getMemberId());
            blodIntestinal.setExamNumber(memberExam.getExamNumber());
            blodIntestinal.setIsQualified(isQualified);
            //保存直观的阴性阳性结果
//            if(blodIntestinal.getIsQualified()==SystemContent.PROJECT_ISQUALIFIED_YES){
//                blodIntestinal.setExamResult("阴性");
//            }else if(blodIntestinal.getIsQualified()==SystemContent.PROJECT_ISQUALIFIED_NO){
//                blodIntestinal.setExamResult("阳性");
//            }
            //保存具体的体检结果描述
//            blodIntestinal.setExamResultId(description);
            blodIntestinal.setExamResult(description);
            blodIntestinal.setProjectId(projectId);
            ExamProject project = projectDao.get(projectId);
            blodIntestinal.setProjectName(project.getProjectName());

            if (isQualified == SystemContent.PROJECT_ISQUALIFIED_YES) { // 合格
                blodIntestinal.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO); // 是否复检 否
            } else if (isQualified == SystemContent.PROJECT_ISQUALIFIED_NO) {
                blodIntestinal.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
            }
            blodIntestinal.setDoctorName(user.getRealName());
            blodIntestinal.setJudgeTime(new Date().getTime());
            blodIntestinal.setEntryState(1);
            blodIntestinalDao.update(blodIntestinal);
            if(isQualified == SystemContent.PROJECT_ISQUALIFIED_NO){
                memberExam.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
                memberExamDao.update(memberExam);
            }
            ExamBlodIntestinal findResult = new ExamBlodIntestinal();
            findResult.setIsNew(1);
            findResult.setMemberExamId(memberExam.getId());
            List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(findResult);
            if(resultList!=null&&resultList.size()>0){
                int num = 0;
                for(ExamBlodIntestinal r:resultList){
                    if(r.getEntryState()==1){
                        num+=1;
                    }
                }
                if(num==resultList.size()){
                    memberExam.setEntryState(1);
                }else{
                    memberExam.setIsPush(0);
                }
            }
            memberExam.setLastJudgeTime(blodIntestinal.getJudgeTime());
            memberExamDao.update(memberExam);
    }

    /**
     * 通过项目id体检信息id查询结果信息
     * @param id
     * @param projectMedical
     * @return
     */
    @Override
    public ExamBlodIntestinal findResultByProIdMeId(Long id, Long projectMedical) {
        ExamBlodIntestinal result = new ExamBlodIntestinal();
        result.setMemberExamId(id);
        result.setProjectId(projectMedical);
        List<ExamBlodIntestinal> results = blodIntestinalDao.findByExample(result);
        if(results!=null&&results.size()>0){
            ExamBlodIntestinal upResult = results.get(0);
//            upResult.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
//            upResult.setExamResult("阴性");
            return  upResult;
        }else{
            return null;
        }
    }

    /**
     * 设置筛选范围内的血检信息对应的血检结果都设为合格（原录入的不合格项保留）
     * @param beginTime
     * @param endTime
     * @param entryState
     * @param areaId
     * @param packageId
     * @param examNumber
     * @param type
     * @param loginUser
     */
    @Override
    public void defaultSelectedResultByConditionT(String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber, String type, User loginUser) throws ParseException {
        List<ExamBlodIntestinal> resultList = blodIntestinalDao.defaultSelectedResultByCondition(beginTime,endTime,entryState,areaId,packageId,examNumber,type);
        if(resultList!=null&&resultList.size()>0) {
            for (ExamBlodIntestinal result : resultList) {
                if (result.getIsQualified().equals(SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN)||result.getIsQualified().equals(SystemContent.PROJECT_ISQUALIFIED_CAIJI)) {
                    result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                    result.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
                    if(result.getProjectId().equals(SystemContent.PROJECT_ALT)){
                        result.setExamResult("<90");
                    }else if(result.getProjectId().equals(SystemContent.PROJECT_HEV)){
                        result.setExamResult("阴性");
                    }else if(result.getProjectId().equals(SystemContent.PROJECT_HAV)){
                        result.setExamResult("阴性");
                    }else if(result.getProjectId().equals(SystemContent.PROJECT_CHANGDAO)){
                        result.setExamResult("正常");
                    }
                    result.setEntryState(1);
                    result.setDoctorName(loginUser.getRealName());
                    result.setJudgeTime(new Date().getTime());
                    blodIntestinalDao.update(result);

                    ExamMemberExam memberExam = memberExamDao.get(result.getMemberExamId());
                    ExamBlodIntestinal findResult = new ExamBlodIntestinal();
                    findResult.setIsNew(1);
                    findResult.setMemberExamId(result.getMemberExamId());
                    List<ExamBlodIntestinal> results = blodIntestinalDao.findByExample(findResult);
                    if(results!=null&&results.size()>0){
                        int num = 0;
                        for(ExamBlodIntestinal r:results){
                            if(r.getEntryState()==1){
                                num+=1;
                            }
                        }
                        if(num==results.size()){
                            memberExam.setEntryState(1);
                        }else{
                            memberExam.setIsPush(0);
                        }
                    }
                    memberExam.setLastJudgeTime(result.getJudgeTime());
                    memberExamDao.update(memberExam);
                }
            }
        }
    }

    /**
     * 保存左侧弹出框血检录入信息
     * @param blodId
     * @param isQualified5
     * @param isQualified7
     * @param isQualified8
     * @param blodResult5
     * @param blodResult7
     * @param blodResult8
     * @param realName
     */
    @Override
    public void entryBlodResultSecondT(Long blodId, String isQualified5, String isQualified7, String isQualified8, String blodResult5, String blodResult7, String blodResult8, String realName) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamBlodId(blodId);
        List<ExamBlodIntestinal> blodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal);
        if(blodIntestinalList!=null&&blodIntestinalList.size()>0){
            for (ExamBlodIntestinal result:blodIntestinalList){
                if(result.getProjectId()== SystemContent.PROJECT_HEV){
                    if("阳性".equals(blodResult5)){
                        saveBlodResultSecond(blodResult5, realName, result);
                    }
                }else if(result.getProjectId()==SystemContent.PROJECT_ALT){
                    if(">=90".equals(blodResult7)){
                        saveBlodResultSecond(blodResult7, realName, result);
                    }else if(Long.valueOf(blodResult7) >=90){
                        saveBlodResultSecond(blodResult7, realName, result);
                    }

                }else if(result.getProjectId()==SystemContent.PROJECT_HAV){
                    if("阳性".equals(blodResult8)){
                        saveBlodResultSecond(blodResult8, realName, result);
                    }
                }

            }
        }
    }

    /**
     * 录入肠检结果信息
     * @param intestinalId
     * @param isQualified6
     * @param intestinalResult6
     * @param user
     */
    @Override
    public void entryIntestinalResultSecondT(Long intestinalId, String isQualified6, String intestinalResult6, User user) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamIntestinalId(intestinalId);
        List<ExamBlodIntestinal> blodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal);
        // TODO 这边判断要改增加了不合格术语
        if(blodIntestinalList!=null&&blodIntestinalList.size()>0){
            for (ExamBlodIntestinal result:blodIntestinalList){
                if(result.getProjectId()==SystemContent.PROJECT_CHANGDAO){
                    if("阴性".equals(intestinalResult6)){
                        result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                        result.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
                    }else{
                        result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                        result.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
                    }
                    result.setExamResult(intestinalResult6);
                    result.setDoctorName(user.getRealName());
                    result.setJudgeTime(new Date().getTime());
                    result.setEntryState(Integer.valueOf(1));
                    result.setRemark(SystemContent.PROJECT_REMARK_DEFAULT);
                    blodIntestinalDao.update(result);
                    ExamMemberExam memberExam = memberExamDao.get(result.getMemberExamId());
                    if(!"阴性".equals(intestinalResult6)){
                        memberExam.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
                    }
                    ExamBlodIntestinal findResult = new ExamBlodIntestinal();
                    findResult.setIsNew(1);
                    findResult.setMemberExamId(result.getMemberExamId());
                    List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(findResult);
                    if(resultList!=null&&resultList.size()>0){
                        int num = 0;
                        for(ExamBlodIntestinal r:resultList){
                            if(r.getEntryState()==1){
                                num+=1;
                            }
                        }
                        if(num==resultList.size()){
                            memberExam.setEntryState(1);
                        }else{
                            memberExam.setIsPush(0);
                        }
                    }
                    memberExam.setLastJudgeTime(result.getJudgeTime());
                    memberExamDao.update(memberExam);
                }
            }
        }
    }

    /**
     * 所有项目判断为合格(除肠检外)
     * @param examMemberId
     * @param loginUser
     */
    @Override
    public void decideQualifiedT(Long examMemberId, User loginUser) {
        ExamBlodIntestinal result = new ExamBlodIntestinal();
        result.setMemberExamId(examMemberId);
        result.setIsNew(1);
        // 未作废
        result.setIsCancel(0);
        List<ExamBlodIntestinal> results = blodIntestinalDao.findByExample(result);
        if(results!=null&&results.size()>0){
            for(ExamBlodIntestinal ru:results){
                if(ru.getIsQualified()!=SystemContent.PROJECT_ISQUALIFIED_YES&&!ru.getProjectId().equals(SystemContent.PROJECT_CHANGDAO)){
                    if(ru.getProjectId()==SystemContent.PROJECT_MEDICAL){
                        ru.setExamResult("正常");
                    }else if(ru.getProjectId()==SystemContent.PROJECT_ALT){
                        ru.setExamResult("<90");
                    }else if(ru.getProjectId()==SystemContent.PROJECT_X){
                        ru.setExamResult("心肺正常");
                    }else{
                        ru.setExamResult("阴性");
                    }
                    ru.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                    ru.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
                    ru.setEntryState(1);
                    ru.setDoctorName(loginUser.getRealName());
                    ru.setJudgeTime(new Date().getTime());
                }

                blodIntestinalDao.update(ru);
            }
            // 当前体检信息录入状态改为已录入
            ExamMemberExam memberExam = memberExamDao.get(examMemberId);
            ExamBlodIntestinal findResult = new ExamBlodIntestinal();
            findResult.setIsNew(1);
            findResult.setMemberExamId(result.getMemberExamId());
            List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(findResult);
            if(resultList!=null&&resultList.size()>0){
                int num = 0;
                for(ExamBlodIntestinal r:resultList){
                    if(r.getEntryState()==1){
                        num+=1;
                    }
                }
                if(num==resultList.size()){
                    memberExam.setEntryState(1);
                }else{
                    memberExam.setIsPush(0);
                }
            }
            memberExam.setLastJudgeTime(result.getJudgeTime());
            memberExamDao.update(memberExam);

//            memberExam.setEntryState(1);
//            memberExam.setLastJudgeTime(result.getJudgeTime());
//            memberExamDao.update(memberExam);
        }else{
            errorMsg("所有项目已合格，无需执行此操作！");
        }

    }

    /**
     * 保存左侧弹出框血检录入通用信息
     * @param blodResult
     * @param doctorName
     * @param result
     */
    private void saveBlodResultSecond(String blodResult, String doctorName, ExamBlodIntestinal result) {
        result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
        result.setExamResult(blodResult);
        result.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
        result.setDoctorName(doctorName);
        result.setEntryState(Integer.valueOf(1));
        result.setJudgeTime(new Date().getTime());
        result.setRemark(SystemContent.PROJECT_REMARK_DEFAULT);
        blodIntestinalDao.update(result);
        ExamMemberExam memberExam = memberExamDao.get(result.getMemberExamId());
        memberExam.setVerifyConclusion(SystemContent.PROJECT_ISRECHECK_NO);
        ExamBlodIntestinal findResult = new ExamBlodIntestinal();
        findResult.setIsNew(1);
        findResult.setMemberExamId(result.getMemberExamId());
        List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(findResult);
        if(resultList!=null&&resultList.size()>0){
            int num = 0;
            for(ExamBlodIntestinal r:resultList){
                if(r.getEntryState()==1){
                    num++;
                }
            }
            if(num==resultList.size()){
                memberExam.setEntryState(1);
            }else{
                memberExam.setIsPush(0);
            }
        }
        memberExam.setLastJudgeTime(result.getJudgeTime());
        memberExamDao.update(memberExam);

    }

    /**
     * 合格与否状态转换
     * @param stateStr
     * @return
     */
    public Integer blodIntestinalQualifiedTrans(String stateStr){
        if("on".equals(stateStr)){
            return SystemContent.PROJECT_ISQUALIFIED_YES;
        }else{
            return SystemContent.PROJECT_ISQUALIFIED_NO;
        }
    }

    /**
     *复检与否状态转换
     * @param stateStr
     * @return
     */
    public Integer blodIntestinalRecheckTrans(String stateStr){
        if("on".equals(stateStr)){
            return SystemContent.PROJECT_ISRECHECK_YES;
        }else{
            return SystemContent.PROJECT_ISRECHECK_NO;
        }
    }

    public void addBlod(ExamBlodIntestinal examBlodIntestinal) {
        blodIntestinalDao.save(examBlodIntestinal);
    }

    /**
     * 查找选定的体检用户体检结果详情(返回map保存登录用户)
     * @param examNumber
     * @param map
     * @return
     */
    @Override
    public List<ExamBlodIntestinal> findSelectedResultDetailBackMap(String examNumber, Map<String, Object> map) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamNumber(examNumber);
        blodIntestinal.setIsShowRefresh(0);
        //未作废
        blodIntestinal.setIsCancel(0);
        List<ExamBlodIntestinal> examBlodIntestinalList = blodIntestinalDao.findByExample(blodIntestinal, MatchMode.EXACT);
        if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0){
            map.put("resultList",examBlodIntestinalList);
            return examBlodIntestinalList;
        }else{
            return null;
        }
    }

    /**
     * 检测血检肠检是否已采集过
     * @param examNumber
     * @param type
     * @param map
     */
    @Override
    public void checkIsCollectT(String examNumber, String type, Map<String, Object> map) {
        ExamBlodIntestinal result = new ExamBlodIntestinal();
        result.setExamNumber(examNumber);
        List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(result,"isNew");
        if(StringUtils.isNotBlank(type)){
            for(ExamBlodIntestinal r:resultList){
                if(r.getProjectId().equals(SystemContent.PROJECT_CHANGDAO)){
                    if(r.getExamIntestinalId()==null)
                    map.put("errorMsg","请先去肠检采集！");
                }
            }
        }else{
            for(ExamBlodIntestinal r:resultList){
                if(r.getProjectId().equals(SystemContent.PROJECT_HEV)){
                    if(r.getExamBlodId()==null){
                        map.put("errorMsg","请先去血检采集！");
                    }
                }
            }
        }

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
     * @param loginUser
     */
    @Override
    public void defaultSelectedResultDecideT(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, User loginUser) throws ParseException {
        List<ExamBlodIntestinal> resultList = blodIntestinalDao.defaultSelectedResultDecide(memberName,idNum,startTime,endTime,payState,areaId,isRecheck,verifyConclusion,examNumber,loginUser);
        if(resultList!=null&&resultList.size()>0) {
            for (ExamBlodIntestinal result : resultList) {
                if (result.getIsQualified().equals(SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN)||result.getIsQualified().equals(SystemContent.PROJECT_ISQUALIFIED_CAIJI)) {
                    result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                    result.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
                    if(result.getProjectId().equals(SystemContent.PROJECT_ALT)){
                        result.setExamResult("<90");
                    }else if(result.getProjectId().equals(SystemContent.PROJECT_X)){
                        result.setExamResult("心肺正常");
                    }else if(result.getProjectId().equals(SystemContent.PROJECT_CHANGDAO)||result.getProjectId().equals(SystemContent.PROJECT_MEDICAL)){
                        result.setExamResult("正常");
                    }else{
                        result.setExamResult("阴性");
                    }
                    result.setEntryState(1);
                    result.setDoctorName(loginUser.getRealName());
                    result.setJudgeTime(new Date().getTime());
                    blodIntestinalDao.update(result);

                    ExamMemberExam memberExam = memberExamDao.get(result.getMemberExamId());
                    ExamBlodIntestinal findResult = new ExamBlodIntestinal();
                    findResult.setIsNew(1);
                    findResult.setMemberExamId(result.getMemberExamId());
                    List<ExamBlodIntestinal> results = blodIntestinalDao.findByExample(findResult);
                    if(results!=null&&results.size()>0){
                        int num = 0;
                        for(ExamBlodIntestinal r:results){
                            if(r.getEntryState()==1){
                                num+=1;
                            }
                        }
                        if(num==results.size()){
                            memberExam.setEntryState(1);
                        }else{
                            memberExam.setIsPush(0);
                        }
                    }
                    memberExam.setLastJudgeTime(result.getJudgeTime());
                    memberExamDao.update(memberExam);
                }
            }
        }
    }

    // 录入体检结果数据
    @Override
    public void entryResultDecideT(JSONObject jsonObject, String realName) {
        for(int i=0;i<jsonObject.length();i++){
            org.json.JSONObject resultObject = (org.json.JSONObject) jsonObject.get(""+i);
            ExamBlodIntestinal result = blodIntestinalDao.get(Long.valueOf((String) resultObject.get("examResultId")));
            ExamMemberExam memberExam = memberExamDao.get(result.getMemberExamId());
            result.setExamResult((String) resultObject.get("examResult"));
            if("1".equals(resultObject.get("isQualified"))){
                result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
            }else if("2".equals(resultObject.get("isQualified"))){
                result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                // 修改体检信息表是否复检状态
                memberExam.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
            }
            result.setJudgeTime(new Date().getTime());
            result.setDoctorName(realName);
            result.setEntryState(Integer.valueOf(1));
            blodIntestinalDao.update(result);

            ExamBlodIntestinal findResult = new ExamBlodIntestinal();
            findResult.setIsNew(1);
            //未作废
            findResult.setIsCancel(0);
            findResult.setMemberExamId(result.getMemberExamId());
            List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(findResult);
            if(resultList!=null&&resultList.size()>0){
                int num = 0;
                for(ExamBlodIntestinal r:resultList){
                    if(r.getEntryState()==1){
                        num+=1;
                    }
                }
                if(num==resultList.size()){
                    memberExam.setEntryState(1);
                }else{
                    memberExam.setIsPush(0);
                }
            }
            memberExam.setLastJudgeTime(result.getJudgeTime());
            memberExamDao.update(memberExam);
        }
    }

    //录入右侧体检结果数据
    @Override
    public void saveRightResultDecideT(JSONObject jsonObject, String realName) {
        for(int i=0;i<jsonObject.length();i++){
            org.json.JSONObject resultObject = (org.json.JSONObject) jsonObject.get(""+i);
            ExamBlodIntestinal result = blodIntestinalDao.get(Long.valueOf((String) resultObject.get("examResultId")));
            ExamMemberExam memberExam = memberExamDao.get(result.getMemberExamId());
            result.setExamResult((String) resultObject.get("examResult"));
            if(!result.getProjectId().equals(SystemContent.PROJECT_CHANGDAO)&&
                   !"3".equals(resultObject.get("isQualified"))){
                if("2".equals(resultObject.get("isQualified"))){
                    result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                    // 修改体检信息表是否复检状态
                    memberExam.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
                }else if("1".equals(resultObject.get("isQualified"))){
                    result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                }else {
                    result.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN);
                }
                result.setEntryState(Integer.valueOf(1));
                result.setJudgeTime(new Date().getTime());
                result.setDoctorName(realName);
                blodIntestinalDao.update(result);
            }

            ExamBlodIntestinal findResult = new ExamBlodIntestinal();
            findResult.setIsNew(1);
            //未作废
            findResult.setIsCancel(0);
            findResult.setMemberExamId(result.getMemberExamId());
            List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(findResult);
            if(resultList!=null&&resultList.size()>0){
                int num = 0;
                for(ExamBlodIntestinal r:resultList){
                    if(r.getEntryState()==1){
                        num+=1;
                    }
                }
                if(num==resultList.size()){
                    memberExam.setEntryState(1);
                }else{
                    memberExam.setIsPush(0);
                }
            }
            memberExam.setLastJudgeTime(result.getJudgeTime());
            memberExamDao.update(memberExam);
        }
    }

    //通过体检号,体检项目类别获取体检人信息，检测内科、X线体检状态
    @Override
    public void checkResultByProIdMeIdT(Map<String, Object> map, String examNumber, String projectId, String realName) {
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setExamNumber(examNumber);

        examMemberExam.setIsNew(1);
        //未作废
        examMemberExam.setIsCancel(0);
        //查询examMemberExam表中is_new为1的数据
        List<ExamMemberExam> examMemberExamList = memberExamDao.findByExample(examMemberExam);
        if(examMemberExamList!=null&&examMemberExamList.size()>0){

            examMemberExam = examMemberExamList.get(0);

            if(examMemberExam.getVerifyConclusion()==0 ){//当为未判断时，可以继续查询体检项
                //根据此条记录再去查询exam_blod_intential表中对应的该条记录 ，
                // 如果没有则提示他 如果有 将未判断状态置为合格 同时返回数据

                // 通过项目id体检信息id查询结果信息


                //复检
                if(examMemberExam.getRecheckTag()>0){
                    ExamBlodIntestinal recheckResult = new ExamBlodIntestinal();
                    recheckResult.setMemberExamId(examMemberExam.getId());
                    recheckResult.setProjectId(Long.valueOf(projectId));
                    //未作废
                    recheckResult.setIsCancel(0);
                    List<ExamBlodIntestinal> recheckResults = blodIntestinalDao.findByExample(recheckResult);
                    if(recheckResults==null||recheckResults.size()<=0){
                        map.put("errorMsg",Long.valueOf(projectId)==SystemContent.PROJECT_MEDICAL?"复检没有内科项目，无须体检！":"复检没有X线项目，无须体检！");
                        return;
                    }else{
                        if(recheckResults.get(0).getIsQualified().equals(SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN)){
                            if (checkPayState(map, examNumber)) {
                                return;
                            }
                            ExamBlodIntestinal qualifiedResult = recheckResults.get(0);
                            qualifiedResult.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                            qualifiedResult.setExamResult(Long.valueOf(projectId).equals(SystemContent.PROJECT_MEDICAL)?"正常":"心肺正常");
                            qualifiedResult.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
                            qualifiedResult.setEntryState(1);
                            qualifiedResult.setJudgeTime(new Date().getTime());
                            qualifiedResult.setDoctorName(realName);
                            blodIntestinalDao.update(qualifiedResult);

                            map.put("qualifiedResult",qualifiedResult);
                            ExamBlodIntestinal findResult = new ExamBlodIntestinal();
                            findResult.setIsNew(1);
                            findResult.setMemberExamId(examMemberExam.getId());
                            //未作废
                            findResult.setIsCancel(0);
                            List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(findResult);
                            if (resultList != null && resultList.size() > 0) {
                                int num = 0;
                                for (ExamBlodIntestinal r : resultList) {
                                    if (r.getEntryState() == 1) {
                                        num += 1;
                                    }
                                }
                                if (num == resultList.size()) {
                                    examMemberExam.setEntryState(1);
                                }else{
                                    examMemberExam.setIsPush(0);
                                }
                            }
                            examMemberExam.setLastJudgeTime(qualifiedResult.getJudgeTime());
                            memberExamDao.update(examMemberExam);
                        }
                        map.put("exam",examMemberExam);
                    }
                }else{
                    ExamPackageProject pp = new ExamPackageProject();
                    pp.setPackageId(examMemberExam.getPackageId());
                    List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);
                    boolean flag = false;
                    if(packageProjectList!=null&&packageProjectList.size()>0){

                        for(ExamPackageProject p:packageProjectList){
                            if(Long.valueOf(projectId)==SystemContent.PROJECT_MEDICAL){
                                if(p.getProjectId()==SystemContent.PROJECT_MEDICAL){
                                    flag=true;
                                }
                            }else if(Long.valueOf(projectId)==SystemContent.PROJECT_X){
                                if(p.getProjectId()==SystemContent.PROJECT_X){
                                    flag=true;
                                }
                            }

                        }
                        if(flag){
//                            ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
//                            examBlodIntestinal.setExamNumber(examNumber);
//                            examBlodIntestinal.setIsNew(1);
//                            examBlodIntestinal.setIsShowRefresh(0);
//                            examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_MEDICAL);
//                            List<ExamBlodIntestinal> examBlodIntestinalList = blodIntestinalService.findByExample(examBlodIntestinal);
//                            if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0){
//                                map.put("errorMsg","内科已检测过，无须再检验！");
//                            }
                            map.put("exam",examMemberExam);
                            // 内科X线扫码默认项目合格
                            ExamBlodIntestinal result = new ExamBlodIntestinal();
                            result.setExamNumber(examNumber);
                            result.setIsNew(1);
                            result.setType(Long.valueOf(projectId)==SystemContent.PROJECT_MEDICAL?SystemContent.PROJECT_TYPE_MEDICAL:SystemContent.PROJECT_TYPE_X);
                            //未作废
                            result.setIsCancel(0);
                            List<ExamBlodIntestinal> results = blodIntestinalDao.findByExample(result);

                            if(results!=null&&results.size()>0&&results.get(0).getIsQualified()==SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN) {
                                if (checkPayState(map, examNumber)){
                                    return;
                                }
                                ExamBlodIntestinal qualifiedResult = results.get(0);
                                qualifiedResult.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                                qualifiedResult.setExamResult(Long.valueOf(projectId).equals(SystemContent.PROJECT_MEDICAL)?"正常":"心肺正常");
                                qualifiedResult.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
                                qualifiedResult.setEntryState(1);
                                qualifiedResult.setJudgeTime(new Date().getTime());
                                qualifiedResult.setDoctorName(realName);
                                qualifiedResult.setIsPush(0);
                                blodIntestinalDao.update(qualifiedResult);

                                map.put("qualifiedResult",qualifiedResult);

                                ExamBlodIntestinal findResult = new ExamBlodIntestinal();
                                findResult.setIsNew(1);
                                findResult.setMemberExamId(examMemberExam.getId());
                                //未作废
                                findResult.setIsCancel(0);
                                List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(findResult);
                                if (resultList != null && resultList.size() > 0) {
                                    int num = 0;
                                    for (ExamBlodIntestinal r : resultList) {
                                        if (r.getEntryState() == 1) {
                                            num += 1;
                                        }
                                    }
                                    if (num == resultList.size()) {
                                        examMemberExam.setEntryState(1);
                                    }else{
                                        examMemberExam.setIsPush(0);
                                    }
                                }
                                examMemberExam.setLastJudgeTime(qualifiedResult.getJudgeTime());
                                memberExamDao.update(examMemberExam);
                            }else{
                                map.put("exam",examMemberExam);
                                ExamBlodIntestinal qualifiedResult = results.get(0);
                                map.put("qualifiedResult",qualifiedResult);
//                                map.put("errorMsg",Long.valueOf(projectId)==SystemContent.PROJECT_MEDICAL?"内科已检测过，无须再检验！":"X线已检测过，无须再检验！");
                            }

                        }else{
                            map.put("errorMsg",Long.valueOf(projectId)==SystemContent.PROJECT_MEDICAL?"您没有要检测的内科项目，无须检验！":"您没有要检测的X线项目，无须检验！");
                        }
                    }
                }

            }else{
                map.put("errorMsg","当前没有需要体检的项目！");
            }
        }else{
            map.put("errorMsg","获取信息失败，请扫描正确的条形码！！");
        }
    }

    //检测是否缴费
    private boolean checkPayState(Map<String, Object> map, String examNumber) {
        ExamPay pay = new ExamPay();
        pay.setIsNew(1);
        pay.setExamNumber(examNumber);
        //未作废
        pay.setIsCancel(0);
        List<ExamPay> payList = payDao.findByExample(pay);
        if(payList!=null&&payList.size()>0){
            if(payList.get(0).getPayState()!=2){
                map.put("errorMsg","您还没有缴费！");
                return true;
            }
        }
        return false;
    }

    public   Boolean updateExamBlodIntestinalT(int type,Long memberExamId,PhysicalItemDetails physicalItemDetails){
        Long areaId=examMemberExamDao.get(memberExamId).getAreaId();
        ExamDoctor ed=new ExamDoctor();
        ed.setAreaId(areaId);
        ed.setProjectId(Long.parseLong(type + ""));
        String doctorName="admin";
        if(examDoctorDao.findByExample(ed).size()>0) {
             doctorName = examDoctorDao.findByExample(ed).get(0).getDoctorName();
        }

        ExamBlodIntestinal eBIX=new ExamBlodIntestinal();
        eBIX.setMemberExamId(memberExamId);
        eBIX.setType(type);
        List<ExamBlodIntestinal>eBIList=blodIntestinalDao.findByExample(eBIX);
        if(eBIList.size()>0){
          /*  ExamBlodIntestinal ebi = eBIList.get(0);*/
            ExamBlodIntestinal ebi = eBIList.get(eBIList.size() - 1);
            if((!ebi.getEntryState().equals(1))&&(!ebi.getIsQualified().equals(1))) {
                ebi.setIsQualified(Integer.parseInt(physicalItemDetails.getQualifiedMark()));//项目体检结果
                ebi.setMemberExamId(memberExamId);//体检人员体检信息
                ebi.setExamResult(physicalItemDetails.getPhysicalResult());//项目体检结果
                /*ebi.setIsShowRefresh(Integer.parseInt(physicalItemDetails.getReSign()));*///是否需要复查
                ebi.setDoctorName(doctorName);//判断人
                ebi.setJudgeTime(DateTool.dateToLong(physicalItemDetails.getJudgeDate()));
                ebi.setRemark(physicalItemDetails.getRemarks());
                ebi.setEntryState(1);//录入标志
                blodIntestinalDao.update(ebi);
                if(ebi.getIsQualified().equals(1)){
                    return true;
                }else{
                    return  false;
                }
                /* return true;*/
            }else if(ebi.getEntryState().equals(1)&&ebi.getIsQualified().equals(1)){
                return true;
            }
        }
        return  false;
    }
}
