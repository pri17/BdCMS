package com.bidanet.bdcms.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xuejike on 2017/3/20.
 */
public class DomainInterceptor implements HandlerInterceptor {

    public String machineDomain;
    public String adminDomain;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String name = httpServletRequest.getServerName();
        String requestURI = httpServletRequest.getRequestURI();
        boolean adminReq = requestURI.startsWith("/admin");
        if (adminReq){
            if (adminDomain!=null&&!adminDomain.isEmpty()){
                int indexOf = adminDomain.indexOf(name);
                if (indexOf==-1){
                    httpServletResponse.setStatus(404);
                    return false;
                }
            }
        }

        boolean machineReq = requestURI.startsWith("/machine");
        if (machineReq){
            if (machineDomain!=null&&!machineDomain.isEmpty()){
                int indexOf = machineDomain.indexOf(name);
                if (indexOf==-1){
                    httpServletResponse.setStatus(404);
                    return false;
                }
            }
            System.out.println("微信端进入： true");
        }

        System.out.println("微信端进入： false");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public String getMachineDomain() {
        return machineDomain;
    }

    public void setMachineDomain(String machineDomain) {
        this.machineDomain = machineDomain;
    }

    public String getAdminDomain() {
        return adminDomain;
    }

    public void setAdminDomain(String adminDomain) {
        this.adminDomain = adminDomain;
    }
}
