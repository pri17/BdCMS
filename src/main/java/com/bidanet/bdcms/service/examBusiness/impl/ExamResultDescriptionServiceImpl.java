package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamBlodIntestinalDao;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberExamDao;
import com.bidanet.bdcms.dao.examBusiness.ExamResultDescriptionDao;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamResultDescription;

import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.examBusiness.ExamResultDescriptionService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
* cf
*/
@Service("examResultDescriptionService")
public class ExamResultDescriptionServiceImpl extends BaseServiceImpl<ExamResultDescription> implements ExamResultDescriptionService {
    @Autowired
    private ExamResultDescriptionDao resultDescriptionDao;
    @Override
    protected Dao getDao() {
        return resultDescriptionDao;
    }
    @Autowired
    private ExamBlodIntestinalDao blodIntestinalDao;
    @Autowired
    private ExamMemberExamDao memberExamDao;


    /**
     * 加入体检描述用语
     * @param description
     * @param descriptionId
     */
    @Override
    public void joinMedicalT(String description,String descriptionId) {
        checkString(description,"请输入描述！");
        // 如果descriptionId有值，说明该描述用语已存在于库里
        if(StringUtils.isNotBlank(descriptionId)){
            errorMsg("请新增全新的描述后尝试加入！");
        }
        ExamResultDescription resultDescription = new ExamResultDescription();
        resultDescription.setDescription(description);
        resultDescription.setProjectId(9L);
        resultDescription.setCreateTime(new Date().getTime());
        resultDescriptionDao.save(resultDescription);
    }

    /**
     * 根据项目类型查询该类型所有描述术语
     * @return
     */
    @Override
    public List<ExamResultDescription> getAllResultDescription(Long projectId) {
        ExamResultDescription resultDescription = new ExamResultDescription();
        resultDescription.setProjectId(projectId);
        List<ExamResultDescription> resultDescriptionList = resultDescriptionDao.findByExample(resultDescription);
        if(resultDescriptionList!=null&&resultDescriptionList.size()>0) {
            return resultDescriptionList;
        }else{
            return null;
        }
    }

    /**
     * 修改体检描述用语
     * @param updescriptionId
     * @param updescription
     */
    @Override
    public void upMedicalT(String updescriptionId, String updescription) {
        checkString(updescriptionId,"请选择你要修改的描述！");
        checkString(updescription,"请选择你要修改的描述！！");
        String[] ids = updescriptionId.split(",");
        String[] descriptions = updescription.split(",");
        for(int i=0;i<ids.length;i++){
            for(int j=0;j<descriptions.length;j++){
                ExamResultDescription upResultDescription = resultDescriptionDao.get(Long.valueOf(ids[i]));
                upResultDescription.setDescription(descriptions[i]);
                resultDescriptionDao.update(upResultDescription);
            }
        }

    }

    @Override
    public void joinXT(String description, String descriptionId) {
        checkString(description,"请输入描述！");
        if(StringUtils.isNotBlank(descriptionId)){
            errorMsg("请新增全新的描述后再尝试加入！");
        }
        ExamResultDescription resultDescription = new ExamResultDescription();
        resultDescription.setDescription(description);
        resultDescription.setProjectId(10L);
        resultDescription.setCreateTime(new Date().getTime());
        resultDescriptionDao.save(resultDescription);
    }

    @Override
    public void joinBlodT(String description, String descriptionId,String projectId) {
        checkString(description,"请输入描述！");
        if(StringUtils.isNotBlank(descriptionId)){
            errorMsg("请新增全新的描述后尝试加入！");
        }
        ExamResultDescription resultDescription = new ExamResultDescription();
        resultDescription.setDescription(description);
        resultDescription.setProjectId(Long.valueOf(projectId));
        resultDescription.setCreateTime(new Date().getTime());
        resultDescriptionDao.save(resultDescription);

    }

    /**
     * 加入体检描述用语
     * @param description
     * @param type
     * @param projectId
     */
    @Override
    public void joinMedicalT(String description, Integer type, Long projectId) {
        checkString(description,"请输入描述！");
        ExamResultDescription resultDescription = new ExamResultDescription();
        resultDescription.setDescription(description);
        resultDescription.setProjectId(projectId);
        resultDescription.setType(type);
        resultDescription.setCreateTime(new Date().getTime());
        resultDescriptionDao.save(resultDescription);
    }

    /**
     * 修改体检信息结果内容
     * @param updescriptionId 参数为2： 不合格  1： 合格
     * @param updescription
     * @param upId
     * @param loginUser
     */
    @Override
    public void upExamResultT(String updescriptionId, String updescription, Long upId, User loginUser) {
        checkString(updescription,"请输入你要修改的描述！！");
        String[] ids = updescriptionId.split(",");
        ExamBlodIntestinal upResult = blodIntestinalDao.get(upId);
        ExamMemberExam memberExam = memberExamDao.get(upResult.getMemberExamId());
        if(!memberExam.getVerifyConclusion().equals(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN)){
            errorMsg("项目已完成，不能再修改！");
        }
//        String[] descriptions = updescription.split(",");
        if(ids.length>0) {
            for (int i = 0; i < ids.length; i++) {
                if (ids[i].equals("2")||ids[i].equals("")) {
                    upResult.setExamResult(updescription);
                    upResult.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                    upResult.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
                } else {
                    upResult.setExamResult(updescription);
                    upResult.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                    upResult.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
                }
                upResult.setDoctorName(loginUser.getRealName());
                upResult.setJudgeTime(new Date().getTime());
                upResult.setEntryState(1);
                blodIntestinalDao.update(upResult);

                ExamBlodIntestinal findResult = new ExamBlodIntestinal();
                findResult.setIsNew(1);
                findResult.setMemberExamId(upResult.getMemberExamId());
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
                memberExam.setLastJudgeTime(upResult.getJudgeTime());
                memberExamDao.update(memberExam);
            }
        }
    }

    /**
     *  @param upExamNumber
     * @param upIdLength  所有描述的合格与否类型(如果有不合格的，则为不合格)
     * @param updescription 待修改的结果描述术语
     * @param loginUser
     * @param projectId
     * @param map
     */
    @Override
    public void upExamResultMedicalXT(String upExamNumber, String upIdLength, String updescription, User loginUser, Long projectId, Map<String, Object> map) {
        checkString(updescription,"请选择你要修改的描述！！");
        String[] ids = upIdLength.split(",");
//        String[] descriptions = updescription.split(",");
        ExamMemberExam examMemberExam = new ExamMemberExam();
        examMemberExam.setExamNumber(upExamNumber);
        examMemberExam.setIsNew(1);
        //未作废
        examMemberExam.setIsCancel(0);
        examMemberExam.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
        List<ExamMemberExam> examMemberExamList = memberExamDao.findByExample(examMemberExam, MatchMode.EXACT);
        if(examMemberExamList!=null&&examMemberExamList.size()>0){
            examMemberExam = examMemberExamList.get(0);
            if(ids.length>0) {
                ExamBlodIntestinal result = new ExamBlodIntestinal();
                result.setMemberExamId(examMemberExam.getId());
                result.setProjectId(projectId);
                List<ExamBlodIntestinal> resultList = blodIntestinalDao.findByExample(result);
                if(resultList!=null&&resultList.size()>0){
                    ExamBlodIntestinal upResult = resultList.get(0);
                    Boolean flag = false;
                    for (int i = 0; i < ids.length; i++) {
                        if(ids[i].equals("2")){
                            flag=true;
                        }
                    }
                        if (flag) {
                            upResult.setExamResult(updescription);
                            upResult.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_NO);
                            upResult.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
                            map.put("type",SystemContent.PROJECT_ISQUALIFIED_NO);
                        } else {
                            upResult.setExamResult(updescription);
                            upResult.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_YES);
                            upResult.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
                            map.put("type",SystemContent.PROJECT_ISQUALIFIED_YES);
                        }
                        upResult.setDoctorName(loginUser.getRealName());
                        upResult.setJudgeTime(new Date().getTime());
                        upResult.setEntryState(1);
                        blodIntestinalDao.update(upResult);

                        ExamMemberExam memberExam = memberExamDao.get(upResult.getMemberExamId());
                        ExamBlodIntestinal findResult = new ExamBlodIntestinal();
                        findResult.setIsNew(1);
                        findResult.setMemberExamId(upResult.getMemberExamId());
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
                        memberExam.setLastJudgeTime(upResult.getJudgeTime());
                        memberExamDao.update(memberExam);
                        map.put("result",upResult);
                }
            }
        }else{
            map.put("errorMsg","体检号错误或者已经体检完，无需录入！");
//            errorMsg("体检号错误或者已经体检完，无需录入！");
        }

    }


}
