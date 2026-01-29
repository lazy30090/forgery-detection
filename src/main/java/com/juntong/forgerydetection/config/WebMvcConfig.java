package com.juntong.forgerydetection.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Mvc 全局配置
 * 主要用于解决跨域问题 (CORS)
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 注册 Sa-Token 拦截器 (核心安全配置)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**") // 拦截所有接口
                .excludePathPatterns(   // 排除以下接口 (白名单)
                        "/api/auth/login",       // 登录接口
                        "/api/auth/isLogin",     // 查询状态
                        "/api/news/list",        // 新闻列表 (允许游客看)
                        "/api/news/{id}",        // 新闻详情 (允许游客看)
                        "/files/**",             // 静态资源图片
                        "/doc.html",             // Swagger 文档页面
                        "/webjars/**",           // Swagger 资源
                        "/v3/api-docs/**",       // Swagger 资源
                        "/favicon.ico",          // 浏览器图标
                        "/",                     // 根路径
                        "/index.html",           // 首页文件
                        "/css/**", "/js/**",     // 静态资源文件夹
                        "/files/**"              // 上传的文件
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                // SpringBoot 2.4.0 之后推荐使用 allowedOriginPatterns 而不是 allowedOrigins
                .allowedOriginPatterns("*")
                // 设置允许的方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 设置允许的 header 属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600)
                // 是否允许携带 cookie
                .allowCredentials(true);
    }

    /**
     * 静态资源映射
     * 作用：把本地硬盘的 D:/forgery_uploads/ 映射为 http://localhost:8080/files/
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 这一句就是把浏览器访问的 /files/xxxx.jpg
        // 映射到你 D 盘的 uploadPath 目录下
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadPath);

        // Swagger 文档资源
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}