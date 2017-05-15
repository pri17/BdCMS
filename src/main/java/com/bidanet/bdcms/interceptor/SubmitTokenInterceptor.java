package com.bidanet.bdcms.interceptor;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.driver.cache.Cache;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/7/9.
 */
public class SubmitTokenInterceptor implements HandlerInterceptor {
    public static final String cacheTag="submitToken.";
    public static final String inputName="submit_token";
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Cache cache = SpringWebTool.getBean(Cache.class);
        String submitToken = httpServletRequest.getParameter(inputName);
        if (submitToken==null){
            return true;
        }
        String key = cacheTag + submitToken;
        String s = cache.get(key);
        if (s!=null){
            cache.delete(key);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
