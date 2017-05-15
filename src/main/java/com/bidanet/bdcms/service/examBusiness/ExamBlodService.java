package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamBlod;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

import java.text.ParseException;

/**
*
*/
public interface ExamBlodService extends Service<ExamBlod> {

    Long findBigNumberToday(Long todayStart, Long todayEnd, String examNumber);

    Long saveExamBlodTS(String examNumber);

    void findBlodMember(Page<ExamBlod> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber) throws ParseException ;

    /**
     * 采样结果查询-血检采样集
     * @param page
     * @param startTime
     * @param endTime
     * @param areaId
     * @throws ParseException
     */
    void queryBlodReocrdQueryList(Page page ,String startTime,String endTime,String areaId) throws ParseException;

    List<ExamBlod> queryPrintBlodReocrdList(int pageCurrent,int pageSize ,String startTime,String endTime,String areaId) throws ParseException;

    HSSFWorkbook downBlodExcel(String beginTime, String endTime, String areaId)  throws ParseException;
}
