package com.bidanet.bdcms.common;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
@Service
public class PictureTool {


    public static String getImgs(List<String> array) {
        String imgs=null;
        if (array.size() > 0 && !array.equals("")) {
          imgs=array.get(0);
        }
        return imgs;
    }
}
