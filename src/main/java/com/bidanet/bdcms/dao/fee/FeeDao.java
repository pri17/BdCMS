package com.bidanet.bdcms.dao.fee;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamPay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * Created by jizhaoqun on 16/10/18.
 */
public interface FeeDao extends Dao<ExamPay> {

    List<ExamMemberExam> findPayList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck, int pageNo, int pageSize) throws ParseException;

    List<ExamPay> findPayNoPageList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck,String tollCollectorId) throws ParseException;

    Long findCountPayList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck,String tollCollectorId) throws ParseException;

    BigDecimal calculateTotalFee(Long userId,String startTime,String endTime) throws ParseException;

    BigDecimal calculateTotalWXFee(String startTime,String endTime) throws ParseException;

    //gcx
    List<ExamPay> findWXPayNoPageList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String startTimePay, String endTimePay, String isRecheck,String isPrint) throws ParseException;

    Long findCountWXPayList(Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String startTimePay, String endTimePay, String isRecheck,String isPrint) throws ParseException;

}
