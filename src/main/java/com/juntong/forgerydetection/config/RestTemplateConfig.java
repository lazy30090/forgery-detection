package com.juntong.forgerydetection.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 设置超时时间，防止 Python 卡死导致 Java 也卡死
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 连接超时 5秒
        factory.setReadTimeout(10000);   // 读取超时 10秒
        return new RestTemplate(factory);
    }
}