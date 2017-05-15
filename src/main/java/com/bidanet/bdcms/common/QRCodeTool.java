package com.bidanet.bdcms.common;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/8/25.
 */
@Service
public class QRCodeTool {

    public void getQRCodeInputStream(OutputStream out,String url){
        FileInputStream fis = null;
        try {
            File file = QRCode.from(url)
                    .withSize(300, 300)
                    .withCharset("UTF-8")
                    .to(ImageType.JPG).file();
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
