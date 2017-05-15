package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.*;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.examBusiness.ExamAffectedService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
* cf
*/
@Service
public class ExamAffectedServiceImpl extends BaseServiceImpl<ExamAffected> implements ExamAffectedService {
    @Autowired
    private ExamAffectedDao affectedDao;
    @Override
    protected Dao getDao() {
        return affectedDao;
    }
    @Autowired
    private ExamAreaCodeDao areaCodeDao;

    @Autowired
    private ExamMemberDao examMemberDao;

    @Autowired
    private ExamMemberExamDao examMemberExamDao;
    @Autowired
    private  ExamAreaCodeDao examAreaCodeDao;
    @Autowired
    private ExamBlodDao examBlodDao;
    /**
     * 添加疫区
     * @param affectedCode
     */
    @Override
    public void setAffectedT(String affectedCode) {
        checkString(affectedCode,"请输入疫区代码");
//        ExamAreaCode areaCode = new ExamAreaCode();
//        areaCode.setId(Long.valueOf(affectedCode));
        ExamAreaCode areaCodes = areaCodeDao.get(Long.valueOf(affectedCode));
        if(areaCodes!=null){
            ExamAffected examAffected = new ExamAffected();
            examAffected.setAffectedCode(affectedCode);
            examAffected.setAffectedName(areaCodes.getName());
            affectedDao.save(examAffected);
        }else{
            errorMsg("请输入存在的疫区代码！");
        }
    }

    /**
     * 删除疫区
     * @param id
     */
    @Override
    public void deleteAffectedT(Long id) {
        affectedDao.delete(id);
    }

    /**
     * 查询所有数据
     * @return
     */
    @Override
    public void findAllAffectedList(ExamAffected query,Page<ExamAffected> page) {

        List<ExamAffected> examAffectedList = affectedDao.findByExample(query,page.getPageCurrent(),page.getPageSize());
        long count = affectedDao.countByExample(query);

        //对数据进行拼接处理
        for (ExamAffected examAffected : examAffectedList){

            String affectedCode = examAffected.getAffectedCode();

            ExamAreaCode examAreaCode = areaCodeDao.get(Long.parseLong(affectedCode));

            ExamAreaCode examAreaParentTwo = areaCodeDao.get(examAreaCode.getPid());

            ExamAreaCode examAreaParentOne = areaCodeDao.get(examAreaParentTwo.getPid());

            String affectedName = examAreaParentOne.getName()+examAreaParentTwo.getName()+examAreaCode.getName();

            examAffected.setAffectedName(affectedName);

        }


        page.setList(examAffectedList);
        page.setTotal(count);


    }

    @Override
    public HSSFWorkbook downAffectedExcel(String beginTime, String endTime, String areaId) throws ParseException {


        List<ExamAffected> examAffecteds = affectedDao.findAll();
        List<ExamMember> examMembers = examMemberDao.findAll();
        List<ExamMember> examMemberList = new ArrayList<ExamMember>();
        List<String> memberIds = new ArrayList<String>();
        if(examAffecteds!=null&&examAffecteds.size()>0){
            for(ExamAffected ea:examAffecteds){
                if(examMembers!=null&&examMembers.size()>0){
                    for(ExamMember em:examMembers){
                        if(em.getIdCardNum().substring(0,6).equals(ea.getAffectedCode())){
                            examMemberList.add(em);
                            memberIds.add(String.valueOf(em.getId()));
                        }
                    }

                }
            }
        }

        String memIds = listToString(memberIds);

        List<ExamMemberExam> list = examMemberExamDao.findExportAffectedExamMember(memIds,beginTime,endTime,areaId);
//        Long count = examMemberExamDao.countAffectedExamMember(memIds,beginTime,endTime,areaId);
//        page.setList(examMemberExams);
//        page.setTotal(count);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("疫区表单");
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
        cell.setCellValue("采样号");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("体检号");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("姓名");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("性别");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("出生日期");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("代码所对应的地区");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("就业单位");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("体检日期");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("IHA");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("DDIA");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("COPT");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("粪检");
        cell.setCellStyle(headstyle);

        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamMemberExam memberExam = list.get(i);
                Row rowx = sheet.createRow(rowno);


                //获取该用户的疫区code对应的具体地址
                String affectedCode = memberExam.getIdCardNum().substring(0, 6);

                ExamAreaCode examAreaCode = examAreaCodeDao.get(Long.parseLong(affectedCode));

                ExamAreaCode examAreaParentTwo = examAreaCodeDao.get(examAreaCode.getPid());

                ExamAreaCode examAreaParentOne = examAreaCodeDao.get(examAreaParentTwo.getPid());

                String affectedName = examAreaParentOne.getName() + examAreaParentTwo.getName() + examAreaCode.getName();

                memberExam.setAffectedCodeName(affectedName);


                //获取该用户的血检流水号
                List<ExamBlod> examBlodList = new ArrayList<>();
                ExamBlod examBlod = new ExamBlod();
                examBlod.setMemberExamId(memberExam.getId());

                examBlodList = examBlodDao.findByExample(examBlod);

                if (examBlodList.size() > 0) {

                    examBlod = examBlodList.get(0);
                    memberExam.setBlodTestNumber(examBlod.getTestNumber());

                }


                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if(memberExam.getBlodTestNumber()!=null) {
                    cell0.setCellValue(memberExam.getBlodTestNumber());
                }else {
                    cell0.setCellValue("");
                }


                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getExamNumber() != null) {
                    if (StringUtils.isNotBlank(memberExam.getExamNumber())) {
                        cell1.setCellValue(memberExam.getExamNumber());
                    } else {
                        cell1.setCellValue("");
                    }

                }


                Cell cell2= rowx.createCell(2);
                cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getName() != "")
                    cell2.setCellValue(memberExam.getName());
                else
                    cell2.setCellValue("");


                Cell cell3 = rowx.createCell(3);
                cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getSex() == 1)
                    cell3.setCellValue("男");
                else
                    cell3.setCellValue("女");


                Cell cell4 = rowx.createCell(4);
                cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell4.setCellValue(memberExam.getBirthday());

                Cell cell5 = rowx.createCell(5);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getWorkUnit() != "")
                    cell5.setCellValue(memberExam.getWorkUnit());
                else
                    cell5.setCellValue("");

                Cell cell6 = rowx.createCell(6);
                cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getAffectedCodeName() != "")
                    cell6.setCellValue(memberExam.getAffectedCodeName());
                else
                    cell6.setCellValue("");

                Cell cell7 = rowx.createCell(7);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getExamTimeStr() != "")
                    cell7.setCellValue(memberExam.getExamTimeStr());
                else
                    cell7.setCellValue("");


                Cell cell8 = rowx.createCell(8);
                cell8.setCellType(XSSFCell.CELL_TYPE_STRING);

                if (StringUtils.isNotEmpty(memberExam.getIHA())){
                    cell8.setCellValue(memberExam.getIHA());
                } else {
                    cell8.setCellValue("");
                }

                Cell cell9 = rowx.createCell(9);
                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotEmpty(memberExam.getDDIA())){
                    cell9.setCellValue(memberExam.getDDIA());
                } else {
                    cell9.setCellValue("");
                }

                Cell cell10 = rowx.createCell(10);
                cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotEmpty(memberExam.getCOPT())){
                    cell10.setCellValue(memberExam.getCOPT());
                } else {
                    cell10.setCellValue("");
                }

                Cell cell11 = rowx.createCell(11);
                cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (StringUtils.isNotEmpty(memberExam.getSTOOL())){
                    cell11.setCellValue(memberExam.getSTOOL());
                } else {
                    cell11.setCellValue("");
                }

                rowno++;
                rowx = null;
            }
        }
        return wb;
    }


    /**
     * 取拼接后的id字符串
     * @param list
     * @return
     */
    public static String listToString(List<String> list){
        if (list==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : list) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
