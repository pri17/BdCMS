package com.bidanet.bdcms.service.examManage.impl;

import com.bidanet.bdcms.common.JsonParseTool;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examManage.ExamEcardDao;
import com.bidanet.bdcms.driver.cache.ECardEntity;
import com.bidanet.bdcms.entity.ExamEcard;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.ExamEcardResponse;
import com.bidanet.bdcms.vo.ExamEcardVo;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * cf
 */
@Service
public class ExamEcardServiceImpl extends BaseServiceImpl<ExamEcard> implements ExamEcardService {
    @Autowired
    private ExamEcardDao examEcardDao;
    @Autowired
    @Qualifier("localFileDrive")
    private LocalFileDrive localFileDrive;

    @Override
    protected Dao getDao() {
        return examEcardDao;
    }

    @Autowired
    private JedisPool jedisPool;

//    private static final String EXAM_ECARD_KEY = "exam_eCard_key";

    @Override
    public void addExamEcardT(ExamEcard examEcard) {
        examEcardDao.save(examEcard);
    }


    @Override
    public void getExamEcardList(ExamEcard examEcard, Page<ExamEcard> page, String memberName, String idCardNum, String beginTime, String endTime, Long categoryId,String beginIssueTime,String endIssueTime,String beginAuditTime,String endAuditTime) {
        List<ExamEcard> list = examEcardDao.getExamEcardList(examEcard,page.getPageCurrent(), page.getPageSize(),memberName,idCardNum, beginTime, endTime,categoryId, beginIssueTime, endIssueTime, beginAuditTime, endAuditTime);
//        long count = examEcardDao.countByExample(examEcard);
        page.setList(list);
        page.setTotal(Long.valueOf(list.size()));
    }

    /**
     * redis 存取数据
     * @param page
     * @param eCardNumber
     * @param examNumber
     * @param workUnit
     * @param areaId
     * @param isPrint
     * @param memberName
     * @param idCardNum

     * @param beginIssueTime
     * @param endIssueTime
     * @param beginAuditTime
     * @param endAuditTime
     */
    @Override
    public void getRedisExamEcardList(Page<ECardEntity> page,Long uid, String eCardNumber, String examNumber, String workUnit, String areaId, String isPrint, String memberName, String idCardNum, String beginTime, String endTime, String categoryId, String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime,String ecardAdd) {

        String EXAM_ECARD_KEY = uid+"exam_eCard_key";

        Jedis jedis = jedisPool.getResource();
        List<ExamEcard> list = new ArrayList<ExamEcard>();

        long count = 0;

        //如果feeadd为空 则是不叠加查询 每次查询后进行删除操作
        if (StringUtils.isEmpty(ecardAdd)) {

            list = examEcardDao.getEcardNoPageList( eCardNumber,  examNumber,  workUnit,  areaId,  isPrint, memberName,  idCardNum,  beginTime,  endTime,  categoryId, beginIssueTime, endIssueTime, beginAuditTime, endAuditTime);
            long countEcardAddNo = Long.valueOf(list.size());

            jedis.del(EXAM_ECARD_KEY);

            count = countEcardAddNo;


        } else if(Integer.valueOf(ecardAdd)==0) {

            list = examEcardDao.getEcardNoPageList( eCardNumber,  examNumber,  workUnit,  areaId,  isPrint, memberName,  idCardNum,  beginTime,  endTime,  categoryId, beginIssueTime, endIssueTime, beginAuditTime, endAuditTime);
            long countEcardAddNo = Long.valueOf(list.size());

            jedis.del(EXAM_ECARD_KEY);

            count = countEcardAddNo;

        }else if (Integer.valueOf(ecardAdd)==1){//选中了复选叠加框后进行数据的叠加
            System.out.println("intoFeeadd");

            System.out.println("page.getPageCurrent()="+page.getPageCurrent());

            System.out.println("page.getPageSize()="+page.getPageSize());

            //如果page.current 不是当前 或者 page.pagesize>=10时 为分页查询 不需要再去查询数据
            if (page.getPageCurrent()>1 || page.getPageSize()>10){

                long redisSize = jedis.llen(EXAM_ECARD_KEY);

                count = redisSize;

            }else{

                list = examEcardDao.getEcardNoPageList( eCardNumber,  examNumber,  workUnit,  areaId,  isPrint, memberName,  idCardNum,  beginTime,  endTime,  categoryId, beginIssueTime, endIssueTime, beginAuditTime, endAuditTime);
                long countEcardAddYes = Long.valueOf(list.size());

                long redisSize = jedis.llen(EXAM_ECARD_KEY);

                count = countEcardAddYes+redisSize;
            }

        }

//        List<ECardEntity> feeList = new ArrayList<ECardEntity>();

        System.out.println("List.size="+list.size());

        System.out.println("startTime="+new Date().getTime());

        Pipeline pip = jedis.pipelined();

        //循环处理数据
        for (ExamEcard examEcard : list) {

            ECardEntity eCardEntity = new ECardEntity();

            eCardEntity.setId(examEcard.getId());
//
            eCardEntity.seteCardNumber(examEcard.geteCardNumber());
//
            eCardEntity.setExamNumber(examEcard.getExamNumber());
//
            eCardEntity.setName(examEcard.getName());
//
            eCardEntity.setSexStr(examEcard.getSexStr());
//
            eCardEntity.setIdCardNum(examEcard.getIdCardNumber());
//
            eCardEntity.setAreaName(examEcard.getAreaName());
//
            eCardEntity.setWorkUnit(examEcard.getWorkUnit());
//
            eCardEntity.setCategoryName(examEcard.getCategoryName());
//
            eCardEntity.setExamTimeStr(examEcard.getExamTimeStr());
//
            eCardEntity.setAuditTimeStr(examEcard.getAuditTimeStr());
//
            eCardEntity.setIssueTimeString(examEcard.getIssueTimeString());
//
            eCardEntity.setExpTimeStr(examEcard.getExpTimeStr());

            eCardEntity.setMemberAge(examEcard.getMemberAge());

            eCardEntity.setQrCodeUrl(examEcard.getQrCodeUrl());

            eCardEntity.setIconUrl(examEcard.getMemberPhoto());



            String jsonStr = JsonParseTool.toJson(eCardEntity);

//            jedis.lpush(EXAM_ECARD_KEY, jsonStr);

            pip.lpush(EXAM_ECARD_KEY, jsonStr);
        }

        pip.sync();

        System.out.println("middleTime="+new Date().getTime());


        List<ECardEntity> resultList = new ArrayList<ECardEntity>();

        List<String> redisist = jedis.lrange(EXAM_ECARD_KEY, (page.getPageCurrent() - 1) * page.getPageSize(), page.getPageSize()-1+(page.getPageCurrent()-1)*page.getPageSize());

        for (int i = 0; i < redisist.size(); i++) {

            //String 转 entity
            ECardEntity entity = JsonParseTool.parseObject(redisist.get(i), ECardEntity.class, "数据类型错误");
            resultList.add(entity);

        }

        System.out.println("endTime="+new Date().getTime());
        System.out.println("page.count="+count);

        page.setList(resultList);
        page.setTotal(count);


    }

    //健康证数据导出
    public HSSFWorkbook downOrderExcel(ExamEcard examEcard, String workUnit, String examNumber, String areaId, String eCardNumber, String isRechekPrint, String memberName, String idCardNum, String beginExamTime, String endExamTime, Long categoryId, String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime){
        List<ExamEcard> examEcardList = examEcardDao.getExamEcardExportList(examEcard,workUnit,examNumber,areaId,eCardNumber,isRechekPrint,memberName,idCardNum, beginExamTime, endExamTime,categoryId, beginIssueTime, endIssueTime, beginAuditTime, endAuditTime);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("健康证列表数据清单");
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
        cell.setCellValue("健康证号");
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
        cell.setCellValue("审核日期");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("发证日期");
        cell.setCellStyle(headstyle);
        cell = row.createCell(cindex++);
        cell.setCellValue("有效日期");
        cell.setCellStyle(headstyle);

        if (examEcardList != null && examEcardList.size() != 0) {
            int rowno = 1;
            for (int i = 0; i < examEcardList.size(); i++) {
                ExamEcard ecard = examEcardList.get(i);
                Row rowx = sheet.createRow(rowno);
                Cell cell0 = rowx.createCell(0);
                cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell0.setCellValue(ecard.geteCardNumber());

                Cell cell1 = rowx.createCell(1);
                cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(ecard.getExamNumber());

                Cell cell2 = rowx.createCell(2);
                cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (ecard.getName()!="")
                    cell2.setCellValue(ecard.getName());
                else
                    cell2.setCellValue("");

                Cell cell3 = rowx.createCell(3);
                cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (ecard.getSex()==1)
                    cell3.setCellValue("男");
                else
                    cell3.setCellValue("女");

                Cell cell4 = rowx.createCell(4);
                cell4.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (ecard.getIdCardNumber()!="")
                    cell4.setCellValue(ecard.getIdCardNumber());
                else
                    cell4.setCellValue("");

                Cell cell5 = rowx.createCell(5);
                cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (ecard.getAreaName()!="")
                    cell5.setCellValue(ecard.getAreaName());
                else
                    cell5.setCellValue("");

                Cell cell6 = rowx.createCell(6);
                cell6.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (ecard.getWorkUnit()!="")
                    cell6.setCellValue(ecard.getWorkUnit());
                else
                    cell6.setCellValue("");

                Cell cell7 = rowx.createCell(7);
                cell7.setCellType(XSSFCell.CELL_TYPE_STRING);
                if (ecard.getCategoryName()!="")
                    cell7.setCellValue(ecard.getCategoryName());
                else
                    cell7.setCellValue("");

                Cell cell8 = rowx.createCell(8);
                cell8.setCellType(XSSFCell.CELL_TYPE_STRING);
                //体检
                if (ecard.getExamTimeStr()!="")
                    cell8.setCellValue(ecard.getExamTimeStr());
                else
                    cell8.setCellValue("");

                Cell cell9 = rowx.createCell(9);
                cell9.setCellType(XSSFCell.CELL_TYPE_STRING);
                //审核
                if (ecard.getAuditTimeStr() != "")
                    cell9.setCellValue(ecard.getAuditTimeStr());
                else
                    cell9.setCellValue("");

                Cell cell10 = rowx.createCell(10);
                cell10.setCellType(XSSFCell.CELL_TYPE_STRING);
                //发证
                if (ecard.getIssueTimeStr() != "")
                    cell10.setCellValue(ecard.getIssueTimeStr());
                else
                    cell10.setCellValue("");

                Cell cell11 = rowx.createCell(11);
                cell11.setCellType(XSSFCell.CELL_TYPE_STRING);
                //有效
                if (ecard.getExpTimeStr() != "")
                    cell11.setCellValue(ecard.getExpTimeStr());
                else
                    cell11.setCellValue("");

                rowno++;
                rowx = null;
            }
        }
        return wb;
    }

    public List<ExamEcard> getExamEcardByMemberId(Long memberId) {
        return examEcardDao.getExamEcardByMemberId(memberId);
    }

//    //是否已经上传
//    @Override
//    public List<ExamEcard> getExamEcardByUpload() {
//
//        return null;
//    }

    /**
     * 批量修改上传状态
     * @param examEcards
     */
    @Override
    public void updateIsUploadT(List<ExamEcard> examEcards) {
        for(ExamEcard examEcard :examEcards){
            examEcard.setIsUpload(1);
            examEcardDao.update(examEcard);
        }
    }

    /**
     * 获取未上传的
     * @return
     */
    @Override
    public List<ExamEcardVo> getExamEcardByUpload() {
        List<ExamEcard> examEcards =examEcardDao.getExamEcardByUpload();
        List<ExamEcardVo> examEcardVos=new ArrayList<ExamEcardVo>();
        for(ExamEcard examEcard:examEcards){
            examEcardVos.add(assignExamEcard(examEcard));
        }
        return examEcardVos;
    }
    public ExamEcardVo assignExamEcard(ExamEcard examEcard) {
        ExamEcardVo examEcardVo = new ExamEcardVo();
        if (examEcard != null) {
            examEcardVo.assign(examEcard);
        }
        return examEcardVo;
    }

    @Override
    public List<ExamEcard> getExamEcardsByUpload() {
        List<ExamEcard> examEcards =examEcardDao.getExamEcardByUpload();
        return examEcards;
    }

    @Override
    public List<ExamEcard> getUpdatePrintCrad(long mID, String headImage) {
        ExamEcard examEcard = new ExamEcard();
        examEcard.setMemberId(mID);
        String url = localFileDrive.uploadBase64(headImage, UUID.randomUUID().toString() + "icon.jpg");
        List<ExamEcard> examEcards = examEcardDao.findByExampleDESC(examEcard, "id");
        if (examEcards.size()>0){
            ExamEcard newEc = examEcards.get(0);
            newEc.setMemberPhoto(url);
            examEcardDao.update(newEc);
        }
        return null;
    }

    //    @Override
//    public ExamEcardResponse sendPost() {
//
//        return null;
//    }
}
