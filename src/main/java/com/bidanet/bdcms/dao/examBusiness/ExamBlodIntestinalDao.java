package com.bidanet.bdcms.dao.examBusiness;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.User;

import java.text.ParseException;
import java.util.List;

/**
* ExamBlodIntestinal DAO
*/
public interface ExamBlodIntestinalDao extends Dao<ExamBlodIntestinal> {


    List<ExamBlodIntestinal> findByMemberId(Long id);

    List<ExamBlodIntestinal> findBIRecordQueryList(String startTime,String endTime,String areaId,String sampleType, int pageNo, int pageSize);

    Long findCountBIRecordList(String startTime,String endTime,String areaId,String sampleType);

    /**
     * 根据memberExamId获取所有对应的数据 便于删除操作
     * @param memberExamId
     * @return
     */
    List<ExamBlodIntestinal> findByMemberExamId(Long memberExamId);

    /**
     * 查询筛选范围内的血检信息对应的血检结果
     * @param beginTime
     * @param endTime
     * @param entryState
     * @param areaId
     * @param packageId
     * @param examNumber
     * @param type
     * @return
     */
    List<ExamBlodIntestinal> defaultSelectedResultByCondition(String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber, String type) throws ParseException;

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
     * @return
     */
    List<ExamBlodIntestinal> defaultSelectedResultDecide(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, User loginUser) throws ParseException;

    //体检结果判断信息导出查询
    List<ExamBlodIntestinal> findExcelResultDecide(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies, int pageCurrent, int pageSize) throws ParseException;

    //体检结果查询信息导出查询
    List<ExamBlodIntestinal> findExcelResultQuery(String name, String idCard, String startTime, String endTime, String payState, String areaId, String verifyConclusion, String examNumber, int pageCurrent, int pageSize) throws ParseException;
}
