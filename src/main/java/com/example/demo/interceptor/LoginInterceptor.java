package com.example.demo.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 * 用于统一校验用户是否已登录
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前请求的URI
        String requestURI = request.getRequestURI();
        
        // 获取session
        HttpSession session = request.getSession();
        
        // 检查用户是否已登录
        if (session.getAttribute("user") != null) {
            // 已登录，放行
            return true;
        }
        
        // 未登录，重定向到登录页面
        response.sendRedirect("/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理之后进行调用，但是在视图被渲染之前
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求结束之后被调用，也就是在DispatcherServlet渲染了对应的视图之后
    }
}