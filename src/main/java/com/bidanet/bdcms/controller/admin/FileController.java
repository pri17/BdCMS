package com.bidanet.bdcms.controller.admin;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.ext.file.FileDrive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xuejike on 2016-06-02.
 */
@Controller
@RequestMapping("/admin/file")
public class FileController extends AdminBaseController {
    @RequestMapping("/index")
    public void index(){
        System.out.println("--");
    }
    @RequestMapping("/uploadImg")
    @ResponseBody
    public Object uploadImg(
            @RequestParam("files") CommonsMultipartFile[] files,Boolean watermark){
        try {
        ArrayList<String> fileUrls = new ArrayList<>();
        for (CommonsMultipartFile file : files) {
            FileDrive fileDrive = ContextLoader.getCurrentWebApplicationContext().getBean("qiNiuFileDrive", FileDrive.class);

            String upload = fileDrive.uploadImg(file.getInputStream(), file.getOriginalFilename());

            fileUrls.add(upload);
        }
        return fileUrls;
        } catch (IOException e) {
            return AjaxCallBack.error("文件保存失败");
        }
    }



    public Object uploadPrivateImg(){
        return null;
    }
    public Object privateFileUrl(String id){
        return null;
    }


    @RequestMapping("/ueditor")
    public void ueditor(String action){


    }

}
