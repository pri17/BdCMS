package com.bidanet.bdcms.controller.examManage;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamAutoDoctor;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.examBusiness.ExamAutoDoctorService;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberService;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.service.systemSetting.ExamProjectService;
import com.bidanet.bdcms.vo.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 财务收费. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-02 09:27:37
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Controller ("resultDecide")
@RequestMapping ("/admin/examManage/resultDecide")
public class ResultDecideController extends AdminBaseController{


    @Autowired
    private ExamMemberExamService examMemberExamService;
    @Autowired
    private ExamMemberService examMemberService;
    @Autowired
    private ExamAgenciesService examAgenciesService;
    @Autowired
    private ExamEcardService examEcardService;
    @Autowired
    private ExamBlodIntestinalService examBlodIntestinalService;
    @Autowired
    private ExamProjectService examProjectService;
    @Autowired
    private ExamPayService examPayService;

    @Autowired
    private ExamAutoDoctorService examAutoDoctorService;

    private String tableId = "";

    /**
     * 进入结论判断审核页面，加载下拉列表的一些数据
     * @param model
     * @param query
     * @param page
     * @param memberName
     * @param idCardNum
     * @param beginTime
     * @param endTime
     * @param openCategoryLevelTwo
     * @param tabid
     * @param isRdQuery
     * @param parentId
     * @param rdOpenCategoryLevelTwo
     */
    @RequestMapping("/index")
    public void index(Model model,
                      @ModelAttribute("query") ExamMemberExam query,
                      @ModelAttribute Page<ExamMemberExam> page, String areaId,String memberName, String idCardNum, String beginTime, String endTime, Long openCategoryLevelTwo, String tabid, Long isRdQuery,Long parentId,Long rdOpenCategoryLevelTwo) {
//        if (isRdQuery != null) {

        User user = uc.getUser();

        // 注释掉地区默认值
/*        if(areaId == null){
            areaId = user.getAreaId().toString();
        }*/
         if(query.getAreaId()!=null){
        areaId = String.valueOf(query.getAreaId());}
        if("".equals(areaId)){
             areaId=null;
        }



            examMemberExamService.getMemberExamList(query, page,areaId, memberName, idCardNum, beginTime, endTime, rdOpenCategoryLevelTwo);

            ExamAutoDoctor examAutoDoctor = examAutoDoctorService.get(1L);

        model.addAttribute("currentAreaId",areaId);
        model.addAttribute("currentAgenciesCode",user.getAgenciesCode());
            model.addAttribute("memberName", memberName);
            model.addAttribute("idCardNum", idCardNum);
            model.addAttribute("beginTime", beginTime);
            model.addAttribute("endTime", endTime);
            model.addAttribute("parentId", parentId);
            model.addAttribute("rdOpenCategoryLevelTwoId", rdOpenCategoryLevelTwo);
             model.addAttribute("examAutoDoctor",examAutoDoctor);
//        }
        tableId = tabid;
    }
/*********************************************** 判断审核 begin *******************************************************************/
    /**
     * 判断审核
     * @param
     * @return
     */
    @RequestMapping("/batchAudit")
    @ResponseBody
    public AjaxCallBack batchAudit(String examIds) {
        List<String> examIdList = Arrays.asList(examIds.split(","));
        User user = uc.getUser();


        examMemberExamService.batchAuditT(examIdList,user);

        AjaxCallBack ajaxCallBack = new AjaxCallBack();

        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }


/*********************************************** 判断审核 end *******************************************************************/

    /**
     * 撤销审核
     * 审核状态   1：审核   2：未审核
     * 体检结果:0、未判断 1：合格  2：不合格  默认值都为0
     * @param
     * @return
     */
    @RequestMapping("/cancelChecked")
    @ResponseBody
    public AjaxCallBack cancelChecked(String examIds) {
        List<String> examIdList = Arrays.asList(examIds.split(","));
        for (int i = 0; i < examIdList.size(); i++) {
            ExamMemberExam examMemberExam = examMemberExamService.get(Long.valueOf(examIdList.get(i)));



        }
        return AjaxCallBack.saveSuccess();
    }



    /**
     * 结论判断审核excel导出
     *
     * @param query
     * @param pageCurrent
     * @param pageSize
     * @param memberName
     * @param idCardNum
     * @param beginTime
     * @param endTime
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/exportFindExcel")
    @ResponseBody
    public FileOutput exportFindExcel(ExamMemberExam query,int pageCurrent, int pageSize, String areaId,String memberName, String idCardNum, String beginTime, String endTime,
                                      Long rdOpenCategoryLevelTwoId,String examNumber,String workUnit,String verifyConclusion,String verifyState) throws IOException {
        HSSFWorkbook hw = examMemberExamService.exportFindExcel(query, pageCurrent, pageSize,areaId, URLDecoder.decode(memberName,"UTF-8"), idCardNum, beginTime, endTime, rdOpenCategoryLevelTwoId,
                examNumber,URLDecoder.decode(workUnit,"UTF-8"),verifyConclusion,verifyState);
        String path = SpringWebTool.getRealPath("/temp/");
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String  downloadTime= DateTool.timeToStrYmd(new Date().getTime());
        String filename = downloadTime + ".xls";
        File file = new File(path + "/" + filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream stream = new FileOutputStream(file);
        hw.write(stream);
        stream.flush();
        stream.close();
        return new FileOutput(file, filename);
    }


    @RequestMapping(value = "/updateAutoDoctor")
    @ResponseBody
    public AjaxCallBack updateAutoDoctor(String doctorName){

        ExamAutoDoctor examAutoDoctor = examAutoDoctorService.get(1L);

        examAutoDoctor.setName(doctorName);

        examAutoDoctor =  examAutoDoctorService.updateBack(examAutoDoctor);

        if (examAutoDoctor.getId()!=null){

            return AjaxCallBack.success("修改成功");
        }else{
            return AjaxCallBack.error("修改失败");
        }
    }
}
