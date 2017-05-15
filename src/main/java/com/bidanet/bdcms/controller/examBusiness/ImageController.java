package com.bidanet.bdcms.controller.examBusiness;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.ImageUtils;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xuejike on 2016/12/21.
 */
@Controller
@RequestMapping("/admin/image")
public class ImageController extends AdminBaseController {


    @Autowired
    @Qualifier("localFileDrive")
    LocalFileDrive localFileDrive;

    @RequestMapping("/upload")
    @ResponseBody
    public AjaxCallBack uploadImage(@RequestParam("file") CommonsMultipartFile files){
        if (files!=null){
            try {
                String tempFile = localFileDrive.uploadTempFile(files.getInputStream(), files.getOriginalFilename());
                String tempFilePath = localFileDrive.getTempFilePath(files.getOriginalFilename());
                ImageUtils.resize(tempFile,tempFilePath,500,0);
                String upload = localFileDrive.upload(new FileInputStream(tempFilePath), files.getOriginalFilename());
//                ImageUtils.resize(files.getInputStream(),out,800);
//                String upload = localFileDrive.upload(files.getInputStream(), files.getOriginalFilename());
//                String randomName = localFileDrive.createRandomName(files.getOriginalFilename());
//                ImageUtils.resize(localFileDrive.getFileRealPath(upload),randomName,800,0);
                return AjaxCallBack.success(upload);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return AjaxCallBack.error("上传失败");

    }

    @RequestMapping("/uploadCamel")
    @ResponseBody
    public HashMap uploadCamelImage(@RequestParam("pic") String pic){
        String filename = localFileDrive.uploadBase64(pic, UUID.randomUUID().toString()+".jpg");
        HashMap map = new HashMap<String, String>();
        map.put("code", 1);
        map.put("picUrl", filename);
        return map;
    }

    @RequestMapping("/cutImagePage")
    public void cutImagePage(String file, Model model){
        model.addAttribute("file",file);
    }

    @RequestMapping("/cutImage")
    @ResponseBody
    public AjaxCallBack cutImage(String file,Integer x,Integer y,Integer w,Integer h){


        try {
            String fileRealPath = localFileDrive.getFileRealPath(file);
            String tempFilePath = localFileDrive.getTempFilePath(file);
            ImageUtils.crop(fileRealPath,tempFilePath,x,y,w,h);
            String upload = localFileDrive.upload(new FileInputStream(tempFilePath), tempFilePath);
            return AjaxCallBack.success(upload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxCallBack.error("图片裁剪失败");

    }

    public AjaxCallBack base64Image(String imgStr){
        int i = imgStr.indexOf("Base64,");
        if (i>=0){
            String base64 = imgStr.substring(i + 1);
            byte[] bytes = Base64.decodeBase64(base64);
        }
        return AjaxCallBack.success("");
    }
}
