package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberDao;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberExamDao;
import com.bidanet.bdcms.dao.fee.ExamPayDao;
import com.bidanet.bdcms.entity.ExamMember;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamPay;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import com.bidanet.bdcms.service.examBusiness.ExamMemberService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/**
*
*/
@Service
public class ExamMemberServiceImpl extends BaseServiceImpl<ExamMember> implements ExamMemberService {
    @Autowired
    private ExamMemberDao examMemberDao;
    @Override
    protected Dao getDao() {
        return examMemberDao;
    }

    @Autowired
    @Qualifier("localFileDrive")
    private LocalFileDrive localFileDrive;

    @Autowired
    private ExamMemberExamDao examMemberExamDao;

    @Autowired
    private ExamPayDao examPayDao;

    @Override
    public List<ExamMember> findById(Long id){
        return examMemberDao.findById(id);
    }
    @Override
    public List<ExamMember> findBynameId(String name,String IdCard){
        return examMemberDao.findByNameID(name,IdCard);
    }


    @Override
    public void updateMemberIcon(Long memberId, String memberPhoto) {

        ExamMember examMember = examMemberDao.get(memberId);

        //头像图片
        String iconPath = localFileDrive.uploadBase64(memberPhoto, UUID.randomUUID().toString() + "icon.jpg");

        examMember.setIcon(iconPath);

        examMemberDao.updateBack(examMember);


    }

    @Override
    public int updateMemberInfo(Long id,Long memberId, String memberPhoto, String memberName, String memberAddress,String memberSex,int tag) {

        int successFlag = 0;


        ExamMember examMember = examMemberDao.get(memberId);

        ExamMemberExam examMemberExam = examMemberExamDao.get(id);

        //当注册渠道是微信时 进行数据的更新
        if (true){

            ExamPay examPay = examPayDao.get(examMemberExam.getPayId());

            String iconPath = "";
            //头像图片
            iconPath  = localFileDrive.uploadBase64(memberPhoto, UUID.randomUUID().toString() + "icon.jpg");

            examMember.setIcon(iconPath);
            examMemberExam.setIcon(iconPath);

            if (memberSex.equals("男")) {
                examMember.setSex(1);
                examMemberExam.setSex(1);
            } else {
                examMember.setSex(0);
                examMemberExam.setSex(0);
            }

            examMember.setName(memberName);
            examMember.setIdCardAddress(memberAddress);


            examMemberExam.setName(memberName);



            examMember =  examMemberDao.updateBack(examMember);
            examMemberExam =  examMemberExamDao.updateBack(examMemberExam);

            //examPay 信息回填
            examPay.setName(examMember.getName());
            examPay.setIcon(examMember.getIcon());

            examPayDao.updateBack(examPay);

            if (examMember.getId()!=null && examMemberExam.getId()!=null){
                successFlag = 1;
            }

        }else{
            successFlag = 1;
        }



       return  successFlag;
    }
}
