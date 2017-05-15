package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamIntestinal;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
*
*/
public interface ExamIntestinalService extends Service<ExamIntestinal> {

    Long generateIntestinalTestNumber(String examNumber);

    Long saveIntestinalTS(String examNumber);
    void saveIntestinalT(String examNumber,Long testNumber);
    /**
     * 采样记录查询-肠检结果集
     * @param page
     * @param startTime
     * @param endTime
     * @param areaId
     * @throws ParseException
     */
    void queryIntestinalReocrdQueryList(Page page , String startTime, String endTime, String areaId) throws ParseException;

    List<ExamIntestinal> queryPrintIntestinalReocrdList(int pageCurrent, int pageSize , String startTime, String endTime, String areaId) throws ParseException;

    void findIntestinalMember(Page<ExamIntestinal> page);
    void findIntestinalMember(Page<ExamIntestinal> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber) throws ParseException;

    /**
     * 扫描条形码操作
     * @param examNumber
     * @param map
     */
    void checkTownsIntestinalT(String examNumber, Map<String, Object> map);

    /**
     * 保存录入的肠检采样号信息
     */
    void saveTownsIntestinalTestNumberT(String examNumber, String sampleNumber);

    HSSFWorkbook downIntestinalExcel(String beginTime, String endTime, String areaId)  throws ParseException;
}
