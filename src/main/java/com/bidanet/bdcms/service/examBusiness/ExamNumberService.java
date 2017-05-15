package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.bean.CompanyPeopleInform;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamNumber;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by zhangbinbin on 2016-10-28 14:16:29
 */

public interface ExamNumberService extends Service<ExamNumber> {

    List<ExamNumber> getNumberListByCreateTime(Long todayStart,Integer areaCode);

    void addExamNumberT(ExamNumber examNumber);

    ExamMemberExam cancelExamT(ExamMemberExam oldExam , String name, int sex, int age, String birthday, String address, String idCardNum,
                    String mobile, String workUnit, Long areaId, Long packageId, BigDecimal packageMoney, User user, String memberIcon, Long agenciesId, Long categoryId,Integer isCameraPhoto);

    ExamMemberExam addExamTS(String name, int sex, int age, String birthday, String address, String idCardNum,
                             String mobile, String workUnit, Long areaId, Long packageId, BigDecimal packageMoney, User user, String memberIcon, Long agenciesId, Long categoryId, Integer isCameraPhoto);


//    void printTransferCpc();

    void printTransferTown();

    //企业登记导入数据接口
    void addExamForCompanyT(List<CompanyPeopleInform> list);
    //Gcx 老数据库向新数据插入体检记录，不用生成新的体检号码，使用旧系统的体检号码
    ExamMemberExam addExamTOldToNewNoCreateMemberNumberT(String name, int sex, int age, String birthday, String address, String idCardNum,
                            String mobile, String workUnit, Long areaId, Long packageId, BigDecimal packageMoney, User user, String memberIcon, Long agenciesId, Long categoryId,Integer isCameraPhoto,String memberNumber);
}
