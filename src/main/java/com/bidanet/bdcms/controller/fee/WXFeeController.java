package com.bidanet.bdcms.controller.fee;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.bean.WXPay;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.driver.cache.FeeEntity;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.ExamPay;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.exception.CheckException;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.fee.FeeService;
import com.bidanet.bdcms.vo.Page;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/3/16.
 */
@Controller
@RequestMapping("/admin/fee/wxfee")
public class WXFeeController  extends AdminBaseController {

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
            String name, String startTime, String endTime, String idCard, String workUnit, String examNumber, String startTimePay, String endTimePay, String isRecheck, Long isFeeQuery,
            @ModelAttribute Page<FeeEntity> page, Model model, String tabid, String feeAdd,String isPrint) throws ParseException {

        User user = uc.getUser();

        if(areaId == null){
            areaId = user.getAreaId();
        }else if(areaId==0){
            areaId=null;
        }

        feeService.findWXPayList(user.getUid(), areaId, name, idCard, workUnit, examNumber, startTime, endTime, startTimePay, endTimePay, isRecheck, feeAdd, isPrint, page);

        if(areaId==null){
            areaId=0L;
        }


        BigDecimal decimal =  feeService.calculateTotalWXFee(startTime,endTime);
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

        model.addAttribute("isPrint", isPrint);
        model.addAttribute("areaId", areaId);
        model.addAttribute("isRecheck", isRecheck);
        model.addAttribute("startTimePay", startTimePay);
        model.addAttribute("endTimePay", endTimePay);
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

        feeService.batchProcessPrintT(ids, tag, uc.getUser().getUid(), uc.getUser().getRealName());

        AjaxCallBack ajaxCallBack = AjaxCallBack.handleSuccess();
        ajaxCallBack.setTabid("index");
        return ajaxCallBack;
    }

    @RequestMapping("/exportWXPay")
    @ResponseBody
    public FileOutput exportWXPay(String ids) {
        String[] idsStrArray = ids.split(",");
        List<ExamPay> list=new ArrayList<>();
        for(int i=0;i<idsStrArray.length;i++){

            ExamPay ep = feeService.get(Long.parseLong(idsStrArray[i]));
            list.add(ep);
        }
            ArrayList<WXPay> exportGradeExcels = new ArrayList<>();
            for (ExamPay epe : list) {
                WXPay wXPaExcel = new WXPay();
                wXPaExcel.setExamNumber(epe.getExamNumber());
                wXPaExcel.setAreaName(epe.getAreaName());
                wXPaExcel.setExamTimeStr(epe.getCreateTimeStr());
                wXPaExcel.setIdCard(epe.getIdCardNum());
                wXPaExcel.setIsPrintStr(epe.getIsPrint() == 1 ? "已打印" : "未打印");
                wXPaExcel.setName(epe.getName());
                wXPaExcel.setNumber(1+"");
                wXPaExcel.setPayMoney(epe.getPayMoney().toString());
                wXPaExcel.setPayTimeStr(epe.getPayTimeStr());
                wXPaExcel.setRecheckTagStr(epe.getRecheckTag() >0 ? "是" : "否");
                wXPaExcel.setWorkUnit(epe.getWorkUnit());
                exportGradeExcels.add(wXPaExcel);
            }

            Workbook workbook = ExcelExportUtil
                    .exportExcel(new ExportParams(), WXPay.class, exportGradeExcels);
            try {
                File tempFile = File.createTempFile("wxFee", ".xls");
                workbook.write(new FileOutputStream(tempFile));
                return new FileOutput(tempFile);
            } catch (IOException e) {
                throw new CheckException(e.getMessage());
            }
    }
}
