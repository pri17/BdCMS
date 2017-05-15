package com.bidanet.bdcms.controller.admin;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.common.UserCenter;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.entity.entityEnum.UserLoginStatus;
import com.bidanet.bdcms.entity.entityEnum.UserStatus;
import com.bidanet.bdcms.ext.file.FileDrive;
import com.bidanet.bdcms.service.UserRoleService;
import com.bidanet.bdcms.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CF on 2016/5/24.
 */
@Controller
@RequestMapping("/admin/public")
public class PublicController extends AdminBaseController {
    @Autowired
    private UserService userService;
    @Autowired
    UserCenter userCenter;
    @Autowired
    UserRoleService userRoleService;

    @RequestMapping("/login")
    public void toLogin(HttpServletRequest request, Model model){

        String userName = "";
        String password = "";
//

        if(request.getCookies()!=null){

            Cookie [] c=request.getCookies();

            for(int i=0;i<c.length;i++){
                if(c[i].getName().equals("examChangshu")){
                    //存着数据（用户名+密码）
                    userName=c[i].getValue().split("-")[0];
                    password=c[i].getValue().split("-")[1];

                    //再一次的存起来（备用）
                    model.addAttribute("userName",userName);
                    model.addAttribute("password",password);
                }
            }
        }




    }

    @RequestMapping("/loginSave")
    @ResponseBody
    public AjaxCallBack login(String username, String password,String isRemember, HttpServletResponse response){

        User loginUser = userService.userLogin(username,password, UserLoginStatus.employee);
        //       userCenter.setLogin(loginUser.getUid());
//        if (!userRoleService.isAdmin(loginUser.getUid())){
//            throw new CheckException("登录失败");
//        }
        uc.setLogin(loginUser.getUid());

        //如果isRemember=1 记住用户名跟密码
        //构造Cookie对象
        //添加到Cookie中
        Cookie c=new Cookie("examChangshu", username+"-"+password);

        //设置过期时间
        c.setMaxAge(10*24*60*60);

        //存储
        response.addCookie(c);

        if(loginUser.getStatus()== UserStatus.disable){
            AjaxCallBack ajaxCallBack = new AjaxCallBack(AjaxCallBack.STATUS_ERROR, "该用户已被禁用!");
            ajaxCallBack.setCloseCurrent(false);
            return  ajaxCallBack;
        }else{
            AjaxCallBack ajaxCallBack = new AjaxCallBack(AjaxCallBack.STATUS_OK, "登录成功!");
            ajaxCallBack.setCloseCurrent(true);
            return  ajaxCallBack;
        }



    }

    @RequestMapping("/loginTimeout")
    public void loginTimeout(){

    }

    @RequestMapping("/logout")
    public void logout(){
        uc.logout();
        SpringWebTool.redirect("login");

    }


    @RequestMapping("/getPhotoFromOld")
    @ResponseBody
    public AjaxCallBack getPhotoFromOld(String ip,String num,String imageName)
    {
        try {
            /*获取网络图片  保存在临时文件夹中*/
            //String destUrl = "http://192.168.3.5:8090/26/260015.jpg";
            String destUrl="http://"+ip+"/"+num+"/"+imageName;
            URL url = new URL(destUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();

            String realPath= SpringWebTool.getRealPath("/tem/"+num+"/"+imageName);
            File file=new File(realPath);
            if(file!=null&&file.exists()) {
                file.delete();
            }
            FileUtils.copyInputStreamToFile(httpUrl.getInputStream(), file);
            /*将临时文件保存起来*/
            FileDrive fileDrive = ContextLoader.getCurrentWebApplicationContext().getBean("qiNiuFileDrive", FileDrive.class);
            String uploadPath= fileDrive.upload(new File(realPath));
            return new AjaxCallBack().success(uploadPath);
        } catch (IOException e) {
            return new AjaxCallBack().error("");
        }
    }
}
