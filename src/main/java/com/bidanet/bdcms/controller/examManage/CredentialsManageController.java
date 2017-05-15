package com.bidanet.bdcms.controller.examManage;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.common.DateTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.driver.cache.ECardEntity;
import com.bidanet.bdcms.entity.ExamEcard;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.vo.Page;
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
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

//import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 健康证管理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-11-02 15:10:16
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 张彬彬
 * @version 1.0.0
 */
@Controller("credentialsManage")
@RequestMapping ("/admin/examManage/credentialsManage")
public class CredentialsManageController extends AdminBaseController{

    @Autowired
    private ExamEcardService examEcardService;

    @RequestMapping("/index")
    public void index(Model model,
                      @ModelAttribute Page<ECardEntity> page,String eCardNumber,String examNumber,String workUnit,String areaId,String isPrint, String memberName, String idCardNum, String beginExamTime,
                      String endExamTime, String credentialsCategoryLevelTwo, String beginIssueTime, String endIssueTime, String beginAuditTime, String endAuditTime,String ecardAdd, Long isCmQuery){
//        if (isCmQuery!=null) {

        User user = uc.getUser();

        if(areaId == null){
            areaId = user.getAreaId().toString();
        }


            examEcardService.getRedisExamEcardList(page,user.getUid(),eCardNumber,examNumber,workUnit,areaId,isPrint,memberName,idCardNum,beginExamTime,endExamTime,credentialsCategoryLevelTwo,beginIssueTime,endIssueTime,beginAuditTime,endAuditTime,ecardAdd);

        model.addAttribute("currentAreaId",areaId);
        model.addAttribute("currentAgenciesCode",user.getAgenciesCode());
            model.addAttribute("memberName",memberName);
            model.addAttribute("workUnit",workUnit);
            model.addAttribute("areaId",areaId);
            model.addAttribute("examNumber",examNumber);
            model.addAttribute("eCardNumber",eCardNumber);
            model.addAttribute("idCardNum",idCardNum);
            model.addAttribute("isPrint",isPrint);
            model.addAttribute("beginExamTime",beginExamTime);
            model.addAttribute("endExamTime",endExamTime);
            model.addAttribute("beginIssueTime",beginIssueTime);
            model.addAttribute("endIssueTime",endIssueTime);
            model.addAttribute("beginAuditTime",beginAuditTime);
            model.addAttribute("endAuditTime",endAuditTime);
            model.addAttribute("ecardAdd",ecardAdd);
            model.addAttribute("credentialsCategoryLevelTwoId",credentialsCategoryLevelTwo);
//        }
    }


    //健康证数据导出
    @RequestMapping(value="/exportExcel")
    @ResponseBody
    public FileOutput exportExcel(ExamEcard examEcard,String workUnit,String examNumber,String areaId,String eCardNumber,String isRechekPrint,String memberName, String idCardNum,
                                  String beginExamTime, String endExamTime, Long credentialsCategoryLevelTwo , String beginIssueTime,
                                  String endIssueTime, String beginAuditTime, String endAuditTime) throws IOException{
        HSSFWorkbook hw = examEcardService.downOrderExcel( examEcard, URLDecoder.decode(workUnit,"UTF-8"),examNumber,areaId,eCardNumber,isRechekPrint,  URLDecoder.decode(memberName,"UTF-8"),  idCardNum,  beginExamTime,
                endExamTime,  credentialsCategoryLevelTwo ,  beginIssueTime,  endIssueTime,  beginAuditTime,  endAuditTime);
        String path= SpringWebTool.getRealPath("/temp/");
        File pathFile = new File(path);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        String  downloadTime= DateTool.timeToStrYmd(new Date().getTime());
        String filename=downloadTime+"-credentials.xls";
        File file = new File(path + "/" + filename);
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream stream = new FileOutputStream(file);

        hw.write(stream);
        stream.flush();
        stream.close();
        return new FileOutput(file,filename);
//        String filename = "订单列表数据清单";//设置下载时客户端Excel的名称
//
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment; filename="+toUtf8String(filename)+".xls");
//        OutputStream output = response.getOutputStream();
//        hw.write(output);
//        output.flush();
//        output.close();
////

//        FileOutputStream outputStream;
//
//        Date date = new  Date();
//        Long currentTime = date.getTime();
//
//        String name = currentTime+".xls";
//
//
//        String path =  "/Users/zangli/Documents/"+name;
//
//        try{
//            outputStream = new FileOutputStream(path);
//        OutputStream output = response.getOutputStream();
//        hw.write(output);
//        output.flush();
//        output.close();
//        }catch (FileNotFoundException e) {
//            System.err.println("获取不到位置");
//            e.printStackTrace();
//        } catch (IOException e) {
//         // TODO Auto-generated catch block
//          e.printStackTrace();
//         }


//         //通过fileOutput返回
//
//        FileOutput fileOutPut = new FileOutput(path,name);
//
//        return fileOutPut;



    }

    /**
     * 跳转预览健康证
     */
    @RequestMapping("/viewCredential")
    public void viewCredential(Model model,Long id){
        ExamEcard examEcard = examEcardService.get(id);
        model.addAttribute("examEcard",examEcard);
    }

    @RequestMapping("/findEcardByIds")
    @ResponseBody
    public Map<String,Object> findEcardByIds(String ids){

        List<ExamEcard> cardList = new ArrayList<ExamEcard>();

        String[] strArray = convertStrToArray(ids);

        for(int i=0;i<strArray.length;i++){
            //查找健康证相关数据 然后list.add
            Long id = Long.parseLong(strArray[i]);

            ExamEcard examEcard = examEcardService.get(id);

            cardList.add(examEcard);

        }

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("examECardList",cardList);

        return map;

    }


    public static String[] convertStrToArray(String str){
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }


    /**
     * 发证日期的修改
     * @param
     * @return
     */
    @RequestMapping("/updatePrint")
    @ResponseBody
    public AjaxCallBack updatePrint(Long bianhao){

        AjaxCallBack callBack = new AjaxCallBack();

        //更新打印标识，同时将发证日期修改成当前打印日期
        ExamEcard examEcard = new ExamEcard();

        examEcard = examEcardService.get(bianhao);

        examEcard.setIsPrint(1);
        examEcard.setIssueTime(new Date().getTime());

        examEcard = examEcardService.updateBack(examEcard);

        if (examEcard.getId()!=0){
            //成功
            callBack.success("数据更新成功");

        }else{
            //失败
            callBack.error("数据保存失败");
        }

        return callBack;

    }

}
