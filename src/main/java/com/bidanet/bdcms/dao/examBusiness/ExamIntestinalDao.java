package com.bidanet.bdcms.dao.examBusiness;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamIntestinal;
import com.bidanet.bdcms.vo.Page;

import java.text.ParseException;
import java.util.List;

/**
* ExamIntestinal DAO
*/
public interface ExamIntestinalDao extends Dao<ExamIntestinal> {

    List<ExamIntestinal> queryIntestinalReocrdQueryList(String startTime, String endTime, String areaId, int pageNo, int pageSize) throws ParseException;

    List<ExamIntestinal> queryIntestinalReocrdExcelQueryList(String startTime, String endTime, String areaId) throws ParseException;

    Long queryCountIntestinalReocrdQuery(String startTime,String endTime,String areaId) throws ParseException;

    List<ExamIntestinal> findIntestinalMember(Page<ExamIntestinal> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber, int pageCurrent, int pageSize) throws ParseException;

    Long countIntestinalMember(Page<ExamIntestinal> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber) throws ParseException;

    /**
     * 查询疾控肠检采集信息
     * @param code
     * @return
     */
    List<ExamIntestinal> findIntestinalByCode(String code);
}
