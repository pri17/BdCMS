package com.bidanet.bdcms.controller.queryStatic;

import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
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
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 体检综合查询. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-03 11:05:23
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Controller("examResultQuery")
@RequestMapping ("/admin/queryStatic/examResultQuery")
public class ExamResultQueryController extends AdminBaseController{

    @Autowired
    private ExamMemberExamService examMemberExamService;

    @Autowired
    private ExamBlodIntestinalService examBlodIntestinalService;

    @Autowired
    private ExamAgenciesService agenciesService;

    @RequestMapping("/index")
    public void index(@ModelAttribute Page<ExamMemberExam> page,
                      Model model,String areaId,String name,String idCard,String searchExamNumber,
                      String verifyConclusion,String startTime,String endTime,Long isExrQuery,String payState,
                        String examNumber
    ) throws ParseException{
//        if (isExrQuery!=null) {

        User user = uc.getUser();
        if (searchExamNumber==null||searchExamNumber==""){
            if (examNumber!=""&&examNumber!=null){
                searchExamNumber=examNumber;
            }
        }
        if(areaId == null){
            areaId = user.getAreaId().toString();
        }
            examMemberExamService.getResultQueryList(page, areaId, name, idCard, searchExamNumber, verifyConclusion, startTime, endTime,payState);

            model.addAttribute("currentAreaId",areaId);
            model.addAttribute("currentAgenciesCode",user.getAgenciesCode());
            model.addAttribute("areaId",areaId);
            model.addAttribute("name",name);
            model.addAttribute("idCard",idCard);
            model.addAttribute("searchExamNumber",searchExamNumber);
            model.addAttribute("verifyConclusion",verifyConclusion);
            model.addAttribute("startTime",startTime);
            model.addAttribute("endTime",endTime);
//        }
    }


    /**
     * 获取选定的血检信息的血检结果信息
     * @param memberExamId
     * @return
     */
    @RequestMapping("/findBlodIntestinalDetail")
    @ResponseBody
    public List<ExamBlodIntestinal> findSelectedBlodIntestinalDetail(String memberExamId){

        List<ExamBlodIntestinal> blodIntestinalList =
                examBlodIntestinalService.findSelectedBlodIntestinalDetail(memberExamId);

        return blodIntestinalList;
    }

    /**
     * 结果查询导出
     * @param pageCurrent
     * @param pageSize
     * @param areaId
     * @param name
     * @param idCard
     * @param examNumber
     * @param verifyConclusion
     * @param startTime
     * @param endTime
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value="/exportResultQueryExcel")
    @ResponseBody
    public FileOutput exportResultQueryExcel(int pageCurrent, int pageSize,String payState, String areaId,String name,String idCard,
                                             String examNumber,String verifyConclusion,String startTime,String endTime) throws IOException, ParseException {
        User user = uc.getUser();
        if(StringUtils.isBlank(areaId)){
            areaId = user.getAreaId().toString();
        }
        HSSFWorkbook hw = examMemberExamService.exportResultQueryExcel(pageCurrent,pageSize,payState,areaId,name,idCard,
                examNumber,verifyConclusion,startTime,endTime);
        String path = SpringWebTool.getRealPath("/temp/");
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String  downloadTime= DateTool.timeToStrYmd(new Date().getTime());
        String filename = downloadTime + "-resultQuery.xls";
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


}
