package com.juntong.forgerydetection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
        // 也就是说：访问 /files/abc.jpg -> 自动去 D:/forgery_uploads/abc.jpg 找
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadPath);
    }
}