package com.bidanet.bdcms.dao.examBusiness;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.vo.Page;

import java.text.ParseException;
import java.util.List;

/**
* ExamMemberExam DAO
 * cf
*/
public interface ExamMemberExamDao extends Dao<ExamMemberExam> {

    List<ExamMemberExam> findAllNo(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies) throws ParseException;



    List<ExamMemberExam> findAffectedExamMember(String memIds, String beginTime, String endTime, String areaId, int pageCurrent, int pageSize) throws ParseException;

    List<ExamMemberExam> findExportAffectedExamMember(String memIds, String beginTime, String endTime, String areaId) throws ParseException;



    Long countAffectedExamMember(String memIds, String beginTime, String endTime, String areaId) throws ParseException;

    /**
     * 体检判断审核
     * @param examMemberExam
     * @param pageNo
     * @param pageSize
     * @param memberName
     * @param idCardNum
     * @param beginTime
     * @param endTime
     * @param categoryId
     * @return
     */
    List<ExamMemberExam> getExamMemberExamList(ExamMemberExam examMemberExam, int pageNo, int pageSize,String areaId,String memberName,String idCardNum,String beginTime,String endTime,Long categoryId);


    Long getCountExamMemberExamList(ExamMemberExam examMemberExam,String areaId, String memberName,String idCardNum,String beginTime,String endTime,Long categoryId);

    List<ExamMemberExam> queryResultQueryList(String areaId, String name, String idCard, String examNumber, String verifyConclusion, String startTime, String endTime, int pageNo, int pageSize, String payState) throws ParseException;

    Long queryCountResultQuery(String areaId, String name, String idCard, String examNumber, String verifyConclusion, String startTime, String endTime, String payState) throws ParseException;

    List<ExamMemberExam> findExamMemberResultDecide(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies, int pageCurrent, int pageSize) throws ParseException;

    Long countExamMemberResultDecide(Page<ExamMemberExam> page, String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies) throws ParseException;

    List<ExamMemberExam> findAffectedExamMemberByIds(String ids);


    ///////////////////////////////////复检管理/////////////////////////////////////
    List<ExamMemberExam> findReviceMemberList(int pageNo, int pageSize,String areaId,String workUnit,String memberName, String idCardNum, String beginTime, String endTime, String categoryId, String isRechekPrint, String examNumber) throws ParseException;

    Long findReviceMemberCount(String areaId,String workUnit,String memberName, String idCardNum, String beginTime, String endTime, String categoryId, String isRechekPrint, String examNumber) throws ParseException;

    List<ExamMemberExam> findByExamNumber(String examNumber);

    //复检管理导出数据查询
    List<ExamMemberExam> getExportReviewList(ExamMemberExam examMemberExam, String areaId, String workUnit, String memberName, String idCardNum, String beginTime, String endTime, String categoryId, String isRechekPrint, String examNumber) throws ParseException;

    List<ExamMemberExam> queryExamReviewList(String ids);

    List<ExamMemberExam> getMemberExamNew(String examNumber);

    //自动判断
    List<ExamMemberExam> getAutoResultDecideList();

    List<ExamMemberExam> findUnFinishExam() throws ParseException;

    // 结论判断审核excel导出信息查询
    List<ExamMemberExam> getExamMemberExamListExcel(ExamMemberExam memberExam, int pageCurrent, int pageSize, String areaId, String memberName, String idCardNum, String beginTime, String endTime, Long openCategoryLevelTwo, String examNumber, String workUnit, String verifyConclusion, String verifyState);

    //乡镇肠检体检信息
    List<ExamMemberExam> getTownsMemberExamLookBack(int pageCurrent, int pageSize, String examNumber, String memberName, String beginTime, String endTime) throws ParseException;

    //乡镇肠检体检条数
    Long countTownsMemberExamLookBack(Page<ExamMemberExam> page, String examNumber, String memberName, String beginTime, String endTime) throws ParseException;
}
