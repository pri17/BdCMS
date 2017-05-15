package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.dao.examBusiness.ExamBlodIntestinalDao;
import com.bidanet.bdcms.dao.examBusiness.ExamIntestinalDao;
import com.bidanet.bdcms.dao.examBusiness.ExamMemberExamDao;
import com.bidanet.bdcms.dao.examBusiness.ExamNumberDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.examBusiness.ExamIntestinalService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;


/**
*
*/
@Service
public class ExamIntestinalServiceImpl extends BaseServiceImpl<ExamIntestinal> implements ExamIntestinalService {
    @Autowired
    private ExamIntestinalDao intestinalDao;
    @Autowired
    private ExamNumberDao examNumberDao;
    @Autowired
    private ExamMemberExamDao memberExamDao;
    @Autowired
    private ExamBlodIntestinalDao blodIntestinalDao;
    @Autowired
    private ExamProjectDao projectDao;
    @Autowired
    private ExamPackageProjectDao packageProjectDao;
    @Override
    protected Dao getDao() {
        return intestinalDao;
    }


    /**
     * 生成肠检号 肠检检验号规则：自然年度中从1-5000循环使用，满5000就自动恢复从1开始，跨年度后清零，再进行1-5000的循环
     * @param examNumber
     * @return
     */
    @Override
    public Long generateIntestinalTestNumber(String examNumber) {
        Long num=null;
        // 查询疾控中心最新肠检号
        String code = String.valueOf(SystemContent.JIKONGZHONGXIN);
        List<ExamIntestinal> intestinalList = intestinalDao.findIntestinalByCode(code);
        if(intestinalList!=null&&intestinalList.size()>0){
            Calendar calendar = Calendar.getInstance();
            int whereMonth=calendar.get(Calendar.MONTH+1);
            int whereDay=calendar.get(Calendar.DAY_OF_MONTH);
            if(2==(whereMonth+whereDay)){
                num=0L;
            }else {
                num = intestinalList.get(0).getTestNumber();
                if (num == 5000L) {
                    num = 0L;
                }
            }
        }else{
            num=0L;
        }
        return num;
    }

    /**
     * 保存肠检号信息
     * @param examNumber
     */
    @Override
    public Long saveIntestinalTS(String examNumber) {
        Long testNumber = generateIntestinalTestNumber(examNumber);
        testNumber++;
        saveIntestinalT(examNumber,testNumber);
//        }else{
//            ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
//            blodIntestinal.setExamIntestinalId(intestinal.getId());
//            blodIntestinal.setMemberExamId(intestinal.getMemberExamId());
//            blodIntestinal.setExamNumber(String.valueOf(intestinal.getExamNumber()));
//            blodIntestinal.setProjectId(6L);
//            ExamProject project = projectDao.get(6L);
//            blodIntestinal.setProjectName(project.getProjectName());
//            blodIntestinal.setIsQualified(0);
//            blodIntestinal.setIsRecheck(0);
//            blodIntestinal.setEntryState(0);
//            blodIntestinal.setIsNew(1);
//            blodIntestinal.setIsShowRefresh(0);
//            blodIntestinal.setRecheckTag(0);
//            blodIntestinal.setType(SystemContent.PROJECT_TYPE_CHANGDAO);
//            blodIntestinalDao.save(blodIntestinal);
//        }

//        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
//        blodIntestinal.setExamIntestinalId(intestinal.getId());
//        blodIntestinal.setMemberExamId(intestinal.getMemberExamId());
//        blodIntestinal.setExamNumber(String.valueOf(intestinal.getExamNumber()));
//        blodIntestinal.setProjectId(6L);
//        ExamProject project = projectDao.get(6L);
//        blodIntestinal.setProjectName(project.getProjectName());
//        blodIntestinal.setIsQualified(0);
//        blodIntestinal.setIsRecheck(0);
//        blodIntestinal.setEntryState(0);
//        blodIntestinal.setIsNew(1);
//        blodIntestinal.setIsShowRefresh(0);
//        blodIntestinal.setRecheckTag(0);
//        blodIntestinal.setType(SystemContent.PROJECT_TYPE_CHANGDAO);
//        blodIntestinalDao.save(blodIntestinal);
        return testNumber;
    }

    @Override
    public void saveIntestinalT(String examNumber, Long testNumber) {
        ExamIntestinal intestinal = new ExamIntestinal();
        intestinal.setExamNumber(examNumber);
        intestinal.setTestNumber(testNumber);
        intestinal.setCode(examNumber.substring(2,5));
        intestinal.setCreateTime(new Date().getTime());
        ExamNumber number = new ExamNumber();
        number.setNumber(examNumber);
        List<ExamNumber> numbers = examNumberDao.findByExample(number, MatchMode.EXACT);
        if(numbers!=null&&numbers.size()>0){
            intestinal.setExamNumberId(numbers.get(0).getId());
        }
        ExamMemberExam memberExam = new ExamMemberExam();
        memberExam.setExamNumber(examNumber);
        memberExam.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
        List<ExamMemberExam> memberExams = memberExamDao.findByExample(memberExam,MatchMode.EXACT);
        if(memberExams!=null&&memberExams.size()>0){
            intestinal.setMemberExamId(memberExams.get(0).getId());
        }

        intestinalDao.save(intestinal);

//        if(memberExams!=null&&memberExams.size()>0&&memberExams.get(0).getRecheckTag()>0){
        ExamBlodIntestinal reviewResult =  new ExamBlodIntestinal();
        reviewResult.setIsNew(1);
        reviewResult.setMemberExamId(memberExams.get(0).getId());
        reviewResult.setRecheckTag(memberExams.get(0).getRecheckTag());
        reviewResult.setProjectId(SystemContent.PROJECT_CHANGDAO);
        List<ExamBlodIntestinal> reviewResults = blodIntestinalDao.findByExample(reviewResult);
        if(reviewResults!=null&&reviewResults.size()>0){
            for(ExamBlodIntestinal result:reviewResults){
                result.setIsQualified(3);
                result.setExamIntestinalId(intestinal.getId());
                blodIntestinalDao.update(result);
            }
        }
    }

    /**
     * 采样记录查询-肠检结果集
     * @param page
     * @param startTime
     * @param endTime
     * @param areaId
     * @throws ParseException
     */
    @Override
    public void queryIntestinalReocrdQueryList(Page page, String startTime, String endTime, String areaId) throws ParseException {
        List<ExamIntestinal> examIntestinalList = intestinalDao.queryIntestinalReocrdQueryList(startTime, endTime, areaId,page.getPageCurrent(), page.getPageSize());
        Long count  = intestinalDao.queryCountIntestinalReocrdQuery(startTime, endTime, areaId);

        Iterator<ExamIntestinal> eiIterator = examIntestinalList.iterator();

        while(eiIterator.hasNext()){

            ExamIntestinal examIntestinal = eiIterator.next();

            List<ExamBlodIntestinal> blodIns =  examIntestinal.getIntestinalBlods();

            if(blodIns.size()==0){
                eiIterator.remove();
                count = count-1;
            }
        }

        page.setList(examIntestinalList);
        page.setTotal(count);
    }

    @Override
    public List<ExamIntestinal> queryPrintIntestinalReocrdList(int pageCurrent, int pageSize, String startTime, String endTime, String areaId) throws ParseException {

       return intestinalDao.queryIntestinalReocrdQueryList(startTime, endTime, areaId,pageCurrent, pageSize);
    }

    @Override
    public void findIntestinalMember(Page<ExamIntestinal> page) {

    }

    @Override
    public void findIntestinalMember(Page<ExamIntestinal> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber) throws ParseException {
        List<ExamIntestinal> examIntestinalList = intestinalDao.findIntestinalMember(page,beginTime,endTime,entryState,areaId,packageId,examNumber,page.getPageCurrent(),page.getPageSize());
        Long count  = intestinalDao.countIntestinalMember(page,beginTime,endTime,entryState,areaId,packageId,examNumber);
        page.setList(examIntestinalList);
        page.setTotal(count);
    }

    /**
     * 扫描条形码操作
     * @param examNumber
     * @param map
     */
    @Override
    public void checkTownsIntestinalT(String examNumber, Map<String, Object> map) {
        ExamMemberExam examMemberExamNoJudge = new ExamMemberExam();
        examMemberExamNoJudge.setExamNumber(examNumber);
        examMemberExamNoJudge.setIsNew(1);
        //未作废
        examMemberExamNoJudge.setIsCancel(0);
        examMemberExamNoJudge.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
        List<ExamMemberExam> memberExamNoJudgeList = memberExamDao.findByExample(examMemberExamNoJudge);
        if(memberExamNoJudgeList!=null&&memberExamNoJudgeList.size()>0){
            if(memberExamNoJudgeList.get(0).getRecheckTag()>0){
                ExamBlodIntestinal recheckResult = new ExamBlodIntestinal();
                recheckResult.setMemberExamId(memberExamNoJudgeList.get(0).getId());
                //未作废
                recheckResult.setIsCancel(0);
                recheckResult.setProjectId(SystemContent.PROJECT_CHANGDAO);
                List<ExamBlodIntestinal> recheckResults = blodIntestinalDao.findByExample(recheckResult);
                boolean f = false;
                if(recheckResults!=null&&recheckResults.size()>0){
                    if(recheckResults.get(0).getExamIntestinalId()!=null){
                        map.put("errorMsg","该肠检项目已检测，无须再检验！");
                        return;
                    }
                }else{
                    map.put("errorMsg","该复检没有肠检项目！");
                    return;
                }
            }else{
                ExamPackageProject pp = new ExamPackageProject();
                pp.setPackageId(memberExamNoJudgeList.get(0).getPackageId());
                pp.setProjectId(SystemContent.PROJECT_CHANGDAO);
                List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);
                boolean flag = false;
                if(packageProjectList!=null&&packageProjectList.size()>0){
                    ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                    examBlodIntestinal.setExamNumber(examNumber);
                    examBlodIntestinal.setIsNew(1);
                    examBlodIntestinal.setIsShowRefresh(0);
                    examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_CHANGDAO);
                    //未作废
                    examBlodIntestinal.setIsCancel(0);
                    List<ExamBlodIntestinal> examBlodIntestinalList = blodIntestinalDao.findByExample(examBlodIntestinal);
                    if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0&&examBlodIntestinalList.get(0).getExamIntestinalId()!=null){
                        map.put("errorMsg","该肠检已检测过，无须再检验！");
                        return;
                    }
                }else{
                    map.put("errorMsg","您没有要检测的肠检项目，无须检验！");
                    return;
                }
            }
        }else{
            map.put("errorMsg","当前没有需要体检的项目！");
            return;
        }
    }

    /**
     * 保存录入的肠检采样号信息
     */
    @Override
    public void saveTownsIntestinalTestNumberT(String examNumber, String sampleNumber) {
        ExamIntestinal intestinal = new ExamIntestinal();
        intestinal.setExamNumber(examNumber);
        intestinal.setTestNumber(Long.valueOf(sampleNumber));
        intestinal.setCreateTime(new Date().getTime());
        intestinal.setCode(examNumber.substring(2,5));
        ExamNumber number = new ExamNumber();
        number.setNumber(examNumber);
        List<ExamNumber> numbers = examNumberDao.findByExample(number, MatchMode.EXACT);
        if(numbers!=null&&numbers.size()>0){
            intestinal.setExamNumberId(numbers.get(0).getId());
        }
        ExamMemberExam memberExam = new ExamMemberExam();
        memberExam.setExamNumber(examNumber);
        memberExam.setIsNew(1);
        memberExam.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
        List<ExamMemberExam> memberExams = memberExamDao.findByExample(memberExam,MatchMode.EXACT);
        if(memberExams!=null&&memberExams.size()>0){
            intestinal.setMemberExamId(memberExams.get(0).getId());
        }

        intestinalDao.save(intestinal);

        ExamBlodIntestinal blodIntestinalResult =  new ExamBlodIntestinal();
        blodIntestinalResult.setIsNew(1);
        blodIntestinalResult.setMemberExamId(memberExams.get(0).getId());
        blodIntestinalResult.setRecheckTag(memberExams.get(0).getRecheckTag());
        blodIntestinalResult.setProjectId(SystemContent.PROJECT_CHANGDAO);
        List<ExamBlodIntestinal> blodIntestinalResults = blodIntestinalDao.findByExample(blodIntestinalResult);
        if(blodIntestinalResults!=null&&blodIntestinalResults.size()>0){
            for(ExamBlodIntestinal result:blodIntestinalResults){
                result.setExamIntestinalId(intestinal.getId());
                result.setIsQualified(3);
                blodIntestinalDao.update(result);
            }
        }
    }

    @Override
    public HSSFWorkbook downIntestinalExcel(String beginTime, String endTime, String areaId) throws ParseException {
        List<ExamIntestinal> list = intestinalDao.queryIntestinalReocrdExcelQueryList(beginTime, endTime, areaId);

        Iterator<ExamIntestinal> eiIterator = list.iterator();

        while(eiIterator.hasNext()){

            ExamIntestinal examIntestinal = eiIterator.next();

            List<ExamBlodIntestinal> blodIns =  examIntestinal.getIntestinalBlods();

            if(blodIns.size()==0){
                eiIterator.remove();

            }
        }


        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("肠检记录");
        Row row = sheet.createRow(0);

        // 第一行标题背景色、文字颜色
        HSSFCellStyle headstyle = wb.createCellStyle();
        headstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headstyle.setFillForegroundColor(HSSFColor.BLACK.index);
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.WHITE.index);
        headstyle.setFont(font);
        headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

        int cindex = 0;
        Cell cell = row.createCell(cindex++);
        cell.setCellValue("体检号");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("检验号");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("体检结果");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("体检日期");
        cell.setCellStyle(headstyle);



        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamIntestinal examIntestinal = list.get(i);
                Row rowx = sheet.createRow(rowno);


                //体检号
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examIntestinal.getExamNumber() != null) {
                    if (StringUtils.isNotBlank(examIntestinal.getExamNumber())) {
                        cell0.setCellValue(examIntestinal.getExamNumber());
                    } else {
                        cell0.setCellValue("");
                    }
                }

                //检验号
                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examIntestinal.getTestNumber() != null) {
                    if (StringUtils.isNotBlank(examIntestinal.getTestNumber().toString())) {
                        cell1.setCellValue(examIntestinal.getTestNumber());
                    } else {
                        cell1.setCellValue("");
                    }
                }

                //体检结果
                Cell cell2 = rowx.createCell(2);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);

                ExamBlodIntestinal examBlodIntestinal = examIntestinal.getIntestinalBlods().get(0);
                if (examBlodIntestinal != null) {
                    if (StringUtils.isNotBlank(examBlodIntestinal.getExamResult())) {
                        cell2.setCellValue(examBlodIntestinal.getExamResult());
                    } else {
                        cell2.setCellValue("");
                    }
                }

                //体检日期
                Cell cell3 = rowx.createCell(3);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);

                if (examIntestinal.getCreateTimeStr() != null) {
                    if (StringUtils.isNotBlank(examIntestinal.getCreateTimeStr())) {
                        cell3.setCellValue(examIntestinal.getCreateTimeStr());
                    } else {
                        cell3.setCellValue("");
                    }
                }


                rowno++;
                rowx = null;
            }
        }
        return wb;
    }
}
