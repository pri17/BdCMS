package com.bidanet.bdcms.service.unirest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.common.Base64Tool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.entity.ExamEcard;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.util.Base64;
import java.util.List;

/**
 * Created by gao on 2017/2/22.
 */
public class UnirestFile {
    @Autowired
    private ExamEcardService examEcardService;

    protected String url = "http://218.94.1.84:8003/JkzUploadServlet";
    protected String unitCode = "unitCode=123205814671441149W";
    protected String regCode = "regCode=670B14728AD9902AECBA32E22FA4F6BD";
    public  void sendProvince(List<ExamEcard> examEcards) {
        String path=(String) SpringWebTool.getSession().getAttribute("zipPath");
        try{



            HttpResponse<String> httpResponse = Unirest.post(url).basicAuth(unitCode,regCode)
                    .field("data", new File(path)).asString();

            System.out.println(httpResponse.getBody());

            String jsonStr = Base64Tool.getFromBase64(httpResponse.getBody());

            System.out.println(jsonStr);

            JSONObject result = JSON.parseObject(jsonStr);

            if(result.get("rtnCode").equals("00")){
                examEcardService.updateIsUploadT(examEcards);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
