package com.bidanet.bdcms.controller;

import com.bidanet.bdcms.exception.CheckException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Created by xuejike on 2016-04-15.
 */

public class BaseController {



    protected void errorMsg(String msg){
        throw new CheckException(msg);
    }
    protected void checkNull(Object obj,String msg){
        if (obj==null){
            errorMsg(msg);
        }
    }

//    protected static void printToResponse(HSSFWorkbook wb, String fileName) {
//        HttpServletResponse response = getResponse();
//        try {
//            response.reset();
//            response.setContentType("application/vnd.ms-excel;charset=utf-8");
//            response.setHeader("Content-Disposition", "attachment; filename="+toUtf8String(fileName)+".xls");
//            OutputStream output = response.getOutputStream();
//            wb.write(output);
//            output.flush();
//            output.close();
//        } catch (Exception e) {
//
//        }
//    }

//    protected static ServletContext getServletContext() {
//        return ServletActionContext.getServletContext();
//
//    }
//
//    protected static HttpServletResponse getResponse() {
//        return ServletActionContext.getResponse();
//    }
//
//    protected static HttpServletRequest getRequest() {
//        return ServletActionContext.getRequest();
//    }

    /**
     * 转utf-8（处理中文乱码）
     * @param s
     * @return
     */
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }


}
