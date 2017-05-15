package com.bidanet.bdcms.common;

import java.util.ResourceBundle;

/**
 * 资源文件解析. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-11 16:50
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public class ParsProperFile {

    private static ResourceBundle configReBun = ResourceBundle.getBundle("config");

    public static String getConfigProp(String key){
        return configReBun.getString(key);
    }
}
