package com.bidanet.bdcms.controller.fee;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.driver.cache.FeeEntity;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamPay;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.fee.FeeService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

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
@Controller
@RequestMapping("/admin/fee")
public class FeeController extends AdminBaseController {


    @Autowired
    private FeeService feeService;

    @Autowired
    private ExamMemberExamService examMemberExamService;

    @Autowired
    private ExamPayService examPayService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(
            Long areaId,
            String name, String startTime, String endTime, String idCard, String workUnit, String examNumber, String payState, String payType, String isRecheck, Long isFeeQuery,
            @ModelAttribute Page<FeeEntity> page, Model model, String tabid, String feeAdd,String tollCollectorId) throws ParseException {
//        if (isFeeQuery == null) {
//            payState = "1";
        User user = uc.getUser();

        if(areaId == null){
            areaId = user.getAreaId();
        }else if(areaId==0){
            areaId=null;
        }

        feeService.findPayList(user.getUid(), areaId, name, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, feeAdd,tollCollectorId, page);

        if(areaId==null){
            areaId=0L;
        }

        //该收费员一天收费的总金额
        //根据时间 从今天的凌晨开始 到凌晨结束 查询该用户等级的数据且收费状态已收费
        //再根据实际收费金额进行累加
        BigDecimal decimal =  feeService.calculateTotalFee(user.getUid(),startTime,endTime);
        model.addAttribute("totalFee",decimal);

        model.addAttribute("userId",user.getUid());
        model.addAttribute("currentAreaId",user.getAreaId());
        model.addAttribute("currentAgenciesCode",user.getAgenciesCode());
        model.addAttribute("examNumber", examNumber);
        model.addAttribute("idCard", idCard);
        model.addAttribute("startTime", startTime);
        model.addAttribute("name", name);
        model.addAttribute("workUnit", workUnit);
        model.addAttribute("endTime", endTime);

        model.addAttribute("tollCollectorId", tollCollectorId);
        model.addAttribute("areaId", areaId);
        model.addAttribute("isRecheck", isRecheck);
        model.addAttribute("payState", payState);
        model.addAttribute("payType", payType);
        model.addAttribute("feeAdd", feeAdd);
        tableId = tabid;
    }

    @RequestMapping("/toActionList")
    public void toActionList(String id, Model model) {

        System.out.print("ids-----" + id);

    }


    /**
     * 弹出体检收费确认框
     * 根据体检号带出相关用户信息
     * 金额不可修改
     * 收费方式和是否已经打印发票这两项可以修改
     *
     * @param examNumber 体检号
     * @param model
     */
    @RequestMapping("/toConfirmFee")
    public void toConfirmFee(String examNumber, Model model) {

        //根据examNumber查询该用户--该用户是最新的一条记录
        ExamMemberExam memberExam = feeService.findPayByExamNumber(examNumber);

        model.addAttribute("memberExam", memberExam);
    }

    /**
     * 保存收费信息
     *
     * @return
     */
    @RequestMapping("/confirmFee")
    @ResponseBody
    public AjaxCallBack confirmFee(ExamPay examPay) {
        ExamPay examPay1 = feeService.get(examPay.getId());

        Long userId = uc.getUser().getUid();
        feeService.updateExamPayT(examPay1, userId);

        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }


    @RequestMapping("/scanBack")
    @ResponseBody
    public AjaxCallBack scanBack(String examNumber) {

        List<ExamMemberExam> examMemberExamsList = examMemberExamService.getMemberExamNew(examNumber);


        if(examMemberExamsList.size()>0){

            ExamMemberExam examMemberExam = examMemberExamsList.get(0);

            return AjaxCallBack.success(examMemberExam.getPayId().toString());

        }else{

            return AjaxCallBack.error("获取相关人员信息失败!");

        }


    }

    /**
     * 弹出体检收费确认框
     * 根据体检号带出相关用户信息
     * 金额不可修改
     * 收费方式和是否已经打印发票这两项可以修改
     *
     * @param
     * @param model
     */
    @RequestMapping("/toCommitFee")
    public void toCommitFee(String id, Model model) {

        //根据examNumber查询该用户
//        ExamMemberExam memberExam = examMemberExamService.get(Long.parseLong(id));
        ExamPay examPay = examPayService.get(Long.parseLong(id));


        model.addAttribute("examPay", examPay);
    }

    /**
     * 保存收费信息,
     *
     * @return
     */
    @RequestMapping("/commitFee")
    @ResponseBody
    public AjaxCallBack commitFee(ExamPay examPay) {

        ExamPay examPay1 = feeService.get(examPay.getId());
        examPay1.setPayType(examPay.getPayType());
        examPay1.setPayActualMoney(examPay.getPayActualMoney());
        examPay1.setRemarks(examPay.getRemarks());
        examPay1.setIsPrint(1);

        Long userId = uc.getUser().getUid();
        feeService.updateExamPayT(examPay1, userId);

        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid("table_46");
        return ajaxCallBack;
    }


    /**
     * 批量处理
     * ids 批处理的ids以都好分隔
     * tag  1：批量确认   2：批量撤销
     *
     * @return
     */
    @RequestMapping("/batchProcess")
    @ResponseBody
    public AjaxCallBack batchProcess(String ids, int tag) {

        uc.getLoginUid();

        feeService.batchProcessT(ids, tag, uc.getUser().getUid(), uc.getUser().getRealName());

        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
        ajaxCallBack.setTabid("index");
        return ajaxCallBack;
    }

}
