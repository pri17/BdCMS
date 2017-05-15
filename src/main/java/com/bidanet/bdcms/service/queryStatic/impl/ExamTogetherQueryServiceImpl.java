package com.bidanet.bdcms.service.queryStatic.impl;

import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.queryStatic.ExamTogetherQueryDao;
import com.bidanet.bdcms.driver.cache.FeeEntity;
import com.bidanet.bdcms.driver.cache.TogetherQueryEntity;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.queryStatic.ExamTogetherQueryService;
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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 查询统计-体检综合查询. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-09  20:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Service
public class ExamTogetherQueryServiceImpl extends BaseServiceImpl<ExamMemberExam> implements ExamTogetherQueryService {

    @Autowired
    private ExamTogetherQueryDao examTogetherQueryDao;

    protected Dao getDao() {
        return examTogetherQueryDao;
    }

//    private static final String EXAM_TOGETHER_QUERY_KEY = "together_query_key";

    @Autowired
    private JedisPool jedisPool;

    public void queryExamTogetherList(Page<TogetherQueryEntity> page,Long uid, String name,String eCardNumber, Long categoryId, String areaId, String idCard, String workUnit, String examNumber, String startTime, String endTime, String payState, String payType, String isRecheck,
                                      String isQualified,String sortByTime,String channel) throws ParseException {
//        List<ExamMemberExam> list = examTogetherQueryDao.queryTogetherList(name, areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified, page.getPageCurrent(), page.getPageSize());
//        long count = examTogetherQueryDao.queryCountTogetherList(name, areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified);
//        page.setList(list);
//        page.setTotal(count);

        String EXAM_TOGETHER_QUERY_KEY = uid+"together_query_key";

        Jedis jedis = jedisPool.getResource();
        List<ExamMemberExam> list = new ArrayList<ExamMemberExam>();

        long count = 0;

        //如果feeadd为空 则是不叠加查询 每次查询后进行删除操作
//        if (StringUtils.isEmpty(togetherQAdd)) {
//
//            list = examTogetherQueryDao.queryTogetherNoPageList(name, eCardNumber,categoryId, areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified,sortByTime,channel);
//            long countTogetherQAddNo = examTogetherQueryDao.queryCountTogetherList(name,eCardNumber, categoryId,areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified,sortByTime,channel);
//
//            jedis.del(EXAM_TOGETHER_QUERY_KEY);
//
//            count = countTogetherQAddNo;
//
//
//        } else if (Integer.valueOf(togetherQAdd) == 0) {
//
//            list = examTogetherQueryDao.queryTogetherNoPageList(name,eCardNumber, categoryId, areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified,sortByTime,channel);
//            long countTogetherQAddNo = examTogetherQueryDao.queryCountTogetherList(name, eCardNumber, categoryId,areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified,sortByTime,channel);
//
//            jedis.del(EXAM_TOGETHER_QUERY_KEY);
//
//            count = countTogetherQAddNo;
//
//        } else if (Integer.valueOf(togetherQAdd) == 1) {//选中了复选叠加框后进行数据的叠加
//
//
//            //如果page.current 不是当前 或者 page.pagesize>=10时 为分页查询 不需要再去查询数据
//            if (page.getPageCurrent() > 1 || page.getPageSize() > 10) {
//
//                long redisSize = jedis.llen(EXAM_TOGETHER_QUERY_KEY);
//
//                count = redisSize;
//
//            } else {
//
//                list = examTogetherQueryDao.queryTogetherNoPageList(name,eCardNumber, categoryId, areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified,sortByTime,channel);
//                long countTogetherQAddYes = examTogetherQueryDao.queryCountTogetherList(name,eCardNumber, categoryId,areaId, idCard, workUnit, examNumber, startTime, endTime, payState, payType, isRecheck, isQualified,sortByTime,channel);
//
//                long redisSize = jedis.llen(EXAM_TOGETHER_QUERY_KEY);
//
//                count = countTogetherQAddYes + redisSize;
//            }
//
//        }

        List<FeeEntity> feeList = new ArrayList<FeeEntity>();

        System.out.println("List.size=" + list.size());

        System.out.println("startTime="+new Date().getTime());

        Pipeline pip = jedis.pipelined();

        //循环处理数据
        for (ExamMemberExam examMemberExam : list) {


            TogetherQueryEntity queryEntity = new TogetherQueryEntity();

            queryEntity.setId(examMemberExam.getId()!=null ? examMemberExam.getId():null);

            queryEntity.setIsRecheck(examMemberExam.getIsRecheck());

            queryEntity.seteCardNumber(examMemberExam.geteCardNumber() != "" ? examMemberExam.geteCardNumber() : "");

            queryEntity.setName(examMemberExam.getName()!= null ? examMemberExam.getName() : "");

            queryEntity.setSexStr(examMemberExam.getSex()!= null ? examMemberExam.getSexStr() : "");

            queryEntity.setIdCardNum(examMemberExam.getIdCardNum()!= null ? examMemberExam.getIdCardNum() : "");

            queryEntity.setAreaName(examMemberExam.getAreaName()!= null ? examMemberExam.getAreaName() : "");

            queryEntity.setWorkUnit(examMemberExam.getWorkUnit() != null ? examMemberExam.getWorkUnit() : "");

            queryEntity.setExamNumber(examMemberExam.getExamNumber() != null ? examMemberExam.getExamNumber() : "");

            queryEntity.setExamTimeStr(examMemberExam.getExamTimeStr() != null ? examMemberExam.getExamTimeStr() : "");

            queryEntity.setExpTimeStr(examMemberExam.getExpTimeStr() != null ? examMemberExam.getExpTimeStr() : "");


            queryEntity.setPayTypeStr(examMemberExam.getPayType()!=null?examMemberExam.getPayTypeStr():"");

            queryEntity.setPayStateStr(examMemberExam.getPayState()!=null?(examMemberExam.getPayState() == 2 ? "已收费" : "未收费"):"");

            queryEntity.setCategoryStr(examMemberExam.getCategoryName()!=null?examMemberExam.getCategoryName():"");

            queryEntity.setIsReCheckStr(examMemberExam.getIsRecheck()!= null ? (examMemberExam.getIsRecheck() == 0 ? "否" :"是") :"");

            queryEntity.setRecheckTag(examMemberExam.getRecheckTag());

            queryEntity.setRecheckTagStr(examMemberExam.getRecheckTagStr());

            queryEntity.setChannelStr(examMemberExam.getChannelStr());

            queryEntity.setMobile(examMemberExam.getMobile());

            String jsonStr = JsonParseTool.toJson(queryEntity);

//            jedis.rpush(EXAM_TOGETHER_QUERY_KEY, jsonStr);
            pip.rpush(EXAM_TOGETHER_QUERY_KEY, jsonStr);


        }

        pip.sync();

        System.out.println("middleTime="+new Date().getTime());


        List<TogetherQueryEntity> resultList = new ArrayList<TogetherQueryEntity>();

//        List<String> redisist = jedis.lrange(EXAM_TOGETHER_QUERY_KEY, (page.getPageCurrent() - 1) * page.getPageSize(), page.getPageSize());
//                                                         $page_num-1)*$page_size                  (($page_num-1)*$page_size+$page_size-1))
        List<String> redisist = jedis.lrange(EXAM_TOGETHER_QUERY_KEY, (page.getPageCurrent() - 1) * page.getPageSize(), page.getPageSize()-1+(page.getPageCurrent()-1)*page.getPageSize());

        for (int i = 0; i < redisist.size(); i++) {

            //String 转 entity
            TogetherQueryEntity entity = JsonParseTool.parseObject(redisist.get(i), TogetherQueryEntity.class, "数据类型错误");
            resultList.add(entity);

        }

        System.out.println("endTime="+new Date().getTime());
        System.out.println("page.count=" + count);

        page.setList(resultList);
        page.setTotal(count);
    }

    public List<ExamMemberExam> getExamMemberExams(String ids) {
        return examTogetherQueryDao.queryTogetherList(ids);
    }

    //体检综合查询excel导出查询
    @Override
    public HSSFWorkbook exportTogetherExcel(int pageCurrent, int pageSize, String name, String areaId, String idCard, String workUnit, String examNumber,
                                            String startTime, String endTime, String payState, String payType, Long togetherCategoryLevelTwoId, String channel, String eCardNumber) throws ParseException {
        List<ExamMemberExam> list = examTogetherQueryDao.queryTogetherList(togetherCategoryLevelTwoId,channel,eCardNumber,name, areaId, idCard, workUnit, examNumber,
                                                               startTime, endTime, payState, payType, pageCurrent, pageSize);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("综合查询数据清单");
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
        cell.setCellValue("健康证号");
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

        /*cell = row.createCell(cindex++);
        cell.setCellValue("收费方式");
        cell.setCellStyle(headstyle);*/
        cell = row.createCell(cindex++);
        cell.setCellValue("是否复检");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("手机号");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("是否收费");
        cell.setCellStyle(headstyle);

        cell = row.createCell(cindex++);
        cell.setCellValue("来源");
        cell.setCellStyle(headstyle);

//        cell = row.createCell(cindex++);
//        cell.setCellValue("备注");
//        cell.setCellStyle(headstyle);

        if (list != null && list.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < list.size(); i++) {
                ExamMemberExam memberExam = list.get(i);
                Row rowx = sheet.createRow(rowno);
               /*体检号*/
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getExamNumber() != null) {
                    if (StringUtils.isNotBlank(memberExam.getExamNumber())) {
                        cell0.setCellValue(memberExam.getExamNumber());
                    } else {
                        cell0.setCellValue("");
                    }

                }

                /*健康证号*/
                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);

                    if (StringUtils.isNotBlank(memberExam.geteCardNumber())) {
                        cell1.setCellValue(memberExam.geteCardNumber());
                    } else {
                        cell1.setCellValue("");
                    }


                /*姓名*/
                Cell cell2 = rowx.createCell(2);
                cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getName() != "")
                    cell2.setCellValue(memberExam.getName());
                else
                    cell2.setCellValue("");
                /*性别*/
                Cell cell3 = rowx.createCell(3);
                cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getSex() == 1)
                    cell3.setCellValue("男");
                else
                    cell3.setCellValue("女");
                /*身份证号*/
                Cell cell4 = rowx.createCell(4);
                cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getIdCardNum() != "")
                    cell4.setCellValue(memberExam.getIdCardNum());
                else
                    cell4.setCellValue("");
                 /*地段区域*/
                Cell cell5 = rowx.createCell(5);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getAreaName() != "")
                    cell5.setCellValue(memberExam.getAreaName());
                else
                    cell5.setCellValue("");
                /*工作单位*/
                Cell cell6 = rowx.createCell(6);
                cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getWorkUnit() != "")
                    cell6.setCellValue(memberExam.getWorkUnit());
                else
                    cell6.setCellValue("");
                /*行业分类*/
                Cell cell7 = rowx.createCell(7);
                cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
                    if (memberExam.getCategoryName() != "")
                        cell7.setCellValue(memberExam.getCategoryName());
                    else
                        cell7.setCellValue("");

                /*体检日期*/
                Cell cell8 = rowx.createCell(8);
                cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getExamTimeStr() != "")
                    cell8.setCellValue(memberExam.getExamTimeStr());
                else
                    cell8.setCellValue("");

                /*是否复检*/
                Cell cell9 = rowx.createCell(9);
                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getRecheckTagStr() != null){
                        cell9.setCellValue(memberExam.getRecheckTagStr());

                }else{
                    cell9.setCellValue("");
                }



                /*手机号码*/
                Cell cell10 = rowx.createCell(10);
                cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getMobile() != "")
                    cell10.setCellValue(memberExam.getMobile());
                else
                    cell10.setCellValue("");

                /*是否收费*/
                Cell cell11 = rowx.createCell(11);
                cell11.setCellType(XSSFCell.CELL_TYPE_STRING);

//                if (memberExam.getExamPay() != null) {
                    if (StringUtils.isNotBlank(memberExam.getPayTypeStr())) {
                        cell11.setCellValue(memberExam.getPayTypeStr());
                    } else {
                        cell11.setCellValue("");
                    }
//                } else {
//                    cell9.setCellValue("");
//                }


                 /*来源*/
                Cell cell12 = rowx.createCell(12);
                cell12.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (memberExam.getChannelStr() != "")
                    cell12.setCellValue(memberExam.getChannelStr());
                else
                    cell12.setCellValue("");



                /*Cell cell10 = rowx.createCell(10);
                cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if (memberExam.getExamPay() != null) {
                    if (memberExam.getPayState() == 2) {
                        cell10.setCellValue("已收费");
                    } else if (memberExam.getPayState() == 1) {
                        cell10.setCellValue("未收费");
                    }*/
//                } else {
//                    cell10.setCellValue("");
//                }



//                Cell cell11 = rowx.createCell(11);
//                cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
//                if (memberExam.getExamPay() != null) {
//                    if (StringUtils.isNotBlank(memberExam.getExamPay().getRemarks()))
//                        cell11.setCellValue(memberExam.getExamPay().getRemarks());
//                    else
//                        cell11.setCellValue("");
//                } else {
//                    cell11.setCellValue("");
//                }

                rowno++;
                rowx = null;
            }
        }
        return wb;
    }

}
