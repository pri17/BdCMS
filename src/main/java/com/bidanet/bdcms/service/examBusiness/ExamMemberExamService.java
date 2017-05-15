package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;
import org.apache.http.ParseException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
* cf
*/
public interface ExamMemberExamService extends Service<ExamMemberExam> {
    /**
     * 查找exam_blod_Inte中的每项体检数据
     */
     List<ExamBlodIntestinal> findEveryBlodIn(long id);

    /**
     * 疫区范围内的体检人员信息
     * @param page
     * @param beginTime
     * @param endTime
     * @param areaId
     * @throws java.text.ParseException
     */
    void findAffectedExamMember(Page<ExamMemberExam> page, String beginTime, String endTime, String areaId) throws java.text.ParseException;



    /**
     * 进入结论判断审核页面，加载下拉列表的一些数据
     * @param examMemberExam
     * @param page
     * @param memberName
     * @param idCardNum
     * @param beginTime
     * @param endTime
     * @param categoryId
     */
    void getMemberExamList(ExamMemberExam examMemberExam, Page<ExamMemberExam> page,String areaId, String memberName,String idCardNum,String beginTime,String endTime,Long categoryId);

    void addExamMemberExamT(ExamMemberExam examMemberExam);

    /**
     * 查询统计-体检结果查询
     * @param page
     * @param areaId
     * @param name
     * @param idCard
     * @param examNumber
     * @param verifyConclusion
     * @param startTime
     * @param endTime
     * @param payState
     * @throws ParseException
     */
    void getResultQueryList(Page<ExamMemberExam> page, String areaId, String name, String idCard, String examNumber, String verifyConclusion, String startTime, String endTime, String payState) throws ParseException, java.text.ParseException;

    /**
     * 体检结果判断页面
     * @param page
     * @param memberName
     * @param idNum
     * @param startTime
     * @param endTime
     * @param payState
     * @param areaId
     * @param isRecheck
     * @param verifyConclusion
     * @param examNumber
     * @param agencies
     */
    void findExamMemberResultDecide(Page<ExamMemberExam> page, String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies) throws java.text.ParseException;

    /**
     * 保存疫区人员血检额外信息
     * @param ids
     * @param iha
     * @param ddia
     * @param copt
     * @param stool
     */
    void saveAffectedMemberT(String[] ids, String iha, String ddia, String copt, String stool);

    /**
     * 结论判断审核excel导出
     * @param query
     * @param pageCurrent
     * @param pageSize
     * @param memberName
     * @param idCardNum
     * @param beginTime
     * @param endTime
     * @param openCategoryLevelTwo
     * @param examNumber
     *@param workUnit
     * @param verifyConclusion
     * @param verifyState @return
     */
    HSSFWorkbook exportFindExcel(ExamMemberExam query, int pageCurrent, int pageSize, String areaId, String memberName, String idCardNum, String beginTime, String endTime, Long openCategoryLevelTwo, String examNumber, String workUnit, String verifyConclusion, String verifyState);

    /**
     * 结果判断导出
     *
     * @param pageCurrent
     * @param pageSize
     *@param memberName
     * @param idNum
     * @param startTime
     * @param endTime
     * @param payState
     * @param areaId
     * @param isRecheck
     * @param verifyConclusion
     * @param examNumber          @return
     * @throws java.text.ParseException
     */
    HSSFWorkbook exportDecideExcel(int pageCurrent, int pageSize, String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies) throws java.text.ParseException;

    /**
     * 结果查询导出
     * @param pageCurrent
     * @param pageSize
     * @param payState
     * @param areaId
     * @param name
     * @param idCard
     * @param examNumber
     * @param verifyConclusion
     * @param startTime
     * @param endTime        @return
     * @throws java.text.ParseException
     */
    HSSFWorkbook exportResultQueryExcel(int pageCurrent, int pageSize, String payState, String areaId, String name, String idCard, String examNumber, String verifyConclusion, String startTime, String endTime) throws java.text.ParseException;

    List<ExamMemberExam> findAffectedExamMemberByIds(String ids);

    ///////////////////////////////////复检管理/////////////////////////////////////
    void findReviewMemberList(Page<ExamMemberExam> page,String areaId, String workUnit,String memberName, String idCardNum, String beginTime, String endTime, String categoryId,String isRechekPrint,String examNumber) throws java.text.ParseException;

    List<ExamMemberExam> findByExamNumber(String examNumber);

    //复检管理导出数据查询
    HSSFWorkbook exportReviewExcel(ExamMemberExam query,String areaId,String workUnit, String memberName, String idCardNum, String beginTime, String endTime, String openCategoryLevelTwoRmId,String isRechekPrint,String examNumber)  throws java.text.ParseException;

    List<ExamMemberExam> getExamReviews(String ids);


    List<ExamMemberExam> getMemberExamNew(String examNumber);

    ///////////////////////////////////结论判断审核////////////////////////////////////////
    //自动处理审核
    List<ExamMemberExam> getAutoResultDecideList();

    void autoResultDecide(ExamMemberExam examMemberExam, ExamAutoDoctor examAutoDoctor);

    void batchAuditT(List<String> examIdList, User user);

    /**
     * 检测体检号是否有效
     * @param examNumber
     * @param map
     */
    void checkMemberExamT(String examNumber, Map<String, Object> map);

    /**
     * 修改体检信息表是否复检状态
     * @param memberExamId
     */
    void upRecheckState(Long memberExamId);


    /**
     * 检测肠检体检号是否有效
     * @param examNumber
     * @param map
     */
    void checkMemberExamForIntestinalT(String examNumber, Map<String, Object> map);

    /**
     * 撤销审核
     * @param examMemberExam
     * @return
     */
    Boolean cancelCheckedService(ExamMemberExam examMemberExam);

    /**
     * 批量删除
     * @param examMemberExam
     * @return
     */
    Boolean etBatchDelete(ExamMemberExam examMemberExam);

    /**
     *体检综合查询 修改用户信息
     * @return
     */
    ExamMemberExam changeEtqMember(String name, String sex, String birthday, String address, String idCardNum, String mobile, String workUnit, Integer age,
                                   String areaId,String agenciesId, String packageId, String packageMoney, String isRemark, String memberExamId,String categoryId);
    List<ExamMemberExam> getUnFinishExam() throws java.text.ParseException;


    /**
     * 乡镇肠检体检信息查找带回
     * @param page
     * @param examNumber
     * @param memberName
     * @param beginTime
     * @param endTime
     */
    void getTownsMemberExamLookBack(Page<ExamMemberExam> page, String examNumber, String memberName, String beginTime, String endTime) throws java.text.ParseException;


    void updatePrintInfoT(long id);


    /**
     * 修改体检人员信息并且修改相关主参数表的人员信息
     * @param id
     * @param name
     * @param sex
     * @param idCardNum
     * @param birthday
     * @param age
     * @param address
     * @param mobile
     * @return
     */
    boolean updateExamMemberExamAndExamMeberUserInfoT(Long id,String name, int sex, String idCardNum,String birthday,
                                                   int age,String address,String mobile,Long categoryId,String workUnit);
}
