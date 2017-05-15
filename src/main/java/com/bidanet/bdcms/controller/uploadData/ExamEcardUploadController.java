package com.bidanet.bdcms.controller.uploadData;


import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamEcard;
import com.bidanet.bdcms.service.examBusiness.ExamMemberExamService;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.service.fileUpload.NewLocalFile;
import com.bidanet.bdcms.service.fileUpload.ZipFile;
import com.bidanet.bdcms.service.unirest.UnirestFile;
import com.bidanet.bdcms.vo.ExamEcardVo;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by gao on 2017/2/20.
 */
@Controller
    @RequestMapping("/admin/examEcard")
public class ExamEcardUploadController extends AdminBaseController {
    @Autowired
    private ExamEcardService examEcardService;
//    @Autowired
//    private NewLocalFile newLocalFile;

//    @RequestMapping("/getAllExamEcard")
//    @ResponseBody
//    public List<ExamEcardVo> getAllExamEcard(){
//
//        return  examEcardService.getExamEcardByUpload();
//    }


    @RequestMapping("/uploadProvince")
    @ResponseBody
    public boolean printFile(){
        boolean flag=false;
        List<ExamEcard> examEcards=examEcardService.getExamEcardsByUpload();
        List<ExamEcardVo> examEcardVos=examEcardService.getExamEcardByUpload();
        Gson gson=new Gson();
        String content=gson.toJson(examEcardVos);

        try{
            content=new String(Base64.encodeBase64(content.getBytes("UTF-8")));
            NewLocalFile.doGet(content);
            flag=ZipFile.zipFile();
            if(flag==true){
                UnirestFile unirestFile=new UnirestFile();
                unirestFile.sendProvince(examEcards);


            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return flag;
    }
}
