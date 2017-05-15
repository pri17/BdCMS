package com.bidanet.bdcms.controller.queryStatic;

import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.examBusiness.ExamAreaService;
import com.bidanet.bdcms.service.examBusiness.ExamBlodService;
import com.bidanet.bdcms.service.examBusiness.ExamIntestinalService;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
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
@Controller("samplingRecord")
@RequestMapping("/admin/queryStatic/samplingRecordQuery")
public class SamplingRecordQueryController extends AdminBaseController {

    @Autowired
    private ExamBlodService examBlodService;

    @Autowired
    private ExamIntestinalService examIntestinalService;

    @Autowired
    private ExamAreaService examAreaService;

    /**
     * 查询列表
     * 1、血检的复检项目需要单独查询后进行拼接加入
     *
     * @param startTime
     * @param endTime
     * @param sampleType
     * @param areaId
     * @param page
     * @param model
     */
    @RequestMapping("/index")
    public void queryBlodRecordList(@ModelAttribute Page page,String simpleRecordIsQuery, String startTime, String endTime, String sampleType, String areaId, Model model, Long isSrQuery) throws ParseException {
        User user = uc.getUser();
        if (simpleRecordIsQuery != null && StringUtils.isNotEmpty(simpleRecordIsQuery)) {

            if (areaId == null) {
                areaId = user.getAreaId().toString();
            }
            if (StringUtils.isEmpty(sampleType)) {
                examBlodService.queryBlodReocrdQueryList(page, startTime, endTime, areaId);

                for (Object b : page.getList()) {
                    String examRecheckStr = "";
                    ExamBlod blod = (ExamBlod) b;

                    for (ExamBlodIntestinal bi : blod.getBlodIntestinals()) {

                        if (bi.getIsRecheck() == 1) {

                            String projectName = bi.getProjectName();

                            examRecheckStr += projectName + ",";
                        }
                    }

                    blod.setExamRecheckStr(examRecheckStr);
                }

            } else if (Integer.parseInt(sampleType) == 1) {
                examBlodService.queryBlodReocrdQueryList(page, startTime, endTime, areaId);

                for (Object b : page.getList()) {
                    String examRecheckStr = "";
                    ExamBlod blod = (ExamBlod) b;

                    for (ExamBlodIntestinal bi : blod.getBlodIntestinals()) {

                        if (bi.getIsRecheck() == 1) {

                            String projectName = bi.getProjectName();

                            examRecheckStr += projectName + ",";
                        }
                    }

                    blod.setExamRecheckStr(examRecheckStr);

                }

            } else if (Integer.parseInt(sampleType) == 2) {
                examIntestinalService.queryIntestinalReocrdQueryList(page, startTime, endTime, areaId);
            }

            model.addAttribute("currentAreaId", areaId);
            model.addAttribute("currentAgenciesCode", user.getAgenciesCode());
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
            model.addAttribute("sampleType", sampleType);
            model.addAttribute("areaId", areaId);
            model.addAttribute("simpleRecordIsQuery",simpleRecordIsQuery);
        } else {
            model.addAttribute("currentAgenciesCode", user.getAgenciesCode());
            model.addAttribute("simpleRecordIsQuery","1");

        }
    }

    /**
     * 打印血检列表
     *
     * @param startTime
     * @param endTime   //     * @param areaId
     * @param model
     */
    @RequestMapping("/printBlod")
    public void printBlod(String startTime, String endTime, String areaId, int pageCurrent, int pageCount, int pageSize, Model model) throws ParseException {
        List<ExamBlod> list = examBlodService.queryPrintBlodReocrdList(1, 2000, startTime, endTime, areaId);

        String examRecheckStr = "";

        for (Object b : list) {

            ExamBlod blod = (ExamBlod) b;

            for (ExamBlodIntestinal bi : blod.getBlodIntestinals()) {

                if (bi.getIsRecheck() == 1) {

                    String projectName = bi.getProjectName();

                    examRecheckStr += projectName + ",";
                }
            }

            blod.setExamRecheckStr(examRecheckStr);

        }
        ExamArea area = new ExamArea();
        if (StringUtils.isNotEmpty(areaId)) {

            area = examAreaService.get(Long.parseLong(areaId));

        }
        int printSize=16;
        int printPage= list.size()/printSize+(list.size()%printSize==0?0:1) ;
        ArrayList<List> examBlods = new ArrayList<>();
        for (int i = 0; i < printPage; i++) {
            ArrayList<ExamBlod> blods = new ArrayList<>();
            for (int j = 0; j < printSize; j++) {
                int getIndex = i * printSize + j;
                if (getIndex<list.size()) {
                    blods.add(list.get(getIndex));
                }
            }
            examBlods.add(blods);
        }
        model.addAttribute("printPage",printPage);
        model.addAttribute("listPage",examBlods);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("sampleType", "血检");
        model.addAttribute("areaName", area.getAreaName()==null?"全部":area.getAreaName());
        model.addAttribute("blodList", list);
        model.addAttribute("pageCurrent", pageCurrent);
        model.addAttribute("pageCount", pageCount);


    }


    /**
     * 打印肠检列表
     *
     * @param startTime
     * @param endTime   //     * @param areaId
     * @param model
     */
    @RequestMapping("/printIntestinal")
    public void printIntestinal(String startTime, String endTime, String areaId, int pageCurrent, int pageCount, int pageSize, Model model) throws ParseException {

        List<ExamIntestinal> list = examIntestinalService.queryPrintIntestinalReocrdList(pageCurrent, 2000, startTime, endTime, areaId);

        ExamArea area = new ExamArea();
        if (StringUtils.isNotEmpty(areaId)) {

            area = examAreaService.get(Long.parseLong(areaId));

        }

        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("sampleType", "肠检");
        model.addAttribute("areaName", area.getAreaName()==null?"全部":area.getAreaName());
        model.addAttribute("intestinalList", list);
        model.addAttribute("pageCurrent", pageCurrent);
        model.addAttribute("pageCount", pageCount);

        int printSize=16;
        int printPage= list.size()/printSize+(list.size()%printSize==0?0:1) ;
        ArrayList<List> examBlods = new ArrayList<>();
        for (int i = 0; i < printPage; i++) {
            ArrayList<ExamIntestinal> blods = new ArrayList<>();
            for (int j = 0; j < printSize; j++) {
                int getIndex = i * printSize + j;
                if (getIndex<list.size()) {
                    blods.add(list.get(getIndex));
                }
            }
            examBlods.add(blods);
        }
        model.addAttribute("printPage",printPage);
        model.addAttribute("listPage",examBlods);


    }


    /**
     * 导出采样表记录
     * @param request
     * @param response
     * @param model
     * @param beginTime
     * @param endTime
     * @param areaId
     * @param sampleType
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/exportRecordExcel")
    @ResponseBody
    public FileOutput exportRecordExcel(HttpServletRequest request, HttpServletResponse response, Model model, String beginTime, String endTime, String areaId, String sampleType ) throws Exception {
        HSSFWorkbook hw = new HSSFWorkbook();

        if (Integer.valueOf(sampleType)==1){//血检

            hw = examBlodService.downBlodExcel(beginTime,endTime,areaId);

        }else if (Integer.valueOf(sampleType)==2){//肠检

            hw = examIntestinalService.downIntestinalExcel(beginTime,endTime,areaId);
        }



        String path = SpringWebTool.getRealPath("/temp/");
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String filename = new Date().getTime() + "采样记录.xls";
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
