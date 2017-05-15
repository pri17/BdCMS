package com.bidanet.bdcms.service.fee;

import com.bidanet.bdcms.driver.cache.FeeEntity;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamPay;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * Created by jizhaoqun on 16/10/18.
 */
public interface FeeService extends Service<ExamPay> {

    /**
     *查询列表
     * @param areaId
     * @param name
     * @param idCard
     * @param workUnit
     * @param examNumber
     * @param startTime
     * @param endTime
     * @param payState
     * @param payType
     * @param isRecheck
     * @param page
     */
    void findPayList(Long uId,Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck,String feeAdd,String tollCollectorId, Page<FeeEntity> page) throws ParseException;

    //gcx
    void findWXPayList(Long uId,Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String startTimePay, String endTimePay, String isRecheck,String feeAdd,String isPrint, Page<FeeEntity> page) throws ParseException;
    BigDecimal calculateTotalFee(Long userId,String startTime,String endTime) throws ParseException;

    BigDecimal calculateTotalWXFee(String startTime,String endTime) throws ParseException;

    ExamMemberExam  findPayByExamNumber(String examNumber);

//    Map<String,String> findPayByExamNumberConfirm(String examNumber);

    /**
     * 修改收费信息
     * @param examPay
     */
    void updateExamPayT(ExamPay examPay,Long uid);

    void batchProcessT(String ids, int tag, Long id, String name, int paytype);

    abstract void batchProcessT(String ids, int tag, Long id, String name);

    void addExamPayT(ExamPay examPay);

    abstract void batchProcessPrintT(String ids, int tag, Long id, String name);

}
