package com.bidanet.bdcms.bean;/**
 * Created by admin on 2016/11/25.
 */

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import org.apache.commons.lang.StringUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
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
@Service("generateBarCode")
public class GenerateBarCode implements GenerateCode {

    @Autowired
    @Qualifier("localFileDrive")
    LocalFileDrive localFileDrive;
    /**
     * 生成文件
     *
     * @param msg
     * @return
     */
    public String generateFile(String msg) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tempPath = "/upload/barCode/" + simpleDateFormat.format(new Date());
        String realPath = SpringWebTool.getRealPath(tempPath);
        File directory = new File(realPath);
        if (!directory.exists()){
            directory.mkdirs();
        }
        UUID uuid = UUID.randomUUID();
        String path = directory + "/" +uuid.toString()+ ".png";
        File file = new File(path);
        String uploadPath="";
        try {
            generate(msg, new FileOutputStream(file));
//            uploadPath =localFileDrive.upload(file);
//
//            file.delete();
            uploadPath=SpringWebTool.getWebRootUrl()+tempPath+"/" +uuid.toString()+ ".png";


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uploadPath;
    }

    /**
     * 生成字节
     *
     * @param msg
     * @return
     */
    public static byte[] generate(String msg) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(msg, ous);
        return ous.toByteArray();
    }

    /**
     * 生成到流
     *
     * @param msg
     * @param ous
     */
    public static void generate(String msg, OutputStream ous) {
        if (StringUtils.isEmpty(msg) || ous == null) {
            return;
        }

//        Code39Bean bean = new Code39Bean();
        Code128Bean b128ean = new Code128Bean();

        // 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

        // 配置对象
        b128ean.setModuleWidth(moduleWidth);
//        bean.setWideFactor(3);
//        bean.doQuietZone(false);
        b128ean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        b128ean.doQuietZone(true);
        b128ean.setQuietZone(0);// 两边空白区

        String format = "image/png";
        try {

            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 生成条形码
            b128ean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
