package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamReviewDao;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamReview;

import com.bidanet.bdcms.service.examBusiness.ExamReviewService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.Page;
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

import java.util.List;


/**
* cf
*/
@Service
public class ExamReviewServiceImpl extends BaseServiceImpl<ExamReview> implements ExamReviewService {
    @Autowired
    private ExamReviewDao reviewDao;
    @Override
    protected Dao getDao() {
        return reviewDao;
    }

    public void getExamReviewList(ExamReview examReview, Page<ExamReview> page, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId) {
        List<ExamReview> list = reviewDao.getExamReviewList(examReview,page.getPageCurrent(), page.getPageSize(),memberName,idCardNum, beginTime, endTime,categoryId);
//        long count = examEcardDao.countByExample(examEcard);
        page.setList(list);
        page.setTotal(Long.valueOf(list.size()));
    }

    /**
     * 通过memberExamId删除相应的复检数据
     * @param memeberExamId
     */
    @Override
    public void deleteReviewByMEIdT(Long memeberExamId) {

        List<ExamReview> list = reviewDao.findReviewByMemberExamId(memeberExamId);

        for(ExamReview review:list){

            //删除相关数据
            reviewDao.delete(review.getId());

        }


    }
    public List<ExamReview> getExamReviews(String ids) {
        return reviewDao.queryExamReviewList(ids);
    }

    @Override
    public HSSFWorkbook exportReviewExcel(ExamReview query, int pageCurrent, int pageSize, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId) {
        List<ExamReview> list = reviewDao.getExamReviewList(query,pageCurrent, pageSize,memberName,idCardNum, beginTime, endTime,categoryId);
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
        cell = row.createCell(cindex++);
        cell.setCellValue("体检结果");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("是否打印");
        cell.setCellStyle(headstyle);

        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamReview review = list.get(i);
                Row rowx = sheet.createRow(rowno);
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell0.setCellValue(review.getExamNumber());


                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (review.getExamMember().getName()!="")
                    cell1.setCellValue(review.getExamMember().getName());
                else
                    cell1.setCellValue("");

                Cell cell2 = rowx.createCell(2);
                cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (review.getExamMember().getSex()==1)
                    cell2.setCellValue("男");
                else
                    cell2.setCellValue("女");

                Cell cell3 = rowx.createCell(3);
                cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (review.getExamMember().getIdCardNum()!="")
                    cell3.setCellValue(review.getExamMember().getIdCardNum());
                else
                    cell3.setCellValue("");

                Cell cell4 = rowx.createCell(4);
                cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (review.getExamArea().getAreaName()!="")
                    cell4.setCellValue(review.getExamArea().getAreaName());
                else
                    cell4.setCellValue("");

                Cell cell5 = rowx.createCell(5);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (review.getExamArea().getAreaName()!="")
                    cell5.setCellValue(review.getExamArea().getAreaName());
                else
                    cell5.setCellValue("");

                Cell cell6 = rowx.createCell(6);
                cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
                if(review.getExamCategory()!=null) {
                    if (review.getExamCategory().getCategoryName() != "")
                        cell6.setCellValue(review.getExamCategory().getCategoryName());
                    else
                        cell6.setCellValue("");
                }else{
                    cell6.setCellValue("");
                }

                Cell cell7 = rowx.createCell(7);
                cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (review.getExamTimeStr()!="")
                    cell7.setCellValue(review.getExamTimeStr());
                else
                    cell7.setCellValue("");

                Cell cell8 = rowx.createCell(8);
                cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (review.getVerifyResult() != "") {
                    cell8.setCellValue(review.getVerifyResult());
                }
                else{
                    cell8.setCellValue("");
                }

                Cell cell9 = rowx.createCell(9);
                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (review.getIsPrint() != 0) {
                    cell9.setCellValue("未打印");
                }
                else if (review.getIsPrint() != 1) {
                    cell9.setCellValue("打印");
                }

                rowno++;
                rowx = null;
            }
        }
        return wb;
    }
}
