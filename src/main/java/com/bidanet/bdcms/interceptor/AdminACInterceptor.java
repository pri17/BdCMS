package com.bidanet.bdcms.interceptor;

import com.bidanet.bdcms.common.ReadTxtTool;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.driver.uc.Uc;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.exception.NoLoginException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/29.
 */
public class AdminACInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Uc bean = SpringWebTool.getBean("uc.admin", Uc.class);
        if (!bean.checkLogin()) {
            throw new NoLoginException();
        }
        // 操作人的用户名：请求地址   ext: 请求参数 Map
        User user = (User) httpServletRequest.getSession().getAttribute("login_user_info");
        if(user==null){
            throw new NoLoginException();
        }
        String userName = user.getUsername();
        String url = httpServletRequest.getRequestURI();

        Map<String, Object> parameterMap = httpServletRequest.getParameterMap();
        // TODO
//        if(logInfoService==null){
//            throw new NoLoginException();
//        }

        String urlName = null;
        //从文件中读取url对应的中文名,如果有中文名就用中文名,如果没有就用url名
        Map<String, String> map = ReadTxtTool.readMap(httpServletRequest);
        if (map != null && map.size() > 0) {
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (entry.getKey().equals(url)) {
                    urlName = (String) entry.getValue();
                    break;
                }
            }
        }
//        if (urlName != null) {
//            logInfoService.addInfoLog("admin.log", userName + "----" + urlName, parameterMap);
//        } else {
//            logInfoService.addInfoLog("admin.log", userName + "----" + url, parameterMap);
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
