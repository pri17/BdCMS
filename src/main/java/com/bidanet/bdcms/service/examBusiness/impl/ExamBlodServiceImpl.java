package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.dao.examBusiness.*;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.examBusiness.ExamBlodService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
*
*/
@Service
public class ExamBlodServiceImpl extends BaseServiceImpl<ExamBlod> implements ExamBlodService {
    @Autowired
    private ExamBlodDao blodDao;
    @Autowired
    private ExamNumberDao examNumberDao;
    @Autowired
    private ExamMemberExamDao memberExamDao;
    @Autowired
    private ExamBlodIntestinalDao blodIntestinalDao;
    @Autowired
    private ExamProjectDao projectDao;
    @Autowired
    private ExamMemberDao memberDao;
    @Autowired
    private  ExamAffectedDao affectedDao;
    @Autowired
    private ExamPackageProjectDao packageProjectDao;
    @Override
    protected Dao getDao() {
        return blodDao;
    }


    /**
     * 生成血检号
     * @param todayStart
     * @param todayEnd
     * @param examNumber
     * @return
     */
    @Override
    public Long findBigNumberToday(Long todayStart, Long todayEnd, String examNumber) {
        List<ExamBlod> examBlodList = blodDao.findBigNumberToday(todayStart,todayEnd);
        ExamBlod examBlod = new ExamBlod();
        examBlod.setExamNumber(examNumber);

        if(examBlodList!=null&&examBlodList.size()>0){
            return examBlodList.get(0).getTestNumber();
        }else{
            return 0L;
        }

    }

    /**
     * 保存血检号，创建时间等
     * @param examNumber
     */
    @Override
    public Long saveExamBlodTS(String examNumber) {

        Long testNumber = null;
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY,0);
        calendarStart.set(Calendar.MINUTE,0);
        calendarStart.set(Calendar.SECOND,0);
        Long todayStart = calendarStart.getTime().getTime();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY,23);
        calendarEnd.set(Calendar.MINUTE,59);
        calendarEnd.set(Calendar.SECOND,59);
        Long todayEnd = calendarEnd.getTime().getTime();
        testNumber = findBigNumberToday(todayStart,todayEnd,examNumber);
        testNumber = testNumber+1;



        ExamBlod blod = new ExamBlod();
        blod.setExamNumber(examNumber);
        blod.setTestNumber(testNumber);
        blod.setCreateTime(new Date().getTime());
//        blod.setAffectedId(2L);
        ExamNumber number = new ExamNumber();
        number.setNumber(examNumber);
        List<ExamNumber> numbers = examNumberDao.findByExample(number, MatchMode.EXACT);
        if(numbers!=null&&numbers.size()>0){
            blod.setExamNumberId(numbers.get(0).getId());
        }
        ExamMemberExam memberExam = new ExamMemberExam();
        memberExam.setExamNumber(examNumber);
        memberExam.setIsNew(1);
        memberExam.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
        List<ExamMemberExam> memberExams = memberExamDao.findByExample(memberExam,MatchMode.EXACT);
        if(memberExams!=null&&memberExams.size()>0){
            blod.setMemberExamId(memberExams.get(0).getId());
            ExamMember member = memberDao.get(memberExams.get(0).getMemberId());
            // 是否在疫区内
            List<ExamAffected> examAffecteds = affectedDao.findAll();
            if(examAffecteds!=null&&examAffecteds.size()>0){
                for(ExamAffected ea:examAffecteds){
                    if(member.getIdCardNum().substring(0,6).equals(ea.getAffectedCode())){
                         blod.setAffectedId(ea.getId());
                    }else{
                         blod.setAffectedId(null);
                    }
                }
            }
        }

        blodDao.save(blod);

//        if(memberExams!=null&&memberExams.size()>0&&memberExams.get(0).getRecheckTag()>0){
            ExamBlodIntestinal reviewResult =  new ExamBlodIntestinal();
            reviewResult.setIsNew(1);
            reviewResult.setMemberExamId(memberExams.get(0).getId());
            reviewResult.setRecheckTag(memberExams.get(0).getRecheckTag());
            List<ExamBlodIntestinal> reviewResults = blodIntestinalDao.findByExample(reviewResult);
            if(reviewResults!=null&&reviewResults.size()>0){
                for(ExamBlodIntestinal result:reviewResults){
                    if(result.getProjectId()==SystemContent.PROJECT_HEV){
                       result.setIsQualified(3);
                       result.setExamBlodId(blod.getId());
                        blodIntestinalDao.update(result);
                    }else if(result.getProjectId()==SystemContent.PROJECT_ALT){
                        result.setIsQualified(3);
                        result.setExamBlodId(blod.getId());
                        blodIntestinalDao.update(result);
                    }else if(result.getProjectId()==SystemContent.PROJECT_HAV){
                        result.setIsQualified(3);
                        result.setExamBlodId(blod.getId());
                        blodIntestinalDao.update(result);
                    }

                }
            }

//        }else{
//            ExamPackageProject pp = new ExamPackageProject();
//            pp.setPackageId(memberExams.get(0).getPackageId());
//            List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);
//            if(packageProjectList!=null&&packageProjectList.size()>0){
//                for(ExamPackageProject p:packageProjectList){
//                    if(p.getProjectId()==SystemContent.PROJECT_HEV){
//                        saveBlodIntestinal(blod,SystemContent.PROJECT_HEV,SystemContent.PROJECT_TYPE_HEV);
//                    }
//                    if(p.getProjectId()==SystemContent.PROJECT_ALT){
//                        saveBlodIntestinal(blod,SystemContent.PROJECT_ALT,SystemContent.PROJECT_TYPE_ALT);
//                    }
//                    if(p.getProjectId()==SystemContent.PROJECT_HAV){
//                        saveBlodIntestinal(blod,SystemContent.PROJECT_HAV,SystemContent.PROJECT_TYPE_HAV);
//                    }
//                }
//            }
//        }



//        ExamPackageProject pp = new ExamPackageProject();
//        pp.setPackageId(memberExams.get(0).getPackageId());
//        List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);
//        if(packageProjectList!=null&&packageProjectList.size()>0){
//            for(ExamPackageProject p:packageProjectList){
//                if(p.getProjectId()==SystemContent.PROJECT_HEV){
//                    saveBlodIntestinal(blod,SystemContent.PROJECT_HEV,SystemContent.PROJECT_TYPE_HEV);
//                }
//                if(p.getProjectId()==SystemContent.PROJECT_ALT){
//                    saveBlodIntestinal(blod,SystemContent.PROJECT_ALT,SystemContent.PROJECT_TYPE_ALT);
//                }
//                if(p.getProjectId()==SystemContent.PROJECT_HAV){
//                    saveBlodIntestinal(blod,SystemContent.PROJECT_HAV,SystemContent.PROJECT_TYPE_HAV);
//                }
//            }
//        }

        return testNumber;
    }

    /**
     * 保存血检信息，体检信息到血检结果录入表
     * @param blod
     * @param projectId
     */
    private void saveBlodIntestinal(ExamBlod blod,Long projectId,Integer type) {
        ExamBlodIntestinal blodIntestinal = new ExamBlodIntestinal();
        blodIntestinal.setExamBlodId(blod.getId());
        blodIntestinal.setMemberExamId(blod.getMemberExamId());
        blodIntestinal.setExamNumber(String.valueOf(blod.getExamNumber()));
        blodIntestinal.setProjectId(projectId);
        ExamProject project = projectDao.get(projectId);
        blodIntestinal.setProjectName(project.getProjectName());
        blodIntestinal.setIsQualified(SystemContent.PROJECT_ISQUALIFIED_WEIPANDUAN);
        blodIntestinal.setIsRecheck(SystemContent.PROJECT_ISRECHECK_NO);
        blodIntestinal.setEntryState(0);
        blodIntestinal.setIsNew(1);
        blodIntestinal.setRecheckTag(0);
        blodIntestinal.setIsShowRefresh(0);
        blodIntestinal.setType(type);
        blodIntestinal.setIsQualified(3);
        blodIntestinalDao.save(blodIntestinal);
    }

    @Override
    public void findBlodMember(Page<ExamBlod> page, String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber) throws ParseException {
        List<ExamBlod> examBlodList = blodDao.findBlodMember(page,beginTime,endTime,entryState,areaId,packageId,examNumber,page.getPageCurrent(),page.getPageSize());
        Long count  = blodDao.countBlodMember(page,beginTime,endTime,entryState,areaId,packageId,examNumber);
        page.setList(examBlodList);
        page.setTotal(count);
    }

    /**
     * 采样结果查询-血检采样集
     * @param page
     * @param startTime
     * @param endTime
     * @param areaId
     * @throws ParseException
     */
    @Override
    public void queryBlodReocrdQueryList(Page page ,String startTime, String endTime, String areaId) throws ParseException {
        List<ExamBlod> examBlodList = blodDao.queryBlodReocrdQueryList(startTime, endTime, areaId,page.getPageCurrent(), page.getPageSize());
        Long count  = blodDao.queryCountBlodReocrdQuery(startTime, endTime, areaId);

        Iterator<ExamBlod> eiIterator = examBlodList.iterator();

        while(eiIterator.hasNext()){

            ExamBlod examBlod = eiIterator.next();

            List<ExamBlodIntestinal> blodIns =  examBlod.getBlodIntestinals();

            if(blodIns.size()==0){
                eiIterator.remove();
                count = count-1;
            }
        }


        page.setList(examBlodList);
        page.setTotal(count);
    }

    @Override
    public List<ExamBlod> queryPrintBlodReocrdList(int pageCurrent, int pageSize, String startTime, String endTime, String areaId) throws ParseException {

        return  blodDao.queryBlodReocrdQueryList(startTime, endTime, areaId,pageCurrent, pageSize);
    }

    @Override
    public HSSFWorkbook downBlodExcel(String beginTime, String endTime, String areaId) throws ParseException {
        List<ExamBlod> list = blodDao.queryBlodReocrdExcelQueryList(beginTime, endTime, areaId);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("血检记录");
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
        cell.setCellValue("检验日期");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("体检号");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("检验号");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("HAV-IgM(甲)");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("HEV-IgM(戊)");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("ALT(谷)");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("疫区");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("复检项目");
        cell.setCellStyle(headstyle);



        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamBlod examBlod = list.get(i);
                Row rowx = sheet.createRow(rowno);

                //校验日期
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examBlod.getCreateTimeStr() != null) {
                    if (StringUtils.isNotBlank(examBlod.getCreateTimeStr())) {
                        cell0.setCellValue(examBlod.getCreateTimeStr());
                    } else {
                        cell0.setCellValue("");
                    }
                }


                //体检号
                Cell cell1 = rowx.createCell(1);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examBlod.getExamNumber() != null) {
                    if (StringUtils.isNotBlank(examBlod.getExamNumber())) {
                        cell1.setCellValue(examBlod.getExamNumber());
                    } else {
                        cell1.setCellValue("");
                    }
                }

                //校验号
                Cell cell2 = rowx.createCell(2);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examBlod.getTestNumber() != null) {
                    if (StringUtils.isNotBlank(examBlod.getTestNumber().toString())) {
                        cell2.setCellValue(examBlod.getTestNumber());
                    } else {
                        cell2.setCellValue("");
                    }
                }




                //这段代码抄前台的。别问我为什么这么写。



                //HAV-IgM(甲)
                Cell cell3 = rowx.createCell(3);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examBlod.getBlodIntestinals() != null) {
                    if (examBlod.getBlodIntestinals().size() > 0) {
                        cell3.setCellValue(examBlod.getBlodIntestinals().get(0).getExamResult());
                    } else {
                        cell3.setCellValue("");
                    }
                }

                //HEV-IgM(戊)

                Cell cell4 = rowx.createCell(4);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examBlod.getBlodIntestinals() != null) {
                    if (examBlod.getBlodIntestinals().size() > 2) {
                        cell4.setCellValue(examBlod.getBlodIntestinals().get(2).getExamResult());
                    } else {
                        cell4.setCellValue("");
                    }
                }

                //ALT(谷)
                Cell cell5 = rowx.createCell(5);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examBlod.getBlodIntestinals() != null) {
                    if (examBlod.getBlodIntestinals().size() > 1) {
                        cell5.setCellValue(examBlod.getBlodIntestinals().get(1).getExamResult());
                    } else {
                        cell5.setCellValue("");
                    }
                }


                rowno++;
                rowx = null;
            }
        }
        return wb;
    }


}
