package com.bidanet.bdcms.controller.examManage;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.bean.TownshipReview;
import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.driver.cache.RecheckEntity;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.examBusiness.*;
import com.bidanet.bdcms.service.fee.ExamPayService;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.vo.Page;
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
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 复检管理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-02 14:57:50
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Controller("reviewManage")
@RequestMapping ("/admin/examManage/reviewManage")
public class ReviewManageController extends AdminBaseController {

    @Autowired
    private ExamReviewService examReviewService;

    @Autowired
    private ExamMemberExamService examMemberExamService;

    @Autowired
    private ExamBlodIntestinalService examBlodIntestinalService;
    @Autowired
    private ExamAgenciesService examAgenciesService;
    @Autowired
    private ExamMemberService examMemberService;

    @Autowired
    private ExamAffectedService examAffectedService;
    @Autowired
    private ExamPayService examPayService;

    private String tableId = "";

    @RequestMapping("/index")
    public void index(Model model, @ModelAttribute Page<ExamMemberExam> page,String tabid,String areaId,String workUnit, String memberName, String idCardNum, String beginTime, String endTime, String openCategoryLevelTwoRm,String isRechekPrint,String examNumber, Long isRmQuery,Long parentId) throws ParseException {
//        if (isRmQuery!=null) {
        User user = uc.getUser();

        if(areaId == null){
            areaId = user.getAreaId().toString();
        }

            examMemberExamService.findReviewMemberList( page,areaId,workUnit,memberName,  idCardNum,  beginTime,  endTime,  openCategoryLevelTwoRm,isRechekPrint,examNumber);

            ExamAgencies examAgencies = examAgenciesService.get(user.getAgenciesId());
        model.addAttribute("currentAreaId",areaId);
        model.addAttribute("currentAgenciesCode",user.getAgenciesCode());
            //通过登录人员判断体检机构
            model.addAttribute("agenciesCode",examAgencies.getAgenciesCode());
            model.addAttribute("memberName",memberName);
            model.addAttribute("idCardNum",idCardNum);
            model.addAttribute("areaId",areaId);
            model.addAttribute("examNumber",examNumber);
            model.addAttribute("beginTime",beginTime);
            model.addAttribute("endTime",endTime);
            model.addAttribute("openCategoryLevelTwoRm",openCategoryLevelTwoRm);
            model.addAttribute("workUnit",workUnit);
            model.addAttribute("isRechekPrint",isRechekPrint);
            model.addAttribute("parentId",parentId);

            tableId = tabid;
//        }
    }
    /**
     * 跳转疾控中心预览复检通知单
     */
    @RequestMapping("/reviewCredential")
    public void reviewCredential(Model model,Long id){
//        ExamMemberExam examMemberExam = examMemberExamService.get(id);
//        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
//        examBlodIntestinal.setMemberExamId(examMemberExam.getId());
//        List<ExamBlodIntestinal> examBlodIntestinals = examBlodIntestinalService.findByExample(examBlodIntestinal);
//        model.addAttribute("examMemberExam",examMemberExam);
//        model.addAttribute("examBlodIntestinals",examBlodIntestinals);

        ExamMemberExam examMemberExam = examMemberExamService.get(id);

        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();

        examBlodIntestinal.setMemberExamId(examMemberExam.getId());

        //体检不合格 需要重新体检 同时打印复检单
        List<ExamBlodIntestinal> blodIntestinalList = examBlodIntestinalService.findByExample(examBlodIntestinal);

        List<String> resultStringList = new ArrayList<>();

        String recheckProjectName = "";

        List<RecheckEntity> recheckEntityList = new ArrayList<>();

        for (ExamBlodIntestinal examBlodIntestinal1 : blodIntestinalList){

            String tempProjectName = examBlodIntestinal1.getProjectName();

            Long tempId = examBlodIntestinal1.getProjectId();

            //5、戊肝 6、肠道检查 7、谷丙转氨酶 8、甲肝 9、内科检查 10、胸部x线

            RecheckEntity recheckEntity = new RecheckEntity();

            if (tempId == 6){//肠道

                recheckEntity.setName(SystemContent.PROJECT_INTESTINAL);

            }else if (tempId == 9){//内科

                recheckEntity.setName(SystemContent.PROJECT_NK);

            }else if (tempId == 10){//胸部

                recheckEntity.setName(SystemContent.PROJECT_XXIAN);

            }else{//血检

                recheckEntity.setName(SystemContent.PROJECT_BLOD);

            }

            recheckEntityList.add(recheckEntity);
            String result="";
            ExamBlodIntestinal ebiM=new ExamBlodIntestinal();
            ebiM.setExamNumber(examBlodIntestinal1.getExamNumber());
            ebiM.setProjectId(examBlodIntestinal1.getProjectId());
            List<ExamBlodIntestinal> list=examBlodIntestinalService.query(ebiM);
            if(list.size()>1){
                result=list.get(list.size()-2).getExamResult();
            }

            recheckProjectName += tempProjectName +"("+result+")"+ ",";

//            recheckProjectName += tempProjectName + ",";

        }

        //返回复检结果
        model.addAttribute("resultStr",recheckProjectName.substring(0,recheckProjectName.length()-1));

        List<RecheckEntity> calculateRecheckEntityList = getSingleList(recheckEntityList);

        List<RecheckEntity> resultRecheckEntityList = new ArrayList<>();

        for (RecheckEntity recheckEntity : calculateRecheckEntityList){
            resultRecheckEntityList.add(recheckEntity);
        }


        if (calculateRecheckEntityList.size()<4){
            int size = 4-calculateRecheckEntityList.size();

            for(int i=0;i<size;i++){

                RecheckEntity recheckEntity = new RecheckEntity();
                recheckEntity.setName("");
                resultRecheckEntityList.add(recheckEntity);
            }
        }

        //拼接生成的属于
        String temp = recheckProjectName.substring(0,recheckProjectName.length()-1);
        String resultString = stitchingString(temp);


        int resultLenght = resultString.length() / SystemContent.PROJECT_NUMBER;//Project_number=30

        for(int j=0;j<(resultLenght+1);j++){

            String str= "";
            if (j==3){
                str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,resultString.length());
            }else{
                int length=(j+1)*SystemContent.PROJECT_NUMBER;
                if(((j+1)*SystemContent.PROJECT_NUMBER)>(resultString.length()-1)){
                    length=resultString.length()-1;
                }

                str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,length);
            }

            resultStringList.add(str);
        }

        model.addAttribute("resultString",resultString);//最终的复检单说明

        //循环返回条数
        model.addAttribute("resultList",resultRecheckEntityList);

        model.addAttribute("resultStrList",resultStringList);

        model.addAttribute("examMemberExam", examMemberExam);
    }

    /**
     * 拼接字符串
     * @param content
     * @return
     */
    public String stitchingString(String content){

        StringBuffer buffer = new StringBuffer();

        buffer.append(SystemContent.PROJECT_BEFORE_WORD);//您在预防性健康检查中发现
        buffer.append(content);
        buffer.append(SystemContent.PROJECT_AFTER_WORD);//不符合健康证明要求,请带好本人身份证及复检单来本中心接受复检,
                                                        // 如在接到本告知书10个工作日内未答复,视为无异议.

        return  buffer.toString();

    }

    /**
     * 去重
     * @param recheckEntityList
     * @return
     */
    public List<RecheckEntity> getSingleList(List<RecheckEntity> recheckEntityList){

//        List<RecheckEntity> list = new ArrayList<RecheckEntity>();

        for (int i = 0; i < recheckEntityList.size()-1; i++) {
            for (int j = recheckEntityList.size()-1; j > i; j--) {
                if (recheckEntityList.get(j).getName().equals(recheckEntityList.get(i).getName())) {
                    recheckEntityList.remove(j);
                }
            }
        }

        return recheckEntityList;

    }


    /**
     * 跳转打印复检通知单
     */
    @RequestMapping("/printReviewCredential")
    public void printReviewCredential(Model model,String ids){
        //ids = ids.substring(0,ids.length()-1);

        List<ExamMemberExam> examReviewList = examMemberExamService.getExamReviews(ids);
        model.addAttribute("examReviewList",examReviewList);
    }

    /**
     * 跳转乡镇不合格告知书
     */
    @RequestMapping("/unqualifiedNoticeRm")
    public void unqualifiedNoticeRm(Model model,Long id){
        ExamMemberExam examMemberExam = examMemberExamService.get(id);
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();
        examBlodIntestinal.setMemberExamId(examMemberExam.getId());
        List<ExamBlodIntestinal> examBlodIntestinals = examBlodIntestinalService.findByExample(examBlodIntestinal);
        model.addAttribute("examMemberExam",examMemberExam);
        model.addAttribute("examBlodIntestinals",examBlodIntestinals);
    }

    /**
     * 跳转乡镇打印复检通知单
     */
    @RequestMapping("/printUnqualifiedNoticeRm")
    public void printUnqualifiedNoticeRm(Model model,String ids){
        ids = ids.substring(0,ids.length()-1);

        List<ExamMemberExam> examReviewList = examMemberExamService.getExamReviews(ids);
        model.addAttribute("examReviewList",examReviewList);
    }

    ///////////////////////////////////////////导出数据/////////////////////////////
    @RequestMapping(value="/exportReviewExcel")
    @ResponseBody
    public FileOutput exportReviewExcel(ExamMemberExam query,String areaId,String workUnit,String memberName, String idCardNum,
                                        String beginTime, String endTime, String openCategoryLevelTwoRmId ,String isRechekPrint, String examNumber) throws IOException,  ParseException {
//        HSSFWorkbook hw = examReviewService.exportReviewExcel(query,pageCurrent,pageSize,memberName,idCardNum,beginTime,endTime,categoryId);
        HSSFWorkbook hw = examMemberExamService.exportReviewExcel(query,areaId,URLDecoder.decode(workUnit,"UTF-8"), URLDecoder.decode(memberName,"UTF-8"),
                idCardNum,beginTime,endTime,openCategoryLevelTwoRmId, isRechekPrint, examNumber);
        String path = SpringWebTool.getRealPath("/temp/");
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String  downloadTime= DateTool.timeToStrYmd(new Date().getTime());
        String filename = downloadTime + "-review.xls";
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


    //////////////////////////////////////复检确认//////////////////////////////////////////////
    /**
     * 跳转预览复检通知单
     */
    @RequestMapping("/toReview")
    public void toReview(Model model,Long memberExamId){
        //获取用户数据
        ExamMemberExam examMemberExam = examMemberExamService.get(memberExamId);

        List<ExamMemberExam> examMemberExamsList = examMemberExamService.findByExamNumber(examMemberExam.getExamNumber());

        if(examMemberExamsList.size()>1){
            //根据降序排序 获取第二条数据
            ExamMemberExam twoExamMemberExam = examMemberExamsList.get(1);

            examMemberExam.setUpExamTimeStr(twoExamMemberExam.getExamTimeStr());
        }


        //获取复检数据列表
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();

        examBlodIntestinal.setMemberExamId(examMemberExam.getId());
        examBlodIntestinal.setIsShowRefresh(1);
        List<ExamBlodIntestinal> examBIList = examBlodIntestinalService.findByExample(examBlodIntestinal);

        model.addAttribute("memberExam",examMemberExam);
        model.addAttribute("examBIList",examBIList);


    }

    @RequestMapping("/review")
    @ResponseBody
    public AjaxCallBack review(Long memberExamId){
        //将复检项目数据中的isshowrefresh置为0 同时复检记录isnew为0 进行显示判断

        //获取用户数据
        ExamMemberExam examMemberExam = examMemberExamService.get(memberExamId);

        examMemberExam.setIsShowRecheck(0);

        examMemberExamService.updateT(examMemberExam);

        //获取复检数据列表
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();

        examBlodIntestinal.setMemberExamId(examMemberExam.getId());
        examBlodIntestinal.setIsShowRefresh(1);
        List<ExamBlodIntestinal> examBIList = examBlodIntestinalService.findByExample(examBlodIntestinal);

        for(ExamBlodIntestinal examBlodIntestinal1 : examBIList){
            //将项目记录进行修改
            examBlodIntestinal1.setIsShowRefresh(0);

            examBlodIntestinalService.updateT(examBlodIntestinal1);
        }

        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;

    }


    /**
     * 打印乡镇复检单
     */
    @RequestMapping("/townshipReview")
    public void townshipReview(Model model,String ids){
        List<ExamMemberExam> examReviewList = examMemberExamService.getExamReviews(ids);



        List<TownshipReview> townshipReviewList = new ArrayList<>();


        TownshipReview townshipReview = null;
        ExamBlodIntestinal examBlodIntestinal = null;


        for (ExamMemberExam temp :examReviewList ){
            townshipReview = new TownshipReview();
            examBlodIntestinal = new ExamBlodIntestinal();
            Long id = temp.getId();
            examBlodIntestinal.setMemberExamId(id);

            //获得不合格的体检项目 list
            List<ExamBlodIntestinal> examBlodIntestinals = examBlodIntestinalService.findByExample(examBlodIntestinal);

            //然后将temp 和 examBlodIntestinals 组成 bean 后  添加到list<bean> 集合里面


            townshipReview .setExamMemberExam(temp);
            townshipReview.setExamBlodIntestinalList(examBlodIntestinals);
            townshipReviewList.add(townshipReview);
        }

        //最后存入model里面

        //获得打印日期显示在VM页面
        String printDate = DateTool.timeToStrYmdCN(System.currentTimeMillis());

        model.addAttribute("printDate",printDate);
       // model.addAttribute("examReviewList",examReviewList);
        model.addAttribute("townshipReviewList",townshipReviewList);
    }


    @RequestMapping("/jkReviewCredential")
    public void jkReviewCredential(Model model,String ids){

        ExamMemberExam examMemberExam = examMemberExamService.get(Long.valueOf(ids));

        ExamPay examPay=examPayService.get(examMemberExam.getPayId());
        examMemberExam.setPayMoney(examPay.getPayMoney());
//        examPay.setExamNumber(examMemberExam.getExamNumber());
//        List<ExamPay> listExamPay=examPayService.query(examPay);
//        if(listExamPay.size()>0){
//            examMemberExam.setPayMoney(listExamPay.get(0).getPayMoney());
//        }
        ExamBlodIntestinal examBlodIntestinal = new ExamBlodIntestinal();

        examBlodIntestinal.setMemberExamId(examMemberExam.getId());

        //体检不合格 需要重新体检 同时打印复检单
        List<ExamBlodIntestinal> blodIntestinalList = examBlodIntestinalService.findByExample(examBlodIntestinal);

        List<String> resultStringList = new ArrayList<>();

        String recheckProjectName = "";

        List<RecheckEntity> recheckEntityList = new ArrayList<>();

        for (ExamBlodIntestinal examBlodIntestinal1 : blodIntestinalList){

            String tempProjectName = examBlodIntestinal1.getProjectName();

            Long tempId = examBlodIntestinal1.getProjectId();

            //5、戊肝 6、肠道检查 7、谷丙转氨酶 8、甲肝 9、内科检查 10、胸部x线

            RecheckEntity recheckEntity = new RecheckEntity();

            if (tempId == 6){//肠道

                recheckEntity.setName(SystemContent.PROJECT_INTESTINAL);

            }else if (tempId == 9){//内科

                recheckEntity.setName(SystemContent.PROJECT_NK);

            }else if (tempId == 10){//胸部

                recheckEntity.setName(SystemContent.PROJECT_XXIAN);

            }else{//血检

                recheckEntity.setName(SystemContent.PROJECT_BLOD);

            }

            recheckEntityList.add(recheckEntity);
            String result="";
            ExamBlodIntestinal ebiM=new ExamBlodIntestinal();
            ebiM.setExamNumber(examBlodIntestinal1.getExamNumber());
            ebiM.setProjectId(examBlodIntestinal1.getProjectId());
            List<ExamBlodIntestinal> list=examBlodIntestinalService.query(ebiM);
            if(list.size()>1){
                result=list.get(list.size()-2).getExamResult();
            }

            recheckProjectName += tempProjectName +"("+result+")"+ ",";

        }

        //返回复检结果
       // model.addAttribute("resultStr",recheckProjectName.substring(0,recheckProjectName.length()-1));

        List<RecheckEntity> calculateRecheckEntityList = getSingleList(recheckEntityList);

        List<RecheckEntity> resultRecheckEntityList = new ArrayList<>();

        for (RecheckEntity recheckEntity : calculateRecheckEntityList){
            resultRecheckEntityList.add(recheckEntity);
        }


        if (calculateRecheckEntityList.size()<4){
            int size = 4-calculateRecheckEntityList.size();

            for(int i=0;i<size;i++){

                RecheckEntity recheckEntity = new RecheckEntity();
                recheckEntity.setName("");
                resultRecheckEntityList.add(recheckEntity);
            }
        }

        //拼接生成的属于
        String temp = recheckProjectName.substring(0,recheckProjectName.length()-1);
        String resultString = stitchingString(temp);

//        String resultString = stitchingString(recheckProjectName);//最终的复检单说明

        int resultLenght = resultString.length() / SystemContent.PROJECT_NUMBER;//Project_number=30

        for(int j=0;j<(resultLenght+1);j++){

            String str= "";
            if (j==3){
                str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,resultString.length());
            }else{
                int length=(j+1)*SystemContent.PROJECT_NUMBER;
                if(((j+1)*SystemContent.PROJECT_NUMBER)>(resultString.length()-1)){
                    length=resultString.length()-1;
                }

                str =  resultString.substring(j*SystemContent.PROJECT_NUMBER,length);
            }

            resultStringList.add(str);
        }


        //循环返回条数
        model.addAttribute("resultList",resultRecheckEntityList);

        model.addAttribute("resultStrList",resultStringList);

        model.addAttribute("examMemberExam", examMemberExam);

        //wanglu

        model.addAttribute("resultString",resultString);//最终的复检单说明

        //gcx  疫区标识
        String idCardHead5=examMemberService.get(examMemberExam.getMemberId()).getIdCardNum().substring(0,6);
        ExamAffected ea=new ExamAffected();
        ea.setAffectedCode(idCardHead5);
        Boolean tag=false;
        if(examAffectedService.query(ea).size()>0){
            tag=true;
        }
        model.addAttribute("tag",tag);


    }


}
