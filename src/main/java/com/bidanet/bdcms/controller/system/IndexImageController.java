package com.bidanet.bdcms.controller.system;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamDepartment;
import com.bidanet.bdcms.entity.ExamIndexImage;
import com.bidanet.bdcms.ext.file.LocalFileDrive;
import com.bidanet.bdcms.service.systemSetting.IndexImageService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * Created by gao on 2017/2/27.
 */
@Controller("indexImage")
@RequestMapping("/admin/systemSetting/indexImage")
public class IndexImageController extends AdminBaseController {

    @Autowired
    private IndexImageService indexImageService;

    @Autowired
    LocalFileDrive localFileDrive;

    private String tableId = "";
    @RequestMapping("/index")
    public void index(@ModelAttribute("query")ExamIndexImage query,
                      @ModelAttribute Page<ExamIndexImage> page,String tabid){
        indexImageService.getAllIndexImages(query,page);
        tableId=tabid;
    }

    @RequestMapping("/toAddIndexImage")
    public void toAddIndexImage(){
    }

    @RequestMapping("/addIndexImage")
    @ResponseBody
    public AjaxCallBack addIndexImage(ExamIndexImage examIndexImage){
        String url=examIndexImage.getUrl();
        if (url!=null){
            if(url.indexOf("base64,")!=-1){
                String[] iconStr = url.split("base64,");
                String icon = iconStr[1];
                url = localFileDrive.uploadBase64(icon, UUID.randomUUID().toString()+".bmp");
            }

        }
        examIndexImage.setUrl(url);
        indexImageService.addIndexImageT(examIndexImage);
       AjaxCallBack ajaxCallBack=AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 调转修改页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateIndexImage")
    public void toUpdateIndexImage(Long id,Model model){
        ExamIndexImage examIndexImage = indexImageService.get(id);
        model.addAttribute("examIndexImage",examIndexImage);
    }

    /**
     * 修改图标
     * @param examIndexImage
     * @return
     */
    @RequestMapping("/updateExamIndexImage")
    @ResponseBody
    public AjaxCallBack updateExamIndexImage(ExamIndexImage examIndexImage){
        String url=examIndexImage.getUrl();
        if (url!=null){
            if(url.indexOf("base64,")!=-1){
                String[] iconStr = url.split("base64,");
                String icon = iconStr[1];
                url = localFileDrive.uploadBase64(icon, UUID.randomUUID().toString()+".bmp");
            }

        }
        examIndexImage.setUrl(url);
        indexImageService.updateIndexImageT(examIndexImage);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除图标
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxCallBack delete(Long id){
        indexImageService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }
}
