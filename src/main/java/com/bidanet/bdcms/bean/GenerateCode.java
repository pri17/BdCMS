package com.bidanet.bdcms.bean;/**
 * Created by admin on 2016/11/25.
 */

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import org.apache.commons.lang.StringUtils;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 生成条形码.
 * Copyright: Copyright (c) 2016-11-25 14:54:58
 * Company: 苏州必答网络科技有限公司
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
public interface GenerateCode {

    /**
     * 生成文件
     *
     * @param msg
     * @return
     */
    String generateFile(String msg);

}
