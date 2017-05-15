package com.bidanet.bdcms.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.bean.PhysicalItemDetails;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.SqlServerTool;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.NewAndOldDataExchangeService;
import com.bidanet.bdcms.service.UserService;
import com.bidanet.bdcms.service.businessSetting.ExamPackageService;
import com.bidanet.bdcms.service.examBusiness.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xuejike-pc on 2017/2/17.
 */
public class SQLServerTest {



    public static void main(String[] args) {
        String destUrl = "http://192.168.3.5:8090/26/260015.jpg";
        String result=getStringBycallInterface("http://192.168.1.103:8086/admin/public/getPhotoFromOld.do", "192.168.3.5:8090", "26", "260015.jpg");
        System.out.println(result);
    }

    private static String getStringBycallInterface(String url,String ip,String num,String imageName){
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

}
