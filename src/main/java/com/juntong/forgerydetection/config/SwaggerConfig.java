package com.juntong.forgerydetection.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * 配置 1: 全局文档信息 (标题、作者等)
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("伪造新闻检测平台 API 接口文档")
                        .version("1.0")
                        .description("基于 Spring Boot 3的前后端分离项目接口文档")
                        .contact(new Contact()
                                .name("lazy") // 你的名字
                                .email("lazy30090@outlook.com")));
    }

    /**
     * 配置 2: 接口扫描分组 (解决左侧菜单为空的关键！)
     * 告诉 Swagger 去哪里找 Controller
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("全部接口") // 分组名称
                .pathsToMatch("/**") // 匹配所有 URL
                .packagesToScan("com.juntong.forgerydetection.controller")
                .build();
    }
}