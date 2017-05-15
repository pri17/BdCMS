package com.bidanet.bdcms.controller.admin;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.BaseController;
import com.bidanet.bdcms.driver.uc.Uc;
import com.bidanet.bdcms.exception.CheckException;
import com.bidanet.bdcms.exception.NoLoginException;
import com.bidanet.bdcms.velocity.UrlFunction;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by bd-xd on 2016-05-26.
 */

public class AdminBaseController extends BaseController {


    @Resource(name = "uc.admin")
    public Uc uc;

    @ExceptionHandler(CheckException.class)
    @ResponseBody
    public AjaxCallBack checkError(CheckException e){
        return AjaxCallBack.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxCallBack error(Exception e){
        e.printStackTrace();
        return AjaxCallBack.error("系统繁忙，请稍后再试！");
    }

    @ExceptionHandler(NoLoginException.class)
    @ResponseBody
    public AjaxCallBack noLogin(){
        if(SpringWebTool.isAjax()){
            return AjaxCallBack.timeout();
        }else{
            try {
                SpringWebTool.getResponse().sendRedirect(UrlFunction.buildUrl("/admin/public/login"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }




}
