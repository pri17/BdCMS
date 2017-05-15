package com.bidanet.bdcms.common;

import com.bidanet.bdcms.service.UserService;
import com.bidanet.bdcms.velocity.UrlFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/12.
 */
@Service
public class URLTool {

    @Autowired
    UserService userService;

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL)
    {
        String strAllParam=null;
        String[] arrSplit=null;

        strURL=strURL.trim().toLowerCase();

        arrSplit=strURL.split("[?]");
        if(strURL.length()>1)
        {
            if(arrSplit.length>1)
            {
                if(arrSplit[1]!=null)
                {
                    strAllParam=arrSplit[1];
                }
            }
        }

        return strAllParam;
    }


    /**
     * 解析出url参数中的键值对
     * @param URL  url地址
     * @return  url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL)
    {
        Map<String, String> mapRequest = new TreeMap<>();

        String[] arrSplit=null;

        String strUrlParam=TruncateUrlPage(URL);
        if(strUrlParam==null)
        {
            return mapRequest;
        }
        //每个键值为一组
        arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit)
        {
            /*String[] arrSplitEqual=null;

            arrSplitEqual= strSplit.split("[=]");*/


            String key = strSplit.substring(0,strSplit.indexOf("="));
            String value = strSplit.substring((strSplit.indexOf("=")+1));

            String[] arrSplitEqual = {key,value};



            //解析出键值
            if(arrSplitEqual.length>1)
            {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            }
            else
            {
                if(arrSplitEqual[0]!="")
                {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }



    public String getReplaceParamterURl(String urlStr,Object...param){
        return UrlFunction.buildUrl(urlStr,param);
    }

}
