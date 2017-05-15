package com.bidanet.bdcms.service.fee.impl;

import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.UserDao;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberExamDao;
import com.bidanet.bdcms.dao.fee.ExamPayDao;
import com.bidanet.bdcms.dao.fee.FeeDao;
import com.bidanet.bdcms.driver.cache.FeeEntity;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamPay;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.fee.FeeService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 财务收费. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-18 09:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Service
public class FeeServiceImpl extends BaseServiceImpl<ExamPay> implements FeeService {

    @Autowired
    private FeeDao feeDao;

    @Override
    protected Dao getDao() {
        return feeDao;
    }

    @Autowired
    private ExamMemberExamDao examMemberExamDao;

    @Autowired
    private ExamPayDao examPayDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JedisPool jedisPool;

//    private static final StringBuffer EXAM_FEE_RESULT_KEY = "exam_fee_key";

    /**
     * 收费查询数据放入redis
     * 如果feeadd选中后 则进行数据叠加查询 然后累加redis数据
     *
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
     * @param feeAdd
     * @param page
     * @throws ParseException
     */
    @Override
    public void findPayList(Long uId,Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck, String feeAdd,String tollCollectorId, Page<FeeEntity> page) throws ParseException {

        String EXAM_FEE_RESULT_KEY = uId.toString()+"exam_fee_key";


        Jedis jedis = jedisPool.getResource();
        List<ExamPay> list = new ArrayList<ExamPay>();

        long count = 0;

        //如果feeadd为空 则是不叠加查询 每次查询后进行删除操作
        if (StringUtils.isEmpty(feeAdd)) {

             list = feeDao.findPayNoPageList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck,tollCollectorId);
            long countFeeAddNo = feeDao.findCountPayList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck,tollCollectorId);

            jedis.del(EXAM_FEE_RESULT_KEY);

            count = countFeeAddNo;


        } else if(Integer.valueOf(feeAdd)==0) {

            list = feeDao.findPayNoPageList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck,tollCollectorId);
            long countFeeAddNo = feeDao.findCountPayList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck,tollCollectorId);

            jedis.del(EXAM_FEE_RESULT_KEY);

            count = countFeeAddNo;

        }else if (Integer.valueOf(feeAdd)==1){//选中了复选叠加框后进行数据的叠加
            //如果page.current 不是当前 或者 page.pagesize>=10时 为分页查询 不需要再去查询数据
            if (page.getPageCurrent()>1 || page.getPageSize()>10){

                long redisSize = jedis.llen(EXAM_FEE_RESULT_KEY);

                count = redisSize;

            }else{

                list = feeDao.findPayNoPageList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck,tollCollectorId);
                long countFeeAddYes = feeDao.findCountPayList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck,tollCollectorId);

                long redisSize = jedis.llen(EXAM_FEE_RESULT_KEY);

                count = countFeeAddYes+redisSize;
            }

        }

        List<FeeEntity> feeList = new ArrayList<FeeEntity>();

        System.out.println("List.size="+list.size());


        System.out.println("startTime="+new Date().getTime());

        Pipeline pip = jedis.pipelined();
        //循环处理数据
        for (ExamPay examPay : list) {

            System.out.println("calculateTime1="+new Date().getTime());

            FeeEntity feeEntity = new FeeEntity();

            feeEntity.setId(examPay.getId());
            feeEntity.setPayId(examPay.getId());
            feeEntity.setExamNumber(examPay.getExamNumber());
            feeEntity.setName(examPay.getName());

            feeEntity.setIdCard(examPay.getIdCardNum());
            feeEntity.setWorkUnit(examPay.getWorkUnit());

            feeEntity.setAreaName(examPay.getAreaName());
            feeEntity.setExamTimeStr(examPay.getCreateTimeStr());
            feeEntity.setPayMoney(examPay.getPayMoney());
            feeEntity.setPayStateStr(examPay.getPayState() == 2 ? "已收费" : "未收费");
            feeEntity.setRealName(examPay.getTollCollectorName()==null?"":examPay.getTollCollectorName());
            feeEntity.setPayTimeStr(examPay.getPayTimeStr());
            feeEntity.setIsPrintStr(examPay.getIsPrint() == 1 ? "已打印" : "未打印");
            feeEntity.setPayTypeStr(examPay.getPayTypeStr());
//            feeEntity.setIsRecheckStr(examPay.getIsRecheck() == 1 ? "是" : "否");
            feeEntity.setRecheckTagStr(examPay.getRecheckTag() >0 ? "是" : "否");



            String jsonStr = JsonParseTool.toJson(feeEntity);

            System.out.println("calculateTime2="+new Date().getTime());

//            p.set(EXAM_FEE_RESULT_KEY, jsonStr);
//            jedis.rpush(EXAM_FEE_RESULT_KEY, jsonStr);

            pip.rpush(EXAM_FEE_RESULT_KEY, jsonStr);


            System.out.println("calculateTime3="+new Date().getTime());
        }

        pip.sync();

        System.out.println("middleTime="+new Date().getTime());

        List<FeeEntity> resultList = new ArrayList<FeeEntity>();

        List<String> redisist = jedis.lrange(EXAM_FEE_RESULT_KEY, (page.getPageCurrent() - 1) * page.getPageSize(), page.getPageSize()-1+(page.getPageCurrent()-1)*page.getPageSize());

        for (int i = 0; i < redisist.size(); i++) {

            //String 转 entity
            FeeEntity entity = JsonParseTool.parseObject(redisist.get(i), FeeEntity.class, "数据类型错误");
            resultList.add(entity);

        }

        System.out.println("endTime="+new Date().getTime());

        System.out.println("page.count="+count);

        page.setList(resultList);
        page.setTotal(count);

    }

    @Override
    public BigDecimal calculateTotalFee(Long userId,String startTime,String endTime) throws ParseException{

        //查询获取
       return feeDao.calculateTotalFee(userId,startTime,endTime);
    }

    @Override
    public BigDecimal calculateTotalWXFee(String startTime, String endTime) throws ParseException {
        return feeDao.calculateTotalWXFee(startTime,endTime);
    }

    /**
     * 根据体检号查询用户相关信息
     *
     * @param examNumber
     * @return
     */
    @Override
    public ExamMemberExam findPayByExamNumber(String examNumber) {

        //获取最新的一条数据 如果已经确认 则保存按钮不可点击


        ExamMemberExam memberExam = new ExamMemberExam();

        memberExam.setExamNumber(examNumber);
        memberExam.setIsNew(1);

        List<ExamMemberExam> memberExamList = examMemberExamDao.findByExample(memberExam);

        ExamMemberExam examMemberExamNew = memberExamList.get(0);

        ExamPay examPay = examPayDao.get(examMemberExamNew.getPayId());

//        examMemberExamNew.setExamPay(examPay);
        examMemberExamNew.setPayId(examPay.getId());

        return examMemberExamNew;
    }

//    @Override
//    public Map<String,String> findPayByExamNumberConfirm(String examNumber) {
//
//        Map<String,String> resultMap = new HashMap<String,String>();
//
//        ExamMemberExam memberExam = new ExamMemberExam();
//
//        memberExam.setExamNumber(examNumber);
//        memberExam.setIsNew(1);
//
//        List<ExamMemberExam> memberExamList = examMemberExamDao.findByExample(memberExam);
//
//        if (memberExamList.size()>0){
//
//
//        }else{
//            resultMap.put("errorMsg","");
//        }
//
//        ExamMemberExam examMemberExamNew = memberExamList.get(0);
//
//        ExamPay examPay = examPayDao.get(examMemberExamNew.getPayId());
//
//        examMemberExamNew.setExamPay(examPay);
//
//
//
//
//        return examMemberExamNew;
//
//    }

    @Override
    public void updateExamPayT(ExamPay examPay, Long uid) {

        checkPrice(examPay.getPayActualMoney(), "请填写实际支付金额！");
        ExamPay updatePay = feeDao.get(examPay.getId());
        checkNull(updatePay, "没有找到此条缴费信息！");

        updatePay.setIsPrint(examPay.getIsPrint());
        updatePay.setPayType(examPay.getPayType());
        updatePay.setRemarks(examPay.getRemarks());
        updatePay.setPayActualMoney(examPay.getPayActualMoney());
        Date date = new Date();
        updatePay.setPayTime(date.getTime());
        updatePay.setTollCollectorId(uid);
        updatePay.setPayState(2);
        User user = userDao.get(uid);
        updatePay.setTollCollectorName(user.getRealName());

        updatePay =  feeDao.updateBack(updatePay);

        //更新成功后再去回调更新exam_member_exam

        ExamMemberExam updateMemberExam = examMemberExamDao.get(updatePay.getExamMemberId());

        updateMemberExam.setPayState(2);
        updateMemberExam.setPayType(examPay.getPayType());
        updateMemberExam.setPayMoney(examPay.getPayMoney());
        updateMemberExam.setPayActualMoney(examPay.getPayActualMoney());


        examMemberExamDao.updateBack(updateMemberExam);

    }

    /**
     * 页面上方“刷新列表”按钮单击显示全部数据（即筛选条件为空或全部）。
     * ## “批量确定”按钮，在选中多人后，单击“批量确定”按钮，提示“是否批量确定”，
     * ## 如果是，就将选中人收费状态改为已收费，收费员改为账号名字，
     * ## 收费日期改为当天，是否已经打印发票改为是，收费方式改为现金。
     * ## 单击“批量取消”按钮，就将选中人的收费状态改为未收费，
     * ## 收费员改为空，收费日期改为空，是否已经打印发票改为否，收费方式改为空。
     */

    @Override
    public void batchProcessT(String ids, int tag, Long id, String name,int paytype) {

        String[] idsStrArray = ids.split(",");


        for (int i = 0; i < idsStrArray.length; i++) {

            ExamPay newPay = feeDao.get(Long.parseLong(idsStrArray[i]));

            checkNull(newPay, "没有找到此条缴费信息！");

            if (tag == 1) {//批量确认

                Date date = new Date();
                //已付款
                newPay.setPayState(2);
                //已打发票
                newPay.setIsPrint(1);
                newPay.setPayType(paytype);
                newPay.setIsNew(0);
                newPay.setPayTime(date.getTime());
                newPay.setTollCollectorId(id);
                newPay.setTollCollectorName(name);
//                newPay.setCreatorId(id);
//                newPay.setCreatorName(name);


            } else if (tag == 2) {//批量撤销

                //待付款
                newPay.setPayState(1);
                //未打发票
                newPay.setIsPrint(0);
                //支付方式
                newPay.setPayType(9);
                newPay.setIsNew(1);
                newPay.setPayTime(null);
                newPay.setTollCollectorName(null);
                newPay.setTollCollectorId(null);
            }

            feeDao.update(newPay);

            ExamMemberExam updateMemberExam = examMemberExamDao.get(newPay.getExamMemberId());

            updateMemberExam.setPayState(newPay.getPayState());
            updateMemberExam.setPayType(newPay.getPayType());
            updateMemberExam.setPayMoney(newPay.getPayMoney());
            updateMemberExam.setPayActualMoney(newPay.getPayActualMoney());


            examMemberExamDao.updateBack(updateMemberExam);
        }



    }
    @Override
    public void batchProcessT(String ids, int tag, Long id, String name){
        batchProcessT(ids, tag, id, name, 0);
    }
    @Override
    public void addExamPayT(ExamPay examPay) {
        feeDao.save(examPay);
    }

    @Override
    public void batchProcessPrintT(String ids, int tag, Long id, String name) {
        String[] idsStrArray = ids.split(",");

        for (int i = 0; i < idsStrArray.length; i++) {

            ExamPay newPay = feeDao.get(Long.parseLong(idsStrArray[i]));

            checkNull(newPay, "没有找到此条缴费信息！");

            if (tag == 1) {//批量确认
                newPay.setIsPrint(1);
            } else if (tag == 2) {//批量撤销
                //未打发票
                newPay.setIsPrint(0);
            }

            feeDao.update(newPay);
        }
    }

    @Override
    public void findWXPayList(Long uId, Long areaId, String name, String idCard, String workUnit, String examNumber, String startTime, String endTime, String startTimePay, String endTimePay, String isRecheck, String feeAdd, String isPrint, Page<FeeEntity> page) throws ParseException {
        String EXAM_FEE_RESULT_KEY = uId.toString()+"exam_fee_key";


        Jedis jedis = jedisPool.getResource();
        List<ExamPay> list = new ArrayList<ExamPay>();

        long count = 0;

        //如果feeadd为空 则是不叠加查询 每次查询后进行删除操作
        if (StringUtils.isEmpty(feeAdd)) {

            list = feeDao.findWXPayNoPageList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, startTimePay, endTimePay, isRecheck, isPrint);
            long countFeeAddNo = feeDao.findCountWXPayList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, startTimePay, endTimePay, isRecheck, isPrint);

            jedis.del(EXAM_FEE_RESULT_KEY);

            count = countFeeAddNo;


        } else if(Integer.valueOf(feeAdd)==0) {

            list = feeDao.findWXPayNoPageList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, startTimePay, endTimePay, isRecheck, isPrint);
            long countFeeAddNo = feeDao.findCountWXPayList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, startTimePay, endTimePay, isRecheck, isPrint);

            jedis.del(EXAM_FEE_RESULT_KEY);

            count = countFeeAddNo;

        }else if (Integer.valueOf(feeAdd)==1){//选中了复选叠加框后进行数据的叠加
            //如果page.current 不是当前 或者 page.pagesize>=10时 为分页查询 不需要再去查询数据
            if (page.getPageCurrent()>1 || page.getPageSize()>10){

                long redisSize = jedis.llen(EXAM_FEE_RESULT_KEY);

                count = redisSize;

            }else{

                list = feeDao.findWXPayNoPageList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, startTimePay, endTimePay, isRecheck, isPrint);
                long countFeeAddYes = feeDao.findCountWXPayList(areaId, name, idCard, workUnit, examNumber, startTime, endTime, startTimePay, endTimePay, isRecheck, isPrint);

                long redisSize = jedis.llen(EXAM_FEE_RESULT_KEY);

                count = countFeeAddYes+redisSize;
            }

        }

        List<FeeEntity> feeList = new ArrayList<FeeEntity>();

        System.out.println("List.size="+list.size());


        System.out.println("startTime="+new Date().getTime());

        Pipeline pip = jedis.pipelined();
        //循环处理数据
        for (ExamPay examPay : list) {

            System.out.println("calculateTime1="+new Date().getTime());

            FeeEntity feeEntity = new FeeEntity();

            feeEntity.setId(examPay.getId());
            feeEntity.setPayId(examPay.getId());
            feeEntity.setExamNumber(examPay.getExamNumber());
            feeEntity.setName(examPay.getName());

            feeEntity.setIdCard(examPay.getIdCardNum());
            feeEntity.setWorkUnit(examPay.getWorkUnit());

            feeEntity.setAreaName(examPay.getAreaName());
            feeEntity.setExamTimeStr(examPay.getCreateTimeStr());
            feeEntity.setPayMoney(examPay.getPayMoney());
            feeEntity.setPayStateStr(examPay.getPayState() == 2 ? "已收费" : "未收费");
            feeEntity.setRealName(examPay.getTollCollectorName()==null?"":examPay.getTollCollectorName());
            feeEntity.setPayTimeStr(examPay.getPayTimeStr());
            feeEntity.setIsPrintStr(examPay.getIsPrint() == 1 ? "已打印" : "未打印");
            feeEntity.setPayTypeStr(examPay.getPayTypeStr());
//            feeEntity.setIsRecheckStr(examPay.getIsRecheck() == 1 ? "是" : "否");
            feeEntity.setRecheckTagStr(examPay.getRecheckTag() >0 ? "是" : "否");



            String jsonStr = JsonParseTool.toJson(feeEntity);

            System.out.println("calculateTime2="+new Date().getTime());

//            p.set(EXAM_FEE_RESULT_KEY, jsonStr);
//            jedis.rpush(EXAM_FEE_RESULT_KEY, jsonStr);

            pip.rpush(EXAM_FEE_RESULT_KEY, jsonStr);


            System.out.println("calculateTime3="+new Date().getTime());
        }

        pip.sync();

        System.out.println("middleTime="+new Date().getTime());

        List<FeeEntity> resultList = new ArrayList<FeeEntity>();

        List<String> redisist = jedis.lrange(EXAM_FEE_RESULT_KEY, (page.getPageCurrent() - 1) * page.getPageSize(), page.getPageSize()-1+(page.getPageCurrent()-1)*page.getPageSize());

        for (int i = 0; i < redisist.size(); i++) {

            //String 转 entity
            FeeEntity entity = JsonParseTool.parseObject(redisist.get(i), FeeEntity.class, "数据类型错误");
            resultList.add(entity);

        }

        System.out.println("endTime="+new Date().getTime());

        System.out.println("page.count="+count);

        page.setList(resultList);
        page.setTotal(count);
    }
}
