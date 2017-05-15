package com.bidanet.bdcms.common;

import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cto on 2016/9/2.
 */
public class ReadTxtTool {
    private static Map<String, String> map = new HashMap<>();

    public static Map<String, String> readMap(HttpServletRequest httpServletRequest) {
        if (map.size() > 0) {
            return map;
        } else {
            try {
                String fileUrl = httpServletRequest.getSession().getServletContext().getRealPath("/") + "/urlToName.txt";
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
                        fileUrl), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    String[] lineString = line.split(",");
                    map.put(lineString[0], lineString[1]);
                }
                br.close();
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
