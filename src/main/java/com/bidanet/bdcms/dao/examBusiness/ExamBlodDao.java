package com.bidanet.bdcms.dao.examBusiness;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamBlod;
import com.bidanet.bdcms.vo.Page;

import java.text.ParseException;
import java.util.List;

/**
* ExamBlod DAO
*/
public interface ExamBlodDao extends Dao<ExamBlod> {

    List<ExamBlod> findBigNumberToday(Long todayStart, Long todayEnd);

    List<ExamBlod> findBlodMember(Page<ExamBlod> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber, int pageCurrent, int pageSize) throws ParseException ;

    Long countBlodMember(Page<ExamBlod> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber) throws ParseException ;

    /**
     * 采样结果查询-血检采样集
     * @param startTime
     * @param endTime
     * @param areaId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ParseException
     */
    List<ExamBlod> queryBlodReocrdQueryList(String startTime,String endTime,String areaId,int pageNo, int pageSize) throws ParseException;

    List<ExamBlod> queryBlodReocrdExcelQueryList(String startTime,String endTime,String areaId) throws ParseException;
    /**
     * 采样结果查询-血检采样集
     * @param startTime
     * @param endTime
     * @param areaId
     * @return
     * @throws ParseException
     */
    Long queryCountBlodReocrdQuery(String startTime,String endTime,String areaId) throws ParseException;
}
