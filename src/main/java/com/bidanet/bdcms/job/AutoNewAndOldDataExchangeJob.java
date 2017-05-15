package com.bidanet.bdcms.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.PhysicalItemDetails;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SqlServerTool;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.ext.file.FileDrive;
import com.bidanet.bdcms.service.NewAndOldDataExchangeService;
import com.bidanet.bdcms.service.UserService;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.service.examBusiness.ExamAreaService;
import com.bidanet.bdcms.service.examBusiness.ExamBlodIntestinalService;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoader;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xuejike-pc on 2017/2/20.
 */
public class AutoNewAndOldDataExchangeJob extends QuartzJobBean {
    private SqlServerTool sqlServerTool = new SqlServerTool();
    private Connection connection = null;
    private Statement stmt = null;
    private Statement stmt1 = null;
    private Statement stmt2 = null;
    private ResultSet rs = null;
    private ResultSet rs1 = null;
    private ResultSet rs2 = null;

    private Map<String, Long> categoryMap = new HashMap<String, Long>() {
        {
            put("40", 71L);//食品生产经营-食品生产经营
            put("50", 7L);//公共场所-公共场所
            put("60", 85L);//药品及化妆品生产-医药
            put("70", 86L);//药品批发零售-药品销售
            put("80", 93L);//生活饮用水-生活用水
            put("90", 87L);//医疗及消毒产品生产-药品生产经营
            put("98", 94L);//其它-其它
        }
    };

    private NewAndOldDataExchangeService newAndOldDataExchangeService = SpringWebTool.getBean(NewAndOldDataExchangeService.class);
    private ExamBlodIntestinalService examBlodIntestinalService = SpringWebTool.getBean(ExamBlodIntestinalService.class);
    private ExamPackageService examPackageService = SpringWebTool.getBean(ExamPackageService.class);
    private ExamAreaService examAreaService = SpringWebTool.getBean(ExamAreaService.class);
    private ExamMemberExamService examMemberExamService = SpringWebTool.getBean(ExamMemberExamService.class);
    private UserService userService = SpringWebTool.getBean(UserService.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        connection = sqlServerTool.getConnection();
        if(connection!=null) {
            //旧数据库体检信息->新数据库
            addExamMemberExamAndExamMemberFromOldToNew();

            //旧数据库->新数据库，体检结果未同步完全，再次同步
            NewAndOldDataExchange nadoeOTN = new NewAndOldDataExchange();
            nadoeOTN.setOldToNewTage(false);
            List<NewAndOldDataExchange> nAODEOTNList = newAndOldDataExchangeService.query(nadoeOTN);
            for (int i = 0; i < nAODEOTNList.size(); i++) {

                Boolean gB = false, wB = false, bB = false, nB = false, xB = false;
                String sql = "select 体检号 from 从业人员体检信息 where 编号 = " + nAODEOTNList.get(i).getOldId() + " order by 编号 DESC ";
                String examNumberId = getItem(sql).toString();

                //血检查
                //甲肝
                String sqlJ = "select * from 体检项目明细 where 体检项目编号 = " + 84 + " and 体检号 = " + "'" + examNumberId + "'" + " order by 编号 DESC ";//+" order by 编号 DESC "
                PhysicalItemDetails pidJ = getPhysicalItemDetailsFromOld(sqlJ);
                if (pidJ != null && "1".equals(pidJ.getQualifiedMark())) {
                    gB = examBlodIntestinalService.updateExamBlodIntestinalT(8,
                            nAODEOTNList.get(i).getNewId(), pidJ);
                } else if (pidJ == null) {
                    gB = true;
                }

                //戊肝
                String sqlW = "select * from 体检项目明细 where 体检项目编号 = " + 105 + " and 体检号 = " + "'" + examNumberId + "'" + " order by 编号 DESC ";//+" order by 编号 DESC "
                PhysicalItemDetails pidW = getPhysicalItemDetailsFromOld(sqlW);
                if (pidW != null && "1".equals(pidW.getQualifiedMark())) {
                    wB = examBlodIntestinalService.updateExamBlodIntestinalT(5, nAODEOTNList.get(i).getNewId(),
                            pidW);
                } else if (pidW == null) {
                    wB = true;
                }

                //丙谷转氨酶
                String sqlB = "select * from 体检项目明细 where 体检项目编号 = " + 83 + " and 体检号 = " + "'" + examNumberId + "'" + " order by 编号 DESC ";//+" order by 编号 DESC "
                PhysicalItemDetails pidB = getPhysicalItemDetailsFromOld(sqlB);
                if (pidB != null && "1".equals(pidB.getQualifiedMark())) {
                    bB = examBlodIntestinalService.updateExamBlodIntestinalT(7, nAODEOTNList.get(i).getNewId(),
                            pidB);
                } else if (pidB == null) {
                    bB = true;
                }

                //内科
                String sqlN = "select * from 体检项目明细 where 体检项目编号 = " + 74 + " and 体检号 = " + "'" + examNumberId + "'" + " order by 编号 DESC ";//+" order by 编号 DESC "
                PhysicalItemDetails pidN = getPhysicalItemDetailsFromOld(sqlN);
                if (pidN != null && "1".equals(pidN.getQualifiedMark())) {
                    nB = examBlodIntestinalService.updateExamBlodIntestinalT(9, nAODEOTNList.get(i).getNewId(),
                            pidN);
                } else if (pidN == null) {
                    nB = true;
                }

                //X光
                String sqlX = "select * from 体检项目明细 where 体检项目编号 = " + 75 + " and 体检号 = " + "'" + examNumberId + "'" + " order by 编号 DESC ";// +" order by 编号 DESC "
                PhysicalItemDetails pidX = getPhysicalItemDetailsFromOld(sqlX);
                if (pidX != null && "1".equals(pidX.getQualifiedMark())) {
                    xB = examBlodIntestinalService.updateExamBlodIntestinalT(10, nAODEOTNList.get(i).getNewId(),
                            pidX);
                } else if (pidX == null) {
                    xB = true;
                }

                if (xB && nB && wB && bB && gB) {
                    NewAndOldDataExchange nAODEU = nAODEOTNList.get(i);
                    nAODEU.setOldToNewTage(true);
                    newAndOldDataExchangeService.updateT(nAODEU);
                }
            }

            //新数据库->旧数据库，体检结果未同步完全，再次同步
            NewAndOldDataExchange nadoeNTO = new NewAndOldDataExchange();
            nadoeNTO.setNewToOldTag(false);
            List<NewAndOldDataExchange> nAODENTOList = newAndOldDataExchangeService.query(nadoeNTO);
            //肠检
            for (int i = 0; i < nAODENTOList.size(); i++) {
                //肠道带菌检查
                exchangeIntestinalBacteriaExaminationResultToOld(nAODENTOList.get(i));
            }

       /* //同步体检最终数据结果到旧数据库
        exchangePhysicalResultFromNewToOld();*/


        /*当6个项目同步结束之后，更改体检信息中录入的状态*/
            NewAndOldDataExchange nadoeLR = new NewAndOldDataExchange();
            nadoeLR.setOldToNewTage(true);
            nadoeLR.setNewToOldTag(true);
            nadoeLR.setPhysicalResult(false);
            List<NewAndOldDataExchange> naodelr = newAndOldDataExchangeService.query(nadoeLR);
            for (int i = 0; i < naodelr.size(); i++) {
                NewAndOldDataExchange ex = naodelr.get(i);
                ExamMemberExam emeE = new ExamMemberExam();
                emeE.setExamNumber(ex.getPhysicalNumberOld());
                List<ExamMemberExam> emeList = examMemberExamService.query(emeE);
                if (emeList.size() > 0) {
                    ExamMemberExam eme = emeList.get(emeList.size() - 1);
                    if (eme.getEntryState() == 0) {
                        eme.setEntryState(1);
                        examMemberExamService.updateT(eme);
                    }
                    ex.setPhysicalResult(true);
                    newAndOldDataExchangeService.updateT(ex);
                }
            }

            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //同步旧数据库中的体检信息到新数据库中去
    public void addExamMemberExamAndExamMemberFromOldToNew() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 6);
        Timestamp beforeTime = new Timestamp(calendar.getTime().getTime());
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
       /* String sql = "select * from 从业人员体检信息 where 编号= 354808";*/
        String sql = "select * from 从业人员体检信息 where " + "'" + beforeTime + "'" + " < = 体检日期 and " + "'" + currentDate + "'" + " >=体检日期";
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Long oldId = rs.getLong("编号");
                NewAndOldDataExchange nodeTOldId = new NewAndOldDataExchange();
                nodeTOldId.setPhysicalNumberOld(rs.getString("体检号"));
                if (newAndOldDataExchangeService.query(nodeTOldId).size() <= 0) {
                    String name = "", address = "", idCardNum = "", mobile = "", workUnit = "", memberIcon = "";
                    Integer isCameraPhoto;
                    Long packageId, categoryId, areaId = null, agenciesId = 2L;
                    BigDecimal packageMoney = null;
                    User user = null;


                        sql = "select * FROM  人员基本信息 where 编号= " + rs.getInt("人员编号");
                        stmt1 = connection.createStatement();
                        rs1 = stmt1.executeQuery(sql);
                        if (rs1.next()) {
                            name = rs1.getString("姓名");
                            address = rs1.getString("联系地址");
                            idCardNum = rs1.getString("身份证号");
                            mobile = rs1.getString("联系电话");
                        }
                        Map<String, String> map = getInformByIdNumber(idCardNum);
                        Integer id = rs.getInt("工作单位编号");
                        workUnit = getItem("select 名称 from 客户基本信息 where 编号 = " + id).toString();
                        ExamArea ea = new ExamArea();
                        ea.setAreaCode(Integer.parseInt(rs.getString("体检号").substring(2, 5)));

                        List<ExamArea> examAreaList = examAreaService.query(ea);
                        if (examAreaList.size() > 0) {
                            areaId = examAreaList.get(0).getId();
                        }

                        packageId = examPackageService.getList().get(0).getId();
                        packageMoney = examPackageService.getList().get(0).getMoney();
                        categoryId = categoryMap.get(rs.getString("行业分类"));
                        if (categoryId == null) {
                            categoryId = categoryMap.get("98");
                        }
                        isCameraPhoto = 1;
                        User u = new User();
                        u.setAreaId(areaId);
                        if (userService.query(u).size() > 0) {
                            user = userService.query(u).get(0);
                        } else {
                            user = userService.get(1L);
                        }

                        //String iconOld="http://192.168.3.5:8090/"+oldId.toString().substring(0,2)+"/"+oldId+".jpg";
                      /*  String iconOld = "http://192.168.3.5:8090/26/260015.jpg";
                        String  url="http:localhost:8080/admin/file/getPhotoFromOld.do?" +
                                "ip=http://192.168.3.5:8090/&num="+oldId.toString().substring(0,2)+"&imageName="+oldId+".jpg";*/

                    memberIcon= getStringByCallInterface("http://"+SqlServerTool.localhostIpAndPost+"/admin/public/getPhotoFromOld.do",SqlServerTool.serverOldIpAddressAndPost,
                            oldId.toString().substring(0,2),oldId+".jpg");
                    /*memberIcon= getStringByCallInterface("http://192.168.1.103:8086/admin/public/getPhotoFromOld.do","192.168.3.5:8090",
                            "26","260015.jpg");
*/
                    NewAndOldDataExchange node = new NewAndOldDataExchange();
                   /* node.setOldId(oldId);*/
                    node.setPhysicalNumberOld(rs.getString("体检号"));
                    if (newAndOldDataExchangeService.query(node).size() <= 0) {
                        newAndOldDataExchangeService.insertDataT(name,
                                Integer.parseInt(map.get("sex")),
                                Integer.parseInt(map.get("age")),
                                map.get("birthday"),
                                address,
                                idCardNum, mobile, workUnit, areaId,
                                packageId, packageMoney, user, memberIcon, agenciesId,
                                categoryId, isCameraPhoto, oldId, rs.getString("体检号"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs1 != null)
                    rs1.close();
                if (stmt1 != null)
                    stmt1.close();
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //获取查询语句中查询的第一个信息
    private Object getItem(String sql) {
        Object result = "";
        try {
            stmt2 = connection.createStatement();
            rs2 = stmt2.executeQuery(sql);
            if (rs2.next()) {
                result = rs2.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs2 != null)
                    rs2.close();

                if (stmt2 != null)
                    stmt2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //执行插入和更新的SQL语句
    private Boolean exeInsertAndUpdateSql(String sql) {
        try {
            stmt2 = connection.createStatement();
            int result = stmt2.executeUpdate(sql);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt2 != null)
                    stmt2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //通过身份证获取出生日期、年龄、性别三个基本信息
    private Map<String, String> getInformByIdNumber(String IdNumber) {
        Map<String, String> map = new HashMap<String, String>();
        // 获取性别
        String id17 = IdNumber.substring(16, 17);
        if (Integer.parseInt(id17) % 2 != 0) {
            map.put("sex", "1");
        } else {
            map.put("sex", "0");
        }

        java.util.Date birthdate = new java.util.Date();
        // 获取出生日期
        String birthday = IdNumber.substring(6, 14);
        String year = IdNumber.substring(6, 10);
        String month = IdNumber.substring(10, 12);
        String day = IdNumber.substring(12, 14);
        map.put("birthday", year + "-" + month + "-" + day);

        GregorianCalendar currentDay = new GregorianCalendar();
        currentDay.setTime(birthdate);
        Integer years = currentDay.get(Calendar.YEAR);

        //获取年龄
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String yearTime = simpleDateFormat.format(new java.util.Date());
        map.put("age", (Integer.parseInt(yearTime) - years) + "");

        return map;
    }

    //获取旧数据库中体检项目明细的单条信息
    private PhysicalItemDetails getPhysicalItemDetailsFromOld(String sql) {
        PhysicalItemDetails physicalItemDetails = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                physicalItemDetails = new PhysicalItemDetails();
                physicalItemDetails.setId(Long.parseLong(rs.getInt("编号") + ""));
                physicalItemDetails.setMedicalNumber(rs.getString("体检号"));
                physicalItemDetails.setPhysicalItemNumber(rs.getString("体检项目编号"));
                physicalItemDetails.setPhysicalResult(rs.getString("体检结果"));
                physicalItemDetails.setQualifiedMark(rs.getString("合格标志"));
                physicalItemDetails.setJudgePeople(rs.getString("判断人"));
                physicalItemDetails.setJudgeDate(rs.getDate("判断日期"));
                physicalItemDetails.setRemarks(rs.getString("备注"));
                physicalItemDetails.setReSign(rs.getString("复检标志"));
                physicalItemDetails.setUpdateFlag(rs.getString("更新标志"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return physicalItemDetails;
    }

    //体检结果出来后，将新数据体检结果同步到旧数据库
    private void exchangePhysicalResultFromNewToOld() {
        NewAndOldDataExchange nadoeNTA = new NewAndOldDataExchange();
        nadoeNTA.setOldToNewTage(true);
        nadoeNTA.setOldToNewTage(true);
        nadoeNTA.setPhysicalResult(false);
        List<NewAndOldDataExchange> nadoeNTAList = newAndOldDataExchangeService.query(nadoeNTA);
        for (int i = 0; i < nadoeNTAList.size(); i++) {
            ExamMemberExam eme = examMemberExamService.get(nadoeNTAList.get(i).getNewId());
            if ((eme.getVerifyConclusion().equals(1)&&eme.getVerifyState().equals(1))||eme.getVerifyConclusion().equals(2)) {
                Integer verifyState=eme.getVerifyState();
                if(eme.getVerifyConclusion().equals(2)){
                    verifyState=0;
                }
                String sql = "update 从业人员体检信息 set"
                        + " 健康证号  = " + eme.geteCardNumber()
                        + ", 审核日期 = " + "'" + new Timestamp(eme.getVerifyTime()) + "'"
                        + ", 体检结论 = " + eme.getVerifyConclusion()
                        + ", 总检医生 = " + "'279'"
                        + ", 审核 = " + "'"+verifyState+"'"
                        + ",体检复检标志 = " + eme.getIsRecheck()
                        + " where 编号 = " + nadoeNTAList.get(i).getOldId();

                if (exeInsertAndUpdateSql(sql)) {
                    NewAndOldDataExchange node = nadoeNTAList.get(i);
                    node.setPhysicalResult(true);
                    newAndOldDataExchangeService.updateT(node);
                }
            }
        }
    }

    //从新数据库同步肠检中肠道带菌检查的结果数据到旧数据库
    private void exchangeIntestinalBacteriaExaminationResultToOld(NewAndOldDataExchange nAODE) {
        ExamBlodIntestinal eBIC = new ExamBlodIntestinal();
        eBIC.setMemberExamId(nAODE.getNewId());
        eBIC.setType(6);
        List<ExamBlodIntestinal> eBIList = examBlodIntestinalService.query(eBIC);
        if (eBIList.size() > 0) {
            ExamBlodIntestinal examBlodIntestinal = eBIList.get(0);
            if (examBlodIntestinal.getEntryState().equals(1) &&
                    (examBlodIntestinal.getIsQualified().equals(1)||
                            examBlodIntestinal.getIsQualified().equals(2))) {
                String sql = "select 编号 from 体检项目明细 where 体检号 = " + "'" + nAODE.getPhysicalNumberOld() + "'"
                        + " and 体检项目编号 = 4";//+" order by 编号 DESC "
                String numberOld = getItem(sql).toString();
                if (numberOld != null && (!"".equals(numberOld))) {
                    String sqlUp = "update 体检项目明细 set "
                            + " 合格标志 = " + examBlodIntestinal.getIsQualified()
                            + " ,判断日期 = " + "'" + new Timestamp(examBlodIntestinal.getJudgeTime()) + "'"
                           /* + " ,复检标志 = " + examBlodIntestinal.getIsShowRefresh()*/
                            + ", 更新标志 = " + examBlodIntestinal.getIsNew();
                    if (examBlodIntestinal.getExamResult() != null) {
                        sqlUp += " ,体检结果 = " + "'" + examBlodIntestinal.getExamResult() + "'";
                    }
                    if (examBlodIntestinal.getRemark() != null) {
                        sqlUp += " ,备注 = " + "'" + examBlodIntestinal.getRemark() + "'";
                    }

                    sqlUp += " where 编号 = " + Integer.parseInt(numberOld);
                    if (exeInsertAndUpdateSql(sqlUp)) {
                        NewAndOldDataExchange newAndOldDataExchange = nAODE;
                        newAndOldDataExchange.setNewToOldTag(true);
                        newAndOldDataExchangeService.updateT(newAndOldDataExchange);
                    }
                }else{
                        NewAndOldDataExchange newAndOldDataExchange = nAODE;
                        newAndOldDataExchange.setNewToOldTag(true);
                        newAndOldDataExchangeService.updateT(newAndOldDataExchange);
                }
            }
        }
    }

    private String getStringByCallInterface(String url,String ip,String num,String imageName){
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("accept", "application/json")
                    .field("ip",ip)
                    .field("num",num)
                    .field("imageName",imageName)
                    .asJson();
            JSONObject jsonObject = JSON.parseObject(jsonResponse.getBody().toString());
            return jsonObject.get("message").toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    //新数据库检查结果是复检，向旧数据插入体检附件记录
    private void insertReviewRecordNewToOld(String medicalNumber,Integer ItemNumber){
        String insertSql="insert into 体检项目明细 (体检号,体检项目编号,合格标志,备注,复检标志,更新标志) values( "
                +"'"+medicalNumber+"' ,"
                +ItemNumber+" ,"
                +"'"+0+"'"+" ,"
                +"复检"+" ,"
                +1+" ,"
                +0+
                ")";
        exeInsertAndUpdateSql(insertSql);
    }

    private void insertReviewRecordOldToNew(Long memberExamId,String examNumber,String projectName,Long projectId,Long memberId){
        ExamBlodIntestinal eBI=new ExamBlodIntestinal();
        eBI.setMemberExamId(memberExamId);
        eBI.setExamNumber(examNumber);
        eBI.setProjectName(projectName);
        eBI.setProjectId(projectId);
        eBI.setMemberExamId(memberExamId);
        examBlodIntestinalService.insertT(eBI);
    }




}
