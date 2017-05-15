package com.bidanet.bdcms.service.examBusiness.impl;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamAddDao;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberDao;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberExamDao;
import com.bidanet.bdcms.dao.examManage.ExamEcardDao;
import com.bidanet.bdcms.entity.ExamEcard;
import com.bidanet.bdcms.entity.ExamMember;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.service.examBusiness.ExamAddService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 体检登记. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-21 10:26:46
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
@Service
public class ExamAddServiceImpl extends BaseServiceImpl<ExamMember> implements ExamAddService{

    @Autowired
    private ExamAddDao examAddDao;

    @Override
    protected Dao getDao() {
        return examAddDao;
    }

    @Autowired
    private ExamMemberExamDao examMemberExamDao;

    @Autowired
    private ExamEcardDao examEcardDao;

    @Autowired
    private ExamMemberDao examMemberDao;

    @Override
    public void addExamAddT(ExamMember examMember) {
        checkString(examMember.getName(),"请填写体检单位名称！");
        Date date =  new Date();
        examMember.setCreateTime(date.getTime());
        examAddDao.save(examMember);
    }

    @Override
    public List<ExamMember> getExamMemberByIdCardNum(String idCardNum) {
        return examAddDao.getExamMemberByIdCardNum(idCardNum);
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
    public JSONObject judgeExamMember(String idCardNum, String judgeTag) {


        JSONObject json = new JSONObject();

        ExamMember examMember = new ExamMember();

        examMember.setIdCardNum(idCardNum);

        List<ExamMember> examMemberList = examMemberDao.findByExample(examMember);


        if(examMemberList.size()>0){//有该用户

            json.put("isNew",1);
            examMember=examMemberList.get(0);

            //是否3个月
            if (Integer.parseInt(judgeTag)==1){


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
        if(memberId==null){
            return 0;
        }
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
