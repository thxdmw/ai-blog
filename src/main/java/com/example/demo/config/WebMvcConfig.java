package com.example.demo.config;

import com.example.demo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 用于注册拦截器和配置拦截路径
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除不需要拦截的路径
                .excludePathPatterns(
                        "/login",       // 登录页面
                        "/logout",      // 登出操作
                        "/preview",     // 文件预览页面
                        "/preview/**",  // 文件预览相关操作
                        "/files",       // 获取文件列表
                        "/css/**",      // 静态资源
                        "/js/**",       // 静态资源
                        "/images/**",   // 静态资源
                        "/fonts/**"     // 静态资源
                );
    }
}