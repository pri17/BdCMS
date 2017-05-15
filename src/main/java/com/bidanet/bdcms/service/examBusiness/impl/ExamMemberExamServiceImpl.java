package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.common.ConfigInfo;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamCategoryDao;
import com.bidanet.bdcms.dao.businessSetting.ExamPackageProjectDao;
import com.bidanet.bdcms.dao.examBusiness.*;
import com.bidanet.bdcms.dao.examManage.ExamEcardDao;
import com.bidanet.bdcms.dao.fee.ExamPayDao;
import com.bidanet.bdcms.dao.systemSetting.ExamAgenciesDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.dao.wap.ExamWxBindDao;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import com.bidanet.bdcms.plugin.wechat.service.SendMessageService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.Page;
import com.thoughtworks.xstream.mapper.Mapper;
import me.chanjar.weixin.common.exception.WxErrorException;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.bidanet.bdcms.controller.examBusiness.ExamMedicalController;
/**
 * cf
 */
@Service
public class ExamMemberExamServiceImpl extends BaseServiceImpl<ExamMemberExam> implements ExamMemberExamService {

    @Autowired
    ConfigInfo configInfo;


    private Logger logger = (Logger.getLogger(getClass()));
    @Autowired
    private ExamMemberExamDao examMemberExamDao;

    @Override
    protected Dao getDao() {
        return examMemberExamDao;
    }

    @Autowired
    private ExamMemberDao examMemberDao;
    @Autowired
    private ExamAffectedDao examAffectedDao;

    @Autowired
    private ExamProjectDao examProjectDao;

    @Autowired
    private ExamEcardDao examEcardDao;

    @Autowired
    private ExamPayDao examPayDao;

    @Autowired
    private ExamBlodIntestinalDao examBlodIntestinalDao;

    @Autowired
    private ExamAgenciesDao examAgenciesDao;

    @Autowired
    @Qualifier("localFileDrive")
    private LocalFileDrive localFileDrive;

    @Autowired
    private ExamPackageProjectDao packageProjectDao;

    @Autowired
    private ExamNumberDao examNumberDao;

    @Autowired
    private ExamFlowNumberDao examFlowNumberDao;

    @Autowired
    private ExamBlodDao examBlodDao;

    @Autowired
    private ExamIntestinalDao examIntestinalDao;

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private ExamWxBindDao examWxBindDao;

    @Autowired
    private ExamSynchronizeDao examSynchronizeDao;

    @Autowired
    private ExamAreaCodeDao examAreaCodeDao;

    @Autowired
    private ExamCategoryDao categoryDao;

    @Autowired
    private ExamPayService examPayService;
    /**
     * 查找exam_blod_Inte中的每项体检数据
     */
    @Override
    public List<ExamBlodIntestinal> findEveryBlodIn(long id){
        List<ExamBlodIntestinal> list1 = examBlodIntestinalDao.findByMemberId(id);
        return list1;
    }


    /**
     * 查询疫区范围内体检人员
     *
     * @param page
     * @param beginTime
     * @param endTime
     * @param areaId
     */
    @Override
    public void findAffectedExamMember(Page<ExamMemberExam> page, String beginTime, String endTime, String areaId) throws ParseException {
        List<ExamAffected> examAffecteds = examAffectedDao.findAll();
        List<ExamMember> examMembers = examMemberDao.findAll();
        List<ExamMember> examMemberList = new ArrayList<ExamMember>();
        List<String> memberIds = new ArrayList<String>();
        if (examAffecteds != null && examAffecteds.size() > 0) {
            for (ExamAffected ea : examAffecteds) {
                if (examMembers != null && examMembers.size() > 0) {
                    for (ExamMember em : examMembers) {
                        if (em.getIdCardNum().substring(0, 6).equals(ea.getAffectedCode())) {
                            examMemberList.add(em);
                            memberIds.add(String.valueOf(em.getId()));
                        }
                    }

                }
            }
        }

        String memIds = listToString(memberIds);

        List<ExamMemberExam> examMemberExams = examMemberExamDao.findAffectedExamMember(memIds, beginTime, endTime, areaId, page.getPageCurrent(), page.getPageSize());

        for (ExamMemberExam examMemberExam : examMemberExams) {

            //获取该用户的疫区code对应的具体地址
            String affectedCode = examMemberExam.getIdCardNum().substring(0, 6);

            ExamAreaCode examAreaCode = examAreaCodeDao.get(Long.parseLong(affectedCode));

            ExamAreaCode examAreaParentTwo = examAreaCodeDao.get(examAreaCode.getPid());

            ExamAreaCode examAreaParentOne = examAreaCodeDao.get(examAreaParentTwo.getPid());

            String affectedName = examAreaParentOne.getName() + examAreaParentTwo.getName() + examAreaCode.getName();

            examMemberExam.setAffectedCodeName(affectedName);

            //获取该用户的血检流水号
            List<ExamBlod> examBlodList = new ArrayList<>();
            ExamBlod examBlod = new ExamBlod();
            examBlod.setMemberExamId(examMemberExam.getId());

            examBlodList = examBlodDao.findByExample(examBlod);

            if (examBlodList.size() > 0) {

                examBlod = examBlodList.get(0);
                examMemberExam.setBlodTestNumber(examBlod.getTestNumber());

            }

        }

        Long count = examMemberExamDao.countAffectedExamMember(memIds, beginTime, endTime, areaId);
        page.setList(examMemberExams);
        page.setTotal(count);

    }


    /**
     * 取拼接后的id字符串
     *
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {
        if (list == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : list) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    @Override
    public void getMemberExamList(ExamMemberExam examMemberExam, Page<ExamMemberExam> page, String areaId, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId) {
//        List<ExamMemberExam> list = examMemberExamDao.findByExample(examMemberExam, page.getPageCurrent(), page.getPageSize());
        List<ExamMemberExam> list = examMemberExamDao.getExamMemberExamList(examMemberExam, page.getPageCurrent(), page.getPageSize(), areaId, memberName, idCardNum, beginTime, endTime, categoryId);
        long count = examMemberExamDao.getCountExamMemberExamList(examMemberExam, areaId, memberName, idCardNum, beginTime, endTime, categoryId);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void addExamMemberExamT(ExamMemberExam examMemberExam) {
        examMemberExamDao.save(examMemberExam);
    }

    /**
     * 查询统计-体检结果查询
     *
     * @param page
     * @param areaId
     * @param name
     * @param idCard
     * @param examNumber
     * @param verifyConclusion
     * @param startTime
     * @param endTime
     * @param payState
     * @throws ParseException
     */
    @Override
    public void getResultQueryList(Page<ExamMemberExam> page, String areaId, String name, String idCard, String examNumber, String verifyConclusion, String startTime, String endTime, String payState) throws ParseException {

        List<ExamMemberExam> list = examMemberExamDao.queryResultQueryList(areaId, name, idCard, examNumber, verifyConclusion, startTime, endTime, page.getPageCurrent(), page.getPageSize(), payState);
        long count = examMemberExamDao.queryCountResultQuery(areaId, name, idCard, examNumber, verifyConclusion, startTime, endTime, payState);
        page.setList(list);
        page.setTotal(count);

    }

    /**
     * 体检结果判断页面信息查询
     *
     * @param page
     * @param memberName
     * @param idNum
     * @param startTime
     * @param endTime
     * @param payState
     * @param areaId
     * @param isRecheck
     * @param verifyConclusion
     * @param examNumber
     * @param agencies
     */
    @Override
    public void findExamMemberResultDecide(Page<ExamMemberExam> page, String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies) throws ParseException {
        List<ExamMemberExam> memberExamList = examMemberExamDao.findExamMemberResultDecide(memberName, idNum, startTime, endTime, payState, areaId, isRecheck, verifyConclusion, examNumber, agencies, page.getPageCurrent(), 10000);


        //Long count = examMemberExamDao.countExamMemberResultDecide(page, memberName, idNum, startTime, endTime, payState, areaId, isRecheck, verifyConclusion, examNumber, agencies);
        //System.out.println("count的值是"+ count);
        //判断是否只有肠检未判断
        if(verifyConclusion!=null)
        {
            List<ExamMemberExam> temp = removeOnly(memberExamList);
          //  List<ExamMemberExam> forCount = examMemberExamDao.findAllNo(memberName, idNum, startTime, endTime, payState, areaId, isRecheck, verifyConclusion, examNumber, agencies);
            //List<ExamMemberExam> temp1 = removeOnly(forCount);
           // Long g = new Long((long)temp1.size());
            //System.out.println("temp的大小是"+ temp.size());
            page.setList(temp);
            page.setTotal(new Long((long)temp.size()));

        }else {
            page.setList(memberExamList);
            page.setTotal(new Long((long)memberExamList.size()));
        }
    }
    /**
     * 判断是否只有肠检未判断
     */
    private boolean isOnlyIntestinal (List<ExamBlodIntestinal> list){
//        long id = list.getMemberId();
//        List<ExamBlodIntestinal> list1 = memberExamService.findEveryBlodIn(id);//6项
        ExamBlodIntestinal temp = new ExamBlodIntestinal();
        for(int i=0;i<list.size();i++){
            temp = list.get(i);
            if(temp.getProjectId()==6){
                continue;
            }else if(temp.getIsQualified()==0){
                return false;
            }

        }
        return true;
    }
    /**
     * 移除只有肠检未判断的
     */
    private List<ExamMemberExam> removeOnly(List <ExamMemberExam> resultlist){
        List<ExamMemberExam> temp = new ArrayList<ExamMemberExam>();
        ExamMemberExam a ;
      //  ExamBlodIntestinal b = new ExamBlodIntestinal();
        for(int i=0;i<resultlist.size();i++){
            a = resultlist.get(i);
            long member_id = a.getMemberId();
            //b.setMemberId(member_id);
            List<ExamBlodIntestinal> c = examBlodIntestinalDao.findByMemberId(member_id);

            if(!isOnlyIntestinal(c)){
                temp.add(a);
            }
        }
        return temp;

    }

    /**
     * 保存疫区人员血检额外信息
     *
     * @param ids
     * @param iha
     * @param ddia
     * @param copt
     * @param stool
     */
    @Override
    public void saveAffectedMemberT(String[] ids, String iha, String ddia, String copt, String stool) {
        for (int i = 0; i < ids.length; i++) {
            ExamMemberExam memberExam = examMemberExamDao.get(Long.valueOf(ids[i]));
            memberExam.setIHA(iha);
            memberExam.setDDIA(ddia);
            memberExam.setCOPT(copt);
            memberExam.setSTOOL(stool);
            examMemberExamDao.update(memberExam);
        }
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
     * @param openCategoryLevelTwo
     * @param examNumber
     * @param workUnit
     * @param verifyConclusion
     * @param verifyState          @return
     */
    @Override
    public HSSFWorkbook exportFindExcel(ExamMemberExam query, int pageCurrent, int pageSize, String areaId, String memberName, String idCardNum, String beginTime, String endTime, Long openCategoryLevelTwo, String examNumber, String workUnit, String verifyConclusion, String verifyState) {
        List<ExamMemberExam> list = examMemberExamDao.getExamMemberExamListExcel(query, pageCurrent, pageSize, areaId, memberName, idCardNum, beginTime, endTime, openCategoryLevelTwo, examNumber, workUnit, verifyConclusion, verifyState);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("结论判断审核数据清单");
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
        cell.setCellValue("姓名");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("性别");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("身份证号");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("地段区域");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("工作单位");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("行业分类");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检日期");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检结论");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("审核");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("审核医生");
        cell.setCellStyle(headstyle);

        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamMemberExam memberExam = list.get(i);
                Row rowx = sheet.createRow(rowno);
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell0.setCellValue(memberExam.getExamNumber());


                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getName() != "")
                    cell1.setCellValue(memberExam.getName());
                else
                    cell1.setCellValue("");

                Cell cell2 = rowx.createCell(2);
                cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getSex() == 1)
                    cell2.setCellValue("男");
                else
                    cell2.setCellValue("女");

                Cell cell3 = rowx.createCell(3);
                cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getIdCardNum() != "")
                    cell3.setCellValue(memberExam.getIdCardNum());
                else
                    cell3.setCellValue("");

                Cell cell4 = rowx.createCell(4);
                cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getAreaName() != "")
                    cell4.setCellValue(memberExam.getAreaName());
                else
                    cell4.setCellValue("");

                Cell cell5 = rowx.createCell(5);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getWorkUnit() != "")
                    cell5.setCellValue(memberExam.getWorkUnit());
                else
                    cell5.setCellValue("");

                Cell cell6 = rowx.createCell(6);
                cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if(memberExam.getExamCategory()!=null) {
                if (memberExam.getCategoryName() != "")
                    cell6.setCellValue(memberExam.getCategoryName());
                else
                    cell6.setCellValue("");
//                }else{
//                    cell6.setCellValue("");
//                }

                Cell cell7 = rowx.createCell(7);
                cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getExamTimeStr() != "")
                    cell7.setCellValue(memberExam.getExamTimeStr());
                else
                    cell7.setCellValue("");

                Cell cell8 = rowx.createCell(8);
                cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getVerifyConclusion() == 0) {
                    cell8.setCellValue("未判断");
                } else if (memberExam.getVerifyConclusion() == 1) {
                    cell8.setCellValue("合格");
                } else if (memberExam.getVerifyConclusion() == 2) {
                    cell8.setCellValue("不合格");
                }

                Cell cell9 = rowx.createCell(9);
                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getVerifyState() == 1) {
                    cell9.setCellValue("审核");
                } else if (memberExam.getVerifyState() == 2) {
                    cell9.setCellValue("未审核");
                }

                Cell cell10 = rowx.createCell(10);
                cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if(memberExam.getUser()!=null) {
                if (memberExam.getVerifyName() != "")
                    cell10.setCellValue(memberExam.getVerifyName());
                else
                    cell10.setCellValue("");
//                }else{
//                    cell10.setCellValue("");
//                }

                rowno++;
                rowx = null;
            }
        }
        return wb;
    }

    /**
     * 结果判断导出
     *
     * @param pageCurrent
     * @param pageSize
     * @param memberName
     * @param idNum
     * @param startTime
     * @param endTime
     * @param payState
     * @param areaId
     * @param isRecheck
     * @param verifyConclusion
     * @param examNumber
     * @param agencies         @return
     * @throws ParseException
     */
    @Override
    public HSSFWorkbook exportDecideExcel(int pageCurrent, int pageSize, String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, ExamAgencies agencies) throws ParseException {
//        List<ExamMemberExam> list = examMemberExamDao.findExamMemberResultDecide(memberName,idNum,startTime,endTime,payState,areaId,isRecheck,verifyConclusion,examNumber,agencies,pageCurrent,pageSize);
        List<ExamBlodIntestinal> list = examBlodIntestinalDao.findExcelResultDecide(memberName, idNum, startTime, endTime, payState, areaId,
                isRecheck, verifyConclusion, examNumber, agencies, pageCurrent, pageSize);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("结果判断数据清单");
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
        cell.setCellValue("姓名");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("身份证号");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("工作单位");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检日期");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检项目");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("结果判断");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检结果");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("判断人");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("判断日期");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("复检");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("收费");
        cell.setCellStyle(headstyle);

        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamBlodIntestinal result = list.get(i);
                Row rowx = sheet.createRow(rowno);
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getExamNumber())) {
                    cell0.setCellValue(result.getExamNumber());
                } else {
                    cell0.setCellValue("");
                }


                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if(memberExam.getExamMember()!=null){
//                    if(StringUtils.isNotBlank(memberExam.getName())){
//                        cell1.setCellValue(memberExam.getName());
                if (result.getMemberExam() != null) {
                    if (StringUtils.isNotBlank(result.getMemberExam().getName())) {
                        cell1.setCellValue(result.getMemberExam().getName());
                    } else {
                        cell1.setCellValue("");
                    }
//                }else{
//                    cell1.setCellValue("");
//                }
                }

                Cell cell2 = rowx.createCell(2);
                cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (result.getMemberExam() != null) {
                    if (StringUtils.isNotBlank(result.getMemberExam().getIdCardNum()))
                        cell2.setCellValue(result.getMemberExam().getIdCardNum());
                    else
                        cell2.setCellValue("");
                } else {
                    cell2.setCellValue("");
                }

                Cell cell3 = rowx.createCell(3);
                cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getMemberExam().getWorkUnit()))
                    cell3.setCellValue(result.getMemberExam().getWorkUnit());
                else
                    cell3.setCellValue("");

                Cell cell4 = rowx.createCell(4);
                cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getMemberExam().getExamTimeStr()))
                    cell4.setCellValue(result.getMemberExam().getExamTimeStr());
                else
                    cell4.setCellValue("");

                Cell cell5 = rowx.createCell(5);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getProjectName()))
                    cell5.setCellValue(result.getProjectName());
                else
                    cell5.setCellValue("");

                Cell cell6 = rowx.createCell(6);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getIsQualifiedStr()))
                    cell6.setCellValue(result.getIsQualifiedStr());
                else
                    cell6.setCellValue("");

                Cell cell7 = rowx.createCell(7);
                cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getExamResult()))
                    cell7.setCellValue(result.getExamResult());
                else
                    cell7.setCellValue("");

                Cell cell8 = rowx.createCell(8);
                cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getDoctorName()))
                    cell8.setCellValue(result.getDoctorName());
                else
                    cell8.setCellValue("");

                Cell cell9 = rowx.createCell(9);
                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getJudgeTimeStr()))
                    cell9.setCellValue(result.getJudgeTimeStr());
                else
                    cell9.setCellValue("");

                Cell cell10 = rowx.createCell(10);
                cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getRecheckTagStr()))
                    cell10.setCellValue(result.getRecheckTagStr());
                else
                    cell10.setCellValue("");

                Cell cell11 = rowx.createCell(11);
                cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(result.getMemberExam().getPayStateStr()))
                    cell11.setCellValue(result.getMemberExam().getPayStateStr());
                else
                    cell11.setCellValue("");

//                Cell cell8 = rowx.createCell(8);
//                cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if (memberExam.getVerifyConclusion() != 0) {
//                    cell8.setCellValue("未判断");
//                }
//                else if (memberExam.getVerifyConclusion() != 1) {
//                    cell8.setCellValue("合格");
//                }
//                else if (memberExam.getVerifyConclusion() != 2){
//                    cell8.setCellValue("不合格");
//                }
//
//                Cell cell9 = rowx.createCell(9);
//                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if (memberExam.getVerifyState() != 0) {
//                    cell9.setCellValue("审核");
//                }
//                else if (memberExam.getVerifyState() != 1) {
//                    cell9.setCellValue("未判断");
//                }
//
//                Cell cell10 = rowx.createCell(10);
//                cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if(memberExam.getUser()!=null) {
//                    if (memberExam.getUser().getRealName()) != "")
//                        cell10.setCellValue(memberExam.getUser().getRealName());
//                    else
//                        cell10.setCellValue("");
//                }else{
//                    cell10.setCellValue("");
//                }

                rowno++;
                rowx = null;
            }
        }
        return wb;
    }

    /**
     * 结果查询导出
     * @param pageCurrent
     * @param pageSize
     * @param payState
     * @param areaId
     * @param name
     * @param idCard
     * @param examNumber
     * @param verifyConclusion
     * @param startTime
     * @param endTime        @return
     * @throws ParseException
     */
    @Override
    public HSSFWorkbook exportResultQueryExcel ( int pageCurrent, int pageSize, String payState, String
            areaId, String name, String idCard, String examNumber, String verifyConclusion, String startTime, String endTime) throws
            ParseException
    {
//        List<ExamMemberExam> list = examMemberExamDao.queryResultQueryList(areaId,name,idCard,examNumber,verifyConclusion,startTime,endTime,pageCurrent, pageSize);
        List<ExamBlodIntestinal> list = examBlodIntestinalDao.findExcelResultQuery(name, idCard, startTime, endTime, payState, areaId,
                verifyConclusion, examNumber, pageCurrent, pageSize);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("结果查询数据清单");
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
        cell.setCellValue("姓名");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("身份证号");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("工作单位");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检日期");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检项目");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("结果判断");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检结果");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("判断人");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("判断日期");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("收费");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("复检");
        cell.setCellStyle(headstyle);

        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamBlodIntestinal biResult = list.get(i);
                Row rowx = sheet.createRow(rowno);
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if(memberExam.getExamMember()!=null){
//                    if(StringUtils.isNotBlank(memberExam.getName())){
//                        cell0.setCellValue(memberExam.getName());
//                    }else{
//                        cell0.setCellValue("");
//                    }
//                }else{
//                    cell0.setCellValue("");
//                }
                if (StringUtils.isNotBlank(biResult.getExamNumber())) {
                    cell0.setCellValue(biResult.getExamNumber());
                } else {
                    cell0.setCellValue("");
                }


                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (biResult.getMemberExam() != null) {
                    if (StringUtils.isNotBlank(biResult.getMemberExam().getName())) {
                        cell1.setCellValue(biResult.getMemberExam().getName());
                    } else {
                        cell1.setCellValue("");
                    }
                } else {
                    cell1.setCellValue("");
                }

                Cell cell2 = rowx.createCell(2);
                cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if(memberExam.getExamMember()!=null) {
//                    if (StringUtils.isNotBlank(memberExam.getIdCardNum()))
//                        cell2.setCellValue(memberExam.getIdCardNum());
                if (biResult.getMemberExam() != null) {
                    if (StringUtils.isNotBlank(biResult.getMemberExam().getIdCardNum()))
                        cell2.setCellValue(biResult.getMemberExam().getIdCardNum());
                    else
                        cell2.setCellValue("");
                }
//                }else{
//                    cell2.setCellValue("");
//                }

                Cell cell3 = rowx.createCell(3);
                cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getMemberExam().getWorkUnit()))
                    cell3.setCellValue(biResult.getMemberExam().getWorkUnit());
                else
                    cell3.setCellValue("");

                Cell cell4 = rowx.createCell(4);
                cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getMemberExam().getExamTimeStr()))
                    cell4.setCellValue(biResult.getMemberExam().getExamTimeStr());
                else
                    cell4.setCellValue("");

                Cell cell5 = rowx.createCell(5);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getProjectName()))
                    cell5.setCellValue(biResult.getProjectName());
                else
                    cell5.setCellValue("");

                Cell cell6 = rowx.createCell(6);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getIsQualifiedStr()))
                    cell6.setCellValue(biResult.getIsQualifiedStr());
                else
                    cell6.setCellValue("");

                Cell cell7 = rowx.createCell(7);
                cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getExamResult()))
                    cell7.setCellValue(biResult.getExamResult());
                else
                    cell7.setCellValue("");

                Cell cell8 = rowx.createCell(8);
                cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getDoctorName()))
                    cell8.setCellValue(biResult.getDoctorName());
                else
                    cell8.setCellValue("");

                Cell cell9 = rowx.createCell(9);
                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getJudgeTimeStr()))
                    cell9.setCellValue(biResult.getJudgeTimeStr());
                else
                    cell9.setCellValue("");

                Cell cell10 = rowx.createCell(10);
                cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getMemberExam().getPayStateStr()))
                    cell10.setCellValue(biResult.getMemberExam().getPayStateStr());
                else
                    cell10.setCellValue("");

                Cell cell11 = rowx.createCell(11);
                cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotBlank(biResult.getRecheckTagStr()))
                    cell11.setCellValue(biResult.getRecheckTagStr());
                else
                    cell11.setCellValue("");


                rowno++;
                rowx = null;
            }
        }
        return wb;
    }


    public List<ExamMemberExam> findAffectedExamMemberByIds (String ids){
        return examMemberExamDao.findAffectedExamMemberByIds(ids);
    }


    ///////////////////////////////////复检管理/////////////////////////////////////

    @Override
    public void findReviewMemberList (Page < ExamMemberExam > page, String areaId, String workUnit, String
            memberName, String idCardNum, String beginTime, String endTime, String categoryId, String isRechekPrint, String
                                              examNumber)  throws ParseException {

        List<ExamMemberExam> memberExamList = examMemberExamDao.findReviceMemberList(page.getPageCurrent(), page.getPageSize(), areaId, workUnit, memberName, idCardNum, beginTime, endTime, categoryId, isRechekPrint, examNumber);
        Long count = examMemberExamDao.findReviceMemberCount(areaId, workUnit, memberName, idCardNum, beginTime, endTime, categoryId, isRechekPrint, examNumber);

        page.setList(memberExamList);
        page.setTotal(count);


    }

    @Override
    public List<ExamMemberExam> findByExamNumber (String examNumber){

        List<ExamMemberExam> memberExamList = examMemberExamDao.findByExamNumber(examNumber);

        return memberExamList;
    }

    //复检管理导出数据查询
    @Override
    public HSSFWorkbook exportReviewExcel (ExamMemberExam query, String areaId, String workUnit, String
            memberName, String idCardNum, String beginTime, String endTime, String openCategoryLevelTwoRmId, String
                                                   isRechekPrint, String examNumber)   throws ParseException {
        List<ExamMemberExam> list = examMemberExamDao.getExportReviewList(query, areaId, workUnit, memberName, idCardNum, beginTime, endTime, openCategoryLevelTwoRmId, isRechekPrint, examNumber);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("复检单数据清单");
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
        cell.setCellValue("姓名");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("性别");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("身份证号");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("地段区域");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("工作单位");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("行业分类");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("体检日期");
        cell.setCellStyle(headstyle);
//        cell = row.createCell(cindex++);
//        cell.setCellValue("体检结果");
//        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("是否打印");
        cell.setCellStyle(headstyle);

        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamMemberExam examMemberExam = list.get(i);
                Row rowx = sheet.createRow(rowno);
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell0.setCellValue(examMemberExam.getExamNumber());


                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examMemberExam.getName() != "")
                    cell1.setCellValue(examMemberExam.getName());
                else
                    cell1.setCellValue("");

                Cell cell2 = rowx.createCell(2);
                cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examMemberExam.getSex() == 1)
                    cell2.setCellValue("男");
                else
                    cell2.setCellValue("女");

                Cell cell3 = rowx.createCell(3);
                cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examMemberExam.getIdCardNum() != "")
                    cell3.setCellValue(examMemberExam.getIdCardNum());
                else
                    cell3.setCellValue("");

                Cell cell4 = rowx.createCell(4);
                cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examMemberExam.getAreaName() != "")
                    cell4.setCellValue(examMemberExam.getAreaName());
                else
                    cell4.setCellValue("");

                Cell cell5 = rowx.createCell(5);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examMemberExam.getWorkUnit() != "")
                    cell5.setCellValue(examMemberExam.getWorkUnit());
                else
                    cell5.setCellValue("");

                Cell cell6 = rowx.createCell(6);
                cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if(examMemberExam.getExamCategory()!=null) {
                if (examMemberExam.getCategoryName() != "")
                    cell6.setCellValue(examMemberExam.getCategoryName());
                else
                    cell6.setCellValue("");
//                }else{
//                    cell6.setCellValue("");
//                }

                Cell cell7 = rowx.createCell(7);
                cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examMemberExam.getExamTimeStr() != "")
                    cell7.setCellValue(examMemberExam.getExamTimeStr());
                else
                    cell7.setCellValue("");


                Cell cell9 = rowx.createCell(8);
                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (examMemberExam.getIsRecheckPrint() == 0) {
                    cell9.setCellValue("未打印");
                } else if (examMemberExam.getIsRecheckPrint() == 1) {
                    cell9.setCellValue("打印");
                }

                rowno++;
                rowx = null;
            }
        }
        return wb;
    }

    @Override
    public List<ExamMemberExam> getExamReviews (String ids){
        return examMemberExamDao.queryExamReviewList(ids);
    }

    @Override
    public List<ExamMemberExam> getMemberExamNew (String examNumber){
        return examMemberExamDao.getMemberExamNew(examNumber);
    }

    /**
     * 自动审核任务-
     * @return
     */
    @Override
    public List<ExamMemberExam> getAutoResultDecideList () {

        return examMemberExamDao.getAutoResultDecideList();
    }

    /**
     * 根据数据进行判断 自动审核
     * @param examMemberExam
     * 判断人-exam_auto_doctor
     * 创建人-sys_user
     */
    @Override
    public void autoResultDecide (ExamMemberExam examMemberExam, ExamAutoDoctor examAutoDoctor){

        //拼接user数据
        User user = new User();
        user.setAgenciesId(examAutoDoctor.getAgenciesId());
        user.setRealName(examAutoDoctor.getName());
        user.setUid(2L);

        // 只有体检项目全部完成并且未判断的情况下，才可以进行审核判断
        if (examMemberExam.getEntryState() == 1 && examMemberExam.getVerifyConclusion() == 0) {
            // 先判断该条体检记录是否有不合格项目，同时修改verifyConclusion
            ExamMemberExam checkReturnMemberExam = checkExamMemberExamQualifiedT(examMemberExam.getId(), user);
            if (checkReturnMemberExam.getId() != 0) {
                // 判断好后，在对收费记录、复检记录进行数据添加 同时
                // 通过检查结果判断是否合格，如果全部合格，那么生成健康证，
                // 如果有任意一项不合格，生成复检通知单；
                ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                examBlodIntestinal.setExamNumber(checkReturnMemberExam.getExamNumber());
                List<ExamBlodIntestinal> examBlodIntestinals = examBlodIntestinalDao.findByExample(examBlodIntestinal);
                if (checkReturnMemberExam.getVerifyConclusion() == 1) {
                    //体检正常 生成健康证
                    //体检正常 生成健康证
                    ExamEcard examEcard = generateEcard(checkReturnMemberExam.getId(), user);

                    if (examEcard.getId() == null) {

                        errorMsg("健康证生成失败!");

                        return;
                    } else {
                        //生成健康证之后，将该用户数据插入平台同步表
                        ExamSynchronize examSynchronize = new ExamSynchronize();

                        examSynchronize.setIsSuccess(0);
                        examSynchronize.seteCardId(examEcard.getId());
                        examSynchronize.setExamMemberId(examEcard.getMemberExamId());
                        examSynchronize.setMemberId(examEcard.getMemberId());
                        examSynchronize.setCreateTime(new Date().getTime());

                        examSynchronize = examSynchronizeDao.saveBack(examSynchronize);

                        if (examSynchronize.getId() == null) {
                            logger.info("健康证ID" + examSynchronize.geteCardId() + "入库失败!");
                        }


                    }
                    // 修改exam_blod_intestinal表is_new 状态
                    if (examBlodIntestinals.size() > 0) {
                        for (int j = 0; j < examBlodIntestinals.size(); j++) {
                            ExamBlodIntestinal examBlod = examBlodIntestinals.get(j);
                            examBlod.setIsNew(0);
                            examBlodIntestinalDao.update(examBlod);
                        }
                    }

//                        examMemberExamDao.update(examMemberExam);
                } else if (checkReturnMemberExam.getVerifyConclusion() == 2) {//不合格

//                        examMemberExamDao.update(examMemberExam);
                    ExamMemberExam newRecheckExamMemberExam = new ExamMemberExam();
                    newRecheckExamMemberExam = generateNewExam(checkReturnMemberExam);
                    if (newRecheckExamMemberExam.getId() != null) {
                        checkReturnMemberExam = updateOldMemberExamIsNewT(checkReturnMemberExam);

                        // 修改exam_blod_intestinal表is_new 状态
                        if (examBlodIntestinals.size() > 0) {
                            for (int j = 0; j < examBlodIntestinals.size(); j++) {
                                ExamBlodIntestinal examBlod = examBlodIntestinals.get(j);
                                examBlod.setIsNew(0);
                                examBlodIntestinalDao.update(examBlod);
                            }
                        }
                    }
                    // 生成项目单，并计算价格存到收费单
                    BigDecimal totalPrice = reckeckTotalPriceT(checkReturnMemberExam.getId(), newRecheckExamMemberExam.getId());
                    // 生成收费单 根据老的id获取收费信息
                    ExamPay examPay = examPayDao.get(checkReturnMemberExam.getPayId());
                    //然后通过新的数据进行生成
                    ExamPay newExamPay = makeNewExamPay(newRecheckExamMemberExam, examPay, totalPrice, user);
                    if (newExamPay.getId() != null) {
                        //更新之前的exampay isnew = 0
                        ExamPay oldExamPay = updateOldExamPayIsNewT(examPay);
                    }

                    ExamMemberExam payIdMemberExam = updateMemerExamPayIdT(newExamPay.getId(), newRecheckExamMemberExam);

                    if (payIdMemberExam.getId() != 0) {

                    } else {
                        errorMsg("");
                    }
                }
            } else {
//                    ajaxCallBack = AjaxCallBack.error("还有未完成项目，无法审核！");
            }
        }


    }


    /**
     * 判断审核
     * 1、首先判断该条记录下面的体检项目合不合格 如果有不合格项 则将该条记录不合格 同时审核状态为未审核
     *      同时生成一条财务收费记录    以及将不合格的项目加入到examBlodIntestinal中 且isnew=1
     * 2、如果判断都为合格 那该条记录置为合格 并且自动生成一条健康证数据
     *
     * @param examIdList
     * @param user
     */
    @Override
    public void batchAuditT (List < String > examIdList, User user){
        for (int i = 0; i < examIdList.size(); i++) {
            ExamMemberExam examMemberExam = examMemberExamDao.get(Long.valueOf(examIdList.get(i)));
//            // 如果该项已审核，则不能重复审核
//            if (examMemberExam.getVerifyState() == 1) {
//                return AjaxCallBack.error("请勿重复审核!");
//            }

            // 只有体检项目全部完成并且未判断的情况下，才可以进行审核判断
            if (examMemberExam.getEntryState() == 1 && examMemberExam.getVerifyConclusion() == 0) {
                // 先判断该条体检记录是否有不合格项目，同时修改verifyConclusion
                ExamMemberExam checkReturnMemberExam = checkExamMemberExamQualifiedT(examMemberExam.getId(), user);
                if (checkReturnMemberExam.getId() != 0) {
                    // 判断好后，在对收费记录、复检记录进行数据添加 同时
                    // 通过检查结果判断是否合格，如果全部合格，那么生成健康证，
                    // 如果有任意一项不合格，生成复检通知单；
                    ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                    examBlodIntestinal.setExamNumber(examMemberExam.getExamNumber());
                    List<ExamBlodIntestinal> examBlodIntestinals = examBlodIntestinalDao.findByExample(examBlodIntestinal);
                    String pushText = "";//定义一条推送消息
                    if (checkReturnMemberExam.getVerifyConclusion() == 1) {
                        //体检正常 生成健康证
                        //体检正常 生成健康证
                        ExamEcard examEcard = generateEcard(examMemberExam.getId(), user);

                        if (examEcard.getId() == null) {

                            errorMsg("健康证生成失败!");

                            return;
                        } else {

                            //2017-02-10 新增 将健康证数据进行回填exam_member_exam
                            ExamMemberExam ecardMemberExam = examMemberExamDao.get(examEcard.getMemberExamId());

                            ecardMemberExam.seteCardNumber(examEcard.geteCardNumber());
                            ecardMemberExam.setExpTime(examEcard.getExpTime());

                            examMemberExamDao.updateBack(ecardMemberExam);


                            //生成健康证之后，将该用户数据插入平台同步表
                            ExamSynchronize examSynchronize = new ExamSynchronize();

                            examSynchronize.setIsSuccess(0);
                            examSynchronize.seteCardId(examEcard.getId());
                            examSynchronize.setExamMemberId(examEcard.getMemberExamId());
                            examSynchronize.setMemberId(examEcard.getMemberId());
                            examSynchronize.setCreateTime(new Date().getTime());

                            examSynchronize = examSynchronizeDao.saveBack(examSynchronize);

                            if (examSynchronize.getId() == null) {
                                logger.info("健康证ID" + examSynchronize.geteCardId() + "入库失败!");
                            }


                        }
                        // 修改exam_blod_intestinal表is_new 状态
                        if (examBlodIntestinals.size() > 0) {
                            for (int j = 0; j < examBlodIntestinals.size(); j++) {
                                ExamBlodIntestinal examBlod = examBlodIntestinals.get(j);
                                examBlod.setIsNew(0);
                                examBlodIntestinalDao.update(examBlod);
                            }
                        }
                        pushText += "您好，您的体检全部合格，请领取健康证，在微信中也可以查看电子健康证。";
//                        examMemberExamDao.update(examMemberExam);
                    } else if (checkReturnMemberExam.getVerifyConclusion() == 2) {//不合格

//                        examMemberExamDao.update(examMemberExam);
                        ExamMemberExam newRecheckExamMemberExam = new ExamMemberExam();
                        newRecheckExamMemberExam = generateNewExam(examMemberExam);
                        if (newRecheckExamMemberExam.getId() != null) {
                            examMemberExam = updateOldMemberExamIsNewT(examMemberExam);

                            // 修改exam_blod_intestinal表is_new 状态
                            if (examBlodIntestinals.size() > 0) {
                                for (int j = 0; j < examBlodIntestinals.size(); j++) {
                                    ExamBlodIntestinal examBlod = examBlodIntestinals.get(j);
                                    examBlod.setIsNew(0);
                                    examBlodIntestinalDao.update(examBlod);
                                }
                            }
                        }
                        // 生成项目单，并计算价格存到收费单
                        BigDecimal totalPrice = reckeckTotalPriceT(examMemberExam.getId(), newRecheckExamMemberExam.getId());
                        // 生成收费单 根据老的id获取收费信息
                        ExamPay examPay = examPayDao.get(examMemberExam.getPayId());
                        //然后通过新的数据进行生成
                        ExamPay newExamPay = makeNewExamPay(newRecheckExamMemberExam, examPay, totalPrice, user);
                        if (newExamPay.getId() != 0) {
                            //更新之前的exampay isnew = 0
                            ExamPay oldExamPay = updateOldExamPayIsNewT(examPay);
                        } else {

                        }
//                        ExamMemberExam payIdMemberExam = new ExamMemberExam();
//
//                        //根据缴费记录 回填examMemberExam
//                        newRecheckExamMemberExam.setPayId(newExamPay.getId());
//
//                        examMemberExamDao.update(newRecheckExamMemberExam);
                        newRecheckExamMemberExam.setPayMoney(newExamPay.getPayMoney());
                        ExamMemberExam payIdMemberExam = updateMemerExamPayIdT(newExamPay.getId(), newRecheckExamMemberExam);

                        if (payIdMemberExam.getId() != 0) {

                        } else {
                            errorMsg("");
                        }
                        pushText += "您好，您的体检有不合格项目，请进行复检。";
                    }
                    //如果是微信用户，向用户推送微信消息
                    ExamWxBind examWxBind = new ExamWxBind();
                    examWxBind.setMemberId(examMemberExam.getMemberId());
                    List<ExamWxBind> examWxBinds = examWxBindDao.findByExample(examWxBind);
                    if (examWxBinds.size() > 0) {
                        try {
                            sendMessageService.sendTextMessage(examWxBinds.get(0).getWxOpenId(), pushText);
                        } catch (WxErrorException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
//                    ajaxCallBack = AjaxCallBack.error("还有未完成项目，无法审核！");
                }
            }
        }
    }


    /**
     * 检测体检号是否有效
     * @param examNumber
     * @param map
     */
    @Override
    public void checkMemberExamT (String examNumber, Map < String, Object > map){
//        ExamMemberExam examMemberExam = new ExamMemberExam();
//        examMemberExam.setExamNumber(examNumber);
//        examMemberExam.setIsNew(1);
//        examMemberExam.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
//        List<ExamMemberExam> memberExamList = examMemberExamDao.findByExample(examMemberExam);
//        if(memberExamList!=null&&memberExamList.size()<=0){
//            map.put("errorMsg","当前没有需要体检的项目！");
//        }else{

        ExamMemberExam examMemberExamNoJudge = new ExamMemberExam();
        examMemberExamNoJudge.setExamNumber(examNumber);
        examMemberExamNoJudge.setIsNew(1);
        //未作废
        examMemberExamNoJudge.setIsCancel(0);
        examMemberExamNoJudge.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
        List<ExamMemberExam> memberExamNoJudgeList = examMemberExamDao.findByExample(examMemberExamNoJudge);




        if (memberExamNoJudgeList != null && memberExamNoJudgeList.size() > 0) {


            if (memberExamNoJudgeList.get(0).getRecheckTag() > 0) {
                ExamBlodIntestinal recheckResult = new ExamBlodIntestinal();
                recheckResult.setMemberExamId(memberExamNoJudgeList.get(0).getId());
                //未作废
                recheckResult.setIsCancel(0);
                List<ExamBlodIntestinal> recheckResults = examBlodIntestinalDao.findByExample(recheckResult);
                boolean f = false;
                if (recheckResults != null && recheckResults.size() > 0) {
                    for (ExamBlodIntestinal result : recheckResults) {
                        if (result.getProjectId().equals(SystemContent.PROJECT_HEV) || result.getProjectId().equals(SystemContent.PROJECT_ALT) || result.getProjectId().equals(SystemContent.PROJECT_HAV)) {
                            f = true;
                        }

                        if (f) {
                            if (checkPayStateForBlodIntestinal(examNumber, map)) {
                                return;
                            }
                            if (result.getProjectId().equals(SystemContent.PROJECT_HEV) && result.getExamBlodId() != null) {

                                map.put("errorMsg", "血检项目已检测过，无须再检验！");
                                return;
                            }
                            if (result.getProjectId().equals(SystemContent.PROJECT_ALT) && result.getExamBlodId() != null) {
                                map.put("errorMsg", "血检项目已检测过，无须再检验！");
                                return;
                            }
                            if (result.getProjectId().equals(SystemContent.PROJECT_HAV) && result.getExamBlodId() != null) {
                                map.put("errorMsg", "血检项目已检测过，无须再检验！");
                                return;
                            }
                        }

                    }
                    if (!f) {
                        map.put("errorMsg", "复检没有血检项目！");
                        return;
                    }
                } else {
                    map.put("errorMsg", "当前没有需要体检的项目！");
                    return;
                }

            } else {
                // 判断血检  是否被乡镇 判断
                ExamBlodIntestinal xzEbi = new ExamBlodIntestinal();
                xzEbi.setExamNumber(examNumber);
                List<ExamBlodIntestinal> xzExp = examBlodIntestinalDao.findByExample(xzEbi);
                for (ExamBlodIntestinal intestinal : xzExp) {
                    if (intestinal.getIsQualified()!=0&&intestinal.getProjectId().equals(SystemContent.PROJECT_HEV)){
                        map.put("errorMsg", "血检项目已检测过，无须再检验！");
                        return;
                    }

                    if (intestinal.getIsQualified()!=0&&intestinal.getProjectId().equals(SystemContent.PROJECT_ALT)){
                        map.put("errorMsg", "血检项目已检测过，无须再检验！");
                        return;
                    }

                    if (intestinal.getIsQualified()!=0&&intestinal.getProjectId().equals(SystemContent.PROJECT_HAV)){
                        map.put("errorMsg", "血检项目已检测过，无须再检验！");
                        return;
                    }
                }

                ExamPackageProject pp = new ExamPackageProject();
                pp.setPackageId(memberExamNoJudgeList.get(0).getPackageId());
                List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);
                boolean flag = false;
                List<Long> projectIds = new ArrayList<>();
                if (packageProjectList != null && packageProjectList.size() > 0) {
                    for (ExamPackageProject p : packageProjectList) {
                        if (p.getProjectId() == SystemContent.PROJECT_HEV || p.getProjectId() == SystemContent.PROJECT_ALT || p.getProjectId() != SystemContent.PROJECT_HAV) {
                            flag = true;
                            projectIds.add(p.getProjectId());
                        }
                    }
                    if (flag) {
                        for (Long projectId : projectIds) {
                            if (checkPayStateForBlodIntestinal(examNumber, map)) {
                                return;
                            }
                            if (projectId == SystemContent.PROJECT_HEV) {
                                ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                                examBlodIntestinal.setExamNumber(examNumber);
                                examBlodIntestinal.setIsNew(1);
                                examBlodIntestinal.setIsShowRefresh(0);
                                examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_HEV);
                                //未作废
                                examBlodIntestinal.setIsCancel(0);
                                List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
                                // 判断是否回填了blodId，复检采集后不允许再采集
                                if (examBlodIntestinalList != null && examBlodIntestinalList.size() > 0 && examBlodIntestinalList.get(0).getExamBlodId() != null) {
                                    map.put("errorMsg", "血检项目已检测过，无须再检验！");
                                    return;
                                }
                            }
                            if (projectId == SystemContent.PROJECT_ALT) {
                                ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                                examBlodIntestinal.setExamNumber(examNumber);
                                examBlodIntestinal.setIsNew(1);
                                examBlodIntestinal.setIsShowRefresh(0);
                                examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_ALT);
                                //未作废
                                examBlodIntestinal.setIsCancel(0);
                                List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
                                if (examBlodIntestinalList != null && examBlodIntestinalList.size() > 0 && examBlodIntestinalList.get(0).getExamBlodId() != null) {
                                    map.put("errorMsg", "血检项目已检测过，无须再检验！");
                                    return;
                                }
                            }
                            if (projectId == SystemContent.PROJECT_HAV) {
                                ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                                examBlodIntestinal.setExamNumber(examNumber);
                                examBlodIntestinal.setIsNew(1);
                                examBlodIntestinal.setIsShowRefresh(0);
                                examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_HAV);
                                //未作废
                                examBlodIntestinal.setIsCancel(0);
                                List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
                                if (examBlodIntestinalList != null && examBlodIntestinalList.size() > 0 && examBlodIntestinalList.get(0).getExamBlodId() != null) {
                                    map.put("errorMsg", "血检项目已检测过，无须再检验！");
                                    return;
                                }
                            }
                        }
                    } else {
                        map.put("errorMsg", "您没有要检测的血检项目，无须检验！");
                        return;
                    }
                }
            }


        } else {
            map.put("errorMsg", "当前没有需要体检的项目！");
            return;
        }


//            }


//        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
//        examBlodIntestinal.setExamNumber(examNumber);
//        examBlodIntestinal.setIsNew(1);
//        examBlodIntestinal.setIsShowRefresh(0);
//        List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
//        if (examBlodIntestinalList.size()>0) {
//            List<ExamBlodIntestinal> examBlodList = examBlodIntestinalList;
//            for (ExamBlodIntestinal examBlodIntestinal1 : examBlodIntestinalList) {
//                if (!examBlodIntestinal1.getProjectName().contains("戊肝")
//                        && !examBlodIntestinal1.getProjectName().contains("谷丙转氨酶")
//                        && !examBlodIntestinal1.getProjectName().contains("甲肝")) {
//                    examBlodList.remove(examBlodIntestinal1);
//                    map.put("errorMsg","当前没有需要体检的血检项目！");
//                    //去除非血检项目
////                    if (examBlodList.size()>0) {
////
////                    }
//                }else{
//
//                }
//            }
//        }else{
//            map.put("errorMsg","当前没有需要体检的项目！");
//        }
    }

    /**
     * 修改体检信息表是否复检状态
     * @param memberExamId
     */
    @Override
    public void upRecheckState (Long memberExamId){
        ExamMemberExam memberExam = examMemberExamDao.get(memberExamId);
        memberExam.setIsRecheck(SystemContent.PROJECT_ISRECHECK_YES);
        examMemberExamDao.update(memberExam);
    }

    /**
     * 检测肠检体检号是否有效
     * @param examNumber
     * @param map
     */
    @Override
    public void checkMemberExamForIntestinalT (String examNumber, Map < String, Object > map){
//        ExamMemberExam examMemberExam = new ExamMemberExam();
//        examMemberExam.setExamNumber(examNumber);
//        examMemberExam.setIsNew(1);
//        examMemberExam.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
//        List<ExamMemberExam> memberExamList = examMemberExamDao.findByExample(examMemberExam);
//        if(memberExamList!=null&&memberExamList.size()<=0){
//            map.put("errorMsg","当前没有需要体检的项目！");
//        }else{
        ExamMemberExam examMemberExamNoJudge = new ExamMemberExam();
        examMemberExamNoJudge.setExamNumber(examNumber);
        examMemberExamNoJudge.setIsNew(1);
        //未作废
        examMemberExamNoJudge.setIsCancel(0);
        examMemberExamNoJudge.setVerifyConclusion(SystemContent.EXAM_ISQUALIFIED_WEIPANDUAN);
        List<ExamMemberExam> memberExamNoJudgeList = examMemberExamDao.findByExample(examMemberExamNoJudge);
        if (memberExamNoJudgeList != null && memberExamNoJudgeList.size() > 0) {

            if (memberExamNoJudgeList.get(0).getRecheckTag() > 0) {
                ExamBlodIntestinal recheckResult = new ExamBlodIntestinal();
                recheckResult.setMemberExamId(memberExamNoJudgeList.get(0).getId());
                //未作废
                recheckResult.setIsCancel(0);
                recheckResult.setProjectId(SystemContent.PROJECT_CHANGDAO);
                List<ExamBlodIntestinal> recheckResults = examBlodIntestinalDao.findByExample(recheckResult);
                boolean f = false;
                if (recheckResults != null && recheckResults.size() > 0) {
                    if (checkPayStateForBlodIntestinal(examNumber, map)) {
                        return;
                    }
                    if (recheckResults.get(0).getExamIntestinalId() != null) {
                        //map.put("alre", "肠检项目已检测过，无须再检验！");
                        ExamIntestinal temp = new ExamIntestinal();
                        temp.setId(recheckResults.get(0).getExamIntestinalId());
                        temp.setMemberExamId(recheckResults.get(0).getMemberExamId());
                        List<ExamIntestinal> temp1 = examIntestinalDao.findByExample(temp);//调出原来的采样号
                        ExamMemberExam temp2 = new ExamMemberExam();//调出体检人信息
                       // ExamMedicalController c = new ExamMedicalController();
                        //ExamMemberExam examMemberExam = new ExamMemberExam();
                        temp2.setExamNumber(examNumber);
                        temp2.setIsCancel(0);
                        List<ExamMemberExam> examMemberExamList = examMemberExamDao.findByExample(temp2);
                        if(examMemberExamList!=null&&examMemberExamList.size()>0){
                            temp2 = examMemberExamList.get(examMemberExamList.size()-1);
                        }else{
                            temp2 = null;
                        }
                        //temp2 =c.findMemberExamByNumber(examNumber);
                        map.put("fla",temp2.getName());
                        map.put("num",temp1.get(temp1.size()-1).getTestNumber());//肠检号
                        map.put("name",temp2.getName());//姓名
                        map.put("examTime",temp2.getExamTimeStr());//体检日期
                        map.put("birthDate",temp2.getBirthday());//出生日期
                        map.put("area",temp2.getAreaName());
                       // map.put("type",temp2.get);
                        map.put("icon",temp2.getIcon());
                        map.put("sex",temp2.getSex());
                        map.put("idCard",temp2.getIdCardNum());
                        map.put("cate",temp2.getCategoryName());
                        map.put("workUnit",temp2.getWorkUnit());
                        return;
                    }
//                        for(ExamBlodIntestinal result:recheckResults){
//                            if(result.getProjectId().equals(SystemContent.PROJECT_CHANGDAO)){
//                                f=true;
//                            }
//
//                            if(f){
//                                if(result.getProjectId().equals(SystemContent.PROJECT_CHANGDAO)&&result.getExamIntestinalId()!=null){
//                                    map.put("errorMsg","肠检项目已检测过，无须再检验！");
//                                    return;
//                                }
//                            }else{
//                                map.put("errorMsg","复检没有肠检项目！");
//                                return;
//                            }
//
//                        }
                } else {
                    map.put("errorMsg", "复检没有肠检项目！");
                    return;
                }
            } else {
                ExamPackageProject pp = new ExamPackageProject();
                pp.setPackageId(memberExamNoJudgeList.get(0).getPackageId());
                pp.setProjectId(SystemContent.PROJECT_CHANGDAO);
                List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);
                boolean flag = false;
                if (packageProjectList != null && packageProjectList.size() > 0) {
                    if (checkPayStateForBlodIntestinal(examNumber, map)) {
                        return;
                    }
                    ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
                    examBlodIntestinal.setExamNumber(examNumber);
                    examBlodIntestinal.setIsNew(1);
                    examBlodIntestinal.setIsShowRefresh(0);
                    examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_CHANGDAO);
                    //未作废
                    examBlodIntestinal.setIsCancel(0);
                    List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
                    if (examBlodIntestinalList != null && examBlodIntestinalList.size() > 0 && examBlodIntestinalList.get(0).getExamIntestinalId() != null) {
                        //map.put("alre", "肠检项目已检测过，无须再检验！");
                        //System.out.println("肠检号为："+ examBlodIntestinalList.get(0).getExamIntestinalId());
                        ExamIntestinal temp = new ExamIntestinal();
                        temp.setId(examBlodIntestinalList.get(0).getExamIntestinalId());
                        temp.setMemberExamId(examBlodIntestinalList.get(0).getMemberExamId());
                        List<ExamIntestinal> temp1 = examIntestinalDao.findByExample(temp);
                       // map.put("alre",temp1.get(temp1.size()-1).getTestNumber());
                        //map.put("alre",examBlodIntestinalList.get(0).getExamIntestinalId() );
                        ExamMemberExam temp2 = new ExamMemberExam();//调出体检人信息
//                        ExamMedicalController c = new ExamMedicalController();
//                        temp2 =c.findMemberExamByNumber(examNumber);
                        temp2.setExamNumber(examNumber);
                        temp2.setIsCancel(0);
                        List<ExamMemberExam> examMemberExamList = examMemberExamDao.findByExample(temp2);
                        if(examMemberExamList!=null&&examMemberExamList.size()>0){
                            temp2 = examMemberExamList.get(examMemberExamList.size()-1);
                        }else{
                            temp2 = null;
                        }
                        map.put("fla",temp2.getName());
                        map.put("num",temp1.get(temp1.size()-1).getTestNumber());//肠检号
                        map.put("name",temp2.getName());//姓名
                        map.put("examTime",temp2.getExamTimeStr());//体检日期
                        map.put("birthDate",temp2.getBirthday());//出生日期
                        map.put("area",temp2.getAreaName());
                        // map.put("type",temp2.get);
                        map.put("icon",temp2.getIcon());
                        map.put("sex",temp2.getSex());
                        map.put("idCard",temp2.getIdCardNum());
                        map.put("cate",temp2.getCategoryName());
                        map.put("workUnit",temp2.getWorkUnit());
                        return;
                    }

//                        for(ExamPackageProject p:packageProjectList){
//                            if(p.getProjectId()==SystemContent.PROJECT_CHANGDAO){
//                                flag=true;
//                            }
//                        }
//                        if(flag){
//                            ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
//                            examBlodIntestinal.setExamNumber(examNumber);
//                            examBlodIntestinal.setIsNew(1);
//                            examBlodIntestinal.setIsShowRefresh(0);
//                            examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_CHANGDAO);
//                            //未作废
//                            examBlodIntestinal.setIsCancel(0);
//                            List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
//                            if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0&&examBlodIntestinalList.get(0).getExamIntestinalId()!=null){
//                                map.put("errorMsg","肠道已检测过，无须再检验！");
//                                return;
//                            }
//                        }else{
//                            map.put("errorMsg","您没有要检测的肠道项目，无须检验！");
//                            return;
//                        }
                } else {
                    map.put("errorMsg", "您没有要检测的肠道项目，无须检验！");
                    return;
                }
            }


//                ExamPackageProject pp = new ExamPackageProject();
//                pp.setPackageId(memberExamNoJudgeList.get(0).getPackageId());
//                List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);
//                boolean flag = false;
//                if(packageProjectList!=null&&packageProjectList.size()>0){
//                    for(ExamPackageProject p:packageProjectList){
//                        if(p.getProjectId()==SystemContent.PROJECT_CHANGDAO){
//                            flag=true;
//                        }
//                    }
//                    if(flag){
//                        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
//                        examBlodIntestinal.setExamNumber(examNumber);
//                        examBlodIntestinal.setIsNew(1);
//                        examBlodIntestinal.setIsShowRefresh(0);
//                        examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_CHANGDAO);
//                        List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
//                        if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0&&examBlodIntestinalList.get(0).getExamIntestinalId()!=null){
//                            map.put("errorMsg","肠道已检测过，无须再检验！");
//                            return;
//                        }
//                    }else{
//                        map.put("errorMsg","您没有要检测的肠道项目，无须检验！");
//                        return;
//                    }
//                }
        } else {
            map.put("errorMsg", "当前没有需要体检的项目！");
            return;
        }

//        }

//        if(memberExamList!=null&&memberExamList.size()>0){
//            map.put("errorMsg","当前没有需要体检的项目！");
//        }else{
//            ExamPay pay = new ExamPay();
//            pay.setIsNew(1);
//            pay.setExamNumber(examNumber);
//            List<ExamPay> payList = examPayDao.findByExample(pay);
//            if(payList!=null&&payList.size()>0){
//                if(payList.get(0).getPayState()!=2){
//                    map.put("errorMsg","您还没有缴费！");
//                }
//            }
//            ExamPackageProject pp = new ExamPackageProject();
//            pp.setPackageId(memberExamList.get(0).getPackageId());
//            List<ExamPackageProject> packageProjectList = packageProjectDao.findByExample(pp);
//            boolean flag = false;
//            if(packageProjectList!=null&&packageProjectList.size()>0){
//                for(ExamPackageProject p:packageProjectList){
//                    if(p.getProjectId()==SystemContent.PROJECT_CHANGDAO){
//                        flag=true;
//                    }
//                }
//                if(flag){
//                    ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
//                    examBlodIntestinal.setExamNumber(examNumber);
//                    examBlodIntestinal.setIsNew(1);
//                    examBlodIntestinal.setIsShowRefresh(0);
//                    examBlodIntestinal.setType(SystemContent.PROJECT_TYPE_CHANGDAO);
//                    List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
//                    if(examBlodIntestinalList!=null&&examBlodIntestinalList.size()>0){
//                        map.put("errorMsg","肠道已检测过，无须再检验！");
//                    }
//                }else{
//                    map.put("errorMsg","您没有要检测的肠道项目，无须检验！");
//                }
//            }
//
//        }
    }

    //检测是否缴费

    private boolean checkPayStateForBlodIntestinal(String examNumber, Map<String, Object> map) {
        ExamPay pay = new ExamPay();
        pay.setIsNew(1);
        pay.setExamNumber(examNumber);
        //未作废
        pay.setIsCancel(0);
        List<ExamPay> payList = examPayDao.findByExample(pay);
        if (payList != null && payList.size() > 0) {
            if (payList.get(0).getPayState() != 2) {
                map.put("errorMsg", "您还没有缴费！");
                return true;
            }
        }
        return false;
    }


    ////////////////////////////////体检结论判断///////////////////////////////////////////////////////

    public ExamMemberExam updateMemerExamPayIdT(Long newExamPayId, ExamMemberExam newRecheckExamMemberExam) {

        //根据缴费记录 回填examMemberExam
        newRecheckExamMemberExam.setPayId(newExamPayId);

        return examMemberExamDao.updateBack(newRecheckExamMemberExam);
    }

    /**
     * 判断该用户是否可以判断合格 还是不合格
     *
     * @param examMemberExamId
     * @return
     */
    //检查体检单是否合格,并修改状态
    public ExamMemberExam checkExamMemberExamQualifiedT(Long examMemberExamId, User user) {

        //获取memberExam记录信息
        ExamMemberExam examMemberExam = examMemberExamDao.get(examMemberExamId);
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
        examBlodIntestinal.setMemberExamId(examMemberExam.getId());
//
        //根据memberExamNum以及isNew为1的数据 然后再去判断
        List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);

//        examMemberExam.setVerifyConclusion(1);
        int isVerifyConclusion = 1;
        if (examBlodIntestinalList.size() > 0) {
            for (int j = 0; j < examBlodIntestinalList.size(); j++) {
                ExamBlodIntestinal ebi = examBlodIntestinalList.get(j);
                if (ebi.getIsQualified() == 2) {
                    //只要有任意一项体检项目不合格，则不合格
//                    examMemberExam.setVerifyConclusion(2);
                    isVerifyConclusion = 2;
                    break;
                }
            }
        }

        if (isVerifyConclusion == 1) {
            //审核通过
            examMemberExam.setVerifyState(1);

        } else {
            //未审核
            examMemberExam.setVerifyState(2);
        }

        examMemberExam.setVerifyConclusion(isVerifyConclusion);

        examMemberExam.setVerifyId(user.getUid());
        examMemberExam.setVerifyTime(new Date().getTime());
        examMemberExam.setAuditTime(new Date().getTime());
        examMemberExam.setVerifyName(user.getRealName());

        //更新examMemberExam表数据
        examMemberExam = examMemberExamDao.updateBack(examMemberExam);


        return examMemberExam;

    }

    public ExamMemberExam updateOldMemberExamIsNewT(ExamMemberExam examMemberExam) {

        examMemberExam.setIsNew(0);

        examMemberExam = examMemberExamDao.updateBack(examMemberExam);

        return examMemberExam;
    }

    /**
     * 生成新的一条体检登记记录 且is_new = 1
     * is_show_recheck = 1
     *
     * @param examMemberExam
     * @return
     */
    public ExamMemberExam generateNewExam(ExamMemberExam examMemberExam) {

        ExamMemberExam exam = new ExamMemberExam();
        exam.setCreateTime(new Date().getTime());
        exam.setMemberId(examMemberExam.getMemberId());
        exam.setMobile(examMemberExam.getMobile());
        exam.setAgenciesId(examMemberExam.getAgenciesId());
        exam.setExamTime(exam.getCreateTime());
        exam.setAreaId(examMemberExam.getAreaId());
        exam.setExamNumberId(examMemberExam.getExamNumberId());
        exam.setExamNumber(examMemberExam.getExamNumber());
        exam.setWorkUnit(examMemberExam.getWorkUnit());
        exam.setIsRecheck(1);
        exam.setVerifyState(2);
        exam.setVerifyConclusion(0);
        exam.setAge(examMemberExam.getAge());
        exam.setPackageId(examMemberExam.getPackageId());
        exam.setCategoryId(examMemberExam.getCategoryId());
        exam.setExamNumberIcon(examMemberExam.getExamNumberIcon());
        exam.setIsNew(1);
        exam.setRecheckTag(examMemberExam.getRecheckTag() + 1);
        exam.setIsRecheckPrint(0);
        exam.setEntryState(0);
        exam.setIsCancel(0);
        exam.setIHA("阴性");
        exam.setDDIA("阴性");
        //自动进入复检管理里面
        exam.setIsShowRecheck(0);
        exam.setIsPhysicalPrint(0);
        exam.setChannel(examMemberExam.getChannel());

        //新增 2017-02-10 固化数据
        //2017-02-10 新增 将部分数据进行固化
        exam.setIcon(examMemberExam.getIcon());
        exam.setIdCardNum(examMemberExam.getIdCardNum());
        exam.setName(examMemberExam.getName());
        exam.setSex(examMemberExam.getSex());
        exam.setBirthday(examMemberExam.getBirthday());
        exam.setAreaName(examMemberExam.getAreaName());
        exam.setCategoryName(examMemberExam.getCategoryName());
        exam.setAgenciesName(examMemberExam.getAgenciesName());
        exam.setAgenciesCode(examMemberExam.getAgenciesCode());

        exam.setPayType(9);
        exam.setPayState(1);

        exam = examMemberExamDao.saveBack(exam);

        return exam;
    }


    /**
     * 体检合格，生成健康证
     *
     * @param examMemberExamId
     */
    //
    public ExamEcard generateEcard(Long examMemberExamId, User user) {

        ExamMemberExam examMemberExam = examMemberExamDao.get(examMemberExamId);
        ExamMember examMember = examMemberDao.get(examMemberExam.getMemberId());

        ExamEcard examEcard = new ExamEcard();
        examEcard.setMemberId(examMemberExam.getMemberId());
        Date currentDate = new Date();

        examEcard.setCreateTime(currentDate.getTime());//创建时间是当前系统时间
        examEcard.setMemberPhoto(examMember.getIcon());
        examEcard.setMemberAge(examMemberExam.getAge());
        examEcard.setExamNumberId(examMemberExam.getExamNumberId());

        Calendar curr = Calendar.getInstance();
        curr.setTime(currentDate);
        curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
        Date date = curr.getTime();
        //有效期
        examEcard.setExpTime(date.getTime());
        examEcard.setExamNumber(examMemberExam.getExamNumber());
        examEcard.setMemberExamId(examMemberExam.getId());
        examEcard.setExamTime(examMemberExam.getExamTime());

        examEcard.setAuditTime(examMemberExam.getAuditTime());
        examEcard.setIssueTime(examMemberExam.getVerifyTime());

        ExamAgencies examAgencies = examAgenciesDao.get(examMemberExam.getAgenciesId());
        examEcard.seteCardAgency(examAgencies.getAgenciesName());

        examEcard.setWorkUnit(examMemberExam.getWorkUnit());
        examEcard.setAreaId(examMemberExam.getAreaId());
        examEcard.setAreaName(examMemberExam.getAreaName());
        examEcard.setCategoryId(examMemberExam.getCategoryId());
        examEcard.setCategoryName(examMemberExam.getCategoryName());
        examEcard.setSex(examMemberExam.getSex());

        Map<String, String> generateMap = generateCertificate(String.valueOf(examAgencies.getAgenciesCode()), examMemberExam.getExamNumber());

        if (generateMap.get("certificate").length() != 17) {

            examEcard = new ExamEcard();

            return examEcard;

        }
        examEcard.seteCardNumber(generateMap.get("certificate"));
        examEcard.setIsPrint(0);
        examEcard.setName(examMember.getName());
        examEcard.setIdCardNumber(examMember.getIdCardNum());
        examEcard.setIsUpload(0);

        examEcard = examEcardDao.saveBack(examEcard);

        if (examEcard.getId() != null) {
            //将获取到的数值回填
            ExamFlowNumber flowNumber = examFlowNumberDao.get(1L);

            flowNumber.setFlowNumber(Long.parseLong(generateMap.get("flowNumber")));

            examFlowNumberDao.update(flowNumber);
        }


        //将生成的健康证信息的id回填到examMemberExam表中

        examMemberExam.seteCardId(examEcard.getId());

        examMemberExamDao.update(examMemberExam);


        try {


            //生成二维码的url地址
            String uuid = UUID.randomUUID().toString();
            examEcard.setUuid(uuid);
            String url = "http://" + configInfo.getQrCodeAddress() + "/wap/showQrEcard.do?uuid=" + uuid;

            File file = QRCode.from(url).withSize(300, 300).withCharset("UTF-8").to(ImageType.JPG).file();

            String returnBack = localFileDrive.upload(file, "QrRCode" + UUID.randomUUID().toString() + ".jpg");

            System.out.println("returnBack=" + returnBack);

            examEcard.setQrCodeUrl(returnBack);

            examEcardDao.update(examEcard);


        } catch (IOException e) {

        }


        return examEcard;


    }

    /**
     * 计算复检项目总费用
     *
     * @param examMemberExamId
     * @return
     */
    public BigDecimal reckeckTotalPriceT(Long examMemberExamId, Long newExamMemberExamId) {

        BigDecimal totalPrice = new BigDecimal(0);
        ExamMemberExam examMemberExam = examMemberExamDao.get(examMemberExamId);
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
        examBlodIntestinal.setMemberExamId(examMemberExam.getId());
        List<ExamBlodIntestinal> examBlodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);
        if (examBlodIntestinalList.size() > 0) {
            for (int j = 0; j < examBlodIntestinalList.size(); j++) {
                ExamBlodIntestinal ebi = examBlodIntestinalList.get(j);
                ebi.setIsNew(0);
                examBlodIntestinalDao.update(ebi);
                if (ebi.getIsQualified() == 2) {//不合格项目的费用
                    //不合格项目取价格加入总价
                    ExamProject examProject = examProjectDao.get(ebi.getProjectId());
                    if (examProject != null) {
                        totalPrice = totalPrice.add(examProject.getProjectPrice());
                    }
                    addNewExamBlodIntestinal(newExamMemberExamId, ebi);
                }
            }
        }
        return totalPrice;
    }

    /**
     * 将不合格的数据重新录入examBlodIntestinal表中
     *
     * @param examMemberExamId
     * @param examBlodIntestinal
     * @return
     */
    public ExamBlodIntestinal addNewExamBlodIntestinal(Long examMemberExamId, ExamBlodIntestinal examBlodIntestinal) {
        ExamBlodIntestinal ebi = new ExamBlodIntestinal();
        // 不合格项目插入表中
//        ebi.setIsNew(0);
//        examBlodIntestinalDao.update(ebi);
        ebi.setMemberExamId(examMemberExamId);
        ebi.setExamNumber(examBlodIntestinal.getExamNumber());
        ebi.setProjectId(examBlodIntestinal.getProjectId());
        ebi.setProjectName(examBlodIntestinal.getProjectName());
        ebi.setIsQualified(0);
        ebi.setEntryState(0);
        ebi.setMemberId(examBlodIntestinal.getMemberId());
        ebi.setCreateTime(new Date().getTime());
        ebi.setIsRecheck(0);
        ebi.setType(examBlodIntestinal.getType());
        ebi.setRecheckTag(examBlodIntestinal.getRecheckTag() + 1);
        ebi.setIsShowRefresh(0);
        ebi.setIsNew(1);
        ebi.setIsCancel(0);
        examBlodIntestinalDao.save(ebi);

        return ebi;
    }

    /**
     * //生成新的缴费单-pay
     *
     * @param examMemberExam
     * @param examPay
     * @param totalPrice
     * @param user
     * @return
     */
    public ExamPay makeNewExamPay(ExamMemberExam examMemberExam, ExamPay examPay, BigDecimal totalPrice, User user) {

        ExamAgencies examAgencies = examAgenciesDao.get(examMemberExam.getAgenciesId());

        ExamPay newExamPay = new ExamPay();
        newExamPay.setExamMemberId(examMemberExam.getId());
        newExamPay.setPayMoney(totalPrice);
        if (examAgencies.getAgenciesCode().equals(SystemContent.JIKONGZHONGXIN)) {
            // 疾控中心体检
            newExamPay.setPayState(1);

            newExamPay.setPayType(9);
        } else {
            //乡镇同时生成收费人员  收费时间 收费方式等 区别于疾控中心
            //乡镇体检
            newExamPay.setPayState(2);
            newExamPay.setTollCollectorId(examPay.getTollCollectorId());
            newExamPay.setTollCollectorName(examPay.getTollCollectorName());
            newExamPay.setPayTime(new Date().getTime());
            newExamPay.setPayType(0);

        }
        newExamPay.setCreateTime(new Date().getTime());
        newExamPay.setIsPrint(0);
        newExamPay.setExamNumberId(examPay.getExamNumberId());
        newExamPay.setCreatorId(user.getUid());
        newExamPay.setExamNumber(examPay.getExamNumber());
        newExamPay.setPayActualMoney(new BigDecimal(0));
        newExamPay.setCreatorName(user.getUsername());
        newExamPay.setIsSpecial(0);
        newExamPay.setExamNumber(examPay.getExamNumber());
        newExamPay.setRecheckTag(examPay.getRecheckTag() + 1);
        newExamPay.setIsNew(1);
        newExamPay.setIsCancel(0);

        //新增 2017-02-10 固化数据
        newExamPay.setIcon(examPay.getIcon());
        newExamPay.setWorkUnit(examPay.getWorkUnit());
        newExamPay.setIdCardNum(examPay.getIdCardNum());
        newExamPay.setAreaId(examPay.getAreaId());
        newExamPay.setAreaName(examPay.getAreaName());
        newExamPay.setName(examPay.getName());

        newExamPay = examPayDao.saveBack(newExamPay);

        return newExamPay;
    }

    /**
     * 更新旧数据is_new = 0
     *
     * @param examPay
     * @return
     */
    public ExamPay updateOldExamPayIsNewT(ExamPay examPay) {

        examPay.setIsNew(0);

        return examPayDao.updateBack(examPay);

    }


//    /**
//     * 生成健康证:格式 苏32058106716037959号 规则：行政区划代码(6位)机构编码(3位)年份(2位)体检流水号(6位)
//     * @param agenciesCode
//     * @param examNumber
//     * @return
//     */
//    public String generateCertificate(String agenciesCode, String examNumber) {
//        String number = "320581";
//        ExamNumber examNumber1 = new ExamNumber();
//        examNumber1.setNumber(examNumber);
//        List<ExamNumber> examNumbers = examNumberDao.findByExample(examNumber1);
//        if (examNumbers.size()>0) {
//            ExamNumber examNumber2 = examNumbers.get(0);
//            DecimalFormat df = new DecimalFormat("000000");
//            String certificateNum = df.format(examNumber2.getWaterNumber() + 1);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//            String date = sdf.format(new Date());
//            date = date.substring(date.length() - 2, date.length());
//            number += agenciesCode + date + certificateNum;
//        }
//        return number;
//    }

    /**
     * 生成健康证:格式 苏32058106716037959号 规则：行政区划代码(6位)机构编码(3位)年份(2位)体检流水号(6位)
     *
     * @param agenciesCode
     * @param examNumber
     * @return
     */
    public Map<String, String> generateCertificate(String agenciesCode, String examNumber) {
        String number = "320581";

//        ExamNumber examNumber1 = new ExamNumber();
//        examNumber1.setNumber(examNumber);
//        List<ExamNumber> examNumbers = examNumberDao.findByExample(examNumber1);

        Map<String, String> map = new HashedMap();

        ExamFlowNumber flowNumber = examFlowNumberDao.get(1L);

        long flowNumbers = flowNumber.getFlowNumber() + 1;


        DecimalFormat df = new DecimalFormat("000000");
        String certificateNum = df.format(flowNumbers);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String date = sdf.format(new Date());
        date = date.substring(date.length() - 2, date.length());
        number += "067" + date + certificateNum;

        map.put("certificate", number);

        map.put("flowNumber", String.valueOf(flowNumbers));

        return map;
    }

    /**
     * 审核状态   1：审核   2：未审核
     * 体检结果:0、未判断 1：合格  2：不合格  默认值都为0
     *
     * @param examMemberExam
     * @return
     */
    @Override
    public Boolean cancelCheckedService(ExamMemberExam examMemberExam) {

        //1、如果该状态已经审核通过并且生成了健康证
        //2、如果该状态已经审核不通过且生成了复检单
        //3、未判断、未审核的订单不需要处理
        if (examMemberExam.getVerifyState() == 2 && examMemberExam.getVerifyConclusion() == 0) {
            //不做任何处理
        } else if (examMemberExam.getVerifyState() == 1 && examMemberExam.getVerifyConclusion() == 1) {
            //审核且合格
            //撤销审核同时删除生成的健康证
            ExamEcard oldECard = examEcardDao.get(examMemberExam.geteCardId());

            if (oldECard.getId() != null) {
                examEcardDao.delete(oldECard.getId());
            }

            //审核状态
            examMemberExam.setVerifyState(2);
            //判断结果
            examMemberExam.setVerifyConclusion(0);
            examMemberExamDao.update(examMemberExam);


        } else if (examMemberExam.getVerifyState() == 2 && examMemberExam.getVerifyConclusion() == 2) {

            //不合格  未审核

        }


        return true;
    }

    /**
     * 批量删除
     *
     * @param examMemberExam
     * @return
     */
    @Override
    public Boolean etBatchDelete(ExamMemberExam examMemberExam) {


        //删除exam_blod_intestinal
        if (examMemberExam.getId() != null) {

            ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();

            examBlodIntestinal.setMemberExamId(examMemberExam.getId());

            List<ExamBlodIntestinal> blodIntestinalList = examBlodIntestinalDao.findByExample(examBlodIntestinal);

            for (ExamBlodIntestinal examBlodIntestinal1 : blodIntestinalList) {

                examBlodIntestinalDao.delete(examBlodIntestinal1.getId());

            }

        }

        //删除exam_blod
        if (examMemberExam.getId() != null) {

            ExamBlod examBlod = new ExamBlod();
            examBlod.setMemberExamId(examMemberExam.getId());

            List<ExamBlod> examBlodList = examBlodDao.findByExample(examBlod);

            for (ExamBlod ExamBlod1 : examBlodList) {

                examBlodDao.delete(ExamBlod1.getId());

            }

        }

        //删除exam_intestinal
        if (examMemberExam.getId() != null) {

            ExamIntestinal examIntestinal = new ExamIntestinal();
            examIntestinal.setMemberExamId(examMemberExam.getId());

            List<ExamIntestinal> examIntestinalList = examIntestinalDao.findByExample(examIntestinal);

            for (ExamIntestinal examIntestinal1 : examIntestinalList) {

                examIntestinalDao.delete(examIntestinal1.getId());

            }

        }

        //删除exam_member_exam
        if (examMemberExam.getId() != null) {

            examMemberExamDao.delete(examMemberExam.getId());

        }

        //删除exam_ecard

        if (examMemberExam.geteCardId() != null) {
            examEcardDao.delete(examMemberExam.geteCardId());
        }

        //删除收费信息
        if (examMemberExam.getPayId() != null) {
            examPayDao.delete(examMemberExam.getPayId());
        }


        return true;
    }

    public List<ExamMemberExam> getUnFinishExam() throws ParseException {
        return examMemberExamDao.findUnFinishExam();
    }

    /**
     * 乡镇肠检体检信息查找带回
     *
     * @param page
     * @param examNumber
     * @param memberName
     * @param beginTime
     * @param endTime
     */
    @Override
    public void getTownsMemberExamLookBack(Page<ExamMemberExam> page, String examNumber, String memberName, String beginTime, String endTime) throws ParseException {
        List<ExamMemberExam> list = examMemberExamDao.getTownsMemberExamLookBack(page.getPageCurrent(), page.getPageSize(), examNumber, memberName, beginTime, endTime);
        Long count = examMemberExamDao.countTownsMemberExamLookBack(page, examNumber, memberName, beginTime, endTime);
        page.setList(list);
        page.setTotal(count);
    }


    @Override
    public ExamMemberExam changeEtqMember(String name, String sex, String birthday, String address, String idCardNum, String mobile, String workUnit, Integer age,
                                          String areaId, String agenciesId, String packageId, String packageMoney, String isRemark, String memberExamId, String categoryId) {

        ExamMemberExam memberExam = examMemberExamDao.get(Long.parseLong(memberExamId));
//        ExamMember examMember = memberExam.getExamMember();
        ExamMember examMember = examMemberDao.get(memberExam.getMemberId());
        examMember.setName(name);
        examMember.setSex(Integer.parseInt(sex));
        examMember.setBirthday(birthday);
        examMember.setIdCardAddress(address);
        examMember.setIdCardNum(idCardNum);
        examMember.setMobile(mobile);
        examMember.setWorkUnit(workUnit);
        examMember.setUpdateTime(new Date().getTime());

        //更新主体基本信息
        examMember = examMemberDao.updateBack(examMember);

        if (examMember.getId() != null) {

            memberExam.setAreaId(Long.parseLong(areaId));
            memberExam.setPackageId(Long.parseLong(packageId));
            memberExam.setAge(age);
            memberExam.setCategoryId(Long.parseLong(categoryId));
            memberExam.setUpdateTime(new Date().getTime());
            memberExam.setAgenciesId(Long.parseLong(agenciesId));

            memberExam = examMemberExamDao.updateBack(memberExam);

            if (memberExam.getId() != null) {

//                ExamPay examPay = memberExam.getExamPay();
                ExamPay examPay = examPayDao.get(memberExam.getPayId());
                examPay.setPayMoney(new BigDecimal(packageMoney));
                if (StringUtils.isNotEmpty(isRemark)) {

                    if (Integer.parseInt(isRemark) == 1)
                        //修改状态、金额
                        examPay.setIsSpecial(1);
                    examPay.setPayMoney(new BigDecimal(0.00));
                    examPay.setPayState(2);
                    examPay.setIsRemark(1);
                }

                examPay = examPayDao.updateBack(examPay);

                if (examPay.getId() != null) {

                    return memberExam;

                } else {
                    return new ExamMemberExam();
                }

            } else {
                return new ExamMemberExam();
            }


        } else {

            return new ExamMemberExam();

        }


    }


    @Override
    public void updatePrintInfoT(long id) {
        ExamMemberExam examMemberExam = examMemberExamDao.get(id);
//        if (examMemberExam.getIsRecheck()==0){
//            examMemberExam.setIsPhysicalPrint(1);
//        }else {
//
//        }
        examMemberExam.setIsRecheckPrint(1);

        examMemberExam =  examMemberExamDao.updateBack(examMemberExam);
    }

    @Override
    public boolean updateExamMemberExamAndExamMeberUserInfoT(Long id, String name, int sex, String idCardNum, String birthday,
                                                          int age, String address, String mobile,Long categoryId,
                                                             String workUnit) {
        ExamMemberExam examMemberExam = examMemberExamDao.get(id);

        Long memberId = examMemberExam.getMemberId();

        //体检信息实体没有地址字段，不用set
        examMemberExam.setName(name);
        examMemberExam.setSex(sex);
        examMemberExam.setIdCardNum(idCardNum);
        examMemberExam.setBirthday(birthday);
        examMemberExam.setAge(age);
        examMemberExam.setMobile(mobile);
        examMemberExam.setCategoryId(categoryId);

        //更新行业分类和工作单位

        ExamCategory examCategory = categoryDao.get(categoryId);
        examMemberExam.setCategoryName(examCategory.getCategoryName());
        examMemberExam.setWorkUnit(workUnit);


        ExamMember examMember = examMemberDao.get(memberId);

        //主参数信息实体没有年龄字段，不用set
        examMember.setName(name);
        examMember.setSex(sex);
        examMember.setIdCardNum(idCardNum);
        examMember.setBirthday(birthday);
        examMember.setIdCardAddress(address);
        examMember.setMobile(mobile);

        //同步两张表的数据
        examMemberExamDao.update(examMemberExam);
        examMemberDao.update(examMember);


        //更新收费表里的信息

        examPayService.updateExamPayT(idCardNum,examMemberExam.getExamNumber());

        return true;

    }
}
